package com.bitwaffle.guts.android.gui.states.movement.buttons.jump;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.states.movement.MovementGUIState;

/**
 * Button to jump that stays on the left side of the screen
 */
public class LeftJumpButton extends JumpButton{
	public LeftJumpButton(){
		super(
				MovementGUIState.buttonWidth,
				Game.windowHeight - (MovementGUIState.buttonHeight * 3.0f),
				MovementGUIState.buttonWidth,
				MovementGUIState.buttonHeight);
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		this.y = Game.windowHeight - (MovementGUIState.buttonHeight * 3.0f);
	}
	
	/*
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.r2D.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? MovementGUIState.pressedAlpha : MovementGUIState.activeAlpha);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		Game.resources.textures.getSubImage("uparrow").render(renderer, this.width, this.height);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	*/

	@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}
}
