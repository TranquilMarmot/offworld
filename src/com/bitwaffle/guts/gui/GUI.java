package com.bitwaffle.guts.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.util.Log;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.gui.buttons.ButtonManager;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.guts.gui.buttons.pause.PauseButtonManager;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	/** List of all GUI objects */
	private ArrayList<GUIObject> objects;
	
	/** All of the pause buttons */
	private PauseButtonManager pauseButtons;
	/** All of the movement buttons */
	private MovementButtonManager movementButtons;
	
	/** Current button manager (only one that gets rendered/has its buttons checked) */
	private ButtonManager currentButtonManager;
	
	public GUI(){
		pauseButtons = new PauseButtonManager();
		movementButtons = new MovementButtonManager();
		objects = new ArrayList<GUIObject>();
		
		if(Game.isPaused())
			currentButtonManager = pauseButtons;
		else
			currentButtonManager = movementButtons;
	}
	
	public void update(float timeStep){
		pauseButtons.update(timeStep);
		movementButtons.update(timeStep);
		
		if(Game.isPaused())
			currentButtonManager = pauseButtons;
		else
			currentButtonManager = movementButtons;
		
		for(GUIObject obj : objects)
			obj.update(timeStep);
	}
	
	public void render(Render2D renderer) {
		renderObjects(getObjectIterator(), renderer);
		renderButtons(renderer);
		renderText(renderer);
	}
	
	public Iterator<GUIObject> getObjectIterator(){
		return objects.iterator();
	}
	
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
	
	
	private void renderObjects(Iterator<GUIObject> objects, Render2D renderer){
		Iterator<GUIObject> it = getObjectIterator();
		
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
	
	public Iterator<Button> getButtonIterator(){
		return currentButtonManager.getButtonIterator();
	}

	private void renderButtons(Render2D renderer) {
		Iterator<Button> it = getButtonIterator();
		
		try{
			while(it.hasNext()){
				Button butt = it.next();
				
				if(butt.isVisible()){
					renderer.modelview.setIdentity();
					Matrix4f.translate(new Vector3f(butt.x, butt.y, 0.0f), renderer.modelview, renderer.modelview);
					renderer.sendModelViewToShader();
					
					butt.render(renderer, false, false);
				}
			}
		} catch(NullPointerException e){
			Log.v("GUI", "Got null button (ignoring)");
		}
	}
}
