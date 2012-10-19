package com.bitwaffle.guts.android;

import java.util.Iterator;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
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
		previousX = 0.0f;
		previousY = 0.0f;
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
		float spacing = MathHelper.spacing(x0, y0, x1, y1);

		switch (action) {
		// initial pointer is put down
		case MotionEvent.ACTION_DOWN:
			// if there's no button presses, start shooting
			if(!checkForButtonPresses(x0, y0) && !camera.currentMode().equals(Camera.Modes.FREE))
				player.beginShooting(MathHelper.toWorldSpace(x0, y0, camera));
			break;
			
		// first pointer is put down (happens when pointer 2 is kept down and pointer 1 goes up then down again)
		case MotionEvent.ACTION_POINTER_1_DOWN:
			// if there's no button presses, start shooting
			if(!checkForButtonPresses(x0, y0) && !camera.currentMode().equals(Camera.Modes.FREE))
				player.beginShooting(MathHelper.toWorldSpace(x0, y0, camera));
			break;
			
		// first pointer is lifted after two pointers are put down
		case MotionEvent.ACTION_POINTER_1_UP:
			// if a button is being pressed with the second pointer,
			// we stop shooting since we just released the first pointer
			if(
			(buttonsDown[0] != null && buttonsDown[0].contains(x1, y1)) ||
			(buttonsDown[1] != null && buttonsDown[1].contains(x1, y1)))
				player.endShooting();
			// else the second finger is down and shooting
			else
				player.updateTarget(MathHelper.toWorldSpace(x1, y1, camera));
			
			break;

		// second pointer is put down
		case MotionEvent.ACTION_POINTER_2_DOWN:
			// if there's no button presses, start shooting
			if(!checkForButtonPresses(x1, y1) && !camera.currentMode().equals(Camera.Modes.FREE))
				player.beginShooting(MathHelper.toWorldSpace(x1, y1, camera));
			break;

		// second pointer released
		case MotionEvent.ACTION_POINTER_2_UP:
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
			
			break;

		// all pointers are released
		case MotionEvent.ACTION_UP:
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
			break;

		// some sort of movement (pretty much the default touch event)
		case MotionEvent.ACTION_MOVE:
			if (pointerCount == 1) {
				// if there's only 1 pointer and it's not on a button, we're dragging or aiming
				if (buttonsDown[0] == null && buttonsDown[1] == null) {
					if(camera.currentMode().equals(Camera.Modes.FREE)){
						dragEvent(x0, y0);
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
			} else if (pointerCount == 2) {
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
						zoomEvent(spacing);
						x0 = previousX;
						y0 = previousY;
						x1 = previousX;
						y1 = previousY;
					// else check if there's any button presses and aim if there aren't
					}else if(!(checkForButtonPresses(x0, y0) || checkForButtonPresses(x1, y1)))
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
	 * Screen is being "dragged"
	 * 
	 * @param x
	 *            X of new location of finger
	 * @param y
	 *            Y of new location of finger
	 */
	private void dragEvent(float x, float y) {
		if(camera.currentMode() == Camera.Modes.FREE){
			float dx = x - previousX;
			float dy = y - previousY;
			
			Vector2 camLoc = camera.getLocation();
			camLoc.x += dx / DRAG_SENSITIVITY;
			camLoc.y -= dy / DRAG_SENSITIVITY;
			camera.setLocation(camLoc);
		}
	}

	/**
	 * Zoom in or out (two finger pinch)
	 * 
	 * @param spacing
	 *            How far apart the two fingers are
	 */
	private void zoomEvent(float spacing) {
		float zoom = camera.getZoom();

		if (spacing < previousSpacing - (MIN_ZOOM_SPACING / 2.0f))
			zoom -= spacing / ZOOM_SENSITIVITY;
		else if (spacing > previousSpacing + (MIN_ZOOM_SPACING / 2.0f))
			zoom += spacing / ZOOM_SENSITIVITY;

		camera.setZoom(zoom);
	}
}
