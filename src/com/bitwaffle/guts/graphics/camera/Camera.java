package com.bitwaffle.guts.graphics.camera;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.camera.modes.CameraMode;
import com.bitwaffle.guts.graphics.camera.modes.FollowMode;
import com.bitwaffle.guts.graphics.camera.modes.FreeMode;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Describes how a scene should be rendered and handles following a given entity
 * 
 * @author TranquilMarmot
 */
public class Camera extends Entity {
	/** Initial values for camera */
	private static final float DEFAULT_CAMX = 0.0f, DEFAULT_CAMY = 0.0f, DEFAULT_CAMZ = 0.04f;
	
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	private float zoom;
	
	/** Minimum and maximum zoom values */
	private float minZoom = 0.008F, maxZoom = 0.08F;
	
	/** Current camera mode */
	private Modes currentMode = Modes.FOLLOW;
	
	/** Used to determine how much of the screen the camera can see */
	private Matrix4f projection, view;
	
	/** How much of the world the camera can see */
	private Vector2 worldWindowSize;
	
	/** Vectors that get reused for maths */
	private Vector2 prevWorldWindowSize, tempVec;
	
	/**
	 * Different camera modes
	 */
	public static enum Modes{
		FOLLOW(new FollowMode(100.0f, 0.0f, -0.3f, 1.0f, 1.5f)),
		FREE(new FreeMode());
		
		private CameraMode mode;
		private Modes(CameraMode mode){
			this.mode = mode;
		}
		
		public void update(float timeStep){
			mode.update(timeStep);
		}
		
		public void setCamera(Camera camera){
			mode.setCamera(camera);
		}
		
		public CameraMode getMode(){
			return mode;
		}
	}
	
	/**
	 * Create a new camera
	 * @param location Initial location to put camera at
	 * @param zoom Initial zoom to put camera at
	 */
	public Camera(){
		super();
		projection = new Matrix4f();
		view = new Matrix4f();
		worldWindowSize = new Vector2();
		prevWorldWindowSize = new Vector2();
		tempVec = new Vector2();
		this.setLocation(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY));
		this.setZoom(DEFAULT_CAMZ);
		
		for(Modes mode : Modes.values())
			mode.setCamera(this);
	}
	
	@Override
	public void update(float timeStep) {
		currentMode.update(timeStep);
	}
	
	/**
	 * @return Current zoom level of this camera
	 */
	public float getZoom(){
		return zoom;
	}
	
	/**
	 * Set the zoom level
	 * @param zoom New zoom level
	 */
	public void setZoom(float zoom){
		if(zoom > maxZoom)
			this.zoom = maxZoom;
		else if(zoom < minZoom)
			this.zoom = minZoom;
		else
			this.zoom = zoom;
		prevWorldWindowSize.set(worldWindowSize);
		
		// update window size, since we zoomed
		updateWorldWindowSize();
		
		// make it so the camera zooms in to the middle
		this.location.x += worldWindowSize.x - prevWorldWindowSize.x;
		this.location.y += worldWindowSize.y - prevWorldWindowSize.y; 
	}
	
	/**
	 * Updates worldWindowSize to represent the latest state of everything
	 */
	public void updateWorldWindowSize(){
		projection.setIdentity();
		MathHelper.orthoM(projection, 0, Game.aspect, 0, 1, -1, 1);
		
		view.setIdentity();
		Matrix4f.scale(new Vector3f(this.zoom, this.zoom, 1.0f), view, view);
		
		// don't ask, that's just how it works, okay?
		MathHelper.toWorldSpace(worldWindowSize, projection, view, (Game.windowWidth / 2.0f), 0.0f);
		MathHelper.toWorldSpace(tempVec, projection, view, 0.0f,  (Game.windowHeight / 2.0f));
		worldWindowSize.sub(tempVec);
	}
	
	
	/**
	 * Set the mode of the camera
	 * @param newMode Mode from Camera.Modes
	 */
	public void setMode(Modes newMode){
		this.currentMode = newMode;
	}
	/**
	 * @return Current mode of camera, from Camera.Modes
	 */
	public Modes currentMode(){
		return currentMode;
	}
	
	/**
	 * Set the camera to follow a new entity
	 * @param entity New target to follow
	 */
	public void setTarget(DynamicEntity entity){
		((FollowMode)Modes.FOLLOW.getMode()).setTarget(entity);
	}
	
	/**
	 * @return What this camera is currently following
	 */
	public DynamicEntity getTarget() {
		return ((FollowMode)Modes.FOLLOW.getMode()).getTarget();
	}
	
	public Vector2 getWorldWindowSize(){
		return worldWindowSize;
	}
	
	@Override
	public void cleanup() {}
}