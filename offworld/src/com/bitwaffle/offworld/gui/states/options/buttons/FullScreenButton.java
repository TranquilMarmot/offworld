package com.bitwaffle.offworld.gui.states.options.buttons;

import com.bitwaffle.guts.gui.elements.button.BooleanButton;
import com.bitwaffle.guts.gui.elements.button.circle.CircleButton;
import com.bitwaffle.guts.gui.elements.button.circle.CircleButtonRenderer;

public class FullScreenButton extends CircleButton implements BooleanButton{
	
	boolean isFullScreen;

	public FullScreenButton(float x, float y, float radius) {
		super(new CircleButtonRenderer("circlebutton"), x, y, radius);
		
		
	}

	@Override
	public boolean getButtonState() {
		return isFullScreen;
	}

	@Override
	protected void onPress() {
	}

	@Override
	protected void onRelease() {
		//System.out.println(SurfaceView.game.getWindow() == null);
		//SurfaceView.game.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onSlideRelease() {
	}

	@Override
	public void update(float timeStep) {
	}

	@Override
	protected void onDrag(float dx, float dy) {
	}
	
		@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}
}
