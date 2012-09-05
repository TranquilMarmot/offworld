package com.bitwaffle.offworld.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.moguts.graphics.render.Render2D;

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
	
	/**
	 * Update this firearm, for example to increment
	 * a cooldown timer
	 * @param timeStep Time passed, in seconds
	 */
	public void update(float timeStep);
	
	public void render(Render2D renderer);
}
