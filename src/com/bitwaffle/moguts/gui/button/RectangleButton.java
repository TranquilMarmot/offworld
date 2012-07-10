package com.bitwaffle.moguts.gui.button;

import com.badlogic.gdx.math.Rectangle;

public class RectangleButton extends Button {
	Rectangle r;
	
	public RectangleButton(float x, float y, float width, float height) {
		super(x, y);
		r = new Rectangle(x, y, width, height);
	}

	@Override
	public void actionPerformed() {
		
	}

	@Override
	public boolean checkForPress(float x, float y) {
		
		return false;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void cleanup() {
		
	}

}
