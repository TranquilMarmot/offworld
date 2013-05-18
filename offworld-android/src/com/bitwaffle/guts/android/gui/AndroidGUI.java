package com.bitwaffle.guts.android.gui;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.states.movement.MovementGUIState;
import com.bitwaffle.offworld.gui.GUIStates;
import com.bitwaffle.offworld.gui.OffworldGUI;

/**
 * GUI for android/touch screen devices
 * 
 * @author TranquilMarmot
 */
public class AndroidGUI extends OffworldGUI {
	private MovementGUIState movementState;
	
	public AndroidGUI(){
		super();
		
		movementState = new MovementGUIState(this);
	}
	
	@Override
	public void update(float timeStep) {
		super.update(timeStep);
		checkState();
	}
	
	
	protected void checkState(){
		// check if we need to switch between the pause menu and the movement keys
		if(!isCurrentState(GUIStates.TITLESCREEN.state) && !isCurrentState(GUIStates.OPTIONS.state)){
			if(Game.isPaused() && !isCurrentState(GUIStates.PAUSE.state))
				setCurrentState(GUIStates.PAUSE.state);
			else if(!Game.isPaused() && !isCurrentState(movementState))
				setCurrentState(movementState);
		}
	}
}
