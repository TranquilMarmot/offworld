package com.bitwaffle.moguts.serialization.shapes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Serializes shapes from Box2D
 * 
 * @author TranquilMarmot
 */
public class ShapeSerializerOld extends Serializer<Shape>{

	@Override
	public Shape read(Kryo kryo, Input input, Class<Shape> type) {
		System.out.println("read shape");
		// figure out the type of shape we're looking at
		Shape.Type shapeType = Shape.Type.values()[input.readInt()];
		
		// get the radius, as every shape has a radius
		float radius = input.readFloat();
		
		// polygon
		if(shapeType == Shape.Type.Polygon){
			PolygonShape poly = new PolygonShape();
			poly.setRadius(radius);
			
			// read in vertex count
			int vertCount = input.readInt();
			
			// read in every vertex
			Vector2[] vecs = new Vector2[vertCount];
			for(int i = 0; i < vertCount - 1; i++){
				vecs[i] = kryo.readObject(input, Vector2.class);
				System.out.println("read vertex " + vecs[i]);
			}
			poly.set(vecs);
			
			return poly;
		
		// edge
		} else if(shapeType == Shape.Type.Edge){
			EdgeShape edge = new EdgeShape();
			edge.setRadius(radius);
			
			// read in both vertices
			Vector2 vert1 = kryo.readObject(input, Vector2.class);
			Vector2 vert2 = kryo.readObject(input, Vector2.class);
			edge.set(vert1, vert2);
			
			return edge;
			
		// circle
		} else if(shapeType == Shape.Type.Circle){
			CircleShape circ = new CircleShape();
			circ.setRadius(radius);
			
			// read in the circle's position
			Vector2 pos = kryo.readObject(input, Vector2.class);
			circ.setPosition(pos);
			
			return circ;
			
		// chain
		} else if(shapeType == Shape.Type.Chain){
			ChainShape chain = new ChainShape();
			chain.setRadius(radius);
			
			// read in the vertex count
			int vertCount = input.readInt();
			
			// read in each vertex
			Vector2[] verts = new Vector2[vertCount];
			for(int i = 0; i < vertCount - 1; i++)
				verts[i] = kryo.readObject(input, Vector2.class);
			chain.createChain(verts);
			// TODO chain also has a createLoop method, look into that (wouldn't it be the same as a polygon?)
			
			return chain;
		}
		
		return null;
	}

	@Override
	public void write(Kryo kryo, Output output, Shape shape) {
		// write out shape type ordinal (used to get shape type from Shape.Type.values())
		Shape.Type shapeType = shape.getType();
		output.writeInt(shapeType.ordinal());
		
		// write the shape's radius, as every shape has a radius
		output.writeFloat(shape.getRadius());
		
		// polygon
		if(shapeType == Shape.Type.Polygon){
			PolygonShape poly = (PolygonShape) shape;
			
			// write out vertex count
			int vertCount = poly.getVertexCount();
			output.writeInt(vertCount);
			
			// write out each vertex
			Vector2 vert = new Vector2();
			for(int i = 0; i < vertCount; i++){
				poly.getVertex(i, vert);
				kryo.writeObject(output, new Vector2(vert.x, vert.y));
			}
		
		// edge
		} else if(shapeType == Shape.Type.Edge){
			EdgeShape edge = (EdgeShape) shape;
			
			Vector2 vert = new Vector2();
			
			// write first vertex
			edge.getVertex1(vert);
			kryo.writeObject(output, new Vector2(vert.x, vert.y));
			
			// write second vertex
			edge.getVertex2(vert);
			kryo.writeObject(output, new Vector2(vert.x, vert.y));
			
		// circle
		} else if(shapeType == Shape.Type.Circle){
			CircleShape circ = (CircleShape) shape;
			
			// write position
			kryo.writeObject(output, circ.getPosition());
			
		// chain
		} else if(shapeType == Shape.Type.Chain){
			ChainShape chain = (ChainShape) shape;
			
			// write out vertex count
			int vertCount = chain.getVertexCount();
			output.writeInt(vertCount);
			
			// write out every vertex
			Vector2 vert = new Vector2();
			for(int i = 0; i < vertCount; i++){
				chain.getVertex(i, vert);
				kryo.writeObject(output, new Vector2(vert.x, vert.y));
			}
		}
	}

}
