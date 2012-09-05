package com.bitwaffle.moguts.graphics.render;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.offworld.renderers.BackdropRenderer;
import com.bitwaffle.offworld.renderers.BoxRenderer;
import com.bitwaffle.offworld.renderers.CircleRenderer;
import com.bitwaffle.offworld.renderers.PlayerRenderer;

/**
 * Every <@link Entity> has one of these Renderers assigned to it, which has its
 * <code>render()</code> method called after the camera modifies the modelview matrix
 * to be at the location of the entity (that is, anything done to the modelview will modify
 * it originating at the center of the entity)
 * 
 * When writing entities, the ordinal for the entity's renderer is written.
 * This means that anything that is reading the written entity will have to have all of
 * these renderers in the same order, and changing the order of these renderers will break
 * any older save files.
 * 
 * @author TranquilMarmot
 */
public enum Renderers{
	BOX(new BoxRenderer()),
	CIRCLE(new CircleRenderer()),
	BACKDROP(new BackdropRenderer()),
	PLAYER(new PlayerRenderer());
	
	private EntityRenderer renderer;
	private Renderers(EntityRenderer renderer){
		this.renderer = renderer;
	}
	
	public void render(Render2D render2D, Entity ent){
		renderer.render(render2D, ent);
	}
	
	public void renderDebug(Render2D render2D, Entity ent){
		renderer.renderDebug(render2D, ent);
	}
}