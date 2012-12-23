package com.bitwaffle.guts.gui.states.pause.buttons;

import android.app.Activity;
import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.button.TextButton;
import com.bitwaffle.guts.gui.states.pause.PauseGUIState;
import com.bitwaffle.guts.swarm.LoginListener;
import com.bitwaffle.guts.swarm.SwarmConsts;
import com.swarmconnect.Swarm;

public class SwarmButton extends TextButton {
	// offset of button from center
	public static float xOffset = -150.0f, yOffset = -150.0f;

	public SwarmButton(PauseGUIState state) {
		super(
				"Swarm",
				25.0f,
				(Game.windowWidth / 2.0f) + xOffset,
				(Game.windowHeight / 2.0f) + yOffset,
				state.buttonRows(),
				state.buttonCols(),
				state.buttonRowWidth(),
				state.buttonColHeight());
	}

	@Override
	public void update(float timeStep) {
		this.x = (Game.windowWidth / 2.0f) + xOffset;
		this.y = (Game.windowHeight / 2.0f) + yOffset;
	}

	@Override
	protected void onRelease() {
		if(!Swarm.isInitialized())
			Swarm.init((Activity)Game.context, SwarmConsts.App.APP_ID, SwarmConsts.App.APP_AUTH, new LoginListener());
		Swarm.showDashboard();
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
		
		renderer.program.setUniform("vColor", 0.4f, 0.4f, 0.4f, 0.9f);
		super.renderBackground(renderer, flipHorizontal, flipVertical);
		
		super.renderText(renderer, flipHorizontal, flipVertical);
		
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
