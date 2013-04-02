package com.bitwaffle.guts.input.listeners.controller.gui;

import com.badlogic.gdx.controllers.mappings.Ouya;
import com.bitwaffle.guts.gui.GUI;

public class OuyaGUIControllerListener extends GUIControllerListener {

	public OuyaGUIControllerListener(GUI gui) {
		super(gui);
	}

	@Override
	protected boolean isConfirmButton(int buttonCode) {
		return buttonCode == Ouya.BUTTON_O;
	}

	@Override
	protected boolean isSelectXAxis(int axisCode) {
		return axisCode == Ouya.AXIS_LEFT_X;
	}

	@Override
	protected boolean isSelectYAxis(int axisCode) {
		return axisCode == Ouya.AXIS_LEFT_Y;
	}

	@Override
	protected boolean isOutsideSelectDeadzone(float value) {
		return value > 0.25f || value < -0.25f;
	}

}
