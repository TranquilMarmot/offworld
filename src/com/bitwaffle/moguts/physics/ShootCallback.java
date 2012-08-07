package com.bitwaffle.moguts.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.offworld.interfaces.Health;

public class ShootCallback implements RayCastCallback{
	
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
			float fraction) {
		
		DynamicEntity ent = Physics.getDynamicEntity(fixture);
		ent.body.applyForce(new Vector2(normal.x * -2500.0f, normal.y * -2500.0f), point);
		
		if(ent instanceof Health)
			((Health)ent).hurt(10);
		
		return fraction;
	}

}
