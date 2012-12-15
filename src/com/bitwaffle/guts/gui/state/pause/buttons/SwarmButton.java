package com.bitwaffle.guts.gui.state.pause.buttons;

import android.app.Activity;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.button.RectangleButton;
import com.bitwaffle.guts.gui.state.pause.PauseGUIState;
import com.bitwaffle.guts.swarm.LoginListener;
import com.bitwaffle.guts.swarm.SwarmConsts;
import com.swarmconnect.Swarm;

public class SwarmButton extends RectangleButton {
	// offset of button from center
	public static float xOffset = -150.0f, yOffset = -150.0f;

	public SwarmButton(PauseGUIState buttMan) {
		super(
				(Game.windowWidth / 2.0f) + xOffset,
				(Game.windowHeight / 2.0f) + yOffset,
				buttMan.pauseMenuButtonWidth(),
				buttMan.pauseMenuButtonHeight());
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
	protected void onPress() {
		Game.vibration.vibrate(25);
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Game.resources.textures.bindTexture("blank");
		super.render(renderer, flipHorizontal, flipVertical);
		renderer.font.drawString("Swarm", renderer, x, y + 17.0f, 0.3f);
	}
}
