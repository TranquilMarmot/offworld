package com.bitwaffle.offworld;

import com.bitwaffle.guts.Game;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.gui.states.GUIStates;

/**
 * Sets the game to the title screen for Offworld.
 * 
 * @author TranquilMarmot
 */
public abstract class OffworldGame extends Game {
	/** Whether or not to show the splash screen */
	public static boolean showSplash = false;
	
	/** List of players */
	public static Player[] players = new Player[4];
	
	@Override
	public void create(){
		super.create();
		
		if(showSplash)
			gui.setCurrentState(GUIStates.SPLASH);
		else
			gui.setCurrentState(GUIStates.TITLESCREEN);
	}

}
