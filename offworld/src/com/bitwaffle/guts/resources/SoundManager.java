package com.bitwaffle.guts.resources;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.bitwaffle.guts.Game;

/**
 * Manages initialized sounds
 * 
 * @author TranquilMarmot
 */
public class SoundManager {
	
	private HashMap<String, Sound> sounds;
	
	public SoundManager(){
		sounds = new HashMap<String, Sound>();
	}
	
	/**
	 * Loud a sound file
	 * @param soundName Name to keep sound as
	 * @param soundPath Path to sound file
	 */
	public void loadSound(String soundName, String soundPath){
		Sound sound = Gdx.audio.newSound(Game.resources.getFileHandle(soundPath));
		this.sounds.put(soundName, sound);
	}
	
	/**
	 * Get rid of a sound
	 * @param soundName Sound to dispose of
	 */
	public void disposeSound(String soundName){
		sounds.get(soundName).dispose();
		sounds.remove(soundName);
	}
	

	/**
	 * Get a sound to play
	 * @param soundName Name of sound to get
	 */
	public Sound getSound(String soundName){
		return this.sounds.get(soundName);
	}
}
