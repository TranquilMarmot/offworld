package com.bitwaffle.moguts.physics.callbacks;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.physics.Physics;

/**
 * A QueryCallback for grabbing the first entity
 * from an AABB query
 * 
 * @author TranquilMarmot
 */
public class GrabCallback implements QueryCallback{
	/** Pointer to grabbed entity */
	private DynamicEntity grabbed;
	
	public boolean reportFixture(Fixture fixture) {
		grabbed = Physics.getDynamicEntity(fixture);
		// returning false here ends the query, so only the first entity gets grabbed
		return false;
	}
	
	/**
	 * @return First entity grabbed by this callback, null if no entities found
	 */
	public DynamicEntity getGrabbedEntity(){
		return grabbed;
	}

}
