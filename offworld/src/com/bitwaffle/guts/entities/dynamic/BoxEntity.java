package com.bitwaffle.guts.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.guts.physics.CollisionFilters;

/**
 * A DynamicEntity... that's a box!
 * 
 * @author TranquilMarmot
 */
public class BoxEntity extends DynamicEntity {
	/** Width and height of box (from center) */
	protected float width, height;
	
	public BoxEntity(){
		super();
		this.width = 0.0f;
		this.height = 0.0f;
	}
	
	/**
	 * Create a new, box-shaped DynamicEntity
	 * @param bodyDef Describes the body of the box
	 * @param width Width (for rendering)
	 * @param height Height (for rendering)
	 * @param fixtureDef Position/material information about the box
	 * @param color What color the box is
	 */
	public BoxEntity(EntityRenderer renderer, int layer, BodyDef bodyDef, float width, float height, FixtureDef fixtureDef){
		super(renderer, layer, bodyDef, fixtureDef);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Create a new, box-shaped DynamicEntity
	 * @param bodyDef Describes the body of the box
	 * @param width Width (for rendering)
	 * @param height Height (for rendering)
	 * @param density How dense the box is
	 * @param color What color the box is
	 */
	public BoxEntity(EntityRenderer renderer, int layer, BodyDef bodyDef, float width, float height, float density){
		super(renderer, layer, bodyDef, getBoxShape(width, height, density));
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Gets a box shape with a given width and height
	 * @param width Width of box to get
	 * @param height Height of box to get
	 * @param density Desity to give box
	 * @return Box.
	 */
	private static FixtureDef getBoxShape(float width, float height, float density){
		PolygonShape box = new PolygonShape();
		box.setAsBox(width, height);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = box;
		fixture.filter.categoryBits = CollisionFilters.GROUND;
		fixture.filter.maskBits = CollisionFilters.EVERYTHING;
		fixture.density = density;
		return fixture;
	}
	
	/** @return Width of this entity */
	public float getWidth(){ return width; }
	/** @return Height of this entity */
	public float getHeight(){ return height; }
	
	/*
	@Override
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
		
		// write out width/height
		output.writeFloat(width);
		output.writeFloat(height);
	}
	
	@Override
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		
		// read in width/height
		this.width = input.readFloat();
		this.height = input.readFloat();
	}
	*/
}