package com.bitwaffle.guts.entities.entities3d;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.util.MathHelper;

public class DynamicEntity3D extends Entity3D {
	
	DynamicEntity ent;
	
	public DynamicEntity3D(Entity3DRenderer renderer, DynamicEntity ent){
		super(renderer, ent.getLayer(), new Vector3(ent.getLocation().x, ent.getLocation().y, 0.0f), new Quaternion(0.0f, 0.0f, 0.0f, 1.0f));
		this.ent = ent;
	}
	
	@Override
	public void update(float timeStep){
		ent.update(timeStep);
		
		Vector2 entLoc = ent.getLocation();
		this.location.x = entLoc.x;
		this.location.y = entLoc.y;
		
		Quaternion newRot = new Quaternion();
		newRot.setEulerAngles(0.0f, 0.0f, MathHelper.toDegrees(ent.getAngle()));
		rotation = newRot;
	}
}
