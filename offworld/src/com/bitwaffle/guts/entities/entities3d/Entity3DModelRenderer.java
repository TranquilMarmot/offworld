package com.bitwaffle.guts.entities.entities3d;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities2d.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.graphics.shapes.model.Model;

public class Entity3DModelRenderer implements EntityRenderer {
	
	private Model model;
	
	private Vector3 location;
	
	private Quaternion quat;
	
	public Entity3DModelRenderer(Model model){
		this.model = model;
	}
	
	@Override
	public void render(Renderer renderer, Entity2D ent, boolean renderDebug){
		renderer.render3D.switchTo3DRender();
		model.render(renderer.render3D);
	}
}
