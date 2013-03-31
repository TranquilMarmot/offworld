package com.bitwaffle.guts.gui.states.titlescreen;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.passive.GLSLSandbox;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.titlescreen.buttons.OptionsButton;
import com.bitwaffle.guts.gui.states.titlescreen.buttons.StartGameButton;

public class TitleScreen extends GUIState {
	/** Alpha values of buttons */
	public static float activeAlpha = 0.8f, pressedAlpha = 0.95f;
	
	/** Super fancy gfx */
	private GLSLSandbox sandbox;
	
	/** Button to start the game */
	private StartGameButton startGameButton;
	/** Button to go to options menu */
	private OptionsButton optionsButton;
	/** Fancy spinning pizza */
	private SpinningPizza spinningPizza;
	
	public TitleScreen(){
		super();
		
		sandbox = new GLSLSandbox();
		
		startGameButton = new StartGameButton(325.0f, -100.0f, 4, 2, 30.0f, 30.0f);
		optionsButton = new OptionsButton(325.0f, 50.0f, 4, 2, 30.0f, 30.0f);
		spinningPizza = new SpinningPizza((Game.windowWidth + Game.windowHeight) / 7.0f, -300.0f, 0.0f);
		
		startGameButton.toDown = optionsButton;
		optionsButton.toUp = startGameButton;
		
		this.addButton(startGameButton);
		this.addButton(optionsButton);
		this.addObject(spinningPizza);
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
	}
	

	@Override
	public void gainCurrentState(){
		super.gainCurrentState();
		
		Game.physics.addEntity(sandbox, false);
		Game.gui.selectedButton = startGameButton;
		startGameButton.select();
	}
	
	@Override
	public void loseCurrentState(){
		super.loseCurrentState();
		
		Game.physics.removeEntity(sandbox, false);
	}
	
	
}
