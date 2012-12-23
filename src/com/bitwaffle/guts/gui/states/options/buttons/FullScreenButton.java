package com.bitwaffle.guts.gui.states.options.buttons;

import android.view.WindowManager;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.button.BooleanButton;
import com.bitwaffle.guts.gui.button.CircleButton;

public class FullScreenButton extends CircleButton implements BooleanButton{
	
	boolean isFullScreen;

	public FullScreenButton(float x, float y, float radius) {
		super(x, y, radius);
		
		
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
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Game.resources.textures.bindTexture("circlebutton");
		renderer.circle.render(this.getRadius(), flipHorizontal, flipVertical);
	}
}
