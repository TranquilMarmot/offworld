package com.bitwaffle.guts.gui;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.states.movement.MovementGUIState;

public class AndroidGUI extends GUI {
	private MovementGUIState movementState;
	
	public AndroidGUI(){
		super();
		
		movementState = new MovementGUIState();
		movementState.setParentGUI(this);
	}
	
	
	@Override
	protected void checkState(){
		// check if we need to switch between the pause menu and the movement keys
		if(currentState != States.TITLESCREEN.state && currentState != States.OPTIONS.state){
			if(Game.isPaused() && currentState != States.PAUSE.state)
				setCurrentState(States.PAUSE);
			else if(!Game.isPaused() && currentState != movementState)
				setCurrentState(movementState);
		}
	}
}
