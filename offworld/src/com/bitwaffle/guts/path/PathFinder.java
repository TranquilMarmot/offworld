package com.bitwaffle.guts.path;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.PhysicsHelper;

public class PathFinder {
	public PathData findPath(Vector2 start, Vector2 goal){
		Game.physics.rayCast(new RayCastCallback(){
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
					float fraction) {
				DynamicEntity ent = PhysicsHelper.getDynamicEntity(fixture);
				System.out.println("hit: " + ent.getClass().getSimpleName() + " at " + point);
				return 1;
			}
		}, start, goal);
		
		return null;
	}
}
