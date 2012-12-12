package com.bitwaffle.guts.android.input;

import java.util.Iterator;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.graphics.Camera;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.buttons.Button;
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
	
	/** Number of pointers currently down */
	private int pointerCount;
	
	/** Current position of first pointer */
	private float x0, y0;
	/** Touch position for first pointer from previous event */
	private float previousX0, previousY0;
	
	/** Current position of second pointer */
	private float x1, y1;
	/** Touch position for second pointer from previous event */
	@SuppressWarnings("unused")
	private float previousX1, previousY1;

	/** If two fingers are being used, how far apart they are */
	private float spacing, previousSpacing;

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
		if(pointerCount >= 2){
			x1 = e.getX(1);
			y1 = e.getY(1);
			spacing = MathHelper.spacing(x0, y0, x1, y1);
		} else{
			// FXIME will using 0.0f as a null value cause any issues? If random bugs ever occur, try changing this!
			x1 = 0.0f;
			y1 = 0.0f;
		}

		// call appropriate method based on action event
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_1_DOWN:  // happens when pointer 2 is kept down and pointer 1 goes up then down again
			pointer1Down();
			break;
		case MotionEvent.ACTION_POINTER_1_UP:    // first pointer is lifted after two pointers are put down
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
	 * First pointer is put down on the screen
	 */
	private void pointer1Down(){
		// if there's no button presses, start shooting
		if(!checkForButtonPresses(x0, y0) && !camera.currentMode().equals(Camera.Modes.FREE))
			player.beginShooting(MathHelper.toWorldSpace(x0, y0, camera));
	}
	
	/**
	 * First pointer is lifted from the screen
	 */
	private void pointer1Up(){
		// if a button is being pressed with the second pointer,
		// we stop shooting since we just released the first pointer
		if(
		(buttonsDown[0] != null && buttonsDown[0].contains(x1, y1)) ||
		(buttonsDown[1] != null && buttonsDown[1].contains(x1, y1)))
			player.endShooting();
		// else the second finger is down and shooting
		else
			player.updateTarget(MathHelper.toWorldSpace(x1, y1, camera));
		
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
		if(!checkForButtonPresses(x1, y1) && !camera.currentMode().equals(Camera.Modes.FREE))
			player.beginShooting(MathHelper.toWorldSpace(x1, y1, camera));
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
			player.endShooting();
		// else the first finger is down and shooting
		else
			player.updateTarget(MathHelper.toWorldSpace(x0, y0, camera));
					
	}
	
	/**
	 * All pointers are released from the screen
	 */
	private void allPointersUp(){
		// release any pressed buttons
		if (buttonsDown[0] != null && buttonsDown[0].isDown()) {
			buttonsDown[0].release();
			buttonsDown[0] = null;
		} 
		if (buttonsDown[1] != null && buttonsDown[1].isDown()) {
			buttonsDown[1].release();
			buttonsDown[1] = null;
		}
		// stop shooting
		player.endShooting();
	}
	
	/**
	 * Single finger is down and moving
	 */
	private void singleFingerMove(){
		// if there's only 1 pointer and it's not on a button, we're dragging or aiming
		if (buttonsDown[0] == null && buttonsDown[1] == null) {
			if(camera.currentMode().equals(Camera.Modes.FREE)){
				dragEvent();
				player.endShooting();
			}else
				player.updateTarget(MathHelper.toWorldSpace(x0, y0, camera));
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
	}
	
	/**
	 * Two fingers down and at least one is moving
	 */
	private void twoFingerMove(){
		// check if either finger slid off of a button
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
		// if there's two pointers and neither are on a button, we're zooming
		if (buttonsDown[0] == null && buttonsDown[1] == null) {
			// zoom if the camera is in free mode
			if(camera.currentMode().equals(Camera.Modes.FREE)){
				player.endShooting();
				zoomEvent();
			// else check if there's any button presses and aim if there aren't
			} else if(!(checkForButtonPresses(x0, y0) || checkForButtonPresses(x1, y1)))
				player.updateTarget(MathHelper.toWorldSpace(x0, y0, camera));
		} else {
			// button 0 is down and button 1 isn't
			if(buttonsDown[0] != null && buttonsDown[1] == null){
				// first pointer is on button 0, aim with second pointer if no button presses
				if(buttonsDown[0].contains(x0, y0) && !checkForButtonPresses(x1, y1))
					player.updateTarget(MathHelper.toWorldSpace(x1, y1, camera));
				
				// check if first pointer hits any buttons, shoot if it didn't
				else if(!checkForButtonPresses(x0, y0))
					player.updateTarget(MathHelper.toWorldSpace(x0, y0, camera));
				
			// button 1 is down and button 0 isn't
			} else if(buttonsDown[0] == null && buttonsDown[1] != null) {
				// first pointer is on button 1, aim with second pointer if no button presses
				if(buttonsDown[1].contains(x0, y0) && !checkForButtonPresses(x1, y1))
					player.updateTarget(MathHelper.toWorldSpace(x1, y1, camera));
				
				
				// check if first pointer hit any buttons, shoot if it didn't
				else if(!checkForButtonPresses(x0, y0))
					player.updateTarget(MathHelper.toWorldSpace(x0, y0, camera));
			}
		}
	}

	/**
	 * Screen is being "dragged" by a single finger
	 */
	private void dragEvent() {
		Vector2 current = MathHelper.toWorldSpace(x0, y0, camera);
		Vector2 previous = MathHelper.toWorldSpace(previousX0, previousY0, camera);
		
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
		
		float zoom = camera.getZoom();
		
		zoom += (ds * zoom) / ZOOM_SENSITIVITY;

		camera.setZoom(zoom);
		
		previousSpacing = spacing;
	}
}
