package com.bitwaffle.moguts.serialization.serializers;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Serializes a FixtureDef
 * 
 * @author TranquilMarmot
 */
public class FixtureDefSerializer extends Serializer<FixtureDef>{
	@SuppressWarnings("unchecked")
	@Override
	public FixtureDef read(Kryo kryo, Input input, Class<FixtureDef> type) {
		FixtureDef def = new FixtureDef();
		
		def.density = input.readFloat();
		def.friction = input.readFloat();
		
		def.isSensor = input.readBoolean();
		
		def.restitution = input.readFloat();
		
		Registration reg = kryo.readClass(input);
		def.shape = kryo.readObject(input, reg.getType());
		
		return def;
	}

	@Override
	public void write(Kryo kryo, Output output, FixtureDef fixt) {
		output.writeFloat(fixt.density);
		output.writeFloat(fixt.friction);
		
		output.writeBoolean(fixt.isSensor);
		
		output.writeFloat(fixt.restitution);
		
		kryo.writeClass(output, fixt.shape.getClass());
		kryo.writeObject(output, fixt.shape);
	}

}
