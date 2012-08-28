package com.bitwaffle.moguts.serialization;

import java.util.LinkedHashMap;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.entities.dynamic.BoxEntity;
import com.bitwaffle.moguts.entities.dynamic.CircleEntity;
import com.bitwaffle.moguts.entities.dynamic.DynamicEntity;
import com.bitwaffle.moguts.serialization.serializers.BodyDefSerializer;
import com.bitwaffle.moguts.serialization.serializers.FixtureDefSerializer;
import com.bitwaffle.moguts.serialization.serializers.Vector2Serializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.ChainShapeSerializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.CircleShapeSerializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.EdgeShapeSerializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.PolygonShapeSerializer;
import com.bitwaffle.offworld.entities.Player;
import com.bitwaffle.offworld.entities.dynamic.DestroyableBox;
import com.bitwaffle.offworld.entities.dynamic.DestroyableCircle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;

/**
 * Whenever a new class of Entity is coded that needs to be serialized,
 * it MUST be added to this map, otherwise it will be written/read
 * as an Entity (which may be fine, in some cases)
 * Generally, put the deepest classes first (classes that implement classes
 * that implement classes etc) so that they will be caught and written/read
 * correctly.
 * IF you put a superclass before a subclass, the game WILL write an entity
 * as the superclass!!! This can cause all sorts of problems and result in lost data.
 * When reading a file, the de-serializer simply goes through each class in this map
 * IN THE ORDER IT WAS PUT IN and stops when it hits the first one that the object it is reading
 * is an instance of, then casts the object being read to the class and attempts to read in the rest of it. 
 * 
 * When kryo writes an object, it first writes an integer representing that object's
 * class. When registering classes, you can give kryo an integer and it will associate that
 * integer with the given class (if no integer is given, it generates the next lowest unregistered integer it can). 
 * In order to load a serialized file, the classes *MUST* be registered with the same IDs that
 * they were originally serialized with.
 * 
 * TL;DR
 * So basically, the way this works is that each class must be given a unique ID that never changes.
 * If the ID given to a class changes, it will make it so that any instance of that class ever written
 * to a save file with the previous ID will not work. Objects are cast to the first class they are an instance of
 * in the list, so if a new class extends an older class, the newer one should be put above the older one, but
 * the ID of the older one should stay the same to keep compatibility with older save versions (that don't have
 * the newer class).
 * 
 * @author TranquilMarmot
 */
public class SerializationInfo {
	/** Map instance */
	LinkedHashMap<Class<?>, Info> map;
	
	/** Constructor initializes map */
	public SerializationInfo(){ 
		map = new LinkedHashMap<Class<?>, Info>();
		
		/* -- Player --*/
		map.put(Player.class, new Info(49));
		
		/*-- Boxes --*/
		map.put(DestroyableBox.class, new Info(50));
		map.put(BoxEntity.class, new Info(51));
		
		/*-- Circles --*/
		map.put(DestroyableCircle.class, new Info(52));
		map.put(CircleEntity.class, new Info(53));
		
		/*-- Generic Entities --*/
		map.put(DynamicEntity.class, new Info(54));
		map.put(Entity.class, new Info(55));
		
		/*----   PUT NEW ENTITIES HERE  ----*/
		/*   Start with next lowest integer */
		
		/*-- Box2D Classes --*/
		map.put(Vector2.class, new Info(42, new Vector2Serializer()));
		map.put(PolygonShape.class, new Info(43, new PolygonShapeSerializer()));
		map.put(CircleShape.class, new Info(44, new CircleShapeSerializer()));
		map.put(EdgeShape.class, new Info(45, new EdgeShapeSerializer()));
		map.put(ChainShape.class, new Info(46, new ChainShapeSerializer()));
		map.put(FixtureDef.class, new Info(47, new FixtureDefSerializer()));
		map.put(BodyDef.class, new Info(48, new BodyDefSerializer()));
	}
	
	/**
	 * Registers every class in the HashMap to a kryo instance
	 * @param kryo Instance of kryo to register classes with
	 */
	public void registerClasses(Kryo kryo){
		for(Class<?> c : map.keySet()){
			Info info = map.get(c);
			// if there's a serializer, register it with the class
			if(info.serializer != null)
				kryo.register(c, info.serializer, info.id);
			// else just register the class
			else
				kryo.register(c, info.id);
		}
	}
	
	/**
	 * This is used when determining how to write a class.
	 * Since a LinkedHashMap is being used, 
	 * @return A set of all serializable classes
	 */
	public Set<Class<?>> getClasses(){
		return map.keySet();
	}
	
	/**
	 * Used to keep track of IDs and serializers for classes
	 */
	private class Info{
		/** What serializer to use */
		Serializer<?> serializer;
		
		/** ID for associated class */
		int id;
		
		public Info(int id, Serializer<?> serializer){
			this.serializer = serializer;
			this.id = id;
		}
		
		public Info(int id){
			this.id = id;
		}
	}
}
