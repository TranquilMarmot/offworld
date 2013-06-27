package com.bitwaffle.offworld.camera;

import com.bitwaffle.guts.Game;

/**
 * Different modes for the 2D camera to be in
 * 
 * @author TranquilMarmot
 */
public class CameraModes {
	/** Mode that follows around an entity */
	public static FollowMode follow = new FollowMode(Game.renderer.camera);
	/** Mode that can be dragged around */
	public static FreeMode free = new FreeMode(Game.renderer.camera);
}
