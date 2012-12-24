package com.bitwaffle.guts.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.camera.Camera;

/**
 * Any math functions not found in Android's FloatMath or 
 * any function in java.lang.Math that can be changed
 * to use floats instead of doubles goes here
 * 
 * @author TranquilMarmot
 */
public class MathHelper {
	/** No pie jokes here */
	public static final float PI = 3.141592653589793238462f;
	
	private static float[] outPoint = new float[4];
	private static Matrix4f projection = new Matrix4f(), view = new Matrix4f(); // (no 'model' since we're not looking at anything specific)
	
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
		double x = (double)x0 - (double)x1;
		double y = (double)y0 - (double)y1;
		return (float) Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Get the midpoint between two points
	 * @param out Vector to output midpoint to
	 * @param x0 First point's X value
	 * @param y0 First point's Y value
	 * @param x1 Second point's X value
	 * @param y1 Second point's Y value
	 */
	public static void midpoint(Vector2 out, float x0, float y0, float x1, float y1){
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
	public static void toWorldSpace(Vector2 out, Matrix4f projection, Matrix4f view, float screenX, float screenY){
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
		multiplyVectorByMatrix(outPoint, normalizedInPoint, compoundMatrix);

		if (outPoint[3] == 0.0f)
			Log.e("MathHelper", "Divide by zero error in screen space to world space conversion!");

		// some sort of magic or something
		out.set(outPoint[0] / outPoint[3], outPoint[1] / outPoint[3]);
	}
	
	/**
	 * Simply creates a new vector and calls the toWorldSpace method with it.
	 * It's preferred that you re-use the same vector with each call but
	 * do what you gotta do
	 */
	public static Vector2 toWorldSpace(float screenX, float screenY, Camera camera){
		Vector2 result = new Vector2();
		toWorldSpace(result, screenX, screenY, camera);
		return result;
	}
	
	/**
	 * Multiply a 4-element vector by a 4x4 matrix
	 * @param dest Where to put the result (must have length of 4)
	 * @param inPoint Vector to multiply
	 * @param matrix Matrix to multiply vector y
	 */
	public static void multiplyVectorByMatrix(float[] dest, float[] inPoint, Matrix4f matrix){
		if(dest.length != 4)
			Log.e("MathHelper", "Destination array not of length 4");
		dest[0] = (matrix.m00 * inPoint[0]) + (matrix.m10 * inPoint[1]) + (matrix.m20 * inPoint[2]) + (matrix.m30 * inPoint[3]);
		dest[1] = (matrix.m01 * inPoint[0]) + (matrix.m11 * inPoint[1]) + (matrix.m21 * inPoint[2]) + (matrix.m31 * inPoint[3]);
		dest[2] = (matrix.m02 * inPoint[0]) + (matrix.m12 * inPoint[1]) + (matrix.m22 * inPoint[2]) + (matrix.m32 * inPoint[3]);
		dest[3] = (matrix.m03 * inPoint[0]) + (matrix.m13 * inPoint[1]) + (matrix.m23 * inPoint[2]) + (matrix.m33 * inPoint[3]);
	}
	
	/**
	 * Convert a screen-spcae vector to a world-space vector,
	 * with a camera offset/zoom
	 * @param screenX X of screen space vector
	 * @param screenY Y of screen space vector
	 * @param camera Camera to translate to
	 * @return Screen-space coordinate translated to world-space
	 */
	public static void toWorldSpace(Vector2 out, float screenX, float screenY, Camera camera){
		// create the projection matrix (mimics Render2D's "setUpProjectionWorldCoords" method)
		projection.setIdentity();
		orthoM(projection, 0, Game.aspect, 0, 1, -1, 1);
		
		// create the view matrix
		view.setIdentity();
		
		// if we're using a camera, rotate/translate to it
		if(camera != null){
			Matrix4f.rotate(camera.getAngle(), new Vector3f(0.0f, 0.0f, 1.0f), projection, projection);
			Matrix4f.scale(new Vector3f(camera.getZoom(), camera.getZoom(), 1.0f), view, view);
			Matrix4f.translate(new Vector2f(camera.getLocation().x, camera.getLocation().y), view, view);
		}
		
		toWorldSpace(out, projection, view, screenX, screenY);
	}
	
	/**
	 * Convert a screen-space vector to a world-space vector
	 * @param screenX X of screen space vector 
	 * @param screenY Y of screen space vector
	 * @return Screen-space coordinate translated to world-space
	 */
	public static void toWorldSpace(Vector2 out, float screenX, float screenY) {
		toWorldSpace(out, screenX, screenY, null);
	}
	
	/**
	 * Set up an orthographic view on a matrix
	 * @param m Target matrix
	 * @param left Left size
	 * @param right Right size
	 * @param bottom Bottom size
	 * @param top Top size
	 * @param near How close things can get
	 * @param far How far things can get
	 */
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

	        final float 
    		r_width  = 1.0f / (right - left),
    		r_height = 1.0f / (top - bottom),
    		r_depth  = 1.0f / (far - near),
    		x =  2.0f * (r_width),
    		y =  2.0f * (r_height),
    		z = -2.0f * (r_depth),
    		tx = -(right + left) * r_width,
    		ty = -(top + bottom) * r_height,
    		tz = -(far + near) * r_depth;
	        
	        m.m00 = x;
	        m.m11 = y;
	        m.m22 = z;
	        m.m30 = tx;
	        m.m31 = ty;
	        m.m32 = tz;
	        m.m33 = 1.0f;
	        m.m01 = 0.0f;
	        m.m02 = 0.0f;
	        m.m03 = 0.0f;
	        m.m10 = 0.0f;
	        m.m12 = 0.0f;
	        m.m13 = 0.0f;
	        m.m20 = 0.0f;
	        m.m21 = 0.0f;
	        m.m23 = 0.0f;
	 }
	
	
	/**
	 * Puts a matrix into a device to use with android
	 * @param mat Matrix to put into array
	 * @param dest Array to put matrix into
	 */
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
