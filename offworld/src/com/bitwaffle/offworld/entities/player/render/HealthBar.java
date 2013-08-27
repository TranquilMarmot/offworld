package com.bitwaffle.offworld.entities.player.render;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.offworld.gui.elements.bar.Bar;

public class HealthBar extends Bar{
	private static final int COLUMNS = 8;
	private static final int ROWS = 2;
	private static final float COLUMN_WIDTH = 14.0f;
	private static final float ROW_HEIGHT = 10.0F;
	private static final Color 
							BG_COLOR = new Color( 0.3f, 0.3f, 0.3f, 0.3f ),
							FILL_COLOR =  new Color( 0.0f, 1.0f, 0.0f, 1.0f );

	public HealthBar(float x, float y) {
		super(x, y, COLUMNS, ROWS, COLUMN_WIDTH, ROW_HEIGHT, BG_COLOR, FILL_COLOR);
		this.setPercent(100.0f);
	}

}
