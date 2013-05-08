package com.bitwaffle.guts.physics.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * Counts the number of hits a ray cast has
 * 
 * @author TranquilMarmot
 */
public class HitCountRayCastCallback implements RayCastCallback {
	private int hitCount;

	public HitCountRayCastCallback() {
		hitCount = 0;
	}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point,
			Vector2 normal, float fraction) {
		hitCount++;
		// NOTE if you ever want to use these you have to make copies since box2d reuses the vector objects
		//DynamicEntity ent = PhysicsHelper.getDynamicEntity(fixture);
		//Vector2 p = new Vector2(point);
		//Vector2 n = new Vector2(normal);
		
		// return 1 to continue through all hits
		return 1;
	}
	
	public int hitCount(){
		return hitCount;
	}
	
	public void reset(){
		hitCount = 0;
	}
}
