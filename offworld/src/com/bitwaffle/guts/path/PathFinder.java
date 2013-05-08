package com.bitwaffle.guts.path;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.PhysicsHelper;

public class PathFinder {	
	class FinderCallback implements RayCastCallback{
		
		PathData data;
		
		public FinderCallback(){
			data = new PathData();
		}

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
				float fraction) {
			DynamicEntity ent = PhysicsHelper.getDynamicEntity(fixture);
			data.addHit(ent, new Vector2(point), new Vector2(normal), fraction);
			return 1;
		}
		
		public PathData getData(){
			return data;
		}
		
		public void reset(){
			data.reset();
		}
		
	}
	
	FinderCallback callback;
	
	public PathFinder(){
		callback = new FinderCallback();
	}
	
	
	public PathData findPath(Vector2 start, Vector2 goal){ 
		Game.physics.rayCast(callback, start, goal);
		
		return callback.getData();
	}
	
	public void reset(){
		//callback.reset();
	}
}
