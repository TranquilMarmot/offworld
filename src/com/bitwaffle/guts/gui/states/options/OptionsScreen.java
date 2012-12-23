package com.bitwaffle.guts.gui.states.options;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.passive.GLSLSandbox;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.options.buttons.FullScreenButton;

public class OptionsScreen extends GUIState {
	
	GLSLSandbox sandbox;
	
	public OptionsScreen(GUI gui) {
		super(gui);
		
		this.addButton(new FullScreenButton(Game.windowWidth / 2.0f, Game.windowHeight / 2.0f, 30));
		
		String text = "Back";
		float textScale  = 25.0f;
		int rows = 3;
		int columns = 2;
		float rowWidth = 25.0f;
		float columnHeight = 25.0f;
		float x = (rows * rowWidth) + 5.0f; 
		float y = Game.windowHeight - ((columns * columnHeight) * 2);
		
		this.addButton(new TextButton(text, textScale, x, y, rows, columns, rowWidth, columnHeight){

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
		});
		
		
		sandbox = new GLSLSandbox("shaders/sandbox/wiggle.frag");
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
	}
}
