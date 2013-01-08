package com.bitwaffle.guts.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.offworld.renderers.PolygonEntityRenderer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class PolygonEntity extends DynamicEntity {
	/** Name of polygon that's stored in Game.resources.polygons */
	private String polygonName;
	
	public PolygonEntity(String polygonName, int layer, BodyDef bodyDef, boolean isChain, float density, float friction, float restitution, boolean isSensor, short categoryBits, short maskBits){
		super(new PolygonEntityRenderer(), layer, bodyDef, getFixtureDef(polygonName, isChain, density, friction, restitution, isSensor, categoryBits, maskBits));
		this.polygonName = polygonName;
	}
	
	private static FixtureDef getFixtureDef(String polygonName, boolean isChain, float density, float friction, float restitution, boolean isSensor, short categoryBits, short maskBits){
		FixtureDef def = new FixtureDef();
		if(isChain)
			def.shape = Game.resources.polygons.getShapeAsChain(polygonName);
		else
			def.shape = Game.resources.polygons.getShapeAsPolygon(polygonName);
		def.density = density;
		def.friction = friction;
		def.restitution = restitution;
		def.isSensor = isSensor;	
		def.filter.categoryBits = categoryBits;
		def.filter.maskBits = maskBits;
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
