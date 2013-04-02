package com.bitwaffle.guts.gui.hud;

public class HealthBar extends Bar{
	
	private static final int COLUMNS = 8;
	private static final int ROWS = 2;
	private static final float COLUMN_WIDTH = 15.0f;
	private static final float ROW_HEIGHT = 10.0F;

	public HealthBar(float x, float y) {
		super(x, y, COLUMNS, ROWS, COLUMN_WIDTH, ROW_HEIGHT);
	}

}
