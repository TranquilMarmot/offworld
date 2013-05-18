package com.bitwaffle.offworld.camera;

import com.bitwaffle.guts.graphics.Renderer;

/**
 * Different modes for the 2D camera to be in
 * 
 * @author TranquilMarmot
 */
public class CameraModes {
	/** Mode that follows around an entity */
	public static FollowMode follow = new FollowMode();
	/** Mode that can be dragged around */
	public static FreeMode free = new FreeMode();
	
	/**
	 * Sets the camera of every mode in this class
	 * @param renderer Renderer to get camera from
	 */
	public static void setCamera(Renderer renderer){
		follow.setCamera(renderer.r2D.camera);
		free.setCamera(renderer.r2D.camera);
	}
}
