package com.bitwaffle.moguts.serialization;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.entities.Entities;
import com.bitwaffle.moguts.physics.Physics;
import com.esotericsoftware.kryo.Kryo;

public class SaveGameSerializer {
	Kryo kryo;
	
	public SaveGameSerializer(){
		kryo = new Kryo();
		kryo.register(Vector2.class, new Vector2Serializer());
		kryo.register(Shape.class, new ShapeSerializer());
		kryo.register(DynamicEntity.class, new DynamicEntitySerializer());
	}
	
	public void writeEntitiesToFile(String file, Entities entities){
		// TODO this!
	}
	
	public Entities readEntitiesFromFile(String file, Physics physics){
		// TODO figure this out
		Entities ents = new Entities();
		
		return ents;
	}
}
