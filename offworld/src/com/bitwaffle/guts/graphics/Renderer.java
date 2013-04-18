package com.bitwaffle.guts.graphics;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.EntityLayer;

public class Renderer {
	public Render2D render2D;
	public Render3D render3D;
	
	public Renderer(){
		render2D = new Render2D();
		render3D = new Render3D();
	}
	
	public void renderScene(){
		EntityLayer[] arr = Game.physics.getLayers();
	}
}
