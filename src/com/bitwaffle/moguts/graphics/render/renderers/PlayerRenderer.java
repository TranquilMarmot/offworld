package com.bitwaffle.moguts.graphics.render.renderers;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
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
class PlayerRenderer extends BoxRenderer{
	/** Offset of legs (world coordinates) */
	private final float 
		FORWARD_LEGS_X_OFFSET = -0.064f, // when the player is looking forwards
		BACKWARD_LEGS_X_OFFSET = -0.158f, // when the player is looking backwards
		LEGS_Y_OFFSET = -0.94f;
	
	/** Location of body (world coordinates) */
	private final float
		BODY_X_OFFSET = 0.138f,
		BODY_Y_OFFSET = 0.94f;
	
	/** Location of arms */
	private final float
		L_ARM_X_OFFSET = 0.1f,
		L_ARM_Y_OFFSET = 0.75f,
		R_ARM_X_OFFSET = 0.15f,
		R_ARM_Y_OFFSET = 0.7f,
		ARM_ROTATION_X_OFFSET = 0.33f,
		ARM_ROTATION_Y_OFFSET = -0.3f;
	
	/** Scale everything is being drawn at (to fit into bounding box) */
	private final float SCALE = 0.95f;
	
	/** Used to preserve the model view between draws */
	private float[] oldMatrix;
	
	public PlayerRenderer(){
		oldMatrix = new float[16];
	}
	
	@Override
	public void render(Render2D renderer, Entity ent){
		Player player = (Player) ent;
		renderer.program.setUniform("vColor", player.color[0], player.color[1], player.color[2], player.color[3]);
		
		// so the player doesn't have to keep getting polled
		boolean movingRight = player.isMovingRight();
		boolean facingRight = player.isFacingRight();
		float armAngle = player.getArmAngle();
		
		/*-- Render right arm --*/
		BufferUtils.deepCopyFloatArray(renderer.modelview, oldMatrix);
		Matrix.translateM(renderer.modelview, 0, facingRight ? R_ARM_X_OFFSET : -R_ARM_X_OFFSET, R_ARM_Y_OFFSET, 0.0f);
		Matrix.rotateM(renderer.modelview, 0, armAngle, 0.0f, 0.0f, 1.0f);
		Matrix.translateM(renderer.modelview, 0, ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerarm").render(renderer.quad, 0.5f * SCALE, 0.317f * SCALE, !facingRight, facingRight);
		
		/*-- Render Legs --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		// if movingRight and facingRight are the same, it means the player is moving in the direction it's looking
		if(movingRight == facingRight)
			Matrix.translateM(renderer.modelview, 0, movingRight ? FORWARD_LEGS_X_OFFSET : -FORWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f);
		// else the legs are moving in the opposite direction as the player is looking
		else 
			Matrix.translateM(renderer.modelview, 0, movingRight ? BACKWARD_LEGS_X_OFFSET : -BACKWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		player.legsAnimation.renderCurrentFrame(renderer, 1.2f * SCALE, 1.0f * SCALE, movingRight, false);
		
		/*-- Render body --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? BODY_X_OFFSET : -BODY_X_OFFSET, BODY_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerbody").render(renderer.quad, 0.474f * SCALE, 1.0f * SCALE, !facingRight, true);
		
		/*-- Render left arm --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? L_ARM_X_OFFSET : -L_ARM_X_OFFSET, L_ARM_Y_OFFSET, 0.0f);
		Matrix.rotateM(renderer.modelview, 0, armAngle, 0.0f, 0.0f, 1.0f);
		Matrix.translateM(renderer.modelview, 0, ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerarm").render(renderer.quad, 0.5f * SCALE, 0.317f * SCALE, !facingRight, facingRight);
	}
	
	@Override
	public void renderDebug(Render2D renderer, Entity ent){
		super.renderDebug(renderer, ent);
		
		/*
		 * Render a box where the player's current target is (under the finger)
		 */
		boolean shooting = ((Player) ent).isShooting();
		Vector2 targetLoc = ((Player) ent).getCurrentTarget();
		
		// the box is given a different color based on whether or not the player is shooting
		renderer.program.setUniform("vColor", shooting ? 0.25f : 0.0f, 0.0f, shooting ? 0.75f : 1.0f, 0.75f);
		GLES20.glEnable(GLES20.GL_BLEND); // also enable blending for some swell transparency
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);

		// reset the modelview and translate it to the right spot
		Matrix.setIdentityM(renderer.modelview, 0);
		renderer.translateModelViewToCamera();
		Matrix.translateM(renderer.modelview, 0, targetLoc.x, targetLoc.y, 0.0f);
		renderer.quad.draw(0.75f, 0.75f);
		
		GLES20.glDisable(GLES20.GL_BLEND); // don't forget to turn blending back off!
	}
}
