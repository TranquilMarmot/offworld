package com.bitwaffle.guts.resources;

import java.io.IOException;
import java.util.HashMap;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

import com.bitwaffle.guts.Game;

/**
 * Manages initializing and playing/stopping sounds.
 * 
 * @author TranquilMarmot
 */
public class SoundManager {
	/** Maximum number of streams that can be playing at once */
	private static int MAX_STREAMS = 16;
	
	/** 
	 * Quality of source
	 * From the Android docs:
	 * "the sample-rate converter quality. Currently has no effect. Use 0 for the default" 
	 */
	private static int SRC_QUALITY = 0;
	
	/** Takes care of... well, everything pretty much */
	private SoundPool pool;
	/** Hashes strings from a resource list to sound ID ints */
	private HashMap<String, Integer> soundIDs; 
	
	/** Called on completing the loading of a sound */
	class LoadCompleteListener implements SoundPool.OnLoadCompleteListener{
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
			// TODO something here? maybe add loaded sound to the hashmap in here (check that sampleID == ID returned from pool.load())
		}
	}
	
	/**
	 * Create a new sound manager
	 */
	public SoundManager(){
		soundIDs = new HashMap<String, Integer>();
		pool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, SRC_QUALITY);
		pool.setOnLoadCompleteListener(new LoadCompleteListener());
	}
	
	/**
	 * Initializes a sound from a file and adds it to the sound list
	 * @param soundName Name to give new sound
	 * @param soundPath Path of file sound is located at
	 * @param priority Priority of loading this sound (relative to other sounds- pretty useless)
	 */
	public void loadSound(String soundName, String soundPath, int priority){
		try{
			AssetFileDescriptor afd = Game.resources.openAssetFD(soundPath);
			soundIDs.put(soundName, pool.load(afd, priority));
			afd.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Play a sound
	 * @param soundID ID of sound to play (from resource list)
	 * @param leftVolume Left volume (between 0.0 and 1.0)
	 * @param rightVolume Right volume (between 0.0 and 1.0)
	 * @param priority Priority of sound (relative to other prioritied, 0 is the lowest)
	 * @param numLoops Number of loops to play sound for (0 == play the sound once, -1 == play forever)
	 * @param rate Rate to play the sound at (between 0.5 and 2.0, 1.0 == normal speed)
	 * @return ID of stream that is playing sound (to use with other methods)
	 */
	public int play(String soundID, float leftVolume, float rightVolume, int priority, int numLoops, float rate){
		return pool.play(soundIDs.get(soundID), leftVolume, rightVolume, priority, numLoops, rate);
	}
	
	/**
	 * Play a sound
	 * @param soundID ID of sound to play (from resource list)
	 * @param volume Volume to play sound at (between 0.0 and 1.0)
	 * @param priority Priority of sound (relative to other priorities, 0 is the lowest)
	 * @param numLoops Number of loops to play sound for (0 == play the sound once, -1 == play forever)
	 * @param rate Rate to play the sound at (between 0.5 and 2.0, 1.0 == normal speed)
	 * @return ID of stream that is playing sound (to use with other methods)
	 */
	public int play(String soundID, float volume, int priority, int numLoops, float rate){
		return play(soundID, volume, volume, priority, numLoops, rate);
	}
	
	/**
	 * Play a sound once, at full volume, at regular speed.
	 * @param soundID ID of sound to play (from resource list)
	 * @return ID of stream that is playing sound (to use with other methods)
	 */
	public int play(String soundID){
		return play(soundID, 1.0f, 1.0f, 0, 0, 1.0f);
	}
	
	/** Pause all currently playing sounds */
	public void pauseAll(){ pool.autoPause(); }
	
	/** Resume all previously paused sounds */
	public void resumeAll(){ pool.autoResume(); }
	
	/** Pause a playing sound @param streamID ID of stream to pause */
	public void pause(int streamID){ pool.pause(streamID); }
	
	/** Resume a playing sound @param streamID ID of stream to resume */
	public void resume(int streamID){ pool.resume(streamID); }
	
	/** Stop a playing sound @param streamID ID of stream to stop */
	public void stop(int streamID){ pool.stop(streamID); }
	
	/**
	 * Change how many times a playing sound will loop
	 * @param streamID ID of stream to set loop amount for
	 * @param loopAmount Number of loops (0 == play the sound once, -1 == play forever)
	 */
	public void setLoopAmount(int streamID, int loopAmount){ pool.setLoop(streamID, loopAmount); }
	
	/**
	 * Change the priority of a playing sound
	 * @param streamID ID of stream to change priority for
	 * @param priority New priority
	 */
	public void setPriority(int streamID, int priority){ pool.setPriority(streamID, priority); }
	
	/**
	 * Set the rate of a playing sound
	 * @param streamID ID of stream to change speed of
	 * @param rate New rate for stream (between 0.5 and 2.0, 1.0 == normal speed)
	 */
	public void setRate(int streamID, float rate){ pool.setRate(streamID, rate); }
	
	/**
	 * Set the volume of a playing sound
	 * @param streamID Stream to set volume of
	 * @param leftVolume Left volume (between 0.0 and 1.0)
	 * @param rightVolume Right volume (between 0.0 and 1.0)
	 */
	public void setVolume(int streamID, float leftVolume, float rightVolume){
		pool.setVolume(streamID, leftVolume, rightVolume);
	}
	
	/**
	 * Set the volume of a playing sound
	 * @param streamID Stream to set volume for
	 * @param volume New volume (between 0.0 and 1.0)
	 */
	public void setVolume(int streamID, float volume){ pool.setVolume(streamID, volume, volume); }
	
	/**
	 * Clean up any resources that the sound manager might have allocated
	 */
	public void cleanup(){
		for(String s : soundIDs.keySet())
			pool.unload(soundIDs.get(s));
		pool.release();
	}
}
