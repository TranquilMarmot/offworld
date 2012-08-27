package com.bitwaffle.moguts.gui.button;

import com.bitwaffle.moguts.graphics.render.Render2D;

public interface ButtonAction {
	public void update(); //FIXME is this necessary?
	public void render(Render2D renderer);
	public void onPress();
	public void onRelease();
	public void onSlideRelease();
}