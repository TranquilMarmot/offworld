package com.bitwaffle.moguts.util;

import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

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
	
	
	/**
	 * Convert a screen-space vector to a world-space vector
	 * @param projection Projection matrix to use for conversion
	 * @param view View matrix to use for conversion
	 * @param screenX X of screen space vector 
	 * @param screenY Y of screen space vector
	 * @return Screen-space coordinate translated to world-space
	 */
	public static Vector2 toWorldSpace(float[] projection, float[] view, float screenX, float screenY){
		float[] 
				// used to multiply projection * view
				compoundMatrix = new float[16],
				// screen-space touch point, normalized
				normalizedInPoint = new float[4],
				// resulting world-space point
				outPoint = new float[4];
			
			// multiply view and projection and invert (basically, GLUUnproject)
			Matrix.multiplyMM(compoundMatrix, 0, projection, 0, view, 0);
			Matrix.invertM(compoundMatrix, 0, compoundMatrix, 0);

			// compensate for Y 0 being on the bottom in OpenGL (touch point 0 is on the top)
			float oglTouchY = Game.windowHeight - screenY;
			// create our normalized vector
			normalizedInPoint[0] = ((screenX * 2.0f) / Game.windowWidth) - 1.0f;
			normalizedInPoint[1] = ((oglTouchY * 2.0f) / Game.windowHeight) - 1.0f;
			normalizedInPoint[2] = 0.0f; // because everything is drawn at 0 (between -1 and 1)
			normalizedInPoint[3] = 1.0f;
			
			// multiply normalized point by our inverted view-projection matrix
			Matrix.multiplyMV(outPoint, 0, compoundMatrix, 0, normalizedInPoint, 0);

			if (outPoint[3] == 0.0f)
				Log.e("Render2D", "Divide by zero error in screen space to world space conversion!");

			// some sort of magic or something
			return new Vector2(outPoint[0] / outPoint[3], outPoint[1] / outPoint[3]);
	}


	/**
	 * Convert a screen-space vector to a world-space vector
	 * @param screenX X of screen space vector 
	 * @param screenY Y of screen space vector
	 * @return Screen-space coordinate translated to world-space
	 */
	public static Vector2 toWorldSpace(float screenX, float screenY) {
		float[] 
				// projection matrix
				projection = new float[16],
				// view matrix (no 'model' since we're not looking at anything specific)
				view = new float[16];
			
			// create the projection matrix (mimics Render2D's "setUpProjectionWorldCoords" method)
			Matrix.setIdentityM(projection, 0);
			Matrix.orthoM(projection, 0, 0, Game.aspect, 0, 1, -1, 1);
			Matrix.rotateM(projection, 0, Render2D.camera.getAngle(), 0.0f, 0.0f, 1.0f);
			
			// create the view matrix (essentially, an identity matrix scaled and translated to the camera's position)
			Matrix.setIdentityM(view, 0);
			Matrix.scaleM(view, 0, Render2D.camera.getZoom(), Render2D.camera.getZoom(), 1.0f);
			Matrix.translateM(view, 0, Render2D.camera.getLocation().x, Render2D.camera.getLocation().y, 0.0f);
			
			return toWorldSpace(projection, view, screenX, screenY);
	}
}
