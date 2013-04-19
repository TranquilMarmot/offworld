package com.bitwaffle.guts.entities.entities3d;

import com.bitwaffle.guts.graphics.render.render3d.Render3D;
import com.bitwaffle.guts.graphics.shapes.model.Model;

public class Entity3DModelRenderer implements Entity3DRenderer {
	
	private Model model;
	
	public Entity3DModelRenderer(Model model){
		this.model = model;
	}
	
	
	public void render(Render3D renderer, Entity3D ent){
		model.render(renderer);
	}
}
