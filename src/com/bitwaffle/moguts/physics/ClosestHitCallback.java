package com.bitwaffle.moguts.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.bitwaffle.moguts.entities.DynamicEntity;

/**
 * A callback to get the closest hit from a ray trace
 * 
 * @author TranquilMarmot
 */
public class ClosestHitCallback implements RayCastCallback{
	/** Where the ray cast is coming from */
	private Vector2 origin;
	
	/** Used to keep track of the closest hit */
	private float closestDist;
	
	/** Pointer to closest hit */
	private DynamicEntity closestEnt;
	
	/** Info about hit */
	private Vector2 normalOnClosest, pointOnClosest;
	
	/**
	 * Create a new ClosestCallback
	 * @param origin Origin of ray cast
	 */
	public ClosestHitCallback(Vector2 origin){
		this.origin = origin;
		closestDist = Float.MAX_VALUE;
		/*
		 * According to the libgdx docs, "The Vector2 instances
		 * passed to the callback will be reused for future calls
		 * so make a copy of them!"
		 * So we set the values of these whenever we get a closer result
		 * rather than just setting the pointers
		 */
		normalOnClosest = new Vector2(0.0f, 0.0f);
		pointOnClosest = new Vector2(0.0f, 0.0f);
	}
	
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
			float fraction) {
		// check if this hit is any closer than the closest one
		float dist = fixture.getBody().getPosition().dst(origin);
		if(dist <= closestDist){
			closestDist = dist;
			closestEnt = Physics.getDynamicEntity(fixture);
			pointOnClosest.set(point);
			normalOnClosest.set(normal);
		}
		
		return 1;
	}
	
	/** @return The closest DynamicEntity to the origin of the ray cast */
	public DynamicEntity getClosestHit(){ return closestEnt; }
	
	/** @return Normal of hit on closest entity */
	public Vector2 normalOnClosest(){ return normalOnClosest; }
	
	/** @return Point of hit on closest entity */
	public Vector2 pointOnClosest(){ return pointOnClosest; }
}
