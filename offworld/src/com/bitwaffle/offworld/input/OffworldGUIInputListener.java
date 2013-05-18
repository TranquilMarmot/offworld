package com.bitwaffle.offworld.input;

import com.badlogic.gdx.Input;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.input.gui.GUIInputListener;
import com.bitwaffle.offworld.gui.GUIStates;

public class OffworldGUIInputListener extends GUIInputListener {
	
	public OffworldGUIInputListener(GUI gui){
		super(gui);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.ESCAPE &&
		  !Game.gui.isCurrentState(GUIStates.TITLESCREEN.state) &&
		  !Game.gui.isCurrentState(GUIStates.OPTIONS.state)){
			Game.togglePause();
			return true;
		}
		
		if(!gui.isCurrentState(GUIStates.NONE.state))
			return super.keyDown(keycode);
		else
			return false;
	}

}
