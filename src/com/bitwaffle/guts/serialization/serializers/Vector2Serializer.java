package com.bitwaffle.guts.serialization.serializers;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Serializes a Vector2
 * 
 * @author TranquilMarmot
 */
public class Vector2Serializer extends Serializer<Vector2>{

	@Override
	public Vector2 read(Kryo kryo, Input input, Class<Vector2> type) {
		return new Vector2(input.readFloat(), input.readFloat());
	}

	@Override
	public void write(Kryo kryo, Output output, Vector2 vec) {
		// this one's nice and easy :3
		output.writeFloat(vec.x);
		output.writeFloat(vec.y);
	}
}
