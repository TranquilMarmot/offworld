package com.bitwaffle.moguts.screen;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.gui.button.Button;

import android.opengl.Matrix;
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
	
	/** How far apart fingers have to be before pinch zooming happens */
	public final float MIN_ZOOM_SPACING = 10.0f;
	
	/** How sensitive zoom is- the higher the value, the less sensitive */
	public final float ZOOM_SENSITIVITY = 700000.0f;
	
	/** How sensitive dragging is- the higher the value, the less sensitive */
	public final float DRAG_SENSITIVITY = 2.0f;
	
	private Button buttonDown;
	
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
		// get new values
		float x = e.getX();
		float y = e.getY();
		float spacing = spacing(e);
		
		int action = e.getAction();
		
		if(action == MotionEvent.ACTION_UP){
			if(buttonDown != null){
				buttonDown.release();
				buttonDown = null;
			}
		} else if(!checkForButtonPresses(x, y)){
			// TODO On the droid this only gives 1 or 2, test it on other devices
			int pointerCount = e.getPointerCount();
			
			// if only one pointer is down, we're in drag mode
			if(pointerCount == 1)
				currentMode = Modes.DRAG;
			// else if two or more pointers are down, we're in zoom mode
			else if(pointerCount >= 2)
				currentMode = Modes.ZOOM;
			
			switch(action){
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
		}
		
		// set previous values for next touch event
		previousX = x;
		previousY = y;
		previousSpacing = spacing;
		
		return true;
	}
	
	/**
	 * Check if any buttons have been pressed
	 * @param x X coordinate of press event
	 * @param y Y coordinate of press event
	 * @return Whether or not a button was pressed
	 */
	private boolean checkForButtonPresses(float x, float y){
		Iterator<Button> it = Game.render2D.gui.getIterator();
		
		while(it.hasNext()){
			Button b = it.next();
			
			// FIXME this will only trigger a pressed event for the first button that gets pressed- is that practical?
			if(b.checkForPress(x, y)){
				buttonDown = b;
				b.press();
				return true;
			}
		}
		
		return false;
	}
	
	public Vector2 toScreenSpace(float touchX, float touchY){
		Vector2 pos = new Vector2(0.0f, 0.0f);
		
		float screenW = Game.windowWidth;
		float screenH = Game.windowHeight;
		
		float[] invertedMatrix, transformMatrix, normalizedInPoint, outPoint;
		invertedMatrix = new float[16];
		transformMatrix = new float[16];
		normalizedInPoint = new float[4];
		outPoint = new float[4];
		
		float oglTouchY = screenH - touchY;
		
		normalizedInPoint[0] = touchX * 2.0f / screenW - 1.0f;
		normalizedInPoint[1] = oglTouchY * 2.0f / screenH - 1.0f;
		normalizedInPoint[2] = -1.0f;
		normalizedInPoint[3] = 1.0f;
		
		Matrix.multiplyMM(transformMatrix, 0, Game.render2D.currenProjection(), 0, Game.render2D.currentModelview(), 0);
		Matrix.invertM(invertedMatrix, 0, transformMatrix, 0);
		
		Matrix.multiplyMV(outPoint, 0, invertedMatrix, 0, normalizedInPoint, 0);
		
		if(outPoint[3] == 0.0f)
			System.out.println("Divide by zero err!");
		
		pos.set(outPoint[0] / outPoint[3], outPoint[1] / outPoint[3]);
		
		//System.out.println(pos.x + " " + pos.y);
		
		return pos;
	}
	
	/**
	 * Screen is being "dragged"
	 * @param x X of new location of finger
	 * @param y Y of new location of finger
	 */
	private void dragEvent(float x, float y){
		float dx = x - previousX;
        float dy = y - previousY;
        
        Vector2 camLoc = Game.render2D.camera.getLocation();
        camLoc.x += dx / DRAG_SENSITIVITY;
        camLoc.y -= dy / DRAG_SENSITIVITY;
        Game.render2D.camera.setLocation(camLoc);
	}
	
	/**
	 * Zoom in or out (two finger pinch zoom)
	 * @param spacing How far apart the two fingers are
	 */
	private void zoomEvent(float spacing){
		float zoom = Game.render2D.camera.getZoom();
		
		if(spacing < previousSpacing - (MIN_ZOOM_SPACING / 2.0f))
			zoom -= spacing / ZOOM_SENSITIVITY;
		else if(spacing > previousSpacing + (MIN_ZOOM_SPACING / 2.0f))
			zoom += spacing / ZOOM_SENSITIVITY;
		
		Game.render2D.camera.setZoom(zoom);
	}
}
