package com.bitwaffle.guts.graphics.camera;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Describes how a scene should be rendered and handles following a given entity
 * 
 * @author TranquilMarmot
 */
public class Camera extends Entity {	
	/** Initial values for camera */
	private static final float DEFAULT_CAMX = 86.1816f * 3.0f, DEFAULT_CAMY = 24.6180f * 3.0f;
	
	/** Current camera mode */
	private CameraMode currentMode = null;
	
	/** Current zoom level of camera (smaller it is, the smaller everything will be rendered) */
	public float zoom = 0.008f;
	
	/** Used to determine how much of the screen the camera can see */
	private Matrix4 projection, view;
	
	/** How much of the world the camera can see. X value is width in world-coordinates, Y value is height */
	protected Vector2 worldWindowSize;
	
	/** Describes how the 3D scene gets rendered */
	public Camera3D camera3D;
	
	public Camera(){
		this(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY));
	}
	
	public Camera(Vector2 location){
		super();
		camera3D = new Camera3D();
		projection = new Matrix4();
		view = new Matrix4();
		worldWindowSize = new Vector2();
		this.location = new Vector2(location);
		this.setLocation(location);
		currentMode = new CameraMode(this){
			@Override
			public void update(float timeStep) {
			}
		};
	}
	
	@Override
	public void update(float timeStep) {
		currentMode.update(timeStep);
		
		if(this.zoom != currentMode.zoom)
			setZoom(currentMode.zoom);
		
		if(currentMode.interpolate)
			this.location.lerp(currentMode.target, timeStep * currentMode.interpolationSpeed);
		else
			this.location.set(currentMode.target);
		
		if(currentMode.boundsCheck)
			boundsCheck();	

		camera3D.setLocation(this.location);
		camera3D.setScale(this.zoom);
	}
	
	/** Set the mode of the camera */
	public void setMode(CameraMode newMode){ this.currentMode = newMode; }
	
	/** @return Current mode of camera, from Camera.Modes */
	public CameraMode currentMode(){ return currentMode; }
	
	/** @param New zoom for camera */
	public void setZoom(float newZoom){
		float oldZoom = this.zoom;
		
		// make sure zoom stays within bounds
		if(newZoom > currentMode.maxZoom)
			newZoom = currentMode.maxZoom;
		else if(newZoom < currentMode.minZoom)
			newZoom = currentMode.minZoom;
		
		if(newZoom != oldZoom){
			this.zoom = newZoom;
			
			Vector2 prevWorldWindowSize = new Vector2(this.worldWindowSize);
			Vector2 newWorldWindowSize = this.getWorldWindowSize();
			
			// make sure the camera doesn't zoom outside of the room if bounds are being checked
			if(currentMode.boundsCheck){
				if(newWorldWindowSize.x > currentMode.boundsSize.x || newWorldWindowSize.y > currentMode.boundsSize.y){
					this.zoom = oldZoom;
					return;
				}
			}
			
			
			// update window size, since we zoomed
			this.worldWindowSize.set(newWorldWindowSize);
			
			// make it so the camera zooms in to the middle
			Vector2 newLocation = new Vector2(this.getLocation());
			newLocation.x += this.worldWindowSize.x - prevWorldWindowSize.x;
			newLocation.y += this.worldWindowSize.y - prevWorldWindowSize.y; 
			this.setLocation(newLocation);
		}
	}
	
	/** @return Size of window in world coordinates */
	public Vector2 getWorldWindowSize(){
		projection.idt();
		MathHelper.orthoM(projection, 0, Game.renderAspect, 0, 1, -1, 1);
		
		view.idt();
		view.scale(zoom, zoom, 1.0f);
		
		// don't ask, that's just how it works, okay?
		Vector2 tempVec = new Vector2(), newWorldWindowSize = new Vector2();
		MathHelper.toWorldSpace(newWorldWindowSize, projection, view, (Game.renderWidth / 2.0f), 0.0f);
		MathHelper.toWorldSpace(tempVec, projection, view, 0.0f,  (Game.renderHeight / 2.0f));
		newWorldWindowSize.sub(tempVec.x, tempVec.y);
		
		return newWorldWindowSize;
	}
	
	/** @return The center of where the camera is currently looking, in world coordinates */
	public Vector2 getWorldCenterPoint(){
		return new Vector2(-(this.location.x) + worldWindowSize.x, -(this.location.y) + worldWindowSize.y);
	}
	
	/** @param r Room to keep camera inside of */
	private void boundsCheck(){
		Vector2 center = getWorldCenterPoint();
		
		// bottom bounds (-y)
		float cameraBottomY = center.y - worldWindowSize.y;
		float roomBottomY = currentMode.boundsCenter.y - currentMode.boundsSize.y;
		if(cameraBottomY < roomBottomY)
			this.location.y += (cameraBottomY - roomBottomY);
		
		// top bounds (+y)
		float cameraTopY = center.y + worldWindowSize.y;
		float roomTopY = currentMode.boundsCenter.y + currentMode.boundsSize.y;
		if(cameraTopY > roomTopY)
			this.location.y += (cameraTopY - roomTopY);
		
		// left bounds (-x)
		float cameraLeftX = center.x - worldWindowSize.x;
		float roomLeftX = currentMode.boundsCenter.x - currentMode.boundsSize.x;
		if(cameraLeftX < roomLeftX)
			this.location.x += (cameraLeftX - roomLeftX);
		
		// right bounds (+x)
		float cameraRightX = center.x + worldWindowSize.x;
		float roomRightX = currentMode.boundsCenter.x + currentMode.boundsSize.x;
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
		
		// FIXME why are these numbers off by so much? Maybe the offset?
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
			Game.out.println("WARNING chain shape isVisible not implemented in Camera");
			break;
		case Circle:
			Game.out.println("WARNING circle shape isVisible not implemented in Camera");
			break;
		case Edge:
			Game.out.println("WARNING edge shape isVisible not implemented in Camera");
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