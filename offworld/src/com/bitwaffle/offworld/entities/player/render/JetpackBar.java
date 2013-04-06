package com.bitwaffle.offworld.entities.player.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.hud.Bar;

public class JetpackBar extends Bar {
	private static final int COLUMNS = 8;
	private static final int ROWS = 2;
	private static final float COLUMN_WIDTH = 8.0f;
	private static final float ROW_HEIGHT = 4.0f;
	private static final float[] BG_COLOR = { 0.3f, 0.3f, 0.3f, 0.5f };
	private static final float[] FILL_COLOR = { 0.0f, 0.0f, 1.0f, 0.8f};

	public JetpackBar(float x, float y) {
		super(x, y, COLUMNS, ROWS, COLUMN_WIDTH, ROW_HEIGHT, BG_COLOR, FILL_COLOR);
		this.setPercent(100.0f);
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		super.render(renderer, flipHorizontal, flipVertical);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}
