package com.bitwaffle.moguts.serialization;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class FixtureDefSerializer extends Serializer<FixtureDef>{
	private static ShapeSerializer shapeSerializer = new ShapeSerializer();

	@Override
	public FixtureDef read(Kryo kryo, Input input, Class<FixtureDef> type) {
		FixtureDef def = new FixtureDef();
		
		def.density = input.readFloat();
		def.friction = input.readFloat();
		def.isSensor = input.readBoolean();
		def.restitution = input.readFloat();
		def.shape = kryo.readObject(input, Shape.class, shapeSerializer);
		
		return def;
	}

	@Override
	public void write(Kryo kryo, Output output, FixtureDef fixt) {
		output.writeFloat(fixt.density);
		output.writeFloat(fixt.friction);
		output.writeBoolean(fixt.isSensor);
		output.writeFloat(fixt.restitution);
		kryo.writeObject(output, fixt.shape, shapeSerializer);
	}

}
