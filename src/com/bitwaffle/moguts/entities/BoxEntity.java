package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.shapes.Quad;

/**
 * A DynamicEntity... that's a box!
 * 
 * @author TranquilMarmot
 */
public class BoxEntity extends DynamicEntity {
	/** Used for rendering */
	private Quad quad;
	
	/** Color to draw entity in (4 floats, between 0.0 and 1.0) */
	protected float[] color;
	
	/**
	 * Create a new, box-shaped DynamicEntity
	 * @param bodyDef Describes the body of the box
	 * @param width Width (for rendering)
	 * @param height Height (for rendering)
	 * @param fixtureDef Position/material information about the box
	 * @param color What color the box is
	 */
	public BoxEntity(BodyDef bodyDef, float width, float height, FixtureDef fixtureDef, float[] color){
		super(bodyDef, fixtureDef);
		quad = new Quad(width, height);
		this.color = color;
	}
	
	/**
	 * Create a new, box-shaped DynamicEntity
	 * @param bodyDef Describes the body of the box
	 * @param width Width (for rendering)
	 * @param height Height (for rendering)
	 * @param shape What shape the box is
	 * @param density How dense the box is
	 * @param color What color the box is
	 */
	public BoxEntity(BodyDef bodyDef, float width, float height, PolygonShape shape, float density, float[] color){
		super(bodyDef, shape, density);
		quad = new Quad(width, height);
		this.color = color;
	}
	
	/**
	 * Set the color and render!
	 */
	public void render(){
		Game.render2D.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		quad.draw();
	}
}
