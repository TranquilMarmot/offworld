package com.bitwaffle.guts.resources;

/**
 * Manages playing, stopping sounds
 * 
 * @author TranquilMarmot
 */
public abstract class SoundManager {
	public SoundManager(){}
	
	/**
	 * Play a given sound, once 
	 * @param soundName Sound to play
	 */
	public void playSound(String soundName){
		this.playSound(soundName, 1.0f, 1.0f, false);
	}
	
	/**
	 * Loud a sound file
	 * @param soundName Name to keep sound as
	 * @param soundPath Path to sound file
	 */
	public abstract void loadSound(String soundName, String soundPath);
	
	/**
	 * Play a sound
	 * @param soundName Name of sound to play
	 * @param volume Volume to play sound at
	 * @param numLoops Number of loops to play sound for
	 * @param rate Rate to play sound at
	 */
	public abstract void playSound(String soundName, float volume, float rate, boolean looping);
	
	/** Pause all sounds */
	public abstract void pauseAll();
	
	/** Resume any paused sounds */
	public abstract void resumeAll();
	
}
