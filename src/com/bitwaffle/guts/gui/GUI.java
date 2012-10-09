package com.bitwaffle.guts.gui;

import java.util.Iterator;

import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.gui.buttons.ButtonManager;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;
import com.bitwaffle.guts.gui.buttons.pause.PauseButtonManager;
import com.bitwaffle.offworld.Game;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	/** All of our pause buttons */
	private PauseButtonManager pauseButtons;
	/** All of our movement buttons */
	private MovementButtonManager movementButtons;
	
	/** Current button manager (only one that gets rendered/has its buttons checked) */
	private ButtonManager currentButtonManager;
	
	public GUI(){
		pauseButtons = new PauseButtonManager();
		movementButtons = new MovementButtonManager();
		
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
	}
	
	public Iterator<Button> getButtonIterator(){
		return currentButtonManager.getButtonIterator();
	}
	
	
}
