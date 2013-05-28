package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.entity.ai.states.AIState;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.CollisionFilters;

/**
 * A sleeping bat.
 * 
 * @author TranquilMarmot
 */
public class SleepAIState extends AIState {
	/** Sensor to detect player */
	public Fixture playerSensor;
	
	/**
	 *@param sensorRadius How far to check for player
	 */
	public SleepAIState(DynamicEntity ent, float sensorRadius){
		super(ent);
		
		playerSensor = ent.body.createFixture(getFixtureDef(sensorRadius));
		playerSensor.setUserData(this);
	}
	
	private static FixtureDef getFixtureDef(float sensorRadius){
		FixtureDef def = new FixtureDef();
		def.isSensor = true;
		CircleShape circ = new CircleShape();
		circ.setRadius(sensorRadius);
		def.shape = circ;
		def.filter.groupIndex = CollisionFilters.EVERYTHING;
		def.filter.maskBits = CollisionFilters.PLAYER;
		def.filter.categoryBits = CollisionFilters.EVERYTHING;
		
		// TODO collision filters?
		
		return def;
	}

	@Override
	public void update(float timeStep) {
		// cling to ceiling
		controlling.body.setLinearVelocity(0.0f, 1.0f);
	}

	@Override
	public void onLoseCurrentState() {}
	@Override
	public void onGainCurrentState() {}

}
