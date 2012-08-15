package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.graphics.render.renderers.Renderers;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * A DynamicEntity... that's a box!
 * 
 * @author TranquilMarmot
 */
public class BoxEntity extends DynamicEntity implements KryoSerializable{
	/** Color to draw entity in (4 floats, between 0.0 and 1.0) */
	public float[] color;
	
	/** Width and height of box (from center) */
	protected float width, height;
	
	public BoxEntity(){
		super();
		this.width = 0.0f;
		this.height = 0.0f;
		color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
	}
	
	/**
	 * Create a new, box-shaped DynamicEntity
	 * @param bodyDef Describes the body of the box
	 * @param width Width (for rendering)
	 * @param height Height (for rendering)
	 * @param fixtureDef Position/material information about the box
	 * @param color What color the box is
	 */
	public BoxEntity(Renderers renderer, BodyDef bodyDef, float width, float height, FixtureDef fixtureDef, float[] color){
		super(renderer, bodyDef, fixtureDef);
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	/**
	 * Create a new, box-shaped DynamicEntity
	 * @param bodyDef Describes the body of the box
	 * @param width Width (for rendering)
	 * @param height Height (for rendering)
	 * @param density How dense the box is
	 * @param color What color the box is
	 */
	public BoxEntity(Renderers renderer, BodyDef bodyDef, float width, float height, float density, float[] color){
		super(renderer, bodyDef, getBoxShape(width, height), density);
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	private static PolygonShape getBoxShape(float width, float height){
		PolygonShape box = new PolygonShape();
		box.setAsBox(width, height);
		return box;
	}
	
	public float getWidth(){ return width; }
	public float getHeight(){ return height; }
	
	@Override
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
		
		// write out width/height
		output.writeFloat(width);
		output.writeFloat(height);
		
		// write out color
		output.writeFloat(color[0]);
		output.writeFloat(color[1]);
		output.writeFloat(color[2]);
	}
	
	@Override
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		
		// read in width/height
		this.width = input.readFloat();
		this.height = input.readFloat();
		
		// read in color
		color[0] = input.readFloat();
		color[1] = input.readFloat();
		color[2] = input.readFloat();
	}
}