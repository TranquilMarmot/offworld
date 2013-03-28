package com.bitwaffle.offworld.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;

public interface FirearmHolder {
	public Vector2 getFirearmLocation();
	public float getFirearmAngle();
	public boolean isFacingRight();
	public DynamicEntity getFirearmOwningEntity();
}
