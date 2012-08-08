package com.bitwaffle.moguts.device;

import java.util.Iterator;

import android.opengl.Matrix;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.button.Button;
import com.bitwaffle.offworld.Game;

/**
 * Handles any touch events (which are generated in {@link SurfaceView} and then
 * sent here)
 * 
 * @author TranquilMarmot
 */
public class TouchHandler {
	/** Touch position from previous event */
	private float previousX, previousY;

	/** If two fingers are being used, how far apart they are */
	private float previousSpacing;

	/** How far apart fingers have to be before pinch zooming happens */
	public final float MIN_ZOOM_SPACING = 10.0f;

	/** How sensitive zoom is- the higher the value, the less sensitive */
	public final float ZOOM_SENSITIVITY = 200000.0f;

	/** How sensitive camera dragging is- the higher the value, the less sensitive */
	public final float DRAG_SENSITIVITY = 15.0f;

	/** See comment for checkForButtonPresses() */
	private Button[] buttonsDown;

	/**
	 * Create a new touch handler
	 */
	public TouchHandler() {
		previousX = 0.0f;
		previousY = 0.0f;
		previousSpacing = 0.0f;
		buttonsDown = new Button[2];
	}

	/**
	 * Get how far apart two fingers are
	 * 
	 * @param event
	 *            Touch event
	 * @return Distance between two fingers
	 */
	private float spacing(float x1, float y1, float x2, float y2) {
		float x = x1 - x2;
		float y = y1 - y2;
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * Get the midpoint between two fingers
	 * 
	 * @param point
	 *            Point to output midpoint to (avoid creating object to save
	 *            garbage collector some time)
	 * @param event
	 *            Touch event
	 */
	@SuppressWarnings("unused")
	private void midPoint(Vector2 point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * Take care of any touch events
	 * @param e MotionEvent
	 * @return ???
	 */
	public boolean touchEvent(MotionEvent e) {
		// MotionEvent has a slew of static ints that reference different actions
		int action = e.getAction();
		
		// how many pointers are down for this event
		int pointerCount = e.getPointerCount();

		// x and y values of first pointer
		float x0 = e.getX(0);
		float y0 = e.getY(0);

		// x and y values of second pointer (will be 0 if no second pointer)
		float x1 = (pointerCount >= 2) ? e.getX(1) : 0.0f;
		float y1 = (pointerCount >= 2) ? e.getY(1) : 0.0f;

		// how far apart the two pointer are
		float spacing = spacing(x0, y0, x1, y1);

		switch (action) {
		// initial pointer is put down
		case MotionEvent.ACTION_DOWN:
			// check for button presses and grab an entity if there aren't any
			if(!checkForButtonPresses(x0, y0)){
				//grabbed = Game.physics.checkForEntityAt(toWorldSpace(x0, y0), 0.5f, 0.5f);
				Game.player.shoot(toWorldSpace(x0, y0));
			}
			break;
			
		// first pointer is put down (happens when pointer 2 is kept down and pointer 1 goes up then down again)
		case MotionEvent.ACTION_POINTER_1_DOWN:
			if(!checkForButtonPresses(x0, y0))
				Game.player.shoot(toWorldSpace(x0, y0));
			break;

		// second pointer is put down
		case MotionEvent.ACTION_POINTER_2_DOWN:
			if(!checkForButtonPresses(x1, y1))
				Game.player.shoot(toWorldSpace(x1, y1));
			break;

		// second pointer released
		case MotionEvent.ACTION_POINTER_2_UP:
			if (buttonsDown[0] != null && buttonsDown[0].isDown()
					&& buttonsDown[0].contains(x1, y1)) {
				buttonsDown[0].release();
				buttonsDown[0] = null;
			}
			if (buttonsDown[1] != null && buttonsDown[1].isDown()
					&& buttonsDown[1].contains(x1, y1)) {
				buttonsDown[1].release();
				buttonsDown[1] = null;
			}
			break;

		// all pointers are released
		case MotionEvent.ACTION_UP:
			if (buttonsDown[0] != null && buttonsDown[0].isDown()) {
				buttonsDown[0].release();
				buttonsDown[0] = null;
			} 
			if (buttonsDown[1] != null && buttonsDown[1].isDown()) {
				buttonsDown[1].release();
				buttonsDown[1] = null;
			}
			break;

		// some sort of movement (pretty much the default touch event)
		case MotionEvent.ACTION_MOVE:
			if (pointerCount == 1) {
				// if there's only 1 pointer and it's not on a button, we're dragging
				if (buttonsDown[0] == null && buttonsDown[1] == null) {
					dragEvent(x0, y0);
				// else check if the pointer slid off a button
				} else{
					if (buttonsDown[0] != null && !buttonsDown[0].contains(x0, y0)) {
							buttonsDown[0].slideRelease();
							buttonsDown[0] = null;
					}
					if (buttonsDown[1] != null && !buttonsDown[1].contains(x0, y0)){
							buttonsDown[1].slideRelease();
							buttonsDown[1] = null;
					}
				}
				checkForButtonPresses(x0, y0);
			} else if (pointerCount == 2) {
				// if there's two pointers and neither are on a button, we're zooming
				if (buttonsDown[0] == null && buttonsDown[1] == null) {
					// note that zoom is done regardless of camera mode
					zoomEvent(spacing);
					x0 = previousX;
					y0 = previousY;
					x1 = previousX;
					y1 = previousY;
				} else {
					// else we check if either finger slid off of a button
					if (buttonsDown[0] != null) {
						if (!buttonsDown[0].contains(x0, y0)
						 && !buttonsDown[0].contains(x1, y1)) {
							buttonsDown[0].slideRelease();
							buttonsDown[0] = null;
						}
					}
					if (buttonsDown[1] != null) {
						if (!buttonsDown[1].contains(x0, y0)
						 && !buttonsDown[1].contains(x1, y1)) {
							buttonsDown[1].slideRelease();
							buttonsDown[1] = null;
						}
					}
					checkForButtonPresses(x0, y0);
					checkForButtonPresses(x1, y1);
				}
			}
		}

		// update values used for panning/zooming
		previousX = x0;
		previousY = y0;
		previousSpacing = spacing;

		return true;
	}

	/**
	 * Checks whether a button lies underneath the given point and presses it if it does.
	 * The Button[] buttonsDown contains two references to Button objects. If they are both null,
	 * then it means no buttons are being pressed at that moment. If either of them are not null,
	 * it means that they are being held down by a pointer.
	 * @param x X of pointer, in screen coordinates
	 * @param y Y of pointer, in screen coordinates
	 * @return Whether or not a button was pressed
	 */
	private boolean checkForButtonPresses(float x, float y) {
		if(Render2D.gui == null)
			return false;
		else{
			boolean pressed = false;
			
			// check every button for presses
			Iterator<Button> it = Render2D.gui.getButtonIterator();
			while (it.hasNext()) {
				// break if two buttons are being pressed
				if(buttonsDown[0] != null && buttonsDown[1] != null)
					break;
				
				Button b = it.next();
	
				if (b.contains(x, y)) {
					if (buttonsDown[0] == null && buttonsDown[1] != b) {
						buttonsDown[0] = b;
						b.press();
						pressed = true;
					} else if (buttonsDown[1] == null && buttonsDown[0] != b) {
						buttonsDown[1] = b;
						b.press();
						pressed = true;
					}
				}
			}
	
			return pressed;
		}
	}

	/**
	 * Screen is being "dragged"
	 * 
	 * @param x
	 *            X of new location of finger
	 * @param y
	 *            Y of new location of finger
	 */
	private void dragEvent(float x, float y) {
		if(Render2D.camera.currentMode() == Camera.Modes.FREE){
			float dx = x - previousX;
			float dy = y - previousY;
			
			Vector2 camLoc = Render2D.camera.getLocation();
			camLoc.x += dx / DRAG_SENSITIVITY;
			camLoc.y -= dy / DRAG_SENSITIVITY;
			Render2D.camera.setLocation(camLoc);
		}
	}

	/**
	 * Zoom in or out (two finger pinch)
	 * 
	 * @param spacing
	 *            How far apart the two fingers are
	 */
	private void zoomEvent(float spacing) {
		float zoom = Render2D.camera.getZoom();

		if (spacing < previousSpacing - (MIN_ZOOM_SPACING / 2.0f))
			zoom -= spacing / ZOOM_SENSITIVITY;
		else if (spacing > previousSpacing + (MIN_ZOOM_SPACING / 2.0f))
			zoom += spacing / ZOOM_SENSITIVITY;

		Render2D.camera.setZoom(zoom);
	}


	/**
	 * Convert a screen-space vector to a world-space vector
	 * @param screenX X of screen space vector 
	 * @param screenY Y of screen space vector
	 * @return Screen-space coordinate translated to world-space
	 */
	public Vector2 toWorldSpace(float screenX, float screenY) {
		float[] 
			// projection matrix
			projection = new float[16],
			// view matrix (no 'model' since we're not looking at anything specific)
			view = new float[16],
			// used to multiply projection * view
			compoundMatrix = new float[16],
			// screen-space touch point, normalized
			normalizedInPoint = new float[4],
			// resulting world-space point
			outPoint = new float[4];
		
		// create the projection matrix (mimics Render2D's "setUpProjectionWorldCoords" method)
		Matrix.setIdentityM(projection, 0);
		Matrix.orthoM(projection, 0, 0, Game.aspect, 0, 1, -1, 1);
		Matrix.rotateM(projection, 0, Render2D.camera.getAngle(), 0.0f, 0.0f, 1.0f);
		
		// create the view matrix (essentially, an identity matrix scaled and translated to the camera's position)
		Matrix.setIdentityM(view, 0);
		Matrix.scaleM(view, 0, Render2D.camera.getZoom(), Render2D.camera.getZoom(), 1.0f);
		Matrix.translateM(view, 0, Render2D.camera.getLocation().x, Render2D.camera.getLocation().y, 0.0f);
		
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
}
