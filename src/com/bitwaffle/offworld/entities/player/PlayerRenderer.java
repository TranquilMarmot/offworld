package com.bitwaffle.offworld.entities.player;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * Renders the player
 * 
 * @author TranquilMarmot
 */
public class PlayerRenderer implements EntityRenderer {
	private static float[] color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
	
	/*
	 * NOTE:
	 * The 'SCALE' variables are usually found out by dividing the image's
	 * width by its height (or the other way around, depending).
	 * This needs to be done because the quad being used to render the images
	 * is 1 unit x 1 unit (in world space) and needs to be scaled to match the
	 * ratio of the image being drawn (otherwise the image gets drawn as a square).
	 */
	
	/** Offset of legs (world coordinates) */
	public static final float 
		FORWARD_LEGS_X_OFFSET = -0.064f, // when the player is looking forwards
		BACKWARD_LEGS_X_OFFSET = -0.158f, // when the player is looking backwards
		LEGS_Y_OFFSET = -0.94f,
		LEGS_X_SCALE = 1.2f,
		LEGS_Y_SCALE = 1.0f;
	
	/** Location of body (world coordinates) */
	public static final float
		BODY_X_OFFSET = 0.138f,
		BODY_Y_OFFSET = 0.94f,
		BODY_X_SCALE = 0.474f,
		BODY_Y_SCALE = 1.0f;
	
	/** Location of arms */
	public static final float
		L_ARM_X_OFFSET = 0.1f,
		L_ARM_Y_OFFSET = 0.75f,
		R_ARM_X_OFFSET = 0.15f,
		R_ARM_Y_OFFSET = 0.7f,
		ARM_ROTATION_X_OFFSET = 0.33f,
		ARM_ROTATION_Y_OFFSET = -0.3f,
		ARM_X_SCALE = 0.5f,
		ARM_Y_SCALE = 0.317f;
	
	/** Location of gun */
	public static final float
		GUN_X_OFFSET = 0.6f,
		GUN_Y_OFFSET = -0.1f,
		GUN_X_SCALE = 0.363f,
		GUN_Y_SCALE = 0.25f;
	
	/** Scale everything is being drawn at (to fit into bounding box) */
	public static final float SCALE = 0.95f;
	
	/** Used to preserve the modelview between draws */
	private Matrix4f oldMatrix;
	
	public PlayerRenderer(){
		oldMatrix = new Matrix4f();
	}
	
	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug){
		Player player = (Player) ent;

		if(renderDebug)
			renderDebug(renderer, ent);
		else{
			renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
			
			// so the player doesn't have to keep getting polled
			boolean movingRight = player.isMovingRight();
			boolean facingRight = player.isFacingRight();
			float armAngle = player.getArmAngle();
			
			/*-- Render right arm --*/
			Matrix4f.load(renderer.modelview, oldMatrix); // save the modelview for repeated drawing at the same spot
			renderer.modelview.translate(new Vector3f(facingRight ? R_ARM_X_OFFSET : -R_ARM_X_OFFSET, R_ARM_Y_OFFSET, 0.0f));
			renderer.modelview.rotate(armAngle, new Vector3f(0.0f, 0.0f, 1.0f));
			renderer.modelview.translate(new Vector3f(ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f));
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("playerarm").render(renderer.quad, ARM_X_SCALE * SCALE, ARM_Y_SCALE * SCALE, !facingRight, facingRight);
			
			/*-- Render Legs --*/
			Matrix4f.load(oldMatrix, renderer.modelview);
			// if movingRight and facingRight are the same, it means the player is moving in the direction it's looking
			if(movingRight == facingRight)
				renderer.modelview.translate(new Vector3f(movingRight ? FORWARD_LEGS_X_OFFSET : -FORWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f));
			// else the legs are moving in the opposite direction as the player is looking
			else 
				renderer.modelview.translate(new Vector3f(movingRight ? BACKWARD_LEGS_X_OFFSET : -BACKWARD_LEGS_X_OFFSET, LEGS_Y_OFFSET, 0.0f));
			renderer.sendModelViewToShader();
			player.legsAnimation.renderCurrentFrame(renderer, LEGS_X_SCALE * SCALE, LEGS_Y_SCALE * SCALE, movingRight, false);
			
			/*-- Render body --*/
			Matrix4f.load(oldMatrix, renderer.modelview);
			renderer.modelview.translate(new Vector3f(facingRight ? BODY_X_OFFSET : -BODY_X_OFFSET, BODY_Y_OFFSET, 0.0f));
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("playerbody").render(renderer.quad, BODY_X_SCALE * SCALE, BODY_Y_SCALE * SCALE, !facingRight, true);
			
			/*-- Render gun --*/
			Matrix4f.load(oldMatrix, renderer.modelview);
			renderer.modelview.translate(new Vector3f(facingRight ? L_ARM_X_OFFSET : -L_ARM_X_OFFSET, L_ARM_Y_OFFSET, 0.0f));
			renderer.modelview.rotate(armAngle, new Vector3f(0.0f, 0.0f, 1.0f));
			renderer.modelview.translate(new Vector3f(ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f));
			renderer.modelview.translate(new Vector3f(GUN_X_OFFSET, facingRight ?  GUN_Y_OFFSET  : -GUN_Y_OFFSET, 0.0f));
			renderer.sendModelViewToShader();
			player.getCurrentFirearm().render(renderer);
			//Game.resources.textures.getSubImage("pistol").render(renderer.quad, GUN_X_SCALE * SCALE, GUN_Y_SCALE * SCALE, !facingRight, facingRight);
			
			/*-- Render left arm --*/
			Matrix4f.load(oldMatrix, renderer.modelview);
			renderer.modelview.translate(new Vector3f(facingRight ? L_ARM_X_OFFSET : -L_ARM_X_OFFSET, L_ARM_Y_OFFSET, 0.0f));
			renderer.modelview.rotate(armAngle, new Vector3f(0.0f, 0.0f, 1.0f));
			renderer.modelview.translate(new Vector3f(ARM_ROTATION_X_OFFSET, facingRight ? ARM_ROTATION_Y_OFFSET : -ARM_ROTATION_Y_OFFSET, 0.0f));
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("playerarm").render(renderer.quad, ARM_X_SCALE * SCALE, ARM_Y_SCALE * SCALE, !facingRight, facingRight);
		}
	}
	
	public void renderDebug(Render2D renderer, Entity ent){
		Player player = (Player) ent;
		Game.resources.textures.bindTexture("blank");

		float[] col = new float[4];
		col[0] = (player.body != null) ? (player.body.isAwake() ? 0.0f : 1.0f) : 0.0f;
		col[1] =(player.body != null) ? (player.body.isAwake() ? 1.0f : 0.0f) : 0.0f;
		col[2] = 0.0f;
		col[3] = 0.2f;

		renderer.program.setUniform("vColor", col[0], col[1], col[2], col[3]);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
		renderer.quad.render(player.getWidth(), player.getHeight());
		GLES20.glDisable(GLES20.GL_BLEND);
		
		/*
		 * Render a box where the player's current target is (under the finger)
		 */
		boolean shooting = player.isShooting();
		Vector2 targetLoc = player.getCurrentTarget();
		
		// the box is given a different color based on whether or not the player is shooting
		renderer.program.setUniform("vColor", shooting ? 0.25f : 0.0f, 0.0f, shooting ? 0.75f : 1.0f, 0.75f);
		GLES20.glEnable(GLES20.GL_BLEND); // enable blending for some swell transparency
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);

		// reset the modelview and translate it to the right spot
		renderer.modelview.setIdentity();
		renderer.translateModelViewToCamera();
		renderer.modelview.translate(new Vector3f(targetLoc.x, targetLoc.y, 0.0f));
		renderer.quad.render(0.75f, 0.75f);
		
		GLES20.glDisable(GLES20.GL_BLEND); // don't forget to turn blending back off!
	}
}
