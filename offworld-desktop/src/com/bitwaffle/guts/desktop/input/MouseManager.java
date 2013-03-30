package com.bitwaffle.guts.desktop.input;

import java.util.Iterator;

import org.lwjgl.input.Mouse;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.desktop.DesktopGame;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.button.Button;


/**
 * Used to simplify mouse input
 * 
 * @author TranquilMarmot
 */
public class MouseManager {
	/** how much the mouse has moved since last update */
	public static float dx = 0.0f, dy = 0.0f;
	/** how much the mouse wheel has moved */
	public static float wheel = 0.0f;
	/** the mouse's current location */
	public static float x = 0.0f, y = 0.0f;
	/** previous mouse location */
	public static float prevX = 0.0f, prevY = 0.0f;
	/** whether or not the mouse if inverted */
	public static boolean inverted = false;

	/**
	 * Update mouse manager-
	 * this updates all the variables that the manager contains,
	 * and controls the Render2D.camera and Game.player as necessary
	 */
	public static void update() {
		updateVariables();
		
		if(Render2D.camera != null){
			updateCamera();
			
			//if(Game.players[0] != null)
			//	Game.players[0].setTarget(MathHelper.toWorldSpace(x, y, Render2D.camera));
		}
	}
	
	/**
	 * Updates all the variables the mouse manager contains
	 */
	private static void updateVariables(){
		prevX = x;
		prevY = y;
		
		x = (float)Mouse.getX();
		y = (float)DesktopGame.windowHeight - Mouse.getY();  // we want the top-left corner to be 0,0
		
		checkHover();
		
		dx = ((float) Mouse.getDX());
		dy = -((float) Mouse.getDY());
		if(inverted)
			dy = -dy;

		wheel = (float) Mouse.getDWheel();
	}
	
	/**
	 * Checks if the mouse is hovering over anything and sets it
	 * as the currently selected button
	 */
	private static void checkHover(){
		if(Game.gui == null)
			return;
		else{
			// check if the mouse went off of the selected button
			if(Game.gui.selectedButton != null && !Game.gui.selectedButton.contains(x,y)){
				Game.gui.selectedButton.unselect();
				Game.gui.selectedButton = null;
			}
			
			// check every button for presses
			Iterator<Button> it = Game.gui.getButtonIterator();
			while (it.hasNext()) {
				Button b = it.next();
				
				if (b != Game.gui.selectedButton && b.isActive() && b.isVisible() && b.contains(x, y)) {
					Game.gui.selectedButton = b;
					b.select();
					System.out.println("selected");
				}
			}
			return;
		}
	}
	
	/**
	 * Update the Render2D.camera (zoom, pan if in free mode)
	 */
	private static void updateCamera(){
		if(wheel != 0 && !Game.gui.console.isOn())
			Render2D.camera.setZoom(Render2D.camera.getZoom() + (wheel / 25000.0f));
		
		/*
		if(Render2D.camera.currentMode() == Camera.Modes.FOLLOW && button1 && !tempFree){
			tempFree = true;
			Render2D.camera.setMode(Camera.Modes.FREE);
		} else if(Render2D.camera.currentMode() == Camera.Modes.FREE && !button1 && tempFree){
			tempFree = false;
			Render2D.camera.setMode(Camera.Modes.FOLLOW);
		}
		
		
		if(!button0 && button1 && ((dx != 0.0f) || (dy != 0.0f))){
			Vector2 current = MathHelper.toWorldSpace(x, y, Render2D.camera);
			Vector2 previous = MathHelper.toWorldSpace(prevX, prevY, Render2D.camera);
			
			float dx = current.x - previous.x;
			float dy = current.y - previous.y;
			
			Vector2 camLoc = Render2D.camera.getLocation();
			camLoc.x += dx;
			camLoc.y += dy;
			Render2D.camera.setLocation(camLoc);
		}
		*/
	}

}