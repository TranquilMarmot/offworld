package com.bitwaffle.guts.graphics.graphics3d;

import com.badlogic.gdx.math.Matrix4;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;

/**
 * Renders an entity in 3D mode. When the render() method is called, assume that the renderer's
 * modelview matrix has been translated to the entity's location (x,y, and z) and rotated to
 * this renderer's rotation.
 * 
 * @author TranquilMarmot
 */
public abstract class ObjectRenderer3D implements ObjectRenderer2D {
	/** Z location of entity */
	public float z;
	
	/** Matrix that gets multiplied with renderer's modelview when rendering */
	public Matrix4 view;
	
	public ObjectRenderer3D(){
		view = new Matrix4();
		z = 0.0f;
	}
	
	@Override
	public abstract void render(Renderer render, Object ent);
}
