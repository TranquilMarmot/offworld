package com.bitwaffle.offworld.moguts.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.offworld.moguts.graphics.shapes.Quad;

public class BoxEntity extends DynamicEntity {
	private Quad quad;
	
	public BoxEntity(BodyDef bodyDef, float width, float height, FixtureDef fixtureDef){
		super(bodyDef, fixtureDef);
		quad = new Quad(width, height);
	}
	
	public BoxEntity(BodyDef bodyDef, float width, float height, PolygonShape shape, float density){
		super(bodyDef, shape, density);
		quad = new Quad(width, height);
	}
	
	public void render(){
		quad.draw();
	}
}
