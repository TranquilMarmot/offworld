package com.bitwaffle.moguts.graphics.render.renderers;

import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.util.BufferUtils;
import com.bitwaffle.moguts.util.MathHelper;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.entities.Player;

/**
 * Renders the player
 * 
 * @author TranquilMarmot
 */
class PlayerRenderer implements EntityRenderer{
	/** Offset of legs (world coordinates) */
	private final float 
		FORWARD_LEGS_X_OFFSET = -0.14f,
		BACKWARD_LEGS_X_OFFSET = -0.23f,
		LEGS_Y_OFFSET = -0.945f;
	
	/** Location of body (world coordinates) */
	private final float
		BODY_X_OFFSET = 0.138f,
		BODY_Y_OFFSET = 0.84F;
	
	/** Location of arms */
	private final float
		L_ARM_X_OFFSET = 0.37f,
		L_ARM_Y_OFFSET = 0.35f,
		R_ARM_X_OFFSET = 0.5f,
		R_ARM_Y_OFFSET = 0.3f,
		ARM_ROTATION_X_OFFSET = 0.2f,
		ARM_ROTATION_Y_OFFSET = 0.2f;
	
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
		
		// so the player doesn't have to keep getting polled
		boolean movingRight = player.isMovingRight();
		boolean facingRight = player.isFacingRight();
		
		/*-- Render Legs --*/
		BufferUtils.deepCopyFloatArray(renderer.modelview, oldMatrix);
		// if movingRight and facingRight are the same, it means the player is moving in the direction it's looking
		if(movingRight == facingRight)
			Matrix.translateM(renderer.modelview, 0, movingRight ? FORWARD_LEGS_X_OFFSET : -FORWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f);
		// else the legs are moving in the opposite direction as the player is looking
		else 
			Matrix.translateM(renderer.modelview, 0, movingRight ? BACKWARD_LEGS_X_OFFSET : -BACKWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		player.animation.renderCurrentFrame(renderer, 1.0f * SCALE, 0.902f * SCALE, movingRight, false);
		
		/*-- Render right arm --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? R_ARM_X_OFFSET : -R_ARM_X_OFFSET, R_ARM_Y_OFFSET, 0.0f);
		Matrix.rotateM(renderer.modelview, 0, player.getArmAngle(), 0.0f, 0.0f, 1.0f);
		//Matrix.translateM(renderer.modelview, 0, facingRight ? ARM_ROTATION_X_OFFSET : ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerarm").draw(renderer.quad, 0.5f * SCALE, 0.317f * SCALE, !facingRight, facingRight);
		
		/*-- Render body --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		Matrix.translateM(renderer.modelview, 0, facingRight ? BODY_X_OFFSET : -BODY_X_OFFSET, BODY_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerbody").draw(renderer.quad, 0.474f * SCALE, 1.0f * SCALE, !facingRight, true);
		
		/*-- Render left arm --*/
		BufferUtils.deepCopyFloatArray(oldMatrix, renderer.modelview);
		//Matrix.translateM(renderer.modelview, 0, facingRight ? ARM_ROTATION_X_OFFSET : ARM_ROTATION_X_OFFSET, ARM_ROTATION_Y_OFFSET, 0.0f);
		Matrix.translateM(renderer.modelview, 0, facingRight ? L_ARM_X_OFFSET : -L_ARM_X_OFFSET, L_ARM_Y_OFFSET, 0.0f);
		Matrix.rotateM(renderer.modelview, 0, player.getArmAngle(), 0.0f, 0.0f, 1.0f);
		//Matrix.translateM(renderer.modelview, 0, facingRight ? ARM_ROTATION_X_OFFSET : ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("playerarm").draw(renderer.quad, 0.5f * SCALE, 0.317f * SCALE, !facingRight, facingRight);
	}
}
