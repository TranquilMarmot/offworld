package com.bitwaffle.guts.gui.hud;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.GUIObject;

/**
 * Handles things like displaying the player's health, etc.
 * and adding any buttons to the gui that relate to the HUD (camera button etc.)
 * 
 * @author TranquilMarmot
 */
public class HUD extends GUIObject {
	/** GUI containing this HUD*/
	private GUI gui;
	
	/** Button for changing camera mode */
	private CameraButton cameraButton;
	/** Whether or not the camera button is in the GUI right now */
	private boolean cameraButtonInGUI;
	
	/** Button for toggling console/opening input dialog */
	private ConsoleButton consoleButton;
	/** Whether or not the console button is in the GUI right now */
	private boolean consoleButtonInGUI;
	
	/**
	 * @param gui GUI containing this HUD
	 */
	public HUD(GUI gui){
		super(0.0f, 0.0f);
		this.gui = gui;
		
		cameraButton = new CameraButton();
		consoleButton = new ConsoleButton();
		
		addCameraButton();
		addConsoleButton();
	}

	@Override
	public void update(float timeStep) {
		// remove buttons if game is paused
		if(Game.isPaused()){
			if(cameraButtonInGUI)
				removeCameraButton();
			if(consoleButtonInGUI)
				removeConsoleButton();
			
		// add buttons if game isn't paused
		} else {
			if(!cameraButtonInGUI)
				addCameraButton();
			if(!consoleButtonInGUI)
				addConsoleButton();
		}
		
	}
	
	/** Adds the console button to the GUI */
	private void addConsoleButton(){
		gui.addButton(consoleButton);
		consoleButtonInGUI = true;
	}
	
	/** Removes the console button from the GUI */
	private void removeConsoleButton(){
		gui.removeButton(consoleButton);
		consoleButtonInGUI = false;
	}
	
	/** Adds the camera button to the GUI */
	private void addCameraButton(){
		gui.addButton(cameraButton);
		cameraButtonInGUI = true;
	}
	
	/** Removes the camera button from the GUI */
	private void removeCameraButton(){
		gui.removeButton(cameraButton);
		cameraButtonInGUI = false;
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical) {}
	
	@Override
	public void cleanup() {
		gui.removeButton(cameraButton);
		gui.removeButton(consoleButton);
	}
}
