package com.bitwaffle.guts.gui.buttons.movement.jump;

import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.buttons.movement.MovementButtonManager;

/**
 * Button to jump that stays on the right side of the screen
 */
public class RightJumpButton extends JumpButton{
	public RightJumpButton(MovementButtonManager buttMan) {
		super(buttMan,
				Game.windowWidth - buttMan.movementButtonWidth(),
				Game.windowHeight - (buttMan.movementButtonHeight() * 3.0f),
				buttMan.movementButtonWidth(), 
				buttMan.movementButtonHeight());
	}
	
	@Override
	public void update(){
		this.x = Game.windowWidth - buttMan.movementButtonWidth();
		this.y = Game.windowHeight - (buttMan.movementButtonHeight() * 3.0f);
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? buttMan.pressedAlpha() : buttMan.activeAlpha());
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		Game.resources.textures.getSubImage("uparrow").draw(renderer.quad, this.width, this.height);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
	
}