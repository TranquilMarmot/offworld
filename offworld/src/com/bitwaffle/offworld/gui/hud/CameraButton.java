package com.bitwaffle.offworld.gui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.Camera2D;
import com.bitwaffle.guts.gui.button.RectangleButton;
import com.bitwaffle.offworld.camera.CameraModes;

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
		if(CameraModes.follow.getTarget() == null && this.isVisible())
			this.hide();
		else if(CameraModes.follow.getTarget() != null && !this.isVisible())
			this.show();
	}
	
	@Override
	protected void onRelease() {
		Camera2D.CameraMode mode = Game.renderer.r2D.camera.currentMode();
		if(mode == CameraModes.follow)
			Game.renderer.r2D.camera.setMode(CameraModes.free);
		else
			Game.renderer.r2D.camera.setMode(CameraModes.follow);
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
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
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
		renderer.r2D.setColor(r, g, b, a);
		
		if(Game.renderer.r2D.camera.currentMode() == CameraModes.free)
			Game.resources.textures.bindTexture("camera-free");
		else
			Game.resources.textures.bindTexture("camera");
		renderer.r2D.quad.render(cameraButtonWidth, cameraButtonHeight);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
}