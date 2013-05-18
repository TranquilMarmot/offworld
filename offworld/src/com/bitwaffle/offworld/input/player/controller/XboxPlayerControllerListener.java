package com.bitwaffle.offworld.input.player.controller;

import com.bitwaffle.guts.input.controller.XboxMappings;
import com.bitwaffle.offworld.entities.player.Player;

public class XboxPlayerControllerListener extends PlayerControllerListener {
	public XboxPlayerControllerListener(Player player) {
		super(player);
	}


	@Override
	protected boolean isJump(int buttonCode) {
		return buttonCode == XboxMappings.BUTTON_A;
	}


	@Override
	protected boolean isMovementAxis(int axisCode) {
		return axisCode == XboxMappings.AXIS_LEFT_X;
	}


	@Override
	protected boolean isOutsideMovementDeadzone(float value) {
		return value > 0.25f || value < -0.25f;
	}


	@Override
	protected boolean isXAimAxis(int axisCode) {
		return axisCode == XboxMappings.AXIS_RIGHT_X;
	}


	@Override
	protected boolean isYAimAxis(int axisCode) {
		return axisCode == XboxMappings.AXIS_RIGHT_Y;
	}


	@Override
	protected boolean isLTriggerAxis(int axisCode, float value) {
		return axisCode == XboxMappings.TRIGGER_AXIS && value > 0;
	}


	@Override
	protected boolean isRTriggerAxis(int axisCode, float value) {
		return axisCode == XboxMappings.TRIGGER_AXIS && value < 0;
	}


	@Override
	protected boolean isOutsideJetpackTriggerDeadzone(float value) {
		return value > 0.5f;
	}


	@Override
	protected boolean isOutsideShootingTriggerDeadzone(float value) {
		return value < -0.5f;
	}
	
	@Override
	protected boolean isOutsideXAimDeadzone(float value) {
		return value > 0.05f || value < -0.05f;
	}

	@Override
	protected boolean isOustdieYAimDeadzone(float value) {
		return value > 0.05f || value < -0.05f;
	}

}
