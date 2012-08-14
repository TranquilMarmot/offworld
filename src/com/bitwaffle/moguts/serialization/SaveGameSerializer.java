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
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SaveGameSerializer {
	Kryo kryo;
	
	public SaveGameSerializer(){
		kryo = new Kryo();
		
		// Box2D classes
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
			File folder = new File(Environment.getExternalStorageDirectory(), "/Android/data/com.bitwaffle.offworld/cache/");
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
			System.out.println(toWrite.exists() + " " + toWrite.getAbsolutePath());
			FileOutputStream os = new FileOutputStream(toWrite);
			
			
			//Output output = new Output(Game.resources.openFileOutput(file, Context.MODE_WORLD_READABLE));
			Output output = new Output(os);
			
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
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Entities readEntitiesFromFile(String file, Physics physics){
		Entities ents = new Entities();
		
		try{			
			File folder = new File(Environment.getExternalStorageDirectory(), "/Android/data/com.bitwaffle.offworld/cache/");
			File toRead = new File(folder, "save.save");
			FileInputStream is = new FileInputStream(toRead);
		
			//Input input = new Input(Game.resources.openFileInput(file));
			Input input = new Input(is);
			Object object = kryo.readClassAndObject(input);
			
			while(object != null){
				System.out.println(object.getClass().getName());
				object = kryo.readClassAndObject(input);
			}
			
			input.close();
			is.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ents;
	}
}
