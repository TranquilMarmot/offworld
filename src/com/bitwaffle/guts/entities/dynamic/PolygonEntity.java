package com.bitwaffle.guts.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.graphics.render.shapes.Polygon;
import com.bitwaffle.offworld.entities.CollisionFilters;
import com.bitwaffle.offworld.renderers.PolygonEntityRenderer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class PolygonEntity extends DynamicEntity {
	private Polygon polygon;
	
	private boolean flipHorizontal, flipVertical;
	
	public PolygonEntity(Polygon polygon, int layer, BodyDef bodyDef, float density, float friction, float restitution, boolean isSensor){
		super(new PolygonEntityRenderer(), layer, bodyDef, getFixtureDef(polygon.getShape(), density, friction, restitution, isSensor));
		this.polygon = polygon;
		
		flipHorizontal = false;
		flipVertical = false;
	}
	
	private static FixtureDef getFixtureDef(Shape shape, float density, float friction, float restitution, boolean isSensor){
		FixtureDef def = new FixtureDef();
		def.shape = shape;
		def.density = density;
		def.friction = friction;
		def.restitution = restitution;
		def.isSensor = isSensor;	
		def.filter.categoryBits = CollisionFilters.GROUND;
		def.filter.maskBits = CollisionFilters.EVERYTHING;
		return def;
	}
	
	public void renderPolygon(Render2D renderer){
		polygon.render(renderer, flipHorizontal, flipVertical);
	}
	
	public void read(Kryo kryo, Input input) {
		super.read(kryo, input);
	}
	
	public void write(Kryo kryo, Output output) {
		super.write(kryo, output);
	}
}
