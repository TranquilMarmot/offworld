package com.bitwaffle.moguts.physics.callbacks;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.bitwaffle.moguts.entities.dynamic.DynamicEntity;
import com.bitwaffle.moguts.util.PhysicsHelper;

/**
 * A QueryCallback for grabbing the first entity
 * from an AABB query
 * 
 * @author TranquilMarmot
 */
public class FirstHitQueryCallback implements QueryCallback{
	/** Pointer to first hit */
	private DynamicEntity hit;
	
	public boolean reportFixture(Fixture fixture) {
		hit = PhysicsHelper.getDynamicEntity(fixture);
		// returning false here ends the query, so only the first entity gets grabbed
		return false;
	}
	
	/**
	 * @return First entity hit by this callback, null if no entities found
	 */
	public DynamicEntity getHit(){
		return hit;
	}

}
