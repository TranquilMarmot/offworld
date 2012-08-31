package com.bitwaffle.moguts.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import android.os.Environment;
import android.util.Log;

import com.bitwaffle.moguts.device.SurfaceView;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.entities.dynamic.DynamicEntity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.physics.Physics;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.entities.Player;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

// TODO this currently only serializes DynamicEntities, might be good to serialize Entities too

/**
 * Handles serializing Entities to a save file
 * 
 * @author TranquilMarmot
 * @see SerializationInfo
 */
public class GameSaver {
	/** Where saves are kept */
	public static final String SAVE_DIRECTORY = "/Android/data/com.bitwaffle.offworld/saves/";
	
	/** What to send to logs to */
	public static final String LOGTAG = "Save";
	
	/** Kryo instance */
	Kryo kryo;
	
	/** Set of possible classes (from {@link SerializationInfo}) */
	Set<Class<?>> serializableClasses;
	
	/**
	 * Create a new SaveGameSerializer
	 */
	public GameSaver(){
		kryo = new Kryo();
		
		// register classes and get class set
		SerializationInfo info = new SerializationInfo();
		info.registerClasses(kryo);
		serializableClasses = info.getClasses();
	}
	
	public void saveGame(String file, Physics physics){
		try {
			// check if file exists and create it if it doesn't
			File folder = new File(Environment.getExternalStorageDirectory(), SAVE_DIRECTORY);
			File toWrite = new File(folder, file);
			if(!toWrite.exists()){
				try {
					if(!folder.mkdirs()){
						Log.e(LOGTAG, "Failed to create directories for save file!");
						return;
					}
					toWrite.createNewFile();
				} catch (IOException e) {
					Log.e(LOGTAG, "Failed to create save file! " + e.getMessage());
					return;
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
				/*
				 * Go through the list of classes and find out which one 'ent' is.
				 * Each entity has its class written then itself, because sometimes
				 * the class will by anonymouse (i.e. if the entity was created inside
				 * of a separate class from itself and had a method overridden)
				 */
				for(Class<?> c : serializableClasses){
					if(c.isInstance(ent)){
						kryo.writeClass(output, c);
						kryo.writeObject(output, c.cast(ent));
						break;
					}
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
	
	@SuppressWarnings("unchecked")
	public void loadGame(String file, Physics physics){
		// clear the physics world
		physics.clearWorld();
		
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
				if(reg.getType().equals(Player.class)){
					Game.player = (Player)object;
					Render2D.camera.follow(Game.player);
					SurfaceView.touchHandler.setPlayer(Game.player);
				}
				
				// add entity to physics world if it's dynamic
				if(object instanceof DynamicEntity)
					physics.addEntity((DynamicEntity) reg.getType().cast(object));
				else if(object instanceof Entity)
					physics.addEntity((Entity)object);
				else
					Log.e(LOGTAG, "Encountered unknown class! " + object.getClass().getName());
			}
			
			input.close();
			in.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// un-pause the game (assumes that the game was paused to load)
		Game.togglePause();
	}
}
