package com.bitwaffle.guts.android;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.AndroidGUI;
import com.bitwaffle.offworld.gui.states.GUIStates;

public class AndroidGame extends Game {
	
	@Override
	protected void initGUI(){
		// android has it's own GUI
		gui = new AndroidGUI();
		gui.setCurrentState(GUIStates.TITLESCREEN);
	}
	
	@Override
	protected void initGDX(){
		super.initGDX();
    	Gdx.input.setCatchBackKey(true);
	}
}
