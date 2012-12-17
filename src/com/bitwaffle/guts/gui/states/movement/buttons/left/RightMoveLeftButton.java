package com.bitwaffle.guts.gui.states.movement.buttons.left;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.states.movement.MovementGUIState;

/**
 * Button to move left that stays on the right side of the screen
 */
public class RightMoveLeftButton extends MoveLeftButton{
	public RightMoveLeftButton() {
		super(Game.windowWidth - (MovementGUIState.buttonWidth * 3.0f),
				Game.windowHeight - MovementGUIState.buttonHeight,
				MovementGUIState.buttonWidth, 
				MovementGUIState.buttonHeight);
	}

	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		this.x = Game.windowWidth - (MovementGUIState.buttonWidth * 3.0f);
		this.y = Game.windowHeight - MovementGUIState.buttonHeight;
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? MovementGUIState.pressedAlpha : MovementGUIState.activeAlpha);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		Game.resources.textures.getSubImage("rightarrow").render(renderer.quad, this.width, this.height, true, false);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}