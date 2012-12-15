package com.bitwaffle.guts.gui.state.titlescreen;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.passive.GLSLSandbox;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.state.GUIState;
import com.bitwaffle.guts.gui.state.titlescreen.buttons.StartGameButton;

public class TitleScreen extends GUIState {
	
	private GLSLSandbox sandbox;
	
	public TitleScreen(GUI gui){
		super(gui);
		
		sandbox = new GLSLSandbox();
		this.addButton(new StartGameButton(Game.windowWidth / 2.0f, Game.windowHeight / 2.0f, 100.0f, 50.0f));
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		/*
		if(Game.player != null){
			this.loseCurrentState();
			gui.setCurrentState(GUI.States.MOVEMENT);
		}
		*/
	}
	

	@Override
	public void gainCurrentState(){
		super.gainCurrentState();
		
		Game.physics.addEntity(sandbox);
	}
	
	@Override
	public void loseCurrentState(){
		super.loseCurrentState();
		
		sandbox.removeFlag = true;
	}
	
	
}
