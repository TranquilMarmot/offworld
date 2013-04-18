package com.bitwaffle.guts.entities.entities3d;

import com.bitwaffle.guts.graphics.model.Model;
import com.bitwaffle.guts.graphics.render.Render3D;

public class Entity3DModelRenderer implements Entity3DRenderer {
	
	private Model model;
	
	public Entity3DModelRenderer(Model model){
		this.model = model;
	}
	
	
	public void render(Render3D renderer, Entity3D ent){
		model.render(renderer);
	}
}
