package com.bitwaffle.guts.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.input.GestureDetector;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.input.gui.GUIInputListener;
import com.bitwaffle.guts.input.gui.controller.OuyaGUIControllerListener;
import com.bitwaffle.guts.input.gui.controller.XboxGUIControllerListener;
import com.bitwaffle.guts.input.listeners.CameraInputListener;
import com.bitwaffle.guts.input.listeners.ConsoleInputListener;

/**
 * Handles the input multiplexer and instances of useful input processors.
 * 
 * @author TranquilMarmot
 */
public abstract class Input {
	/** The input multiplexer */
	public InputMultiplexer multiplexer;
	
	/** Gesture detector for zooming/panning camera*/
	public GestureDetector cameraGestureListener;
	
	/** Input listener for moving the camera around when it's in free mode */
	public CameraInputListener cameraInputListener; // TODO should this be added/removed every time the camera enters/exits free mode?
	
	/** Grabs things being typed and sends them to the console */
	public ConsoleInputListener consoleInputListener;
	
	/** Handles iterating through GUI elements with the keyboard */
	public GUIInputListener guiInputListener;
	
	/**
	 * Initialize input and add default processors
	 */
	public Input(){
		multiplexer = new InputMultiplexer();
		
		// initialize processors
		guiInputListener = getGUIInputListener();
		cameraInputListener = getCameraInputListener();
		cameraGestureListener = new GestureDetector(cameraInputListener);
		consoleInputListener = new ConsoleInputListener(Game.gui.console);
		
		// add to multiplexer
		multiplexer.addProcessor(guiInputListener);
		multiplexer.addProcessor(cameraGestureListener);
		multiplexer.addProcessor(cameraInputListener);
		multiplexer.addProcessor(consoleInputListener);
		
		Gdx.input.setInputProcessor(multiplexer);
		
		
		// FIXME temp?
		for(Controller con : Controllers.getControllers()){
			if(con.getName().equals(Ouya.ID))
				con.addListener(new OuyaGUIControllerListener(Game.gui));
			else if(con.getName().contains("XBOX 360"))
				con.addListener(new XboxGUIControllerListener(Game.gui));
		}
		
	}
	
	protected abstract GUIInputListener getGUIInputListener();
	protected abstract CameraInputListener getCameraInputListener();
}
