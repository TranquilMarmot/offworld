package com.bitwaffle.offworld.input;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.input.Input;
import com.bitwaffle.guts.input.gui.GUIInputListener;

public class OffworldInput extends Input {
	@Override
	protected GUIInputListener getGUIInputListener(){
		return new OffworldGUIInputListener(Game.gui);
	}
}
