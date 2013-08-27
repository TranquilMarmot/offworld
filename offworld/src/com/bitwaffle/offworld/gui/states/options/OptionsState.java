package com.bitwaffle.offworld.gui.states.options;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.passive.GLSLSandbox;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.elements.button.TextButtonRenderer;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.offworld.gui.states.GUIStates;
import com.bitwaffle.offworld.gui.states.options.buttons.FullScreenButton;

public class OptionsState extends GUIState {
	
	GLSLSandbox sandbox;
	
	FullScreenButton fullscreenButton;
	
	RectangleButton backButton;
	
	public OptionsState(GUI gui) {
		super(gui);
		
		fullscreenButton = new FullScreenButton(Game.windowWidth / 2.0f, Game.windowHeight / 2.0f, 30);
		this.addButton(fullscreenButton);
		
		String text = "Back";
		float textScale  = 25.0f;
		int columns = 3;
		int rows = 2;
		float columnWidth = 25.0f;
		float rowHeight = 25.0f;
		float x = (columns * columnWidth) + 5.0f; 
		float y = Game.windowHeight - ((rows * rowHeight) * 2);
		
		backButton = new RectangleButton(
				new TextButtonRenderer(
						new Color(0.5f, 0.5f, 0.5f, 0.75f), 
						new Color(0.5f, 0.5f, 0.5f, 1.0f),
						new Color(0.75f, 0.75f, 0.75f, 1.0f),
						new Color(0.5f, 0.5f, 0.5f, 0.8f),
						text, textScale,
						rows, columns,
						columnWidth, rowHeight), 
				x, y, 
				columns * columnWidth, rows * rowHeight){
			@Override
			protected void onRelease() {
				Game.gui.setCurrentState(GUIStates.TITLESCREEN.state);
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
		
		Game.gui.setSelectedButton(backButton);
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
