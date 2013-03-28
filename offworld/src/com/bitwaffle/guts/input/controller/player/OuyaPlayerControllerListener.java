package com.bitwaffle.guts.input.controller.player;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.bitwaffle.guts.input.KeyBindings;
import com.bitwaffle.offworld.entities.player.Player;

public class OuyaPlayerControllerListener extends PlayerControllerListener {

	public OuyaPlayerControllerListener(Player player) {
		super(player);
	}

	@Override
	protected KeyBindings getBinding(Controller controller, int buttonCode) {
		return null;
	}

	@Override
	protected boolean isJump(int buttonCode) {
		return buttonCode == Ouya.BUTTON_O;
	}

	@Override
	protected boolean isMovementAxis(int axisCode) {
		return axisCode == Ouya.AXIS_LEFT_X;
	}

	@Override
	protected float movementThreshold() {
		return 0.25f;
	}

	@Override
	protected boolean isXAimAxis(int axisCode) {
		return axisCode == Ouya.AXIS_RIGHT_X;
	}

	@Override
	protected boolean isYAimAxis(int axisCode) {
		return axisCode == Ouya.AXIS_RIGHT_Y;
	}

	@Override
	protected boolean isLTriggerAxis(int axisCode, float value) {
		return axisCode == Ouya.AXIS_LEFT_TRIGGER;
	}

	@Override
	protected boolean isRTriggerAxis(int axisCode, float value) {
		return axisCode == Ouya.AXIS_RIGHT_TRIGGER;
	}

	@Override
	protected boolean isPastJetpackTriggerThreshold(float value) {
		return value >= 0.5f;
	}

	@Override
	protected boolean isPastShootingTriggerThreshold(float value) {
		return value >= 0.5f;
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
