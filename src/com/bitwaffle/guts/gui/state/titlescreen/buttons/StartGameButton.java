package com.bitwaffle.guts.gui.state.titlescreen.buttons;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.guts.util.PhysicsHelper;

public class StartGameButton extends TextButton {

	public StartGameButton(float x, float y, float width, float height) {
		super("Start Game", x, y, width, height);
	}
	
	@Override
	public void update(float timeStep){
		
	}

	@Override
	protected void onRelease() {
		PhysicsHelper.temp(Game.physics);
		Game.gui.setCurrentState(GUI.States.MOVEMENT);
	}

	@Override
	protected void onSlideRelease() {
		
	}

	@Override
	protected void onPress() {
		
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Game.resources.textures.bindTexture("blankbutton");
		super.render(renderer, flipHorizontal, flipVertical);
	}
}
