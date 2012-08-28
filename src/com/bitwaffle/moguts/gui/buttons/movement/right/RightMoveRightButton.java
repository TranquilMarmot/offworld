package com.bitwaffle.moguts.gui.buttons.movement.right;

import android.opengl.GLES20;

import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.buttons.ButtonManager;
import com.bitwaffle.offworld.Game;

/**
 * Button to move right that stays on the left side of the screen
 */
public class RightMoveRightButton extends MoveRightButton{
	public RightMoveRightButton(ButtonManager buttMan) {
		super(buttMan,
				Game.windowWidth - buttMan.movementButtonWidth(),
				Game.windowHeight - buttMan.movementButtonHeight(), 
				buttMan.movementButtonWidth(),
				buttMan.movementButtonHeight());
	}
	
	@Override
	public void update(){
		super.update();
		
		this.x = Game.windowWidth - buttMan.movementButtonWidth();
		this.y = Game.windowHeight - buttMan.movementButtonHeight();
	}
	
	@Override
	public void render(Render2D renderer){
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? buttMan.pressedAlpha() : buttMan.activeAlpha());
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		Game.resources.textures.getSubImage("rightarrow").draw(renderer.quad, this.width, this.height);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
