package com.bitwaffle.guts.android.gui.states.movement.buttons.left;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.states.movement.MovementGUIState;
import com.bitwaffle.guts.graphics.render.Renderer;

/**
 * Button to move left that stays on the left side of the screen
 */
public class LeftMoveLeftButton extends MoveLeftButton{
	public LeftMoveLeftButton() {
		super(  MovementGUIState.buttonWidth,
				Game.windowHeight - MovementGUIState.buttonHeight,
				MovementGUIState.buttonWidth,
				MovementGUIState.buttonHeight);
	}

	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		this.y = Game.windowHeight - MovementGUIState.buttonHeight;
	}
	
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.r2D.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? MovementGUIState.pressedAlpha : MovementGUIState.activeAlpha);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		Game.resources.textures.getSubImage("leftarrow").render(renderer, this.width, this.height, false, false);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

	@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}
}