package com.bitwaffle.guts.android.input;

import java.util.Iterator;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.Player;

/**
 * Handles any touch events (which are generated in {@link SurfaceView} and then
 * sent here)
 * 
 * @author TranquilMarmot
 */
public class TouchHandler {
	/** How sensitive zoom is- the higher the value, the less sensitive */
	public final float ZOOM_SENSITIVITY = 100.0f;
	
	/** How much two fingers have to move from each other before zooming occurs */
	public final float ZOOM_THRESHOLD = 5.0f;
	
	/** Number of pointers currently down */
	private int pointerCount;
	
	/** Current position of first pointer */
	private float x0, y0;
	/** Touch position for first pointer from previous event */
	private float previousX0, previousY0;
	
	/** Current position of second pointer */
	private float x1, y1;
	/** Touch position for second pointer from previous event */
	private float previousX1, previousY1;

	/** If two fingers are being used, how far apart they are */
	private float spacing, previousSpacing;
	
	/** If two fingers are being used, the point in the middle of them */
	private Vector2 midpoint, previousMidpoint;

	/** See comment for checkForButtonPresses() */
	private Button[] buttonsDown;
	
	/** The player being controlled by this touch handler */
	private Player player;
	
	/** The camera being controlled by this touch handler */
	private Camera camera;

	/**
	 * Create a new touch handler
	 */
	public TouchHandler(Player player, Camera camera) {
		this.player = player;
		this.camera = camera;
		x0 = 0.0f;
		y0 = 0.0f;
		x1 = 0.0f;
		y1 = 0.0f;
		previousX0 = 0.0f;
		previousY0 = 0.0f;
		previousX1 = 0.0f;
		previousY1 = 0.0f;
		previousSpacing = 0.0f;
		midpoint = new Vector2(0.0f, 0.0f);
		previousMidpoint = new Vector2(0.0f, 0.0f);
		buttonsDown = new Button[2];
	}
	
	/**
	 * Set the player being controlled by this touch handler
	 * @param player New player to control
	 */
	public void setPlayer(Player player){
		this.player = player;
	}
	
	/**
	 * Set the camera this touch handler is controlling
	 * @param camera New camera to control
	 */
	public void setCamera(Camera camera){
		this.camera = camera;
	}

	/**
	 * Take care of any touch events
	 * @param e MotionEvent touch event came from
	 * @return ???
	 */
	public boolean touchEvent(MotionEvent e) {
		// update all values
		pointerCount = e.getPointerCount();
		x0 = e.getX(0);
		y0 = e.getY(0);
		
		// 2 pointers down, get pointer 2 values
		if(pointerCount >= 2){
			x1 = e.getX(1);
			y1 = e.getY(1);
			// if there's two pointers, they have a spacing and a midpoint
			spacing = MathHelper.spacing(x0, y0, x1, y1);
			MathHelper.midPoint(midpoint, x0, y0, x1, y1);
		} else{
			x1 = Float.NaN;
			y1 = Float.NaN;
			spacing = Float.NaN;
			midpoint.set(Float.NaN, Float.NaN);
		}

		// call appropriate method based on action event
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_1_DOWN:  // happens when pointer 2 is kept down and pointer 1 goes up then down again
			pointer1Down();
			break;
		case MotionEvent.ACTION_POINTER_1_UP:    // first pointer is lifted after two pointers are put down (second finger still down)
			pointer1Up();
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:
			pointer2Down();
			break;
		case MotionEvent.ACTION_POINTER_2_UP:
			pointer2Up();
			break;
		case MotionEvent.ACTION_UP:              // all pointers are released
			allPointersUp();
			break;
		case MotionEvent.ACTION_MOVE:            // some sort of movement (pretty much the default touch event)
			if (pointerCount == 1)
				singleFingerMove();
			else if (pointerCount >= 2)
				twoFingerMove();
		}

		// update values used for panning/zooming
		previousSpacing = spacing;
		previousMidpoint.set(midpoint);
		previousX0 = x0;
		previousY0 = y0;
		previousX1 = x1;
		previousY1 = y1;

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
		if(Game.gui == null)
			return false;
		else{
			boolean pressed = false;
			
			// check every button for presses
			Iterator<Button> it = Game.gui.getButtonIterator();
			while (it.hasNext()) {
				// break if two buttons are being pressed
				if(buttonsDown[0] != null && buttonsDown[1] != null)
					break;
				
				Button b = it.next();
	
				if (b.isActive() && b.isVisible() && b.contains(x, y)) {
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
	 * Make the player begin shooting
	 * @param x X location of spot to shoot, in screen space
	 * @param y Y location of spot to shoot, in screen space
	 */
	private void beginShooting(float x, float y){
		if(player != null && !player.isShooting())
			player.beginShooting(MathHelper.toWorldSpace(x, y, camera));
	}
	
	/**
	 * Tell the player to stop shooting
	 */
	private void endShooting(){
		if(player != null)
			player.endShooting();
	}
	
	
	/**
	 * Update the player's target
	 * @param x X location of spot to shoot, in screen space
	 * @param y Y location of spot to shoot, in screen space
	 */
	private void updateTarget(float x, float y){
		if(player != null)
			player.updateTarget(MathHelper.toWorldSpace(x, y, camera));
	}
	
	/**
	 * First pointer is put down on the screen
	 */
	private void pointer1Down(){
		// if there's no button presses, start shooting
		if(!checkForButtonPresses(x0, y0)){
			if(camera.currentMode().equals(Camera.Modes.FOLLOW))
				beginShooting(x0, y0);
		}
		
	}
	
	/**
	 * First pointer is lifted from the screen, second pointer still down
	 */
	private void pointer1Up(){
		// if a button is being pressed with the second pointer,
		// we stop shooting since we just released the first pointer
		if(
		(buttonsDown[0] != null && buttonsDown[0].contains(x1, y1)) ||
		(buttonsDown[1] != null && buttonsDown[1].contains(x1, y1)))
			endShooting();
		// else the second finger is down and shooting
		else if(camera.currentMode() == Camera.Modes.FOLLOW)
			updateTarget(x1, y1);
		
		/*
		 * Since draggaing uses x0 and y0's values, but the finger controlling
		 * x0 and y0 just switched (to the second finger) so we need to set
		 * values accordingly
		 */
		x0 = x1;
		y0 = y1;
		
	}
	
	/**
	 * Second pointer is put down
	 */
	private void pointer2Down(){
		// if there's no button presses, start shooting
		if(!checkForButtonPresses(x1, y1) && camera.currentMode().equals(Camera.Modes.FOLLOW))
			beginShooting(x1, y1);
	}
	
	/**
	 * Second pointer released
	 */
	private void pointer2Up(){
		// check for any button releases
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
		
		// if a button is being pressed with the first pointer,
		// we stop shooting since we just released the second pointer
		if(
		(buttonsDown[0] != null && buttonsDown[0].contains(x0, y0)) ||
		(buttonsDown[1] != null && buttonsDown[1].contains(x0, y0)))
			endShooting();
		// else the first finger is down and shooting
		else if(camera.currentMode() == Camera.Modes.FOLLOW)
			updateTarget(x0, y0);
					
	}
	
	/**
	 * All pointers are released from the screen
	 */
	private void allPointersUp(){
		// either release or slide-release button 0
		if(buttonsDown[0] != null){
			if(buttonsDown[0].isDown() && (buttonsDown[0].contains(x0, y0) || buttonsDown[0].contains(x1,y1))){
				buttonsDown[0].release();
				buttonsDown[0] = null;
			} else {
				buttonsDown[0].slideRelease();
				buttonsDown[0] = null;
			}
		}
		
		// either release or slide-release button 0
		if(buttonsDown[1] != null){
			if(buttonsDown[1].isDown() && (buttonsDown[1].contains(x0, y0) || buttonsDown[1].contains(x1,y1))){
				buttonsDown[1].release();
				buttonsDown[1] = null;
			} else {
				buttonsDown[1].slideRelease();
				buttonsDown[1] = null;
			}
		}
		
		// stop shooting
		endShooting();
	}
	
	/**
	 * Single finger is down and moving
	 */
	private void singleFingerMove(){
		// if there's only 1 pointer and it's not on a button, we're dragging or aiming
		if (buttonsDown[0] == null && buttonsDown[1] == null) {
			if(camera.currentMode().equals(Camera.Modes.FREE)){
				panEvent(previousX0, previousY0, x0, y0);
				endShooting();
			}else if(player != null)
				updateTarget(x0, y0);
		// else drag button
		} else{
			// drag first button
			if (buttonsDown[0] != null)
					buttonsDown[0].drag(x0 - previousX0, y0 - previousY0);
			
			// drag second button
			if (buttonsDown[1] != null)
					buttonsDown[1].drag(x0 - previousX0, y0 - previousY0);
		}
	}
	
	/**
	 * Two fingers down and at least one is moving
	 */
	private void twoFingerMove(){
		// drag/slide off of button 0
		if (buttonsDown[0] != null) {
			// contains first finger, drag with first finger
			if(buttonsDown[0].contains(x0, y0))
				buttonsDown[0].drag(x0 - previousX0, y0 - previousY0);
			// contains second finger, drag with second finger
			else if(buttonsDown[0].contains(x1, y1))
				buttonsDown[0].drag(x1 - previousX1, y1 - previousY1);
		}
		
		// drag/slide off of button 1
		if (buttonsDown[1] != null) {
			// contains first finger, drag with first finger
			if(buttonsDown[1].contains(x0, y0))
				buttonsDown[1].drag(x0 - previousX0, y0 - previousY0);
			// contains second finger, drag with second finger
			else if(buttonsDown[1].contains(x1, y1))
				buttonsDown[1].drag(x1 - previousX1, y1 - previousY1);
		}
		
		// if there's two pointers and neither are on a button, we're zooming
		if (buttonsDown[0] == null && buttonsDown[1] == null) {
			// zoom if the camera is in free mode
			if(camera.currentMode().equals(Camera.Modes.FREE)){
				endShooting();
				zoomEvent();
				panEvent(previousMidpoint.x, previousMidpoint.y, midpoint.x, midpoint.y);
			// else check if there's any button presses and aim if there aren't
			} else if(!(checkForButtonPresses(x0, y0) || checkForButtonPresses(x1, y1)))
				updateTarget(x0, y0);
		} else {
			// button 0 is down and button 1 isn't
			if(buttonsDown[0] != null && buttonsDown[1] == null){
				// first pointer is on button 0, aim with second pointer if no button presses
				if(buttonsDown[0].contains(x0, y0) && !checkForButtonPresses(x1, y1))
					updateTarget(x1, y1);
				
				// check if first pointer hits any buttons, shoot if it didn't
				else if(!checkForButtonPresses(x0, y0))
					updateTarget(x0, y0);
				
			// button 1 is down and button 0 isn't
			} else if(buttonsDown[0] == null && buttonsDown[1] != null) {
				// first pointer is on button 1, aim with second pointer if no button presses
				if(buttonsDown[1].contains(x0, y0) && !checkForButtonPresses(x1, y1))
					updateTarget(x1, y1);
				
				
				// check if first pointer hit any buttons, shoot if it didn't
				else if(!checkForButtonPresses(x0, y0))
					updateTarget(x0, y0);
			}
		}
	}

	/**
	 * Screen is being "dragged" by a single finger to change where the camera is looking
	 */
	private void panEvent(float prevX, float prevY, float curX, float curY) {
		Vector2 current = MathHelper.toWorldSpace(curX, curY, camera);
		Vector2 previous = MathHelper.toWorldSpace(prevX, prevY, camera);
		
		float dx = current.x - previous.x;
		float dy = current.y - previous.y;
		
		Vector2 camLoc = camera.getLocation();
		camLoc.x += dx;
		camLoc.y += dy;
		camera.setLocation(camLoc);
	}

	/**
	 * Zoom in or out (two finger pinch)
	 */
	private void zoomEvent() {
		float ds = spacing - previousSpacing;

		// only zoom if the amount is in the threshold
		if(ds > ZOOM_THRESHOLD || ds < -ZOOM_THRESHOLD){
			float zoom = camera.getZoom();
			
			zoom += (ds * zoom) / ZOOM_SENSITIVITY;

			camera.setZoom(zoom);
			
			previousSpacing = spacing;
		}
	}
}
