package com.bitwaffle.offworld.camera;

import com.bitwaffle.guts.graphics.render.Renderer;


public class CameraModes {
	public static FollowMode follow = new FollowMode();
	public static FreeMode free = new FreeMode();
	
	public static void setCamera(Renderer renderer){
		follow.setCamera(renderer.r2D.camera);
		free.setCamera(renderer.r2D.camera);
	}
}
