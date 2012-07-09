package com.bitwaffle.moguts;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.graphics.render.GLRenderer;

import android.util.FloatMath;
import android.view.MotionEvent;

/**
 * Handles any touch events (which are generated in {@link SurfaceView}
 * and then sent here)
 * 
 * @author TranquilMarmot
 */
public class TouchHandler {
	/**
	 * Different touch modes (changes the way touchEvent works)
	 */
	public enum Modes{
		DRAG(0),
		ZOOM(1);
		
		int mode;
		Modes(int mode){
			this.mode = mode;
		}
	}
	
	/** The current touch mode */
	private Modes currentMode = Modes.DRAG;
	
	/** Touch position from previous event */
	private float previousX, previousY;
	
	/** If two fingers are being used, how far apart they are */
	private float previousSpacing;
	
	public final float MIN_ZOOM_SPACING = 20.0f;
	
	public final float ZOOM_SENSITIVITY = 500000.0f;
	public final float DRAG_SENSITIVITY = 20.0f;
	
	/**
	 * Create a new touch handler
	 */
	public TouchHandler(){
		currentMode = Modes.DRAG;
		previousX = 0.0f;
		previousY = 0.0f;
		previousSpacing = 0.0f;
	}
	
	/**
	 * @return Current mode of the TouchHandler
	 */
	public Modes currentMode(){
		return currentMode;
	}
	
	/**
	 * Get how far apart two fingers are
	 * @param event Touch event
	 * @return Distance between two fingers
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	
	/**
	 * Get the midpoint between two fingers
	 * @param point Point to output midpoint to (avoid creating object to save garbage collector some time)
	 * @param event Touch event
	 */
	@SuppressWarnings("unused")
	private void midPoint(Vector2 point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	
	/**
	 * Take care of any TouchEvents
	 * @param e MotionEvent
	 * @return ???
	 */
	public boolean touchEvent(MotionEvent e){
		// TODO On the droid this only gives 1 or 2, test it on other devices
		int pointerCount = e.getPointerCount();
		
		// get new values
		float x = e.getX();
		float y = e.getY();
		float spacing = spacing(e);
		
		// TODO check for button presses here
		
		// if only one pointer is down, we're in drag mode
		if(pointerCount == 1)
			currentMode = Modes.DRAG;
		// else if two pointers are down, we're in zoom mode
		else if(pointerCount >= 2)
			currentMode = Modes.ZOOM;
		
		switch(e.getAction()){
		case MotionEvent.ACTION_MOVE:
			switch(currentMode){
			case DRAG:
				dragEvent(x, y);
				break;
			case ZOOM:
				if(spacing > MIN_ZOOM_SPACING)
					zoomEvent(spacing);
				break;
			}
			break;
		}
		
		// set previous values for next touch event
		previousX = x;
		previousY = y;
		previousSpacing = spacing;
		
		return true;
	}
	
	/**
	 * Screen is being "dragged"
	 * @param x X of new location of finger
	 * @param y Y of new location of finger
	 */
	private void dragEvent(float x, float y){
		float dx = x - previousX;
        float dy = y - previousY;
        
        Vector2 camLoc = GLRenderer.render2D.camera.getLocation();
        camLoc.x += dx / DRAG_SENSITIVITY;
        camLoc.y -= dy / DRAG_SENSITIVITY;
        GLRenderer.render2D.camera.setLocation(camLoc);
	}
	
	/**
	 * Zoom in or out (two finger pinch zoom)
	 * @param spacing How far apart the two fingers are
	 */
	private void zoomEvent(float spacing){
		float zoom = GLRenderer.render2D.camera.getZoom();
		
		if(spacing < previousSpacing - (MIN_ZOOM_SPACING / 2.0f))
			zoom -= spacing / ZOOM_SENSITIVITY;
		else if(spacing > previousSpacing + (MIN_ZOOM_SPACING / 2.0f))
			zoom += spacing / ZOOM_SENSITIVITY;
		
		GLRenderer.render2D.camera.setZoom(zoom);
	}
}
