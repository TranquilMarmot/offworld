package com.bitwaffle.moguts.serialization.serializers.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Serializes an edge
 * 
 * @author TranquilMarmot
 */
public class EdgeShapeSerializer extends Serializer<EdgeShape>{

	@Override
	public EdgeShape read(Kryo kryo, Input input, Class<EdgeShape> type) {
		EdgeShape edge = new EdgeShape();
		
		// read radius
		float radius = input.readFloat();
		edge.setRadius(radius);
		
		// read first vertex
		Vector2 vert1 = kryo.readObject(input, Vector2.class);
		// read second vertex
		Vector2 vert2 = kryo.readObject(input, Vector2.class);
		edge.set(vert1, vert2);
		
		return edge;
	}

	@Override
	public void write(Kryo kryo, Output output, EdgeShape edge) {
		// write radius
		output.writeFloat(edge.getRadius());
		
		Vector2 vec = new Vector2();
		
		// write first vertex
		edge.getVertex1(vec);
		kryo.writeObject(output, new Vector2(vec.x, vec.y));
		
		// write second vertex
		edge.getVertex2(vec);
		kryo.writeObject(output, new Vector2(vec.x, vec.y));
	}

}