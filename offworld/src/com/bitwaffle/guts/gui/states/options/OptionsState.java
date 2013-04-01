package com.bitwaffle.guts.gui.states.options;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.passive.GLSLSandbox;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.options.buttons.FullScreenButton;

public class OptionsState extends GUIState {
	
	GLSLSandbox sandbox;
	
	FullScreenButton fullscreenButton;
	
	TextButton backButton;
	
	public OptionsState() {
		super();
		
		fullscreenButton = new FullScreenButton(Game.windowWidth / 2.0f, Game.windowHeight / 2.0f, 30);
		this.addButton(fullscreenButton);
		
		String text = "Back";
		float textScale  = 25.0f;
		int rows = 3;
		int columns = 2;
		float rowWidth = 25.0f;
		float columnHeight = 25.0f;
		float x = (rows * rowWidth) + 5.0f; 
		float y = Game.windowHeight - ((columns * columnHeight) * 2);
		
		backButton = new TextButton(text, textScale, x, y, rows, columns, rowWidth, columnHeight){
			@Override
			protected void onRelease() {
				Game.gui.setCurrentState(GUI.States.TITLESCREEN);
			}

			@Override
			protected void onSlideRelease() {}

			@Override
			protected void onPress() {}

			@Override
			protected void onDrag(float dx, float dy) {}

			@Override
			public void update(float timeStep) {}
			
			@Override
			protected void onSelect() {}

			@Override
			protected void onUnselect() {}
		};
		
		this.addButton(backButton);
		
		
		sandbox = new GLSLSandbox("shaders/sandbox/wiggle.frag");
	}
	
	@Override
	protected void onGainCurrentState() {
		Game.physics.addEntity(sandbox, false);
		
		Game.gui.selectedButton.unselect();
		Game.gui.selectedButton = backButton;
		Game.gui.selectedButton.select();
	}

	@Override
	protected void onLoseCurrentState() {
		Game.physics.removeEntity(sandbox, false);
	}

	@Override
	public Button initialLeftButton() {
		
		return null;
	}

	@Override
	public Button initialRightButton() {
		
		return null;
	}

	@Override
	public Button initialUpButton() {
		
		return null;
	}

	@Override
	public Button initialDownButton() {
		
		return null;
	}
}
