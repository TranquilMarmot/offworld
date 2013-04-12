package com.bitwaffle.guts.gui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.gui.button.RectangleButton;

/** Button to change camera modes */
public class CameraButton extends RectangleButton{
	/** How large the camera button is */
	public static float cameraButtonWidth = 32.0f, cameraButtonHeight = 32.0f;
	
	public CameraButton() {
		super(
				cameraButtonWidth + 1.0f,
				cameraButtonHeight,
				cameraButtonWidth,
				cameraButtonHeight);
	}

	@Override
	public void update(float timeStep) {
		if(Render2D.camera.getTarget() == null && this.isVisible())
			this.hide();
		else if(Render2D.camera.getTarget() != null && !this.isVisible())
			this.show();
	}
	
	@Override
	protected void onRelease() {
		Camera.Modes mode = Render2D.camera.currentMode();
		if(mode == Camera.Modes.FOLLOW)
			Render2D.camera.setMode(Camera.Modes.FREE);
		else
			Render2D.camera.setMode(Camera.Modes.FOLLOW);
	}
	
	@Override
	protected void onSlideRelease() {}
	@Override
	protected void onDrag(float dx, float dy){}
	@Override
	protected void onPress() {
		//Game.vibration.vibrate(25);
	}
	@Override
	protected void onSelect() {}
	@Override
	protected void onUnselect() {}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
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
		renderer.program.setUniform("vColor", r, g, b, a);
		
		if(Render2D.camera.currentMode() == Camera.Modes.FREE)
			Game.resources.textures.bindTexture("camera-free");
		else
			Game.resources.textures.bindTexture("camera");
		renderer.quad.render(cameraButtonWidth, cameraButtonHeight);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
}
