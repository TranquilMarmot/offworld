package com.bitwaffle.guts.physics.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.util.PhysicsHelper;

/**
 * A RayCastCallback that grabs the first hit from a ray cast
 * 
 * @author TranquilMarmot
 */
public class FirstHitRayCastCallback implements RayCastCallback{
	/** Pointer to first hit */
	private DynamicEntity hit;
	
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
			float fraction) {
		hit = PhysicsHelper.getDynamicEntity(fixture);
		
		// returning 0 here terminates the callback
		return 0;
	}
	
	/**
	 * @return First DynamicEntity hit in RayCast, null if no hits
	 */
	public DynamicEntity getHit(){
		return hit;
	}
}
