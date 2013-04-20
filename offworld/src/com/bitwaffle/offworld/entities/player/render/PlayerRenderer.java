package com.bitwaffle.offworld.entities.player.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity;
import com.bitwaffle.guts.entities.entities2d.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.graphics.render.render2d.Render2D;
import com.bitwaffle.offworld.entities.player.JumpSensor;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Renders the player
 * 
 * @author TranquilMarmot
 */
public class PlayerRenderer implements EntityRenderer {
	private static float[] color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
	private static Matrix4 tempmat = new Matrix4();
	
	/** Scale to draw arms at (should be half of scale that body animation is at) */
	public static final float SCALE = 0.95f;
	
	private JetpackBar jetpackBar;
	
	private HealthBar healthBar;
	private float healthBarYOffset = 3.0f, healthBarScale = 0.0005f;
	
	public PlayerRenderer(){
		jetpackBar = new JetpackBar();
		healthBar = new HealthBar(125.0f, 22.0f);
	}
	
	@Override
	public void render(Renderer renderer, Entity ent, boolean renderDebug){
		Player player = (Player) ent;

		if(renderDebug)
			renderDebug(renderer, ent);
		else{
			renderer.render2D.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
			
			float armAngle = player.getArmAngle();
			boolean facingRight = player.isFacingRight();
			Vector2 rArmLoc = player.getBodyAnimation().getCurrentRShoulderLocation();
			Vector2 lArmLoc = player.getBodyAnimation().getCurrentLShoulderLocation();
			Vector2 gunOffset = player.getBodyAnimation().getGunOffset();
			
			// draw left arm
			renderer.render2D.modelview.translate(facingRight ? lArmLoc.x : -lArmLoc.x, lArmLoc.y, 0.0f);
			renderer.render2D.modelview.rotate(0.0f, 0.0f, 1.0f, armAngle);
			renderer.render2D.sendMatrixToShader();
			Game.resources.textures.bindTexture("player-arm");
			// enable blending so alpha doesn't show up white
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			renderer.render2D.quad.render(SCALE, SCALE, facingRight, facingRight);
			Gdx.gl20.glDisable(GL20.GL_BLEND);
			
			// draw body
			renderer.render2D.modelview.rotate(0.0f, 0.0f, 1.0f, -armAngle);
			renderer.render2D.modelview.translate(facingRight ? -lArmLoc.x : lArmLoc.x, -lArmLoc.y, 0.0f);
			renderer.render2D.sendMatrixToShader();
			
			player.getBodyAnimation().renderCurrentFrame(renderer.render2D, !facingRight, false);
			
			// draw gun
			renderer.render2D.modelview.translate(facingRight ? rArmLoc.x : -rArmLoc.x, rArmLoc.y, 0.0f);
			renderer.render2D.modelview.rotate(0.0f, 0.0f, 1.0f, armAngle);
			renderer.render2D.modelview.translate(gunOffset.x, facingRight ? gunOffset.y : -gunOffset.y, 0.0f);
			renderer.render2D.sendMatrixToShader();
			player.getCurrentFirearm().render(renderer.render2D);
			
			// draw right arm
			renderer.render2D.modelview.translate(-gunOffset.x, facingRight ? -gunOffset.y : gunOffset.y, 0.0f);
			renderer.render2D.sendMatrixToShader();
			Game.resources.textures.bindTexture("player-arm");
			// enable blending so alpha doesn't show up white
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			renderer.render2D.quad.render(SCALE, SCALE, facingRight, facingRight);
			Gdx.gl20.glDisable(GL20.GL_BLEND);
			
			renderer.render2D.modelview.rotate(0.0f, 0.0f, 1.0f, -armAngle);
			renderer.render2D.modelview.translate(facingRight ? -rArmLoc.x : rArmLoc.x, -rArmLoc.y, 0.0f);
		}
		
		// render the health bar
		tempmat.set(renderer.render2D.modelview);
		if(player.currentHealth() < 100.0f){
			renderer.render2D.modelview.translate(0.0f, healthBarYOffset, 0.0f);
			renderer.render2D.modelview.scale(
					healthBarScale / Game.renderer.render2D.camera.getZoom(),
					healthBarScale / Game.renderer.render2D.camera.getZoom(),
					1.0f);
			renderer.render2D.sendMatrixToShader();
			healthBar.setPercent(player.currentHealth());
			healthBar.update(1.0f / Game.currentFPS);
			healthBar.render(renderer.render2D, false, false);
			renderer.render2D.modelview.set(tempmat);
		}
		
		jetpackBar.render(renderer.render2D, player.jetpack);
	}
	
	public void renderDebug(Renderer renderer, Entity ent){
		Player player = (Player) ent;
		Game.resources.textures.bindTexture("blank");

		float[] col = new float[4];
		col[0] = (player.body != null) ? (player.body.isAwake() ? 0.0f : 1.0f) : 0.0f;
		col[1] =(player.body != null) ? (player.body.isAwake() ? 1.0f : 0.0f) : 0.0f;
		col[2] = 0.0f;
		col[3] = 0.2f;

		renderer.render2D.program.setUniform("vColor", col[0], col[1], col[2], col[3]);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		renderer.render2D.quad.render(player.getWidth(), player.getHeight());
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
		// render player's jump sensor box
		JumpSensor sensor = player.getJumpSensor();
		renderer.render2D.modelview.translate(sensor.getX(), sensor.getY(), 0.0f);
		renderer.render2D.quad.render(sensor.getWidth(), sensor.getHeight());
		
		
		//Render a box where the player's current target is (under the finger)
		boolean shooting = player.isShooting();
		Vector2 targetLoc = player.getCurrentTarget();
		
		// the box is given a different color based on whether or not the player is shooting
		renderer.render2D.program.setUniform("vColor", shooting ? 0.25f : 0.0f, 0.0f, shooting ? 0.75f : 1.0f, 0.75f);
		Gdx.gl20.glEnable(GL20.GL_BLEND); // enable blending for some swell transparency
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);

		// reset the modelview and translate it to the right spot
		renderer.render2D.modelview.idt();
		renderer.render2D.translateModelViewToCamera();
		renderer.render2D.modelview.translate(targetLoc.x, targetLoc.y, 0.0f);
		renderer.render2D.quad.render(0.75f, 0.75f);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND); // don't forget to turn blending back off!
	}
}
