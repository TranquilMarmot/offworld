package com.bitwaffle.guts.input.listeners.controller.gui;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.input.listeners.controller.XboxMappings;

public class XboxGUIControllerListener extends GUIControllerListener {
	public XboxGUIControllerListener(GUI gui) {
		super(gui);
	}

	@Override
	protected boolean isConfirmButton(int buttonCode) {
		return buttonCode == XboxMappings.BUTTON_A;
	}

	@Override
	protected boolean isSelectXAxis(int axisCode) {
		return axisCode == XboxMappings.AXIS_LEFT_X;
	}

	@Override
	protected boolean isSelectYAxis(int axisCode) {
		return axisCode == XboxMappings.AXIS_LEFT_Y;
	}

	@Override
	protected boolean isOutsideSelectDeadzone(float value) {
		return value > 0.25f || value < -0.25f;
	}

}
