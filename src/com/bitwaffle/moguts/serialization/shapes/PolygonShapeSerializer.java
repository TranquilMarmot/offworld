package com.bitwaffle.moguts.serialization.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class PolygonShapeSerializer extends Serializer<PolygonShape>{

	@Override
	public PolygonShape read(Kryo kryo, Input input, Class<PolygonShape> type) {
		PolygonShape poly = new PolygonShape();
		
		float radius = input.readFloat();
		poly.setRadius(radius);
		
		int vertCount = input.readInt();
		
		Vector2[] vecs = new Vector2[vertCount];
		for(int i = 0; i < vertCount; i++){
			vecs[i] = kryo.readObject(input, Vector2.class);
		}
		poly.set(vecs);
		
		return poly;
	}

	@Override
	public void write(Kryo kryo, Output output, PolygonShape poly) {
		output.writeFloat(poly.getRadius());
		
		int vertCount = poly.getVertexCount();
		output.writeInt(vertCount);
		
		Vector2 vert = new Vector2();
		for(int i = 0; i < vertCount; i++){
			poly.getVertex(i, vert);
			kryo.writeObject(output, new Vector2(vert.x, vert.y));
		}
	}

}
