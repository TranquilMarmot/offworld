package com.bitwaffle.guts.graphics.graphics2d;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Describes how a scene should be rendered and handles following a given entity
 * 
 * @author TranquilMarmot
 */
public class Camera2D extends Entity {
	public static abstract class CameraMode {
		protected Camera2D camera;
		
		public CameraMode(Camera2D camera){ this.camera = camera; }
		
		public abstract void update(float timeStep);
		
		public void setCamera(Camera2D camera){ this.camera = camera; }
	}
	
	/** Initial values for camera */
	private static final float DEFAULT_CAMX = 86.1816f * 3.0f, DEFAULT_CAMY = 24.6180f * 3.0f, DEFAULT_CAMZ = 0.03f;
	
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	private float zoom;
	
	/** Minimum and maximum zoom values */
	private float minZoom = 0.008f, maxZoom = 0.15f;
	
	/** Current camera mode */
	private CameraMode currentMode = null;
	
	/** Used to determine how much of the screen the camera can see */
	private Matrix4 projection, view;
	
	/** How much of the world the camera can see. X value is width in world-coordinates, Y value is height */
	private Vector2 worldWindowSize;
	
	/** Whether or not to keep the camera within Physics.getCurrentRoom */
	private boolean boundsCheck = false;
	
	public Camera2D(){
		super();
		projection = new Matrix4();
		view = new Matrix4();
		worldWindowSize = new Vector2();
		this.setLocation(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY));
		this.location.set(DEFAULT_CAMX, DEFAULT_CAMY);
		this.setZoom(DEFAULT_CAMZ);
		
		
	}
	
	@Override
	public void update(float timeStep) {
		if(currentMode != null)
			currentMode.update(timeStep);
		
		if(boundsCheck){
			Room r = Game.physics.currentRoom();
			if(r != null)
				boundsCheck(r);	
		}
	}
	
	/** Set the mode of the camera */
	public void setMode(CameraMode newMode){ this.currentMode = newMode; }
	
	/** @return Current mode of camera, from Camera.Modes */
	public CameraMode currentMode(){ return currentMode; }
	
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
			Vector2 newWorldWindowSize = getWorldWindowSize();
			
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
	
	/** @return Size of window in world coordinates */
	public Vector2 getWorldWindowSize(){
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
	
	/** @return Whether or not a point is inside the screen */
	public boolean isPointVisible(Vector2 p){
		Vector2 windowSize = this.getWorldWindowSize();
		Vector2 center = this.getWorldCenterPoint();
		
		Vector2 topLeft     = new Vector2(center.x - windowSize.x, center.y + windowSize.y),
				topRight    = new Vector2(center.x + windowSize.x, center.y + windowSize.y),
				bottomRight = new Vector2(center.x + windowSize.x, center.y - windowSize.y),
				bottomLeft  = new Vector2(center.x - windowSize.x, center.y - windowSize.y);
		
		// FIXME why are these numbers off by so much?
		return
				(MathHelper.triangleArea(topRight,    topLeft,     p) >= 0) &&
				(MathHelper.triangleArea(topLeft,     bottomLeft,  p) >= -50) && 
				(MathHelper.triangleArea(bottomLeft,  bottomRight, p) >= 0) &&
				(MathHelper.triangleArea(bottomRight, topRight,    p) >= -5);
	}
	
	/** @return Whether a given fixture is visible by the camera */
	public boolean isFixtureVisible(Vector2 bodyLoc, Fixture fixt){
		Shape s = fixt.getShape();
		Shape.Type st = s.getType();
		switch(st){
		case Chain:
			Game.out.println("WARNING chain shape isVisible not implemented in Camera2D");
			break;
		case Circle:
			Game.out.println("WARNING circle shape isVisible not implemented in Camera2D");
			break;
		case Edge:
			Game.out.println("WARNING edge shape isVisible not implemented in Camera2D");
			break;
		case Polygon:
			PolygonShape poly = (PolygonShape)s;
			Vector2 v = new Vector2();
			// if any point is visible, return true
			for(int i = 0; i < poly.getVertexCount(); i++){
				poly.getVertex(i, v);
				v = v.add(bodyLoc);
				if(isPointVisible(v))
					return true;
			}
			
			return false;
		default:
			break;
		}
		
		return true;
	}
	
	@Override
	public void cleanup() {}
}