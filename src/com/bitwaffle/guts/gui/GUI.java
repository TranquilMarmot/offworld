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
	
	public void update(){
		pauseButtons.update();
		movementButtons.update();
		
		if(Game.isPaused())
			currentButtonManager = pauseButtons;
		else
			currentButtonManager = movementButtons;
		
		for(GUIObject obj : objects)
			obj.update();
	}
	
	public void render(Render2D renderer) {
		renderButtons(renderer);
		renderObjects(getObjectIterator(), renderer);
	}
	
	public Iterator<GUIObject> getObjectIterator(){
		return objects.iterator();
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
