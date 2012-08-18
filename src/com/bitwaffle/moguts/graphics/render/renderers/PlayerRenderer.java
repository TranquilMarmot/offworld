package com.bitwaffle.moguts.graphics.render.renderers;

import android.opengl.Matrix;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.util.BufferUtils;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.entities.Player;

/**
 * Renders the player
 * 
 * @author TranquilMarmot
 */
class PlayerRenderer implements EntityRenderer{
	/** Location of legs (world coordinates) */
	private final float 
		LEGS_X_OFFSET = -0.14f,
		LEGS_Y_OFFSET = -0.945f;
	
	/** Location of body (world coordinates) */
	private final float
		BODY_X_OFFSET = 0.138f,
		BODY_Y_OFFSET = 0.84F;
	
	private final float
		ARM_X_OFFSET = 0.37f,
		ARM_Y_OFFSET = 0.35f;
	
	/** Scale everything is being drawn at (to fit into bounding box) */
	private final float SCALE = 0.95f;
	
	/** Used to preserve the model view between draws */
	private float[] oldMatrix;
	
	public PlayerRenderer(){
		oldMatrix = new float[16];
	}
	
	public void render(Render2D renderer, Entity ent){
		Player player = (Player) ent;
		renderer.program.setUniform("vColor", player.color[0], player.color[1], player.color[2], player.color[3]);
		
		boolean facingRight = player.isFacingRight();
		
		/* Render Legs */
		BufferUtils.deepCopyFloatArray(renderer.modelview, oldMatrix);
		Matrix.translateM(renderer.modelview, 0, facingRight ? LEGS_X_OFFSET : -LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		player.animation.renderCurrentFrame(renderer, 1.0f * SCALE, 0.902f * SCALE, facingRight, false);
		
		/* Render body */
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? BODY_X_OFFSET : -BODY_X_OFFSET, BODY_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerbody").draw(renderer.quad, 0.474f * SCALE, 1.0f * SCALE, !facingRight, true);
		
		/* Render arm */
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? ARM_X_OFFSET : -ARM_X_OFFSET, ARM_Y_OFFSET, 0.0f);
		Matrix.rotateM(renderer.modelview, 0, facingRight ? player.armAngle : -player.armAngle, 0.0f, 0.0f, 1.0f);
		
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerarm").draw(renderer.quad, 0.5f * SCALE, 0.317f * SCALE, !facingRight, true);
	}
}
