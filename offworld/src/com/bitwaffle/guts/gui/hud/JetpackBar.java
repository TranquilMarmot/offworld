package com.bitwaffle.guts.gui.hud;

public class JetpackBar extends Bar {
	private static final int COLUMNS = 8;
	private static final int ROWS = 2;
	private static final float COLUMN_WIDTH = 12.0f;
	private static final float ROW_HEIGHT = 9.0f;
	private static final float[] BG_COLOR = { 0.3f, 0.3f, 0.3f, 0.3f };
	private static final float[] FILL_COLOR = { 0.0f, 0.0f, 1.0f, 1.0f};

	public JetpackBar(float x, float y) {
		super(x, y, COLUMNS, ROWS, COLUMN_WIDTH, ROW_HEIGHT, BG_COLOR, FILL_COLOR);
		this.setPercent(100.0f);
	}
}
