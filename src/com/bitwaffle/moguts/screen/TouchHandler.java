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
	
	//private Button buttonDown;
	private Button[] buttonsDown;
	
	/**
	 * Create a new touch handler
	 */
	public TouchHandler(){
		currentMode = Modes.DRAG;
		previousX = 0.0f;
		previousY = 0.0f;
		previousSpacing = 0.0f;
		buttonsDown = new Button[2];
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
	
	private float spacing(float x1, float y1, float x2, float y2){
		float x = x1 - x2;
		float y = y1 - y2;
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
	
	public boolean touchEvent(MotionEvent e){
		int pointerCount = e.getPointerCount();
		int action = e.getAction();
		
		for(int currentPointer = 0; currentPointer < pointerCount; currentPointer++){
			float x = e.getX(currentPointer);
			float y = e.getY(currentPointer);
			
			if(buttonsDown[0] != null && buttonsDown[0].getPointer() != currentPointer){
				if(action == MotionEvent.ACTION_UP){
					buttonsDown[0].release();
					buttonsDown[0] = null;
				} else if(action == MotionEvent.ACTION_MOVE){
					if(!buttonsDown[0].checkForPress(x, y)){
						buttonsDown[0].release();
						buttonsDown[0] = null;
					}
				}
			}
		}
		
		return true;
	}
	
	public boolean touchEventFucked(MotionEvent e){
		int action = e.getAction();
		int pointerCount = e.getPointerCount();
		float watX = e.getX();
		float watY = e.getY();
		
		for(int currentPointer = 0; currentPointer < pointerCount; currentPointer++){
			float x = e.getX(currentPointer);
			float y = e.getY(currentPointer);
			
			System.out.println("currentPointer " + currentPointer + " action " + action + " x " + x + " y " + y + " watx " + watX + " watY " + watY);
			
			if(buttonsDown[0] != null && buttonsDown[0].getPointer() != currentPointer){
				if(action == MotionEvent.ACTION_UP){
					buttonsDown[0].release();
					buttonsDown[0] = null;
				} else if(action == MotionEvent.ACTION_MOVE){
					if(!buttonsDown[0].checkForPress(x, y)){
						buttonsDown[0].release();
						buttonsDown[0] = null;
					}
				}
			}
			
			if(buttonsDown[1] != null && buttonsDown[1].getPointer() != currentPointer){
				if(action == MotionEvent.ACTION_UP){
					buttonsDown[1].release();
					buttonsDown[1] = null;
				} else if(action == MotionEvent.ACTION_MOVE){
					if(!buttonsDown[1].checkForPress(x, y)){
						buttonsDown[1].release();
						buttonsDown[1] = null;
					}
				}
			}
			
			if(!checkForButtonPresses(currentPointer, x, y)){
				System.out.println("no presses");
				if(pointerCount >= 2)
					currentMode = Modes.ZOOM;
				else
					currentMode = Modes.DRAG;
				
				switch(action){
				case MotionEvent.ACTION_MOVE:
					switch(currentMode){
					case DRAG:
						dragEvent(x, y);
						break;
					case ZOOM:
						float spacing = spacing(e);
						if(spacing > MIN_ZOOM_SPACING){
							zoomEvent(spacing);
							previousSpacing = spacing;
						}
						break;
					}
				}
			} else{
				System.out.println("presses");
			}
		}
		
		previousX = watX;
		previousY = watY;
		
		return true;
	}

	
	/**
	 * Check if any buttons have been pressed
	 * @param x X coordinate of press event
	 * @param y Y coordinate of press event
	 * @return Whether or not a button was pressed
	 */
	private boolean checkForButtonPresses(int pointer, float x, float y){
		// TODO this should really check every pointer that's down to see if it's on a button (maybe have a list of buttons that are down?)
		Iterator<Button> it = Game.render2D.gui.getIterator();
		
		boolean pressed = false;
		
		while(it.hasNext()){
			Button b = it.next();
			
			// FIXME this will only trigger a pressed event for the first button that gets pressed- is that practical?
			if(b.checkForPress(x, y)){
				if(buttonsDown[0] == null && buttonsDown[1] != b){
					buttonsDown[0] = b;
					b.press(pointer);
					pressed = true;
				} else if(buttonsDown[1] == null && buttonsDown[0] != b){
					buttonsDown[1] = b;
					b.press(pointer);
					pressed = true;
				}
			}
		}
		
		return pressed;
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
