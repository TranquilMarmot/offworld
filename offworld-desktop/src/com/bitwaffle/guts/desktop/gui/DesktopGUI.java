package com.bitwaffle.guts.desktop.gui;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.offworld.gui.states.GUIStates;

/**
 * GUI for desktop game
 * 
 * @author TranquilMarmot
 */
public class DesktopGUI extends GUI {

	@Override
	public void update(float timeStep) {
		super.update(timeStep);
		checkState();
	}
	
	protected void checkState(){
		// check if we need to switch between the pause menu and the movement keys
		if(!isCurrentState(GUIStates.TITLESCREEN.state) && !isCurrentState(GUIStates.OPTIONS.state)){
			if(Game.isPaused() && !isCurrentState(GUIStates.PAUSE.state))
					setCurrentState(GUIStates.PAUSE);
			else if(!Game.isPaused() && isCurrentState(GUIStates.PAUSE.state))
					setCurrentState(GUIStates.NONE);
		}
	}

}
