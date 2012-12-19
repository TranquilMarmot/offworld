package com.bitwaffle.guts.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import android.os.Environment;
import android.util.Log;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.offworld.entities.Player;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

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
	
	/**
	 * Makes all the necessary directories to get to SAVE_DIRECTORY and then
	 * creates the given file in said directory
	 * @param file Name of file to create
	 * @return File representing new file, null if file creation failed
	 */
	private File makeFile(String file){
		File folder = new File(Environment.getExternalStorageDirectory(), SAVE_DIRECTORY);
		File toWrite = new File(folder, file);
		if(!toWrite.exists()){
			try {
				// create directories to get to save folder
				if(!folder.exists() && !folder.mkdirs()){
					Log.e(LOGTAG, "Failed to create directories for save file!");
					return null;
				}
				toWrite.createNewFile();
			} catch (IOException e) {
				Log.e(LOGTAG, "Failed to create save file! " + e.getMessage());
				return null;
			}
		}
		return toWrite;
	}
	
	/**
	 * Saves the current Physics world to a file
	 * @param file File to save world to
	 * @param physics Physics world to save to file
	 */
	public void saveGame(String file, Physics physics){
		try {
			File toWrite = makeFile(file);
			if(toWrite == null)
				return;
			FileOutputStream out = new FileOutputStream(toWrite);
			Output output = new Output(out);
			
			// write number of dynamic entitites
			output.writeInt(physics.numEntities());
			
			// iterate through every dynamic entity and write them all;
			Iterator<Entity>[] its = physics.getAllIterators();
			for(Iterator<Entity> it : its){
				while(it.hasNext()){
					Entity ent = it.next();
					/*
					 * Go through the list of classes and find out which one 'ent' is.
					 * Each entity has its class written then itself, because sometimes
					 * the class will by anonymouse (i.e. if the entity was created inside
					 * of a separate class from itself and had a method overridden)
					 */
					boolean written = false;
					for(Class<?> c : serializableClasses){
						if(c.isInstance(ent)){
							kryo.writeClass(output, c);
							kryo.writeObject(output, c.cast(ent));
							written = true;
							break;
						}
					}
					// if all else fails, attempt to use field serializer
					if(!written){
						kryo.writeClass(output, ent.getClass());
						kryo.writeObject(output, ent);
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
	
	/**
	 * Load a game into a Physics world
	 * @param file Location of file to load
	 * @param physics Physics world to shove loaded entities into
	 */
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
			int numEntities = input.readInt();
			
			// read in each entity
			for(int i = 0; i < numEntities; i++){
				// check which class we're reading
				Registration reg = kryo.readClass(input);
				Object object = kryo.readObject(input, reg.getType());
				
				// check if we've hit the player
				if(reg.getType().equals(Player.class)){
					Game.player = (Player)object;
					Render2D.camera.setTarget(Game.player);
					Render2D.camera.setMode(Camera.Modes.FOLLOW);
					SurfaceView.touchHandler.setPlayer(Game.player);
				}
				
				// Adding to physics with addDynamicEntity() calls the entity's init()
				// method before adding it to the world
				if(object instanceof DynamicEntity)
					physics.addDynamicEntity((DynamicEntity) reg.getType().cast(object));
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
