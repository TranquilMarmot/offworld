package com.bitwaffle.offworld.gui.titlescreen.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.gui.GUIStates;

public class StartGameButton extends TextButton {
	private static final String TEXT = "Start\nGame";
	
	float xOffset, yOffset;

	public StartGameButton(float xOffset, float yOffset, int rows, int columns, float rowWidth, float columnHeight) {
		super(TEXT, 30.0f,0.0f, 0.0f, rows, columns, rowWidth, columnHeight);
		
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
		OffworldGame.tempInit(Game.physics);
		Game.gui.setCurrentState(GUIStates.NONE.state);
	}

	@Override
	protected void onSlideRelease() {
		
	}

	@Override
	protected void onPress() {
		
	}
	
	@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}
	
	@Override
	protected void onDrag(float dx, float dy){}
	
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		float r = 0.5f;
		float g = 0.5f;
		float b = 0.5f;
		float a = 0.5f;
		if(this.isDown())
			a = 1.0f;
		else if(this.isSelected())
			a = 0.75f;
		renderer.r2D.setColor(r, g, b, a);

		super.renderBackground(renderer, flipHorizontal, flipVertical);		
		super.renderText(renderer, flipHorizontal, flipVertical);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}