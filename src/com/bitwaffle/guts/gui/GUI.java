package com.bitwaffle.guts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.util.Log;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.gui.buttons.ButtonManager;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.guts.gui.buttons.pause.PauseButtonManager;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	/** List of all GUI objects */
	private ArrayList<GUIObject> objects;
	
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<GUIObject> objectsToAdd, objectsToRemove;
	
	/** Anything that can be clicked/pressed */
	private ArrayList<Button> buttons;
	
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/** All of the pause buttons */
	private PauseButtonManager pauseButtons;
	/** All of the movement buttons */
	private MovementButtonManager movementButtons;
	/** The current button manager (basically, the state of the GUI) */
	private ButtonManager currentButtonManager;
	
	/**
	 * Create a new GUI
	 */
	public GUI(){
		pauseButtons = new PauseButtonManager();
		movementButtons = new MovementButtonManager();
		
		objects = new ArrayList<GUIObject>();
		objectsToRemove = new Stack<GUIObject>();
		objectsToAdd = new Stack<GUIObject>();
		
		buttons = new ArrayList<Button>();
		buttonsToRemove = new Stack<Button>();
		buttonsToAdd = new Stack<Button>();
		
		if(Game.isPaused())
			setButtonManager(pauseButtons);
		else
			setButtonManager(movementButtons);
	}
	
	/**
	 * Update the GUI
	 * @param timeStep Time passed since last update, in seconds
	 */
	public void update(float timeStep){
		checkState();
		updateButtons(timeStep);
		updateObjects(timeStep);
	}
	
	/**
	 * Checks the state of the GUI and changes it if necessary
	 */
	private void checkState(){
		if(Game.isPaused() && currentButtonManager != pauseButtons)
			setButtonManager(pauseButtons);
		else if(!Game.isPaused() && currentButtonManager != movementButtons)
			setButtonManager(movementButtons);
	}
	
	/**
	 * Updates every button in the GUI
	 * @param timeStep Time since last update, in seconds
	 */
	private void updateButtons(float timeStep){
		while(!buttonsToRemove.isEmpty())
			buttons.remove(buttonsToRemove.pop());
		
		while(!buttonsToAdd.isEmpty())
			buttons.add(buttonsToAdd.pop());
		
		for(Button butt : buttons)
			butt.update(timeStep);
	}
	
	/**
	 * Updates every object in the GUI
	 * @param timeStep Time since last update, in seconds
	 */
	private void updateObjects(float timeStep){
		while(!objectsToRemove.isEmpty())
			objects.remove(objectsToRemove.pop());
		
		while(!objectsToAdd.isEmpty())
			objects.add(objectsToAdd.pop());
		
		for(GUIObject obj : objects)
			obj.update(timeStep);
	}
	
	/**
	 * @param o Object to add to GUI
	 */
	public void addObject(GUIObject o){
		objectsToAdd.push(o);
	}
	
	/**
	 * @param o Object to remove from GUI
	 */
	public void removeObject(GUIObject o){
		objectsToAdd.push(o);
	}
	
	/**
	 * @return An iterator that goes through every GUIObject currently in the GUI
	 */
	public Iterator<GUIObject> getObjectIterator(){
		return objects.iterator();
	}
	
	/**
	 * @param b Button to add to GUI
	 */
	public void addButton(Button b){
		buttonsToAdd.push(b);
	}
	
	/**
	 * @param b Button to remove from GUI
	 */
	public void removeButton(Button b){
		buttonsToRemove.push(b);
	}
	
	/**
	 * @return Iterator that goes through every button currently in the GUI
	 */
	public Iterator<Button> getButtonIterator(){
		return buttons.iterator();
	}
	
	/**
	 * Set the current button manager. Accepts null for no button manager
	 * @param bm Button manager to use
	 */
	public void setButtonManager(ButtonManager bm){
		if(currentButtonManager != null){
			Iterator<Button> it = currentButtonManager.getButtonIterator();
			while(it.hasNext())
				removeButton(it.next());
		} 
		
		if(bm != null && currentButtonManager != bm){
			Iterator<Button> ti = bm.getButtonIterator();
			while(ti.hasNext())
				addButton(ti.next());
		}
		
		currentButtonManager = bm;
	}
	
	/**
	 * Draw the GUI/
	 * @param renderer
	 */
	public void render(Render2D renderer) {
		setUpProjectionWorldCoords(renderer);
		renderObjects(getObjectIterator(), renderer);
		renderObjects(getButtonIterator(), renderer);
		renderText(renderer);
		
		if(Game.console.isVisible)
			Game.console.render(renderer, false, false);
	}
	
	/**
	 * Sets up the projection matrix with an orthographic projection
	 * for drawing things in screen coordinates
	 */
	private void setUpProjectionWorldCoords(Render2D renderer){
		renderer.projection.setIdentity();
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
		
		renderer.program.setUniformMatrix4f("Projection", renderer.projection);
	}
	
	/**
	 * @param objects Objects to render
	 * @param renderer Renderer to use
	 */
	private void renderObjects(Iterator<? extends GUIObject> it, Render2D renderer){
		try{
			while(it.hasNext()){
				GUIObject obj = it.next();
				
				if(obj.isVisible){
					renderer.modelview.setIdentity();
					Matrix4f.translate(new Vector3f(obj.x, obj.y, 0.0f), renderer.modelview, renderer.modelview);
					renderer.sendModelViewToShader();
					
					obj.render(renderer, false, false);
				}
			}
		} catch(NullPointerException e){
			Log.v("GUI", "Got null gui object (ignoring)");
		}
	}
	
	/**
	 * Debug text rendering
	 * @param renderer What to use to render text
	 */
	private void renderText(Render2D renderer){
		// draw some debug info TODO move this somewhere else!
		float[] debugTextColor = new float[]{ 0.3f, 0.3f, 0.3f, 1.0f };
		float tscale = 0.15f;
		
		String vers = "Version " + Game.VERSION;
		Game.resources.font.drawString(vers, renderer, Game.windowWidth - Game.resources.font.stringWidth(vers, tscale), Game.resources.font.stringHeight(vers, tscale), tscale, debugTextColor);
		
		String fps = Game.currentFPS + " FPS";
		Game.resources.font.drawString(fps, renderer, Game.windowWidth - Game.resources.font.stringWidth(fps, tscale), Game.resources.font.stringHeight(fps, tscale) * 2, tscale, debugTextColor);
		
		String ents = Game.physics.numEntities() + " ents";
		Game.resources.font.drawString(ents, renderer, Game.windowWidth - Game.resources.font.stringWidth(ents, tscale), Game.resources.font.stringHeight(ents, tscale) * 3, tscale, debugTextColor);
		
		
		// draw pause text FIXME temp
		if(Game.isPaused()){
			String pauseString = "Hello. This is a message to let you know that\nthe game is paused. Have a nice day.";
			float scale = 0.3f;
			float stringWidth = Game.resources.font.stringWidth(pauseString, scale);
			float stringHeight = Game.resources.font.stringHeight(pauseString, scale);
			float textX = ((float)Game.windowWidth / 2.0f) - (stringWidth / 2.0f);
			float textY = ((float)Game.windowHeight / 2.0f) - (stringHeight / 2.0f);
			Game.resources.font.drawString(pauseString, renderer, textX, textY, scale);
		}
	}
}
