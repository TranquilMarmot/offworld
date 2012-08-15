package com.bitwaffle.moguts.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import android.os.Environment;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.physics.Physics;
import com.bitwaffle.moguts.serialization.serializers.BodyDefSerializer;
import com.bitwaffle.moguts.serialization.serializers.FixtureDefSerializer;
import com.bitwaffle.moguts.serialization.serializers.Vector2Serializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.ChainShapeSerializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.CircleShapeSerializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.EdgeShapeSerializer;
import com.bitwaffle.moguts.serialization.serializers.shapes.PolygonShapeSerializer;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.entities.Player;
import com.bitwaffle.offworld.entities.dynamic.DestroyableBox;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Handles serializing Entities to a save file
 * 
 * @author TranquilMarmot
 */
public class GameSaver {
	/** Where saves are kept */
	public static final String SAVE_DIRECTORY = "/Android/data/com.bitwaffle.offworld/cache/";
	
	/** What to send to logs to */
	public static final String LOGTAG = "Save";
	
	/** Kryo instance */
	Kryo kryo;
	
	/**
	 * Create a new SaveGameSerializer
	 */
	public GameSaver(){
		kryo = new Kryo();
		
		/*--- Box2D classes ---*/
		kryo.register(Vector2.class, new Vector2Serializer());
		
		kryo.register(PolygonShape.class, new PolygonShapeSerializer());
		kryo.register(CircleShape.class, new CircleShapeSerializer());
		kryo.register(EdgeShape.class, new EdgeShapeSerializer());
		kryo.register(ChainShape.class, new ChainShapeSerializer());
		
		kryo.register(FixtureDef.class, new FixtureDefSerializer());
		kryo.register(BodyDef.class, new BodyDefSerializer());
		
		/*--- Entities ---*/
		kryo.register(Entity.class);
		kryo.register(DynamicEntity.class);
		kryo.register(Player.class);
		kryo.register(BoxEntity.class);
		kryo.register(DestroyableBox.class);
	}
	
	public void saveGame(String file, Physics physics){
		try {
			// check if file exists and create it if it doesn't
			File folder = new File(Environment.getExternalStorageDirectory(), SAVE_DIRECTORY);
			File toWrite = new File(folder, file);
			if(!toWrite.exists()){
				try {
					if(!folder.mkdirs())
						Log.e(LOGTAG, "Failed to create directories for save file!");
					toWrite.createNewFile();
				} catch (IOException e) {
					Log.e(LOGTAG, "Failed to create save file! " + e.getMessage());
				}
			}
			FileOutputStream out = new FileOutputStream(toWrite);
			
			Output output = new Output(out);
			
			// write number of dynamic entitites
			output.writeInt(physics.numDynamicEntities());
			
			// iterate through every dynamic entity and write them all;
			Iterator<DynamicEntity> it = physics.getDynamicEntityIterator();
			while(it.hasNext()){
				DynamicEntity ent = it.next();
				writeDynamicEntity(kryo, output, ent);
			}
			output.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a DynamicEntity to an instance of Kryo
	 * Each entity has its class written then itself
	 * because sometimes the class will by anonymouse 
	 * (i.e. if the entity was created inside of a separate
	 * class from itself and had a method overridden)
	 * @param kryo Kryo instance
	 * @param output Stream to output to
	 * @param ent Entity to write
	 */
	private void writeDynamicEntity(Kryo kryo, Output output, DynamicEntity ent){
		/*
		 * Whenever a new class of DynamicEntity is coded that needs to be serialized,
		 * it MUST be added to this if/else statement, otherwise it will be written/read
		 * as a DynamicEntity (which may be fine, in some cases)
		 * Generally, put the deepest classes first (classes that implement classes
		 * that implement classes etc) so that they will be caught and written/read
		 * correctly.
		 */
		if(ent instanceof Player) {
			kryo.writeClass(output, Player.class);
			kryo.writeObject(output, (Player)ent);
		} else if(ent instanceof DestroyableBox){
			kryo.writeClass(output, DestroyableBox.class);
			kryo.writeObject(output, (DestroyableBox)ent);
		} else if(ent instanceof BoxEntity) {
			kryo.writeClass(output, BoxEntity.class);
			kryo.writeObject(output, (BoxEntity)ent);
		} else {
			kryo.writeClass(output, DynamicEntity.class);
			kryo.writeObject(output, (DynamicEntity)ent);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadGame(String file, Physics physics){
		// clear the physics world
		physics.restart();
		
		try{
			// open save file
			File folder = new File(Environment.getExternalStorageDirectory(), SAVE_DIRECTORY);
			File toRead = new File(folder, file);
			FileInputStream in = new FileInputStream(toRead);
		
			Input input = new Input(in);
			
			// read number of entities
			int numDynamicEntities = input.readInt();
			
			// read in each entity
			for(int i = 0; i < numDynamicEntities; i++){
				// check which class we're reading
				Registration reg = kryo.readClass(input);
				Object object = kryo.readObject(input, reg.getType());
				
				// check if we've hit the player
				if(reg.getType().equals(Player.class))
					Game.player = (Player)object;
				
				// add entity to physics world if it's dynamic
				if(object instanceof DynamicEntity)
					physics.addEntity((DynamicEntity)object);
			}
			
			input.close();
			in.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
