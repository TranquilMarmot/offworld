package com.bitwaffle.guts.graphics.graphics2d;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.Room;

public abstract class Camera2DMode {
	/** Camera this mode belongs to */
	private Camera2D camera;
	
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	private float zoom = 0.008f;
	
	/** Minimum and maximum zoom values */
	private float minZoom = 0.008f, maxZoom = 0.15f;
	
	/** Whether or not to keep the camera within Physics.getCurrentRoom */
	private boolean boundsCheck = false;

	public Camera2DMode(Camera2D camera){
		this.camera = camera;
	}
	
	public float zoom(){
		return zoom;
	}
	
	public void setZoom(float newZoom){
		float oldZoom = this.zoom;
		
		// make sure zoom stays within bounds
		if(newZoom > maxZoom)
			newZoom = maxZoom;
		else if(newZoom < minZoom)
			newZoom = minZoom;
		
		if(newZoom != oldZoom){
			this.zoom = newZoom;
			
			Vector2 prevWorldWindowSize = new Vector2(camera.worldWindowSize);
			Vector2 newWorldWindowSize = camera.getWorldWindowSize();
			
			// make sure the camera doesn't zoom outside of the room if bounds are being checked
			if(boundsCheck){
				Room r = Game.physics.currentRoom();
				if(r != null){
					if(newWorldWindowSize.x > r.getRoomWidth() || newWorldWindowSize.y > r.getRoomHeight()){
						this.zoom = oldZoom;
						return;
					}
				}
			}
			
			
			// update window size, since we zoomed
			camera.worldWindowSize.set(newWorldWindowSize);
			
			// make it so the camera zooms in to the middle
			Vector2 newLocation = new Vector2(camera.getLocation());
			newLocation.x += camera.worldWindowSize.x - prevWorldWindowSize.x;
			newLocation.y += camera.worldWindowSize.y - prevWorldWindowSize.y; 
			camera.setLocation(newLocation);
		}
	}
	
	public boolean boundsCheck(){ return boundsCheck; }
		
	public abstract void update(Camera2D camera, float timeStep);

}
