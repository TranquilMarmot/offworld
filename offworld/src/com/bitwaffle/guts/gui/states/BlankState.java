package com.bitwaffle.guts.gui.states;

import com.bitwaffle.guts.gui.button.Button;

public class BlankState extends GUIState {
	@Override
	protected void onGainCurrentState() {}

	@Override
	protected void onLoseCurrentState() {}

	@Override
	public Button initialLeftButton() { return null; }

	@Override
	public Button initialRightButton() { return null; }

	@Override
	public Button initialUpButton() { return null; }

	@Override
	public Button initialDownButton() { return null; }
}
