package com.bitwaffle.guts.android.input.touch;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.Player;

/**
 * Handles touch events. See {@link Pointer} as well.
 * 
 * @author TranquilMarmot
 */
public class TouchHandler {
	/** How sensitive zoom is- the higher the value, the less sensitive */
	public final float ZOOM_SENSITIVITY = 100.0f;
	
	/** How much two fingers have to move from each other before zooming occurs */
	public final float ZOOM_THRESHOLD = 3.0f;
	
	/** 
	 * Array of pointers, gets expanded if necessary (if number of pointers down is > length)
	 * Given a MotionEvent e,  calling e.getPointerId(e.getActionIndex()) will give you the index
	 * in this array of the action's Pointer
	 */
	private ArrayList<Pointer> pointers;
	
	/** The player being controlled by this touch handler */
	protected Player player;
	
	/** The camera being controlled by this touch handler */
	protected Camera camera;
	
	/** How many pointers are currently down */
	protected int pointerCount;
	
	/** How far apart the first two pointers put down are  */
	protected float spacing, prevSpacing;
	
	/** Midpoint between two fingers */
	protected Vector2 midpoint, prevMidpoint;
	
	/**
	 * Create a new touch handler to handle touch events
	 * @param player Player being touched
	 * @param camera Camera being touched
	 */
	public TouchHandler(Player player, Camera camera){
		this.player = player;
		this.camera = camera;
		
		pointers = new ArrayList<Pointer>(3);
		midpoint = new Vector2(0.0f, 0.0f);
		prevMidpoint = new Vector2(0.0f, 0.0f);
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
	 * Touch event, generated in SurfaceView
	 * @param e Event from SurfaceView
	 * @return ???
	 */
	public boolean touchEvent(MotionEvent e) {
		this.pointerCount = e.getPointerCount();
		// expand number of pointers in array if necessary
		while(pointers.size() < pointerCount)
			pointers.add(new Pointer(this));
		
		// get event's pointer ID, X and Y values
		int actionIndex = e.getActionIndex();
		int pointerID = e.getPointerId(actionIndex);
		float pointerX = e.getX(actionIndex);
		float pointerY = e.getY(actionIndex);
		
		// call appropriate method for event pointer
		switch(e.getActionMasked() & MotionEvent.ACTION_MASK){
		// pointer put down
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			pointers.get(pointerID).down(pointerID, pointerX, pointerY);
			break;
		
		// pointer released
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_UP:
			pointers.get(pointerID).up(pointerX, pointerY);
			break;
			
		// see move method
		case MotionEvent.ACTION_MOVE:
			move(e);
			break;
			
		// haven't figured out how to trigger this one
		case MotionEvent.ACTION_OUTSIDE:
			//System.out.println("outside");
			break;
			
		// or this one
		case MotionEvent.ACTION_CANCEL:
			//System.out.println("cancel");
			break;
		}

		return true;
	}
	
	/**
	 * What happens on a move event
	 * @param e Event movement came from
	 */
	private void move(MotionEvent e){
		/*
		 * if there's >= 2 pointers down, they get "captured" and skipped in
		 * the loop that sends the pointers the move events (this gets set to 2,
		 * so it skips the first two buttons)
		 */
		int i = 0;
		
		// zoom/pan
		if(pointerCount >= 2){
			// find which two pointers are down (iterate through all of them and grab the first two that are down)
			Pointer p0 = null, p1 = null;
			Iterator<Pointer> it = pointers.iterator();
			while(it.hasNext()){
				Pointer p = it.next();
				if(p.isDown){
					if(p0 == null)
						p0 = p;
					else if(p1 == null)
						p1 = p;
				}
				if(p0 == null && p1 == null)
					break;
			}
			
			
			if(p0 != null && p1 != null){
				// grab pointer location
				int pointer0Index = e.findPointerIndex(p0.pointerID);
				float p0X = e.getX(pointer0Index);
				float p0Y = e.getY(pointer0Index);
				
				int pointer1Index = e.findPointerIndex(p1.pointerID);
				float p1X = e.getX(pointer1Index);
				float p1Y = e.getY(pointer1Index);

				// if neither are on buttons and we're in free mode, zoom
				if(camera.currentMode() == Camera.Modes.FREE && p0.buttonDown == null && p1.buttonDown == null){
					this.spacing = MathHelper.spacing(p0X, p0Y, p1X, p1Y);
					MathHelper.midpoint(midpoint, p0X, p0Y, p1X, p1Y);
					
					// if these value are 0, this is the first two-pointer event
					if(this.prevSpacing == 0.0f)
						this.prevSpacing = this.spacing;
					if(prevMidpoint.x == 0.0f && prevMidpoint.y == 0.0f)
						prevMidpoint.set(midpoint);
					
					// zoom and pan with midpoint
					zoomEvent(spacing, prevSpacing);
					//panEvent(midpoint.x, midpoint.y, prevMidpoint.x, prevMidpoint.y);
					
					// set values for next event
					this.prevSpacing = this.spacing;
					prevMidpoint.set(midpoint);
				} else{
					p0.move(p0X, p0Y);
					p1.move(p1X, p1Y);
				}
			}
			
			// set i to 2 so it skips the first two buttons
			i = 2;
		} else{
			// set all these to 0 for the next time there's >= 2 pointers
			spacing = 0.0f;
			prevSpacing = 0.0f;
			midpoint.set(0.0f, 0.0f);
			prevMidpoint.set(0.0f, 0.0f);
		}
		
		
		
		// iterate through each pointer in the event
		for(; i < pointerCount; i++){
			Pointer p = pointers.get(i);

			// 	send the pointer a move event
			if(p.isDown){
				int pointerIndex = e.findPointerIndex(p.pointerID);
				p.move(e.getX(pointerIndex), e.getY(pointerIndex));
			}
		}
	}
	
	
	/**
	 * Screen is being "dragged" to change where the camera is looking
	 * @param curX X location being dragged to
	 * @param curY Y location being dragged to
	 * @param prevX Previous X location
	 * @param prevY Previous Y location
	 */
	protected void panEvent(float curX, float curY, float prevX, float prevY) {
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
	 * Two fingers down, neither on a button
	 * @param spacing Space between pointers
	 * @param prevSpacing Previous spacing between pointers
	 */
	private void zoomEvent(float spacing, float prevSpacing){
		float ds = spacing - prevSpacing;

		// only zoom if the amount is in the threshold
		if(ds > ZOOM_THRESHOLD || ds < -ZOOM_THRESHOLD){
			float zoom = camera.getZoom();
			
			zoom += (ds * zoom) / ZOOM_SENSITIVITY;

			camera.setZoom(zoom);
		}
	}
	
}
