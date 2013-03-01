package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class JumpSensorFixture {
	private Fixture fixture;

	public JumpSensorFixture(Player player){
		FixtureDef jumpSensorDef = new FixtureDef();
		jumpSensorDef.isSensor = true;
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(1.0f, 1.0f, new Vector2(0.0f, -player.getHeight()), 0.0f);
		jumpSensorDef.shape = sensorShape;
		
		fixture = player.body.createFixture(jumpSensorDef);
		fixture.setUserData(this);
	}
	
	public Fixture fixture(){
		return fixture;
	}

}
