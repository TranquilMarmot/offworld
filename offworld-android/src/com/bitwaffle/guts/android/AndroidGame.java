package com.bitwaffle.guts.android;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.AndroidGUI;
import com.bitwaffle.guts.gui.GUI;

public class AndroidGame extends Game {
	
	@Override
	protected void initGUI(){
		// android has it's own GUI
		gui = new AndroidGUI();
		gui.setCurrentState(GUI.States.TITLESCREEN);
	}
	
	@Override
	protected void initGDX(){
		super.initGDX();
    	Gdx.input.setCatchBackKey(true);
	}
}
