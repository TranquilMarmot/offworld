package com.bitwaffle.guts.graphics.render.render2d.camera;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.entities.entities2d.Entity;
import com.bitwaffle.guts.graphics.render.render2d.camera.modes.CameraMode;
import com.bitwaffle.guts.graphics.render.render2d.camera.modes.FollowMode;
import com.bitwaffle.guts.graphics.render.render2d.camera.modes.FreeMode;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Describes how a scene should be rendered and handles following a given entity
 * 
 * @author TranquilMarmot
 */
public class Camera2D extends Entity {
	/** Initial values for camera */
	private static final float DEFAULT_CAMX = 1.0f, DEFAULT_CAMY = -30.0f, DEFAULT_CAMZ = 0.03f;
	
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	private float zoom;
	
	/** Minimum and maximum zoom values */
	private float minZoom = 0.008F, maxZoom = 0.15F;
	
	/** Current camera mode */
	private Modes currentMode = Modes.FOLLOW;
	
	/** Used to determine how much of the screen the camera can see */
	private Matrix4 projection, view;
	
	/** How much of the world the camera can see. X value is width in world-coordinates, Y value is height */
	private Vector2 worldWindowSize;
	
	/** Whether or not to keep the camera within Physics.getCurrentRoom */
	private boolean boundsCheck = true;
	
	/** Different camera modes */
	public static enum Modes{
		FOLLOW(new FollowMode(100.0f, 0.0f, -0.3f, 2.25f, 1.0f)),
		FREE(new FreeMode());
		
		private CameraMode mode;
		private Modes(CameraMode mode){ this.mode = mode; }
		public void update(float timeStep){ mode.update(timeStep); } 
		public void setCamera(Camera2D camera){ mode.setCamera(camera); }
		public CameraMode getMode(){ return mode; }
	}
	
	public Camera2D(){
		super();
		projection = new Matrix4();
		view = new Matrix4();
		worldWindowSize = new Vector2();
		this.setLocation(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY));
		this.location.set(DEFAULT_CAMX, DEFAULT_CAMY);
		this.setZoom(DEFAULT_CAMZ);
		
		for(Modes mode : Modes.values())
			mode.setCamera(this);
	}
	
	@Override
	public void update(float timeStep) {
		currentMode.update(timeStep);
		
		if(boundsCheck){
			Room r = Game.physics.currentRoom();
			if(r != null)
				boundsCheck(r);	
		}
	}
	
	/** Set the camera to follow a new entity */
	public void setTarget(DynamicEntity entity){
		((FollowMode)Modes.FOLLOW.getMode()).setTarget(entity);
	}
	
	/** @return What this camera is currently following */
	public DynamicEntity getTarget() {
		return ((FollowMode)Modes.FOLLOW.getMode()).getTarget();
	}
	
	/** Set the mode of the camera */
	public void setMode(Modes newMode){ this.currentMode = newMode; }
	
	/** @return Current mode of camera, from Camera.Modes */
	public Modes currentMode(){ return currentMode; }
	
	/** @return Current zoom level of this camera */
	public float getZoom(){ return zoom; }
	
	public void setZoom(float newZoom){
		float oldZoom = this.zoom;
		
		// make sure zoom stays within bounds
		if(newZoom > maxZoom)
			newZoom = maxZoom;
		else if(newZoom < minZoom)
			newZoom = minZoom;
		
		if(newZoom != oldZoom){
			this.zoom = newZoom;
			
			Vector2 prevWorldWindowSize = new Vector2(worldWindowSize);
			Vector2 newWorldWindowSize = getWorldWindowSize(this.zoom);
			
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
			this.worldWindowSize.set(newWorldWindowSize);
			
			// make it so the camera zooms in to the middle
			Vector2 newLocation = new Vector2(this.location);
			newLocation.x += worldWindowSize.x - prevWorldWindowSize.x;
			newLocation.y += worldWindowSize.y - prevWorldWindowSize.y; 
			this.setLocation(newLocation);
		}
	}
	
	@Override
	public void setLocation(Vector2 newLoc){
		super.setLocation(newLoc);
		
		if(boundsCheck){
			Room r = Game.physics.currentRoom();
			if(r != null)
				boundsCheck(r);	
		}
	}
	
	/**
	 * @param zoom Zoom level to get window size for
	 * @return Size of window in world coordinates for given zoom level
	 */
	private Vector2 getWorldWindowSize(float zoom){
		projection.idt();
		MathHelper.orthoM(projection, 0, Game.aspect, 0, 1, -1, 1);
		
		view.idt();
		view.scale(this.zoom, this.zoom, 1.0f);
		
		// don't ask, that's just how it works, okay?
		Vector2 tempVec = new Vector2(), newWorldWindowSize = new Vector2();
		MathHelper.toWorldSpace(newWorldWindowSize, projection, view, (Game.windowWidth / 2.0f), 0.0f);
		MathHelper.toWorldSpace(tempVec, projection, view, 0.0f,  (Game.windowHeight / 2.0f));
		newWorldWindowSize.sub(tempVec.x, tempVec.y);
		
		return newWorldWindowSize;
	}
	
	/** @return The center of where the camera is currently looking, in world coordinates */
	public Vector2 getWorldCenterPoint(){
		return new Vector2(-(this.location.x) + worldWindowSize.x, -(this.location.y) + worldWindowSize.y);
	}
	
	/** @param r Room to keep camera inside of */
	private void boundsCheck(Room r){
		Vector2 center = getWorldCenterPoint();
		
		// bottom bounds (-y)
		float cameraBottomY = center.y - worldWindowSize.y;
		float roomBottomY = r.getRoomY() - r.getRoomHeight();
		if(cameraBottomY < roomBottomY)
			this.location.y += (cameraBottomY - roomBottomY);
		
		// top bounds (+y)
		float cameraTopY = center.y + worldWindowSize.y;
		float roomTopY = r.getRoomY() + r.getRoomHeight();
		if(cameraTopY > roomTopY)
			this.location.y += (cameraTopY - roomTopY);
		
		// left bounds (-x)
		float cameraLeftX = center.x - worldWindowSize.x;
		float roomLeftX = r.getRoomX() - r.getRoomWidth();
		if(cameraLeftX < roomLeftX)
			this.location.x += (cameraLeftX - roomLeftX);
		
		// right bounds (+x)
		float cameraRightX = center.x + worldWindowSize.x;
		float roomRightX = r.getRoomX() + r.getRoomWidth();
		if(cameraRightX > roomRightX)
			this.location.x += (cameraRightX - roomRightX);
	}
	
	/** @return How much the camera can see, in world coordinates */
	public Vector2 getWorldWindowSize(){
		return worldWindowSize;
	}
	
	@Override
	public void cleanup() {}
}