package com.bitwaffle.moguts.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Any math functions not found in Android's FloatMath or 
 * any function in java.lang.Math that can be changed
 * to use floats instead of doubles goes here
 * 
 * @author TranquilMarmot
 */
public class MathHelper {
	/** No pie jokes here */
	public static final float PI = 3.14159265f;
	
	/**
	 * Find an angle between two vectors
	 * @param a First vector
	 * @param b Second vector
	 * @return Angle between vectors (in degrees)
	 */
	public static float angle(Vector2 a, Vector2 b){
		float dx = b.x - a.x;
		float dy = b.y - a.y;
		return (float)(Math.atan2(dy, dx) * 180.0 / Math.PI);
	}
	
	/**
	 * Convert radians to degrees- with floats!
	 * (AKA Why the hell does everything in java.lang.Math use doubles?)
	 * @param radians radians
	 * @return degrees
	 */
	public static float toDegrees(float radians){
		return radians * 180.0f / PI;
	}
	
	/**
	 * Convert degrees to radians- with floats!
	 * (AKA Why the hell does everything in java.lang.Math use doubles?)
	 * @param degrees degrees
	 * @return radians
	 */
	public static float toRadians(float degrees){
		return degrees * PI / 180.0f;
	}
}
