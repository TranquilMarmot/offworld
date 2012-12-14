package com.bitwaffle.guts.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.util.FloatMath;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.Camera;

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
	 * Get the distance between two points
	 * @param x0 First point's X value
	 * @param y0 First point's Y value
	 * @param x1 Second point's X value
	 * @param y1 Second point's Y value
	 * @return Distance between points
	 */
	public static float spacing(float x0, float y0, float x1, float y1){
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}
	
	/**
	 * Get the midpoint between two points
	 * @param out Vector to output midpoint to
	 * @param x0 First point's X value
	 * @param y0 First point's Y value
	 * @param x1 Second point's X value
	 * @param y1 Second point's Y value
	 */
	public static void midPoint(Vector2 out, float x0, float y0, float x1, float y1){
		float x = x0 + x1;
		float y = y0 + y1;
		out.set(x / 2.0f, y / 2.0f);
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
	public static Vector2 toWorldSpace(Matrix4f projection, Matrix4f view, float screenX, float screenY){
		// used to multiply projection * view
		Matrix4f compoundMatrix = new Matrix4f();
		
		// screen-space touch point, normalized
		float[] normalizedInPoint = new float[4];			
		
		// multiply view and projection and invert (basically, GLUUnproject)
		Matrix4f.mul(projection, view, compoundMatrix);
		Matrix4f.invert(compoundMatrix, compoundMatrix);

		// compensate for Y 0 being on the bottom in OpenGL (touch point 0 is on the top)
		float oglTouchY = Game.windowHeight - screenY;
		// create our normalized vector
		normalizedInPoint[0] = ((screenX * 2.0f) / Game.windowWidth) - 1.0f;
		normalizedInPoint[1] = ((oglTouchY * 2.0f) / Game.windowHeight) - 1.0f;
		normalizedInPoint[2] = 0.0f; // because everything is drawn at 0 (between -1 and 1)
		normalizedInPoint[3] = 1.0f;
		
		// multiply normalized point by our inverted view-projection matrix
		float[] outPoint = multiplyVectorByMatrix(normalizedInPoint, compoundMatrix);
		//Matrix.multiplyMV(outPoint, 0, compoundMatrix, 0, normalizedInPoint, 0);

		if (outPoint[3] == 0.0f)
			Log.e("Math","Divide by zero error in screen space to world space conversion!");

		// some sort of magic or something
		return new Vector2(outPoint[0] / outPoint[3], outPoint[1] / outPoint[3]);
	}
	
	public static float[] multiplyVectorByMatrix(float[] inPoint, Matrix4f matrix){
		float[] result = new float[4];
		
		result[0] = (matrix.m00 * inPoint[0]) + (matrix.m10 * inPoint[1]) + (matrix.m20 * inPoint[2]) + (matrix.m30 * inPoint[3]);
		result[1] = (matrix.m01 * inPoint[0]) + (matrix.m11 * inPoint[1]) + (matrix.m21 * inPoint[2]) + (matrix.m31 * inPoint[3]);
		result[2] = (matrix.m02 * inPoint[0]) + (matrix.m12 * inPoint[1]) + (matrix.m22 * inPoint[2]) + (matrix.m32 * inPoint[3]);
		result[3] = (matrix.m03 * inPoint[0]) + (matrix.m13 * inPoint[1]) + (matrix.m23 * inPoint[2]) + (matrix.m33 * inPoint[3]);

		return result;
	}
	
	/**
	 * Convert a screen-spcae vector to a world-space vector,
	 * with a camera offset/zoom
	 * @param screenX X of screen space vector
	 * @param screenY Y of screen space vector
	 * @param camera Camera to translate to
	 * @return Screen-space coordinate translated to world-space
	 */
	public static Vector2 toWorldSpace(float screenX, float screenY, Camera camera){
		Matrix4f 
			projection = new Matrix4f(), 
			view = new Matrix4f(); // (no 'model' since we're not looking at anything specific)
		
		// create the projection matrix (mimics Render2D's "setUpProjectionWorldCoords" method)
		projection.setIdentity();
		orthoM(projection, 0, Game.aspect, 0, 1, -1, 1);
		
		// create the view matrix
		view.setIdentity();
		
		// if we're using a camera, rotate/translate to it
		if(camera != null){
			Matrix4f.rotate(camera.getAngle(), new Vector3f(0.0f, 0.0f, 1.0f), projection, projection);
			Matrix4f.scale(new Vector3f(camera.getZoom(), camera.getZoom(), 1.0f), view, view);
			Matrix4f.translate(new Vector3f(camera.getLocation().x, camera.getLocation().y, 0.0f), view, view);
		}
		
		return toWorldSpace(projection, view, screenX, screenY);
	}
	
	/**
	 * Convert a screen-space vector to a world-space vector
	 * @param screenX X of screen space vector 
	 * @param screenY Y of screen space vector
	 * @return Screen-space coordinate translated to world-space
	 */
	public static Vector2 toWorldSpace(float screenX, float screenY) {
		return toWorldSpace(screenX, screenY, null);
	}
	
	public static void orthoM(Matrix4f m,
	        float left, float right, float bottom, float top,
	        float near, float far) {
	        if (left == right) {
	            throw new IllegalArgumentException("left == right");
	        }
	        if (bottom == top) {
	            throw new IllegalArgumentException("bottom == top");
	        }
	        if (near == far) {
	            throw new IllegalArgumentException("near == far");
	        }

	        final float r_width  = 1.0f / (right - left);
	        final float r_height = 1.0f / (top - bottom);
	        final float r_depth  = 1.0f / (far - near);
	        final float x =  2.0f * (r_width);
	        final float y =  2.0f * (r_height);
	        final float z = -2.0f * (r_depth);
	        final float tx = -(right + left) * r_width;
	        final float ty = -(top + bottom) * r_height;
	        final float tz = -(far + near) * r_depth;
	        /*m[mOffset + 0] = x;*/ m.m00 = x;
	        /*m[mOffset + 5] = y;*/m.m11 = y;
	        /*m[mOffset +10] = z;*/ m.m22 = z;
	        /*m[mOffset +12] = tx;*/ m.m30 = tx;
	        /*m[mOffset +13] = ty;*/ m.m31 = ty;
	        /*m[mOffset +14] = tz;*/ m.m32 = tz;
	        /*m[mOffset +15] = 1.0f;*/ m.m33 = 1.0f;
	        /*m[mOffset + 1] = 0.0f;*/ m.m01 = 0.0f;
	        /*m[mOffset + 2] = 0.0f;*/ m.m02 = 0.0f;
	        /*m[mOffset + 3] = 0.0f;*/ m.m03 = 0.0f;
	        /*m[mOffset + 4] = 0.0f;*/ m.m10 = 0.0f;
	        /*m[mOffset + 6] = 0.0f;*/ m.m12 = 0.0f;
	        /*m[mOffset + 7] = 0.0f;*/ m.m13 = 0.0f;
	        /*m[mOffset + 8] = 0.0f;*/ m.m20 = 0.0f;
	        /*m[mOffset + 9] = 0.0f;*/ m.m21 = 0.0f;
	        /*m[mOffset + 11] = 0.0f;*/ m.m23 = 0.0f;
	 }
	
	public static void putMatrixIntoArray(Matrix4f mat, float[] dest){
		dest[0] = mat.m00;
		dest[1] = mat.m01;
		dest[2] = mat.m02;
		dest[3] = mat.m03;
		dest[4] = mat.m10;
		dest[5] = mat.m11;
		dest[6] = mat.m12;
		dest[7] = mat.m13;
		dest[8] = mat.m20;
		dest[9] = mat.m21;
		dest[10] = mat.m22;
		dest[11] = mat.m23;
		dest[12] = mat.m30;
		dest[13] = mat.m31;
		dest[14] = mat.m32;
		dest[15] = mat.m33;
	}
}
