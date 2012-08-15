package com.bitwaffle.moguts.serialization.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class ChainShapeSerializer extends Serializer<ChainShape>{

	@Override
	public ChainShape read(Kryo kryo, Input input, Class<ChainShape> type) {
		ChainShape chain = new ChainShape();
		
		float radius = input.readFloat();
		chain.setRadius(radius);
		
		int numVerts = input.readInt();
		
		Vector2[] verts = new Vector2[numVerts];
		for(int i = 0; i < numVerts; i++){
			verts[i] = kryo.readObject(input, Vector2.class);
		}
		chain.createChain(verts);
		
		return chain;
	}

	@Override
	public void write(Kryo kryo, Output output, ChainShape chain) {
		output.writeFloat(chain.getRadius());
		
		int numVerts = chain.getVertexCount();
		output.writeInt(numVerts);
		
		Vector2 vert = new Vector2();
		for(int i = 0; i < numVerts; i++){
			chain.getVertex(i, vert);
			kryo.writeObject(output, new Vector2(vert.x, vert.y));
		}
	}

}
