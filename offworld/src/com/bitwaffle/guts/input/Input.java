package com.bitwaffle.guts.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.input.GestureDetector;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.input.controller.gui.OuyaGUIControllerListener;
import com.bitwaffle.guts.input.controller.gui.XboxGUIControllerListener;
import com.bitwaffle.guts.input.controller.player.OuyaPlayerControllerListener;
import com.bitwaffle.guts.input.controller.player.XboxPlayerControllerListener;
import com.bitwaffle.guts.input.listeners.CameraInputListener;
import com.bitwaffle.guts.input.listeners.ConsoleInputListener;
import com.bitwaffle.guts.input.listeners.gui.GUIInputListener;

/**
 * Handles the input multiplexer and instances of useful input processors.
 * 
 * @author TranquilMarmot
 */
public class Input {
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
		guiInputListener = new GUIInputListener(Game.gui);
		cameraInputListener = new CameraInputListener(Render2D.camera);
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
			if(con.getName().equals(Ouya.ID)){
				con.addListener(new OuyaGUIControllerListener(Game.gui));
			}else if(con.getName().contains("XBOX 360")){
				con.addListener(new XboxGUIControllerListener(Game.gui));
			}
		}
		
	}
}
