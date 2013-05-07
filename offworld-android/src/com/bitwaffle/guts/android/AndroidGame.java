package com.bitwaffle.guts.android;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.AndroidGUI;
import com.bitwaffle.offworld.gui.states.GUIStates;

/**
 * Game class with special stuff for android
 * 
 * @author TranquilMarmot
 */
public class AndroidGame extends Game {
	
	@Override
	protected void initGUI(){
		gui = new AndroidGUI();
		gui.setCurrentState(GUIStates.SPLASH);
	}
	
	@Override
	protected void initGDX(){
		super.initGDX();
    	Gdx.input.setCatchBackKey(true);
	}
}
