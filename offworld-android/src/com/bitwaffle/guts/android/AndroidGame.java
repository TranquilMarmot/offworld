package com.bitwaffle.guts.android;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.android.gui.AndroidGUI;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.offworld.OffworldGame;

/**
 * Game class with special stuff for android
 * 
 * @author TranquilMarmot
 */
public class AndroidGame extends OffworldGame {
	
	@Override
	protected GUI initGUI(){
		return new AndroidGUI();
	}
	
	@Override
	protected void initGDX(){
		super.initGDX();
    	Gdx.input.setCatchBackKey(true);
	}
}
