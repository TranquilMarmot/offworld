package com.bitwaffle.guts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.opengl.GLES20;
import android.util.Log;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.console.Console;
import com.bitwaffle.guts.gui.hud.HUD;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.movement.MovementGUIState;
import com.bitwaffle.guts.gui.states.pause.PauseGUIState;
import com.bitwaffle.guts.gui.states.titlescreen.TitleScreen;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	public enum States{
		PAUSE,
		MOVEMENT,
		TITLESCREEN;
	}
	
	/** Console for interacting with game */
	public static Console console;
	
	/** List of all GUI objects */
	private ArrayList<GUIObject> objects;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<GUIObject> objectsToAdd, objectsToRemove;
	
	/** Anything that can be clicked/pressed */
	private ArrayList<Button> buttons;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	private PauseGUIState pauseState;
	private MovementGUIState movementState;
	private TitleScreen titleScreen;
	
	/** The current button manager (basically, the state of the GUI) */
	private States currentState;
	
	
	/**
	 * Create a new GUI
	 */
	public GUI(){		
		objects = new ArrayList<GUIObject>();
		objectsToRemove = new Stack<GUIObject>();
		objectsToAdd = new Stack<GUIObject>();
		
		buttons = new ArrayList<Button>();
		buttonsToRemove = new Stack<Button>();
		buttonsToAdd = new Stack<Button>();
		
		console = new Console();
		
		pauseState = new PauseGUIState(this);
		movementState = new MovementGUIState(this);
		titleScreen = new TitleScreen(this);
		
		// add a HUD to this GUI
		this.addObject(new HUD(this));
	}
	
	/**
	 * Update the GUI
	 * @param timeStep Time passed since last update, in seconds
	 */
	public void update(float timeStep){
		checkState();
		if(currentState != null)
			getState(currentState).update(timeStep);
		
		updateButtons(timeStep);
		updateObjects(timeStep);
		console.update(timeStep);
	}
	
	/**
	 * Checks the state of the GUI and changes it if necessary
	 */
	private void checkState(){
		if(currentState != States.TITLESCREEN){
			if(Game.isPaused() && currentState != States.PAUSE)
				setCurrentState(States.PAUSE);
			else if(!Game.isPaused() && currentState != States.MOVEMENT)
				setCurrentState(States.MOVEMENT);
		}
	}
	
	private GUIState getState(States state){
		if(state == null)
			return null;
		
		switch(state){
		case PAUSE:
			return pauseState;
		case MOVEMENT:
			return movementState;
		case TITLESCREEN:
			return titleScreen;
		default:
			return null;
		}
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
		objectsToRemove.push(o);
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
	 * Set the current state of the GUI
	 * @param newState New state to use
	 */
	public void setCurrentState(States newState){
		if(currentState != null)
		getState(currentState).loseCurrentState();
		
		if(newState != null)
			 getState(newState).gainCurrentState();
		
		currentState = newState;
	}
	
	/**
	 * Draw the GUI/
	 * @param renderer
	 */
	public void render(Render2D renderer) {
		renderer.setUpProjectionScreenCoords();
		renderObjects(getObjectIterator(), renderer);
		renderObjects(getButtonIterator(), renderer);
		renderText(renderer);
		
		if(console.isVisible())
			console.render(renderer, false, false);
	}
	
	/**
	 * @param objects Objects to render
	 * @param renderer Renderer to use
	 */
	private void renderObjects(Iterator<? extends GUIObject> it, Render2D renderer){
		try{
			while(it.hasNext()){
				GUIObject obj = it.next();
				
				if(obj.isVisible()){
					renderObject(obj, renderer);
				}
			}
		} catch(NullPointerException e){
			Log.v("GUI", "Got null gui object (ignoring)");
		}
	}
	
	public void renderObject(GUIObject obj, Render2D renderer){
		renderer.modelview.setIdentity();
		Matrix4f.translate(new Vector3f(obj.x, obj.y, 0.0f), renderer.modelview, renderer.modelview);
		renderer.sendModelViewToShader();
		
		obj.render(renderer, false, false);
	}
	
	/**
	 * Debug text rendering
	 * @param renderer What to use to render text
	 */
	private void renderText(Render2D renderer){
		// draw some debug info TODO move this somewhere else!
		float[] debugTextColor = new float[]{ 0.3f, 0.3f, 0.3f, 1.0f };
		float tscale = 0.4f;
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE_MINUS_DST_COLOR, GLES20.GL_ZERO);
		
		String vers = "Version " + Game.VERSION;
		renderer.font.drawString(vers, renderer, Game.windowWidth - renderer.font.stringWidth(vers, tscale), renderer.font.stringHeight(vers, tscale) * 2, tscale, debugTextColor);
		
		String fps = Game.currentFPS + " FPS";
		renderer.font.drawString(fps, renderer, Game.windowWidth - renderer.font.stringWidth(fps, tscale), renderer.font.stringHeight(fps, tscale) * 4, tscale, debugTextColor);
		
		String ents = Game.physics.numEntities() + " ents";
		renderer.font.drawString(ents, renderer, Game.windowWidth - renderer.font.stringWidth(ents, tscale), renderer.font.stringHeight(ents, tscale) * 6, tscale, debugTextColor);
		
		GLES20.glDisable(GLES20.GL_BLEND);
		
		
		// draw pause text FIXME temp
		if(Game.isPaused()){
			String pauseString = "Hello. This is a message to let you know that\nthe game is paused. Have a nice day.";
			float scale = 0.75f;
			float stringWidth = renderer.font.stringWidth(pauseString, scale);
			float stringHeight = renderer.font.stringHeight(pauseString, scale);
			float textX = ((float)Game.windowWidth / 2.0f) - (stringWidth / 2.0f);
			float textY = ((float)Game.windowHeight / 2.0f) - (stringHeight / 2.0f);
			renderer.font.drawString(pauseString, renderer, textX, textY, scale);
		}
	}
}
