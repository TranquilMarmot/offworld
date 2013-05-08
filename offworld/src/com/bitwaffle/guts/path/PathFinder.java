package com.bitwaffle.guts.path;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;

public class PathFinder {	
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
