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
		if(!isCurrentState(States.TITLESCREEN) && !isCurrentState(States.OPTIONS.state)){
			if(Game.isPaused() && !isCurrentState(States.PAUSE))
				setCurrentState(States.PAUSE);
			else if(!Game.isPaused() && !isCurrentState(movementState))
				setCurrentState(movementState);
		}
	}
}
