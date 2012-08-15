package com.bitwaffle.offworld.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public interface Firearm {
	public void shootAt(World world, Vector2 target);
}
