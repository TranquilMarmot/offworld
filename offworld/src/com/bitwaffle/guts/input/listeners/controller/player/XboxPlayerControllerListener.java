package com.bitwaffle.guts.input.listeners.controller.player;

import com.badlogic.gdx.controllers.Controller;
import com.bitwaffle.guts.input.KeyBindings;
import com.bitwaffle.offworld.entities.player.Player;

public class XboxPlayerControllerListener extends PlayerControllerListener {
	public static final int BUTTON_A = 0;
	public static final int BUTTON_B = 1;
	public static final int BUTTON_X = 2;
	public static final int BUTTON_Y = 3;
	public static final int BUTTON_LB = 4;
	public static final int BUTTON_RB = 5;
	public static final int BUTTON_BACK = 6;
	public static final int BUTTON_START = 7;
	public static final int BUTTON_LCLICK = 8;
	public static final int BUTTON_RCLICK = 9;
	
	public static final int MOVEMENT_AXIS = 1;
	public static final int AIM_Y_AXIS = 2;
	public static final int AIM_X_AXIS = 3;
	public static final int TRIGGER_AXIS = 4;
	
	public XboxPlayerControllerListener(Player player) {
		super(player);
	}
	

	@Override
	protected KeyBindings getBinding(Controller controller, int buttonCode) {
		switch(buttonCode){
		case BUTTON_A:
			
		case BUTTON_B:
			
		case BUTTON_X:
		
		case BUTTON_Y:
			
		case BUTTON_LB:
			
		case BUTTON_RB:
			
		case BUTTON_BACK:
			
		case BUTTON_START:
			
		case BUTTON_LCLICK:
			
		case BUTTON_RCLICK:
			
		}
		return null;
	}


	@Override
	protected boolean isJump(int buttonCode) {
		return buttonCode == BUTTON_A;
	}


	@Override
	protected boolean isMovementAxis(int axisCode) {
		return axisCode == MOVEMENT_AXIS;
	}


	@Override
	protected float movementThreshold() {
		return 0.25f;
	}


	@Override
	protected boolean isXAimAxis(int axisCode) {
		return axisCode == AIM_X_AXIS;
	}


	@Override
	protected boolean isYAimAxis(int axisCode) {
		return axisCode == AIM_Y_AXIS;
	}


	@Override
	protected boolean isLTriggerAxis(int axisCode, float value) {
		return axisCode == TRIGGER_AXIS && value > 0;
	}


	@Override
	protected boolean isRTriggerAxis(int axisCode, float value) {
		return axisCode == TRIGGER_AXIS && value < 0;
	}


	@Override
	protected boolean isPastJetpackTriggerThreshold(float value) {
		return value > 0.5f;
	}


	@Override
	protected boolean isPastShootingTriggerThreshold(float value) {
		return value < -0.5f;
	}
	
	@Override
	protected boolean isAboveXAimThreshold(float value) {
		return value > 0.05f || value < -0.05f;
	}

	@Override
	protected boolean isAboveYAimThreshold(float value) {
		return value > 0.05f || value < -0.05f;
	}

}
