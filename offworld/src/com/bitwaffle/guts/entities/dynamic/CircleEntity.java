package com.bitwaffle.guts.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.bitwaffle.guts.graphics.EntityRenderer;

/**
 * A circle-shaped {@link DynamicEntity}
 * 
 * @author TranquilMarmot
 */
public class CircleEntity extends DynamicEntity {
	/** Color to draw entity in (4 floats, between 0.0 and 1.0) */
	public float[] color;
	
	/** Radius of the entity's circle */
	private float radius;
	
	/**
	 * No-args constructor for kryo only!
	 */
	public CircleEntity(){
		super();
		color = new float[4];
	}
	
	/**
	 * Create a new circle-shaped entity
	 * @param renderer Renderer to use to render circle
	 * @param radius Radius of circle
	 * @param bodyDef Body definition
	 * @param density Density of entity
	 * @param color Color to use when rendering entity
	 */
	public CircleEntity(EntityRenderer renderer, int layer, float radius, BodyDef bodyDef, float density, float[] color){
		super(renderer, layer, bodyDef, getCircleShape(radius), density);
		this.radius = radius;
		this.color = color;
	}
	
	/**
	 * @param radius Radius to use for shape
	 * @return Circle shape with given radius
	 */
	private static CircleShape getCircleShape(float radius){
		CircleShape circ = new CircleShape();
		circ.setRadius(radius);
		return circ;
	}
	
	/**
	 * @return Radius of circle
	 */
	public float getRadius(){
		return this.radius;
	}
	
	/*
	@Override
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
		
		output.writeFloat(radius);
		
		output.writeFloat(color[0]);
		output.writeFloat(color[1]);
		output.writeFloat(color[2]);
		output.writeFloat(color[3]);
	}
	
	@Override
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		
		this.radius = input.readFloat();
		
		color[0] = input.readFloat();
		color[1] = input.readFloat();
		color[2] = input.readFloat();
		color[3] = input.readFloat();
	}
	*/
}
