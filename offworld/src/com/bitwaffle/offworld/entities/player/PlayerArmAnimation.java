package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.animation.Animation;

/**
 * Contains info on where the player's shoulder and gun are each frame
 * 
 * @author TranquilMarmot
 */
public class PlayerArmAnimation extends Animation {
	public static final float[][] rShoulderLocations = {
			{-0.20352936f, 0.7545428f},
			{-0.20352936f, 0.78058434f},
			{-0.1948471f, 0.7545428f},
			{-0.1948471f, 0.76322365f},
			{-0.1948471f, 0.7285013f},
			{-0.17748642f, 0.7285013f},
			{-0.16880798f, 0.76322365f},
			{-0.16012573f, 0.771904f},
			{-0.1514473f, 0.771904f},
			{-0.16012573f, 0.76322365f},
			{-0.17748642f, 0.78058434f},
			{-0.1948471f, 0.78058434f},
			{-0.1948471f, 0.771904f},
			{-0.1948471f, 0.78058434f},
			{-0.18616867f, 0.771904f},
			{-0.1948471f, 0.771904f},
			{-0.17748642f, 0.76322365f},
			{-0.18616867f, 0.745862f},
			{-0.17748642f, 0.73718166f},
			{-0.18616867f, 0.7285013f},
			{-0.17748642f, 0.7198205f},
			{-0.16880798f, 0.7545428f},
			{-0.16880798f, 0.771904f},
			{-0.1514473f, 0.771904f},
			{-0.1514473f, 0.78058434f},
			{-0.14276505f, 0.78926516f},
			{-0.13408661f, 0.78058434f},
			{-0.13408661f, 0.8066263f},
			{-0.13408661f, 0.78058434f},
			{-0.1514473f, 0.7979455f},
			{-0.16880798f, 0.78058434f},
			{-0.1948471f, 0.78058434f}
	};
	
	public static final float[][] lShoulderLocations = {
			{-0.050628662f, 0.847105f},
			{-0.04194641f, 0.8384242f},
			{-0.033267975f, 0.8123827f},
			{-0.04194641f, 0.8123827f},
			{-0.033267975f, 0.82106304f},
			{-0.024585724f, 0.82974386f},
			{-0.015907288f, 0.82106304f},
			{-0.0072250366f, 0.8123827f},
			{0.0014533997f, 0.8384242f},
			{-0.0072250366f, 0.8384242f},
			{-0.015907288f, 0.873147f},
			{-0.033267975f, 0.85578537f},
			{-0.04194641f, 0.8644662f},
			{-0.04194641f, 0.85578537f},
			{-0.033267975f, 0.8384242f},
			{-0.033267975f, 0.82974386f},
			{-0.024585724f, 0.8123827f},
			{-0.024585724f, 0.80370235f},
			{-0.024585724f, 0.82974386f},
			{-0.015907288f, 0.8123827f},
			{-0.024585724f, 0.8123827f},
			{-0.015907288f, 0.8123827f},
			{-0.015907288f, 0.847105f},
			{-0.015907288f, 0.8384242f},
			{0.010135651f, 0.8644662f},
			{0.010135651f, 0.85578537f},
			{0.010135651f, 0.8644662f},
			{0.018814087f, 0.847105f},
			{0.018814087f, 0.85578537f},
			{-0.0072250366f, 0.85578537f},
			{-0.024585724f, 0.8384242f},
			{-0.04194641f, 0.8384242f}
	};
	
	public static final Vector2 gunOffset = new Vector2(0.61631774f, -0.58159732f);
	
	private float[][] shoulderLocations;
	
	public PlayerArmAnimation(Animation animation, float[][] shoulderLocations) {
		super(animation);
		this.shoulderLocations = shoulderLocations;
		
	}
	
	public Vector2 getShoulderLocation(){
		return new Vector2(shoulderLocations[currentFrame()][0], shoulderLocations[currentFrame()][1]);
	}
}
