package com.bitwaffle.guts.gui.states.titlescreen;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.passive.GLSLSandbox;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.titlescreen.buttons.OptionsButton;
import com.bitwaffle.guts.gui.states.titlescreen.buttons.StartGameButton;

public class TitleScreen extends GUIState {
	/** Alpha values of buttons */
	public static float activeAlpha = 0.8f, pressedAlpha = 0.95f;
	
	private GLSLSandbox sandbox;
	
	public TitleScreen(GUI gui){
		super(gui);
		
		sandbox = new GLSLSandbox();
		
		this.addButton(new StartGameButton(325.0f, -100.0f, 150.0f, 50.0f));
		this.addButton(new OptionsButton(325.0f, 50.0f, 150.0f, 50.0f));
		this.addObject(new SpinningPizza((Game.windowWidth + Game.windowHeight) / 7.0f, -300.0f, 0.0f));
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
		
		Game.physics.removeEntity(sandbox);
		
		//sandbox.removeFlag = true;
	}
	
	
}
