package com.bitwaffle.offworld.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Something that can shoot at other things
 * 
 * @author TranquilMarmot
 */
public interface Firearm {
	/**
	 * Shoot, man!
	 * @param world World to shoot in
	 * @param target Target to shoot at in world
	 */
	public void shootAt(World world, Vector2 target);
}
