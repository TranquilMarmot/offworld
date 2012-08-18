package com.bitwaffle.moguts.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.bitwaffle.moguts.util.XMLHelper;
import com.bitwaffle.offworld.Game;

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
		// TODO this should be done on a per-room (level?) basis
		try {
			parseXML(Game.resources.openAsset("resourcelists/sounds.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses an XML resource list
	 * @param file InputStream from resource list
	 */
	private void parseXML(InputStream file){
		NodeList nodes = XMLHelper.getNodeList(file);
		
		// grab all the resources
		if (nodes != null && nodes.getLength() > 0) {
			for (int i = 0; i < nodes.getLength(); i++) {
				// we want to skip anything that's not an element node
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
					loadSound((Element) nodes.item(i));
			}
		} else {
			Log.e("XML", "Error parsing XML in TextureManager! Either there was nothing in the given file or the parser simply just didn't want to work");
		}
	}
	
	/**
	 * Load a sound from an XML element
	 * @param ele Element to load sound from
	 */
	private void loadSound(Element ele){
		String name = ele.getAttribute("name");
		String path = XMLHelper.getString(ele, "path");
		int priority = Integer.parseInt(XMLHelper.getString(ele, "priority"));
		
		try{
			AssetFileDescriptor afd = Game.resources.openAssetFD(path);
			soundIDs.put(name, pool.load(afd, priority));
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
