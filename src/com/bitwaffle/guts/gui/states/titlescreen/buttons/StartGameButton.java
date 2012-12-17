package com.bitwaffle.guts.gui.states.titlescreen.buttons;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.guts.util.PhysicsHelper;

public class StartGameButton extends TextButton {
	private static final String TEXT = "Start Game";
	
	float xOffset, yOffset;

	public StartGameButton(float xOffset, float yOffset, float width, float height) {
		super(TEXT, 0.0f, 0.0f, width, height);
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	@Override
	public void update(float timeStep){
		this.x = ((float)Game.windowWidth / 2.0f) + xOffset;
		this.y = ((float)Game.windowHeight / 2.0f) + yOffset;
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
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
		
		renderer.program.setUniform("vColor", 0.4f, 0.4f, 0.4f, 0.9f);
		super.renderBackground(renderer, flipHorizontal, flipVertical);
		
		super.renderText(renderer, flipHorizontal, flipVertical);
		
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
