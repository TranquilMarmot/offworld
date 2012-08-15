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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.bitwaffle.moguts.entities.Entities;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.physics.Physics;
import com.bitwaffle.offworld.entities.Player;
import com.bitwaffle.offworld.entities.dynamic.DestroyableBox;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SaveGameSerializer {
	public static final String SAVE_DIRECTORY = "/Android/data/com.bitwaffle.offworld/cache/";
	Kryo kryo;
	
	public SaveGameSerializer(){
		kryo = new Kryo();
		
		// Box2D classes
		// TODO serialize each shape class?
		kryo.register(Vector2.class, new Vector2Serializer());
		kryo.register(Shape.class, new ShapeSerializer());
		kryo.register(FixtureDef.class, new FixtureDefSerializer());
		kryo.register(BodyDef.class, new BodyDefSerializer());
		
		// Entities
		kryo.register(Entity.class);
		kryo.register(DynamicEntity.class);
		kryo.register(Player.class);
		kryo.register(BoxEntity.class);
		kryo.register(DestroyableBox.class);
	}
	
	public void writeEntitiesToFile(String file, Entities entities){
		try {
			String state = Environment.getExternalStorageState();
			System.out.println(state.equals(Environment.MEDIA_MOUNTED));
			
			File folder = new File(Environment.getExternalStorageDirectory(), SAVE_DIRECTORY);
			File toWrite = new File(folder, "save.save");
			if(!toWrite.exists()){
				try {
					if(!folder.mkdirs())
						Log.e("Save", "Failed to create directories for save file!");
					toWrite.createNewFile();
				} catch (IOException e) {
					Log.e("Save", "Failed to create save file! " + e.getMessage());
				}
			}
			FileOutputStream out = new FileOutputStream(toWrite);
			
			Output output = new Output(out);
			
			output.writeInt(entities.numDynamicEntities());
			
			Iterator<DynamicEntity> it = entities.getDynamicEntityIterator();
			while(it.hasNext()){
				DynamicEntity ent = it.next();
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
			output.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Entities readEntitiesFromFile(String file, Physics physics){
		Entities ents = new Entities();
		
		try{			
			File folder = new File(Environment.getExternalStorageDirectory(), SAVE_DIRECTORY);
			File toRead = new File(folder, "save.save");
			FileInputStream in = new FileInputStream(toRead);
		
			Input input = new Input(in);
			
			int numEntities = input.readInt();
			
			System.out.println(numEntities);
			
			for(int i = 0; i < numEntities; i++){
				Registration reg = kryo.readClass(input);
				System.out.println(i + " " + reg.getType().getName());
				Object object = kryo.readObject(input, reg.getType());
			}
				
			/*
			Object object = kryo.readClassAndObject(input);
			
			while(object != null){
				object = kryo.readClassAndObject(input);
			}*/
			
			input.close();
			in.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ents;
	}
}
