package com.bitwaffle.guts.graphics.shapes.model;

import com.bitwaffle.guts.entities.entities2d.Entity;
import com.bitwaffle.guts.entities.entities2d.EntityRenderer3D;
import com.bitwaffle.guts.graphics.render.Renderer;

public class ModelRenderer extends EntityRenderer3D {
	
	private Model model;
	
	public ModelRenderer(Model model){
		this.model = model;
	}
	
	@Override
	public void render(Renderer renderer, Entity ent, boolean renderDebug){
		model.render(renderer.render3D);
	}
}
