package com.bitwaffle.guts.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.offworld.entities.CollisionFilters;
import com.bitwaffle.offworld.renderers.PolygonEntityRenderer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class PolygonEntity extends DynamicEntity {
	/** Name of polygon that's stored in Game.resources.polygons */
	private String polygonName;
	
	public PolygonEntity(String polygonName, int layer, BodyDef bodyDef, float density, float friction, float restitution, boolean isSensor){
		super(new PolygonEntityRenderer(), layer, bodyDef, getFixtureDef(polygonName, density, friction, restitution, isSensor));
		this.polygonName = polygonName;
	}
	
	private static FixtureDef getFixtureDef(String polygonName, float density, float friction, float restitution, boolean isSensor){
		FixtureDef def = new FixtureDef();
		def.shape = Game.resources.polygons.getPhysicsShape(polygonName);
		def.density = density;
		def.friction = friction;
		def.restitution = restitution;
		def.isSensor = isSensor;	
		def.filter.categoryBits = CollisionFilters.GROUND;
		def.filter.maskBits = CollisionFilters.EVERYTHING;
		return def;
	}
	
	/**
	 * @return Name of polygon this entity is using, in Game.resources.polygons
	 */
	public String getPolygonName(){
		return polygonName;
	}
	
	public void read(Kryo kryo, Input input) {
		super.read(kryo, input);
	}
	
	public void write(Kryo kryo, Output output) {
		super.write(kryo, output);
	}
}
