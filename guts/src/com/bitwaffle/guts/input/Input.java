package com.bitwaffle.guts.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.input.gui.GUIInputListener;
import com.bitwaffle.guts.input.gui.controller.OuyaGUIControllerListener;
import com.bitwaffle.guts.input.gui.controller.XboxGUIControllerListener;

/**
 * Handles the input multiplexer and instances of useful input processors.
 * 
 * @author TranquilMarmot
 */
public abstract class Input {
	/** The input multiplexer */
	public InputMultiplexer multiplexer;
	
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
		consoleInputListener = new ConsoleInputListener(Game.gui.console);
		
		// add to multiplexer
		multiplexer.addProcessor(guiInputListener);
		multiplexer.addProcessor(consoleInputListener);
		
		Gdx.input.setInputProcessor(multiplexer);
		
		
		// FIXME temp
		for(Controller con : Controllers.getControllers()){
			if(con.getName().equals(Ouya.ID))
				con.addListener(new OuyaGUIControllerListener(Game.gui));
			else if(con.getName().contains("XBOX 360"))
				con.addListener(new XboxGUIControllerListener(Game.gui));
		}
		
	}
	
	protected abstract GUIInputListener getGUIInputListener();
}
