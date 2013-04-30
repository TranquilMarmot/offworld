package com.bitwaffle.guts.graphics.render.render3d;

import com.badlogic.gdx.math.Matrix4;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.render.Renderer;
import com.bitwaffle.guts.graphics.render.render2d.EntityRenderer;

/**
 * Renders an entity in 3D mode. When the render() method is called, assume that the renderer's
 * modelview matrix has been translated to the entity's location (x,y, and z) and rotated to
 * this renderer's rotation.
 * 
 * @author TranquilMarmot
 */
public abstract class EntityRenderer3D implements EntityRenderer {
	/** Z location of entity */
	public float z;
	
	/** Rotation of entity */
	public Matrix4 rotation;
	
	public EntityRenderer3D(){
		rotation = new Matrix4();
		z = 0.0f;
	}
	
	@Override
	public abstract void render(Renderer render, Entity ent, boolean renderDebug);
}
