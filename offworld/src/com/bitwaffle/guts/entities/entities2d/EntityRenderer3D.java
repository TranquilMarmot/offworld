package com.bitwaffle.guts.entities.entities2d;

import com.badlogic.gdx.math.Quaternion;

public abstract class EntityRenderer3D implements EntityRenderer {
	public float z;
	
	public Quaternion rotation;
	
	public EntityRenderer3D(){
		rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		z = 0.0f;
	}
}
