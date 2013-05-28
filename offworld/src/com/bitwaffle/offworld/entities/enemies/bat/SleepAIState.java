package com.bitwaffle.offworld.entities.enemies.bat;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.ai.states.AIState;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;

public class SleepAIState extends AIState {
	
	public Fixture playerSensor;
	
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
		return def;
	}

	@Override
	public void update(float timeStep) {
		controlling.body.setLinearVelocity(0.0f, 1.0f);
	}

	@Override
	public void onLoseCurrentState() {
	}

	@Override
	public void onGainCurrentState() {
	}

}
