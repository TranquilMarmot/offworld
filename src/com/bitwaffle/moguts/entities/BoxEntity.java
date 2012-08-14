package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.graphics.render.renderers.Renderers;
import com.bitwaffle.offworld.Game;

/**
 * A DynamicEntity... that's a box!
 * 
 * @author TranquilMarmot
 */
public class BoxEntity extends DynamicEntity {
	/** Color to draw entity in (4 floats, between 0.0 and 1.0) */
	public float[] color;
	
	/** Width and height of box (from center) */
	protected float width, height;
	
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
	public BoxEntity(BodyDef bodyDef, float width, float height, float density, float[] color){
		super(bodyDef, getBoxShape(width, height), density);
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
	
	/**
	 * Set the color and render!
	 */
	public void render(Render2D renderer){
		Game.resources.textures.bindTexture("box");
		renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		renderer.quad.draw(renderer, this.width, this.height);
	}
}
