package com.bitwaffle.guts.path;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;

public class PathData {
	
	ArrayList<Vector2> points;
	
	public PathData(){
		points = new ArrayList<Vector2>();
	}

	public void addHit(DynamicEntity ent, Vector2 point, Vector2 normal,
			float fraction) {
		points.add(point);
	}
	
	public Iterator<Vector2> points(){
		return points.iterator();
	}
	
	public void reset(){
		points.clear();
	}

}
