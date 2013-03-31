package com.bitwaffle.guts.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.bitwaffle.guts.input.listeners.CameraGestureListener;
import com.bitwaffle.guts.input.listeners.ConsoleInputListener;
import com.bitwaffle.guts.input.listeners.GUIInputListener;
import com.bitwaffle.guts.input.listeners.KeyBindingListener;
import com.bitwaffle.guts.input.listeners.button.ButtonInputListener;

/**
 * Handles the input multiplexer and instances of useful input processors.
 * 
 * @author TranquilMarmot
 */
public class Input {
	/** The input multiplexer */
	public InputMultiplexer multiplexer;
	
	/** Presses/releases things in the {@link Keys} enum */
	public KeyBindingListener keyBindingListener;
	
	/** Listener for detecting UI button presses */
	public ButtonInputListener buttonPressListener;
	
	/** Gesture detector for zooming/panning camera*/
	public GestureDetector cameraGestureListener;
	
	/** Input listener for moving the camera around when it's in free mode */
	public CameraGestureListener cameraInputListener; // TODO should this be added/removed every time the camera enters/exits free mode?
	
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
		keyBindingListener = new KeyBindingListener();
		buttonPressListener = new ButtonInputListener();
		guiInputListener = new GUIInputListener();
		cameraInputListener = new CameraGestureListener();
		cameraGestureListener = new GestureDetector(cameraInputListener);
		consoleInputListener = new ConsoleInputListener();
		
		// add to multiplexer
		multiplexer.addProcessor(keyBindingListener);
		multiplexer.addProcessor(buttonPressListener);
		multiplexer.addProcessor(guiInputListener);
		multiplexer.addProcessor(cameraGestureListener);
		multiplexer.addProcessor(cameraInputListener);
		multiplexer.addProcessor(consoleInputListener);
		
		Gdx.input.setInputProcessor(multiplexer);
		
	}
}
