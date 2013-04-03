package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.particles.ParticleEmitter;
import com.bitwaffle.guts.gui.hud.JetpackBar;

public class Jetpack {
	/** Amount of force jetpack outputs */
	private final float JETPACK_FORCE = 0.375f;
	
	/** Player this jetpack belongs to */
	private Player player;
	
	/** Max speed the player can go on the Y axis */
	private float maxVelocityY = 15.0f;
	
	/** Whether or not the player is using the jetpack */
	private boolean jetpackOn;
	
	/** Particle emitter for jetpack */
	private ParticleEmitter jetpackEmitter;
	
	/** GUI bar representing this jetpack */
	private JetpackBar jetpackBar;
	
	/** How much fuel is left in this jetpack. Should always be between 0 and 100 */
	private float fuel = 100.0f;
	
	/** How fast the jetpack depletes/refuels */
	private float depleteRate = 0.5f, rechargeRate = 0.4f;
	
	/** 
	 * Whether or not the jetpack is recharging.
	 * The jetpack only recharges once the player touches the ground and doesn't
	 * stop charging til it hits 100 or the player uses the jetpack again.
	 */
	private boolean isRecharging;
	
	/**
	 * @param player Player owning this jetpack
	 */
	public Jetpack(Player player){
		this.player = player;
		jetpackOn = false;
		isRecharging = false;
		
		jetpackEmitter = new ParticleEmitter(player.getLayer() - 1, new JetpackEmitterSettings(player));
		jetpackEmitter.deactivate();
		jetpackBar = new JetpackBar(100.0f, 62.0f);
		Game.gui.addObject(jetpackBar);
	}
	
	public void update(float timeStep){
		jetpackEmitter.update(timeStep);
		
		if(jetpackOn && fuel > 0.0f){
			applyForce();
			fuel -= depleteRate;
			isRecharging = false;
		}
		
		if(isRecharging && fuel < 100.0f){
			fuel += rechargeRate;
			if(fuel > 100.0f)
				fuel = 100.0f;
		}
		
		if(player.getJumpSensor().numContacts() > 0){
			isRecharging = true;
		}
		
		if(fuel <= 0.0f){
			this.disable();
		}
		
		jetpackBar.setPercent(fuel);
	}
	
	/**
	 * Activates the player's jetpack
	 */
	private void applyForce(){
		Vector2 linVec = player.body.getLinearVelocity();
		if(linVec.y <= maxVelocityY){
			linVec.y += JETPACK_FORCE;
			player.body.setLinearVelocity(linVec);
			jetpackEmitter.activate();
		}
	}
	
	/** Start using the jetpack */
	public void enable(){ 
		jetpackOn = true;
		jetpackEmitter.activate();
	}
	
	/** Stop using the jetpack */
	public void disable(){
		jetpackOn = false;
		jetpackEmitter.deactivate();
	}
	
	/** @return Whether or not the jetpack is being used */
	public boolean isEnabled(){ return jetpackOn; }
	
	public void cleanup(){
		Game.gui.removeObject(jetpackBar);
	}
}
