package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.graphics.render.GLRenderer;
import com.bitwaffle.moguts.graphics.shapes.Quad;

public class BoxEntity extends DynamicEntity {
	private Quad quad;
	private float[] color;
	
	public BoxEntity(BodyDef bodyDef, float width, float height, FixtureDef fixtureDef, float[] color){
		super(bodyDef, fixtureDef);
		quad = new Quad(width, height);
		this.color = color;
	}
	
	public BoxEntity(BodyDef bodyDef, float width, float height, PolygonShape shape, float density, float[] color){
		super(bodyDef, shape, density);
		quad = new Quad(width, height);
		this.color = color;
	}
	
	public void render(){
		GLRenderer.render2D.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
		quad.draw();
	}
}
