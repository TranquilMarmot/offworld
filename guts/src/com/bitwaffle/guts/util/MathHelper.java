package com.bitwaffle.guts.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.render2d.camera.Camera2D;

/**
 * Any math functions not found in Android's FloatMath or 
 * any function in java.lang.Math that can be changed
 * to use floats instead of doubles goes here
 * 
 * @author TranquilMarmot
 */
public class MathHelper {
	private static final String LOGTAG = "MathHelper";
	
	/** No pie jokes here */
	public static final float PI = 3.141592653589793238462f;
	
	/** Temporary use float[] */
	private static float[] outPoint = new float[4], normalizedInPoint = new float[4];
	
	/**
	 * Temporary matrices for calculations
	 * No 'model' since we're not looking at anything specific.
	 */
	private static Matrix4 projection = new Matrix4(), view = new Matrix4(); 
	
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
	 */
	public static float toDegrees(float radians){
		return radians * 180.0f / PI;
	}
	
	/**
	 * Convert degrees to radians- with floats!
	 * (AKA Why the hell does everything in java.lang.Math use doubles?)
	 */
	public static float toRadians(float degrees){
		return degrees * PI / 180.0f;
	}
	
	/**
	 * Convert a screen-space vector to a world-space vector
	 * @param out Vector to put result into
	 * @param projection Projection matrix to use for conversion
	 * @param view View matrix to use for conversion (basically, matrix translated to camera)
	 * @param screenX X of screen space vector 
	 * @param screenY Y of screen space vector
	 */
	public static void toWorldSpace(Vector2 out, Matrix4 projection, Matrix4 view, float screenX, float screenY){
		// compensate for Y 0 being on the bottom in OpenGL (touch point 0 is on the top)
		float oglTouchY = (float)Game.windowHeight - screenY;
		
		// create our normalized vector
		normalizedInPoint[0] = ((screenX * 2.0f) / (float)Game.windowWidth) - 1.0f;
		normalizedInPoint[1] = ((oglTouchY * 2.0f) / (float)Game.windowHeight) - 1.0f;
		normalizedInPoint[2] = 0.0f; // because everything is drawn at 0 (between -1 and 1)
		normalizedInPoint[3] = 1.0f;
		
		// multiply view and projection and invert (basically, GLUUnproject)
		Matrix4 compoundMatrix = projection.cpy();
		compoundMatrix.mul(view);
		compoundMatrix = compoundMatrix.inv();
		
		// multiply normalized point by our inverted view-projection matrix
		multiplyVectorByMatrix(outPoint, normalizedInPoint, compoundMatrix);

		if (outPoint[3] == 0.0f)
			Gdx.app.error(LOGTAG, "Divide by zero error in screen space to world space conversion!");

		// some sort of magic or something
		out.set(outPoint[0] / outPoint[3], outPoint[1] / outPoint[3]);
	}
	
	/**
	 * Creates a new vector and calls the toWorldSpace method with it.
	 * It's preferred that you re-use the same vector with each call to minimize
	 * memory allocations but do what you gotta do.
	 */
	public static Vector2 toWorldSpace(float screenX, float screenY, Camera2D camera){
		Vector2 result = new Vector2();
		toWorldSpace(result, screenX, screenY, camera);
		return result;
	}
	
	/**
	 * Multiply a 4-element vector by a 4x4 matrix
	 * @param dest Where to put the result (must have length of 4)
	 * @param inPoint Vector to multiply
	 * @param matrix Matrix to multiply vector by
	 */
	public static void multiplyVectorByMatrix(float[] dest, float[] inPoint, Matrix4 matrix){
		if(dest.length != 4)
			Gdx.app.error(LOGTAG, "Destination array not of length 4");
		float[] m = matrix.getValues();
		dest[0] = (m[Matrix4.M00] * inPoint[0]) + (m[Matrix4.M01] * inPoint[1]) + (m[Matrix4.M02] * inPoint[2]) + (m[Matrix4.M03] * inPoint[3]);
		dest[1] = (m[Matrix4.M10] * inPoint[0]) + (m[Matrix4.M11] * inPoint[1]) + (m[Matrix4.M12] * inPoint[2]) + (m[Matrix4.M13] * inPoint[3]);
		dest[2] = (m[Matrix4.M20] * inPoint[0]) + (m[Matrix4.M21] * inPoint[1]) + (m[Matrix4.M22] * inPoint[2]) + (m[Matrix4.M23] * inPoint[3]);
		dest[3] = (m[Matrix4.M31] * inPoint[0]) + (m[Matrix4.M31] * inPoint[1]) + (m[Matrix4.M32] * inPoint[2]) + (m[Matrix4.M33] * inPoint[3]);
	}
	
	/**
	 * Convert a screen-spcae vector to a world-space vector,
	 * with a camera offset/zoom
	 * @param out Vector to put result into
	 * @param screenX X of screen space vector
	 * @param screenY Y of screen space vector
	 * @param camera Camera to translate to
	 */
	public static void toWorldSpace(Vector2 out, float screenX, float screenY, Camera2D camera){
		// create the projection matrix (mimics Render2D's "setUpProjectionWorldCoords" method)
		projection.idt();
		orthoM(projection, 0, Game.aspect, 0, 1, -1, 1);
		
		// create the view matrix
		view.idt();
		
		// if we're using a camera, rotate/translate to it
		if(camera != null){
			projection.rotate(0.0f, 0.0f, 1.0f, camera.getAngle());
			view.scale(camera.getZoom(), camera.getZoom(), 1.0f);
			view.translate(camera.getLocation().x, camera.getLocation().y, 0.0f);
		}
		
		toWorldSpace(out, projection, view, screenX, screenY);
	}
	
	/**
	 * Convert a screen-space vector to a world-space vector, using no camera.
	 * @param screenX X of screen space vector 
	 * @param screenY Y of screen space vector
	 * @return Screen-space coordinate translated to world-space
	 */
	public static void toWorldSpace(Vector2 out, float screenX, float screenY) {
		toWorldSpace(out, screenX, screenY, null);
	}
	
	/**
	 * Set up an orthographic view on a matrix.
	 * See http://www.opengl.org/sdk/docs/man2/xhtml/glOrtho.xml for more info.
	 * @param m Target matrix
	 */
	public static void orthoM(Matrix4 m,
			float left, float right, float bottom, float top,
			float near, float far) {
		
		if (left == right)
			throw new IllegalArgumentException("left == right");
		if (bottom == top)
			throw new IllegalArgumentException("bottom == top");
		if (near == far)
			throw new IllegalArgumentException("near == far");
	        
		m.idt();

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
	        
		float[] arr = new float[16];
        arr[Matrix4.M00] = x;
        arr[Matrix4.M11] = y;
        arr[Matrix4.M22] = z;
        arr[Matrix4.M03] = tx;
        arr[Matrix4.M13] = ty;
        arr[Matrix4.M23] = tz;
        arr[Matrix4.M33] = 1.0f;
        arr[Matrix4.M10] = 0.0f;
        arr[Matrix4.M20] = 0.0f;
        arr[Matrix4.M30] = 0.0f;
        arr[Matrix4.M01] = 0.0f;
        arr[Matrix4.M21] = 0.0f;
        arr[Matrix4.M31] = 0.0f;
        arr[Matrix4.M02] = 0.0f;
        arr[Matrix4.M12] = 0.0f;
        arr[Matrix4.M32] = 0.0f;
	        
	    m.set(arr);
	 }

	/**
	 * Set up a perspective view on a matrix
	 * See http://www.opengl.org/sdk/docs/man2/xhtml/gluPerspective.xml for more info
	 * @param mat Target matrix
	 */
	public static void perspective(Matrix4 mat, float fovy, float aspect, float zNear, float zFar){
		mat.idt();
		
		float radians = (fovy / 2.0f) * (PI / 180.0f);

		float deltaZ = zFar - zNear;
		float sine = (float) Math.sin((double)radians);

		if ((deltaZ == 0) || (sine == 0) || (aspect == 0))
			return;

		float cotangent = ((float)Math.cos((double)radians)) / sine;
		
		float[] arr = new float[16];
        arr[Matrix4.M00] = cotangent / aspect;
        arr[Matrix4.M10] = 0.0f;
        arr[Matrix4.M20] = 0.0f;
        arr[Matrix4.M30] = 0.0f;

        arr[Matrix4.M01] = 0.0f;
        arr[Matrix4.M11] = cotangent;
        arr[Matrix4.M21] = 0.0f;
        arr[Matrix4.M31] = 0.0f;

        arr[Matrix4.M02] = 0.0f;
        arr[Matrix4.M12] = 0.0f;
        arr[Matrix4.M22] = -(zFar + zNear) / deltaZ;
        arr[Matrix4.M32] = -1.0f;

        arr[Matrix4.M03] = 0.0f;
        arr[Matrix4.M13] = 0.0f;
        arr[Matrix4.M23] = -2.0f * zNear * zFar / deltaZ;
        arr[Matrix4.M33] = 0.0f;

		mat.set(arr);
	}
	
	/**
	 * Rotates a vector by a quaternion
	 * 
	 * @param vector
	 *            The vector to rotate
	 * @param quat
	 *            The quaternion to rotate the vector by
	 * @return Rotate vector
	 */
	public static Vector3 rotateVectorByQuaternion(Vector3 vector,
			Quaternion quat) {
		Quaternion vecQuat = new Quaternion(vector.x, vector.y, vector.z, 0.0f);

		Quaternion quatNegate = quat.conjugate();

		Quaternion resQuat = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		resQuat = vecQuat.mul(quatNegate);
		resQuat = quat.mul(resQuat);

		return new Vector3(resQuat.x, resQuat.y, resQuat.z);
	}

	/**
	 * Converts a quaternion to good ol' euler angles
	 * 
	 * @param quat
	 *            The quaternion to convert
	 * @return A vector containing the three euler angles
	 */
	public static Vector3 getEulerAnglesFromQuaternion(Quaternion quat) {
		float xn = (2 * ((quat.x * quat.y) + (quat.z * quat.w)))
				/ (1 - (2 * ((quat.y * quat.y) + (quat.z * quat.z))));
		float x = (float) (Math.atan((double) xn));

		float yn = 2 * ((quat.x * quat.z) - (quat.w * quat.y));
		float y = (float) (Math.asin((double) yn));

		float zn = (2 * ((quat.x * quat.w) + (quat.y * quat.z)))
				/ (1 - (2 * ((quat.z * quat.z) + (quat.w * quat.w))));
		float z = (float) (Math.atan((double) zn));

		float oneEightyOverPI = 180.0f / PI;
		x *= oneEightyOverPI;
		y *= oneEightyOverPI;
		z *= oneEightyOverPI;

		return new Vector3(x, y, z);
	}
	
	/**
	 * Rotate a quaternion by a vector
	 * @param quat Quaternion to rotate
	 * @param amount Amount to rotate quaternion by
	 * @return Rotated quaternion
	 */
	public static Quaternion rotate(Quaternion quat, Vector3 amount){
		Quaternion ret = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		
		ret = rotateX(quat, amount.x);
		ret = rotateY(ret, amount.y);
		ret = rotateZ(ret, amount.z);
		
		return ret;
	}

	/**
	 * Rotate a quaternion along it's x axis a certain amount
	 * 
	 * @param amount
	 *            Amount to rotate the quaternion
	 * @return Rotated quaternion
	 */
	public static Quaternion rotateX(Quaternion quat, float amount) {
		double radHalfAngle = Math.toRadians((double) amount) / 2.0;
		float sinVal = (float) Math.sin(radHalfAngle);
		float cosVal = (float) Math.cos(radHalfAngle);
		Quaternion rot = new Quaternion(sinVal, 0.0f, 0.0f, cosVal);
		return quat.mul(rot);
	}

	/**
	 * Rotate a quaternion along it's y axis a certain amount
	 * 
	 * @param amount
	 *            Amount to rotate the quaternion
	 * @return Rotated quaternion
	 */
	public static Quaternion rotateY(Quaternion quat, float amount) {
		double radHalfAngle = Math.toRadians((double) amount) / 2.0;
		float sinVal = (float) Math.sin(radHalfAngle);
		float cosVal = (float) Math.cos(radHalfAngle);
		Quaternion rot = new Quaternion(0.0f, sinVal, 0.0f, cosVal);
		return quat.mul(rot);
	}

	/**
	 * Rotate a quaternion along it's z axis a certain amount
	 * 
	 * @param amount
	 *            Amount to rotate the quaternion
	 * @return Rotated quaternion
	 */
	public static Quaternion rotateZ(Quaternion quat, float amount) {
		double radHalfAngle = Math.toRadians((double) amount) / 2.0;
		float sinVal = (float) Math.sin(radHalfAngle);
		float cosVal = (float) Math.cos(radHalfAngle);
		Quaternion rot = new Quaternion(0.0f, 0.0f, sinVal, cosVal);
		return quat.mul(rot);
	}

	/**
	 * Moves the given vector a certain amount along the rotation of the given
	 * quaternion
	 * 
	 * @param quat
	 *            Rotation to use for movement
	 * @param vec
	 *            Vector to add to
	 * @param amount
	 *            Amount to add to the vector
	 */
	public static Vector3 moveX(Quaternion quat, Vector3 vec, float amount) {
		Vector3 multi = rotateVectorByQuaternion(new Vector3(amount, 0.0f,0.0f), quat);
		return vec.add(multi);
	}

	/**
	 * Moves the given vector a certain amount along the rotation of the given
	 * quaternion
	 * 
	 * @param quat
	 *            Rotation to use for movement
	 * @param vec
	 *            Vector to add to
	 * @param amount
	 *            Amount to add to the vector
	 */
	public static Vector3 moveY(Quaternion quat, Vector3 vec, float amount) {
		Vector3 multi = rotateVectorByQuaternion(new Vector3(0.0f, amount,0.0f), quat);
		return vec.add(multi);
	}

	/**
	 * Moves the given vector a certain amount along the rotation of the given
	 * quaternion
	 * 
	 * @param quat
	 *            Rotation to use for movement
	 * @param vec
	 *            Vector to add to
	 * @param amount
	 *            Amount to add to the vector
	 */
	public static Vector3 moveZ(Quaternion quat, Vector3 vec, float amount) {
		Vector3 multi = rotateVectorByQuaternion(new Vector3(0.0f, 0.0f,amount), quat);
		return vec.add(multi);
	}
}
