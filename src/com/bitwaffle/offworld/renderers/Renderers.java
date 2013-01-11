package com.bitwaffle.offworld.renderers;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;

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
	/*
	 * FIXME right now, on serialization, the oridnal for each entity's renderer gets written
	 * This is fine, except if the order of renderers gets changed then it will break old saves
	 * (adding more renderers works fine)
	 * There's got to be a better way to reference renderers! Then again, maybe there's not :P
	 * Might be a good idea to have something similar to SerializationInfo where it fills a map
	 * and each renderer has an int that references it in the map- so that the order can be changed but their
	 * ints could stay the same to avoid breaking saves
	 */
	BOX(new BoxRenderer()),
	CIRCLE(new CircleRenderer()),
	BACKDROP(new BackdropRenderer()),
	PLAYER(new PlayerRenderer()),
	SPARK(new SparkRenderer()),
	BULLET(new BulletRenderer());
	
	public EntityRenderer renderer;
	private Renderers(EntityRenderer renderer){
		this.renderer = renderer;
	}
	
	public void render(Render2D render2D, Entity ent, boolean renderDebug){
		renderer.render(render2D, ent, renderDebug);
	}
	
	public static Renderers valueOf(EntityRenderer renderer){
		for(Renderers r : Renderers.values()){
			if(r.renderer == renderer)
				return r;
		}
		return null;
	}
}

class SparkRenderer implements EntityRenderer{
	final float X_RATIO = 0.631f, Y_RATIO = 1.0f;
	final float SCALE = 0.4f;
	public void render(Render2D renderer, Entity ent, boolean drawDebug){
		if(!drawDebug){
			renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
			Game.resources.textures.getSubImage("spark").render(renderer.quad, X_RATIO * SCALE, Y_RATIO * SCALE);
		}
	}
}