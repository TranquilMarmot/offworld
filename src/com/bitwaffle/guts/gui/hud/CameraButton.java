package com.bitwaffle.guts.gui.hud;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.button.RectangleButton;

/**
 * Button to change camera modes
 */
public class CameraButton extends RectangleButton{
	/** How large the camera button is */
	public static float cameraButtonWidth = 32.0f, cameraButtonHeight = 32.0f;
	
	public CameraButton() {
		super(
				cameraButtonWidth + 1.0f,
				cameraButtonHeight,
				cameraButtonWidth,
				cameraButtonHeight);
		
		this.active[0] = 0.1f;
		this.active[1] = 0.75f;
		this.active[2] = 0.1f;
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
		Game.vibration.vibrate(25);
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
		renderer.program.setUniform("vColor", 0.5f, 0.5f, 0.5f, this.isDown() ? 1.0f : 0.5f);
		
		if(Render2D.camera.currentMode() == Camera.Modes.FREE)
			Game.resources.textures.bindTexture("camera-free");
		else
			Game.resources.textures.bindTexture("camera");
		renderer.quad.render(cameraButtonWidth, cameraButtonHeight);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
	
}
