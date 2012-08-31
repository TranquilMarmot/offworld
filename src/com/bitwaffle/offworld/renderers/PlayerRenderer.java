package com.bitwaffle.offworld.renderers;

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
public class PlayerRenderer extends BoxRenderer{
	/*
	 * NOTE:
	 * The 'SCALE' variables are usually found out by dividing the image's
	 * width by its height (or the other way around, depending).
	 * This needs to be done because the quad being used to render the images
	 * is 1 unit x 1 unit (in world space) and needs to be scaled to match the
	 * ratio of the image being drawn (otherwise the image gets drawn as a square).
	 */
	
	/** Offset of legs (world coordinates) */
	private final float 
		FORWARD_LEGS_X_OFFSET = -0.064f, // when the player is looking forwards
		BACKWARD_LEGS_X_OFFSET = -0.158f, // when the player is looking backwards
		LEGS_Y_OFFSET = -0.94f,
		LEGS_X_SCALE = 1.2f,
		LEGS_Y_SCALE = 1.0f;
	
	/** Location of body (world coordinates) */
	private final float
		BODY_X_OFFSET = 0.138f,
		BODY_Y_OFFSET = 0.94f,
		BODY_X_SCALE = 0.474f,
		BODY_Y_SCALE = 1.0f;
	
	/** Location of arms */
	private final float
		L_ARM_X_OFFSET = 0.1f,
		L_ARM_Y_OFFSET = 0.75f,
		R_ARM_X_OFFSET = 0.15f,
		R_ARM_Y_OFFSET = 0.7f,
		ARM_ROTATION_X_OFFSET = 0.33f,
		ARM_ROTATION_Y_OFFSET = -0.3f,
		ARM_X_SCALE = 0.5f,
		ARM_Y_SCALE = 0.317f;
	
	/** Scale everything is being drawn at (to fit into bounding box) */
	private final float SCALE = 0.95f;
	
	/** Used to preserve the modelview between draws */
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
		BufferUtils.deepCopyFloatArray(renderer.modelview, oldMatrix); // save the modelview for repeated drawing at the same spot
		Matrix.translateM(renderer.modelview, 0, facingRight ? R_ARM_X_OFFSET : -R_ARM_X_OFFSET, R_ARM_Y_OFFSET, 0.0f);
		Matrix.rotateM(renderer.modelview, 0, armAngle, 0.0f, 0.0f, 1.0f);
		Matrix.translateM(renderer.modelview, 0, ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerarm").render(renderer.quad, ARM_X_SCALE * SCALE, ARM_Y_SCALE * SCALE, !facingRight, facingRight);
		
		/*-- Render Legs --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		// if movingRight and facingRight are the same, it means the player is moving in the direction it's looking
		if(movingRight == facingRight)
			Matrix.translateM(renderer.modelview, 0, movingRight ? FORWARD_LEGS_X_OFFSET : -FORWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f);
		// else the legs are moving in the opposite direction as the player is looking
		else 
			Matrix.translateM(renderer.modelview, 0, movingRight ? BACKWARD_LEGS_X_OFFSET : -BACKWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		player.legsAnimation.renderCurrentFrame(renderer, LEGS_X_SCALE * SCALE, LEGS_Y_SCALE * SCALE, movingRight, false);
		
		/*-- Render body --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? BODY_X_OFFSET : -BODY_X_OFFSET, BODY_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerbody").render(renderer.quad, BODY_X_SCALE * SCALE, BODY_Y_SCALE * SCALE, !facingRight, true);
		
		/*-- Render left arm --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? L_ARM_X_OFFSET : -L_ARM_X_OFFSET, L_ARM_Y_OFFSET, 0.0f);
		Matrix.rotateM(renderer.modelview, 0, armAngle, 0.0f, 0.0f, 1.0f);
		Matrix.translateM(renderer.modelview, 0, ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerarm").render(renderer.quad, ARM_X_SCALE * SCALE, ARM_Y_SCALE * SCALE, !facingRight, facingRight);
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
		GLES20.glEnable(GLES20.GL_BLEND); // enable blending for some swell transparency
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);

		// reset the modelview and translate it to the right spot
		Matrix.setIdentityM(renderer.modelview, 0);
		renderer.translateModelViewToCamera();
		Matrix.translateM(renderer.modelview, 0, targetLoc.x, targetLoc.y, 0.0f);
		renderer.quad.draw(0.75f, 0.75f);
		
		GLES20.glDisable(GLES20.GL_BLEND); // don't forget to turn blending back off!
	}
}
