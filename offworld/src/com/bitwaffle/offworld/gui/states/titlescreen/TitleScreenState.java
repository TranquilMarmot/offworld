package com.bitwaffle.offworld.gui.states.titlescreen;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.offworld.gui.states.titlescreen.buttons.OptionsButton;
import com.bitwaffle.offworld.gui.states.titlescreen.buttons.StartGameButton;

public class TitleScreenState extends GUIState {
	/** Alpha values of buttons */
	public static float activeAlpha = 0.8f, pressedAlpha = 0.95f;
	
	/** Super fancy gfx */
	//private GLSLSandbox sandbox;
	
	/** Button to start the game */
	private StartGameButton startGameButton;
	/** Button to go to options menu */
	private OptionsButton optionsButton;
	/** Fancy spinning pizza */
	private SpinningPizza spinningPizza;
	
	public TitleScreenState(GUI gui){
		super(gui);
		
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
	protected void onGainCurrentState() {
		Gdx.gl20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		Game.gui.setSelectedButton(startGameButton);
	}

	@Override
	protected void onLoseCurrentState() {
		Game.gui.setSelectedButton(null);
	}

	@Override
	public Button initialLeftButton() {
		return optionsButton;
	}

	@Override
	public Button initialRightButton() {
		return startGameButton;
	}

	@Override
	public Button initialUpButton() {
		return optionsButton;
	}

	@Override
	public Button initialDownButton() {
		return startGameButton;
	}
}
