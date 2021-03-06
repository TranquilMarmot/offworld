package com.bitwaffle.offworld.entities.player.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.offworld.entities.player.JumpSensor;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Renders the player
 * 
 * @author TranquilMarmot
 */
public class PlayerRenderer implements ObjectRenderer2D {
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
	public void render(Renderer renderer, Object ent){
		Player player = (Player) ent;
		jetpackBar.jetpack = player.jetpack;

		if(renderer.renderDebug)
			renderDebug(renderer, ent);
		else{
			renderer.r2D.setColor(color);
			
			float armAngle = player.getArmAngle();
			boolean facingRight = player.isFacingRight();
			Vector2 rArmLoc = player.getBodyAnimation().getCurrentRShoulderLocation();
			Vector2 lArmLoc = player.getBodyAnimation().getCurrentLShoulderLocation();
			Vector2 gunOffset = player.getBodyAnimation().getGunOffset();
			
			// draw left arm
			renderer.modelview.translate(facingRight ? lArmLoc.x : -lArmLoc.x, lArmLoc.y, 0.0f);
			renderer.modelview.rotate(0.0f, 0.0f, 1.0f, armAngle);
			renderer.r2D.sendMatrixToShader();
			Game.resources.textures.bindTexture("player-arm");
			// enable blending so alpha doesn't show up white
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			renderer.r2D.quad.render(SCALE, SCALE, facingRight, facingRight);
			Gdx.gl20.glDisable(GL20.GL_BLEND);
			
			// draw body
			renderer.modelview.rotate(0.0f, 0.0f, 1.0f, -armAngle);
			renderer.modelview.translate(facingRight ? -lArmLoc.x : lArmLoc.x, -lArmLoc.y, 0.0f);
			renderer.r2D.sendMatrixToShader();
			
			player.getBodyAnimation().renderCurrentFrame(renderer, !facingRight, false);
			
			// draw gun
			renderer.modelview.translate(facingRight ? rArmLoc.x : -rArmLoc.x, rArmLoc.y, 0.0f);
			renderer.modelview.rotate(0.0f, 0.0f, 1.0f, armAngle);
			renderer.modelview.translate(gunOffset.x, facingRight ? gunOffset.y : -gunOffset.y, 0.0f);
			renderer.r2D.sendMatrixToShader();
			if(player.getCurrentFirearm() != null)
				player.getCurrentFirearm().render(renderer);
			
			// draw right arm
			renderer.modelview.translate(-gunOffset.x, facingRight ? -gunOffset.y : gunOffset.y, 0.0f);
			renderer.r2D.sendMatrixToShader();
			Game.resources.textures.bindTexture("player-arm");
			// enable blending so alpha doesn't show up white
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			renderer.r2D.quad.render(SCALE, SCALE, facingRight, facingRight);
			Gdx.gl20.glDisable(GL20.GL_BLEND);
			
			renderer.modelview.rotate(0.0f, 0.0f, 1.0f, -armAngle);
			renderer.modelview.translate(facingRight ? -rArmLoc.x : rArmLoc.x, -rArmLoc.y, 0.0f);
		}
		
		// render the health bar
		tempmat.set(renderer.modelview);
		if(player.currentHealth() < 100.0f){
			renderer.modelview.translate(0.0f, healthBarYOffset, 0.0f);
			renderer.modelview.scale(
					healthBarScale / Game.renderer.camera.zoom,
					healthBarScale / Game.renderer.camera.zoom,
					1.0f);
			renderer.r2D.sendMatrixToShader();
			healthBar.setPercent(player.currentHealth());
			healthBar.update(1.0f / Game.currentFPS);
			healthBar.renderer.render(renderer, healthBar);
			renderer.modelview.set(tempmat);
		}
		
		jetpackBar.update(1.0f / 60.0f);
		renderer.modelview.set(tempmat);
		renderer.modelview.translate(0.0f, jetpackBar.yOffset, 0.0f);
		renderer.modelview.scale(jetpackBar.scale, jetpackBar.scale, 1.0f);
		jetpackBar.renderer.render(renderer, jetpackBar);
	}
	
	public void renderDebug(Renderer renderer, Object ent){
		Player player = (Player) ent;
		Game.resources.textures.bindTexture("blank");

		float[] col = new float[4];
		col[0] = (player.body != null) ? (player.body.isAwake() ? 0.0f : 1.0f) : 0.0f;
		col[1] =(player.body != null) ? (player.body.isAwake() ? 1.0f : 0.0f) : 0.0f;
		col[2] = 0.0f;
		col[3] = 0.2f;

		renderer.r2D.setColor(col);

		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		renderer.r2D.quad.render(player.getWidth(), player.getHeight());
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
		// render player's jump sensor box
		JumpSensor sensor = player.getJumpSensor();
		renderer.modelview.translate(sensor.getX(), sensor.getY(), 0.0f);
		renderer.r2D.quad.render(sensor.getWidth(), sensor.getHeight());
		
		
		//Render a box where the player's current target is (under the finger)
		boolean shooting = player.isShooting();
		Vector2 targetLoc = player.getCurrentTarget();
		
		// the box is given a different color based on whether or not the player is shooting
		renderer.r2D.setColor(shooting ? 0.25f : 0.0f, 0.0f, shooting ? 0.75f : 1.0f, 0.75f);
		Gdx.gl20.glEnable(GL20.GL_BLEND); // enable blending for some swell transparency
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);

		// reset the modelview and translate it to the right spot
		renderer.modelview.idt();
		renderer.r2D.translateModelViewToCamera();
		renderer.modelview.translate(targetLoc.x, targetLoc.y, 0.0f);
		renderer.r2D.quad.render(0.75f, 0.75f);
		renderer.modelview.translate(-targetLoc.x, -targetLoc.y, 0.0f);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND); // don't forget to turn blending back off!
	}
}
