package com.bitwaffle.moguts.serialization.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class CircleShapeSerializer extends Serializer<CircleShape>{

	@Override
	public CircleShape read(Kryo kryo, Input input, Class<CircleShape> type) {
		CircleShape circ = new CircleShape();
		
		float radius = input.readFloat();
		circ.setRadius(radius);
		
		Vector2 position = kryo.readObject(input, Vector2.class);
		circ.setPosition(position);
		
		return circ;
	}

	@Override
	public void write(Kryo kryo, Output output, CircleShape circ) {
		output.writeFloat(circ.getRadius());
		
		kryo.writeObject(output, circ.getPosition());
	}

}
