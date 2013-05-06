package com.bitwaffle.guts.gui;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.states.movement.MovementGUIState;
import com.bitwaffle.offworld.gui.states.GUIStates;

public class AndroidGUI extends GUI {
	private MovementGUIState movementState;
	
	public AndroidGUI(){
		super();
		
		movementState = new MovementGUIState(this);
	}
	
	
	@Override
	protected void checkState(){
		// check if we need to switch between the pause menu and the movement keys
		if(!isCurrentState(GUIStates.TITLESCREEN) && !isCurrentState(GUIStates.OPTIONS.state)){
			if(Game.isPaused() && !isCurrentState(GUIStates.PAUSE))
				setCurrentState(GUIStates.PAUSE);
			else if(!Game.isPaused() && !isCurrentState(movementState))
				setCurrentState(movementState);
		}
	}
}
