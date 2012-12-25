package com.bitwaffle.guts.gui.states.movement.buttons.jump;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.states.movement.MovementGUIState;

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
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? MovementGUIState.pressedAlpha : MovementGUIState.activeAlpha);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		Game.resources.textures.getSubImage("uparrow").render(renderer.quad, this.width, this.height);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}