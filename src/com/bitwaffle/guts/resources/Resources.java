package com.bitwaffle.guts.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.bitwaffle.guts.resources.textures.TextureManager;

/**
 * Manages resources for the game. All resources should be acquired through this.
 * Also holds a reference to a Textures object that keeps track of texture names/handles.
 * 
 * @author TranquilMarmot
 */
public class Resources {			
	/** 
	 * Everything in the 'assets' folder is read-only,
	 * so that's where all the game's resources are stored-
	 * images, sounds, levels, XML, etc.
	 * This class provides <code>openAsset</code> and
	 * <code>openAssetFD</code> for opening assets.
	 */
	private AssetManager assMan;
	
	/** 
	 * Each app is given a folder, somewhere on the device.
	 * Files in the folder can be written or read, so that's
	 * where any files generated by the game (i.e. save files)
	 * should get saved.
	 * This class provides <code>openFileOutput</code> and
	 * <code>openFileInput</code> methods for opening files.
	 */
	private Context context;
	
	/** Texture manager */
	public TextureManager textures;
	
	/** Sound manager */
	public SoundManager sounds;
	

	
	/**
	 * Create a new resource manager
	 * @param assetMan Asset manager to get assets from
	 */
	public Resources(Context context){
		this.context = context;
		this.assMan = context.getAssets();
		textures = new TextureManager();
		sounds = new SoundManager();
	}
	
	/**
	 * This must be called AFTER OpenGL has been initialized
	 */
	public void init(){
		ResourceLoader.loadResourceFile("base.res");
	}
	
	/**
	 * Open an output stream inside of this app's storage. Creates the file if it doesn't already exist.
	 * @param fileLoc Name of file to open
	 * @param mode Mode, from Context class- MODE_PRIVATE, MODE_APPEND, MODE_WORLD_READABLE, MODE_WORLD_WRITEABLE
	 * @return OutputStream for given file
	 * @throws FileNotFoundException If given file isn't found
	 */
	public OutputStream openFileOutput(String fileLoc, int mode) throws FileNotFoundException{
		// TODO test this on android!
		try { 
			return context.openFileOutput(fileLoc, mode); 
		} catch (FileNotFoundException e) { throw e; }
	}
	
	/**
	 * Open an existing file in this app's storage
	 * @param fileLoc Name of file to open
	 * @return InputStream of given file
	 * @throws FileNotFoundException If file doesn't exist
	 */
	public InputStream openFileInput(String fileLoc) throws FileNotFoundException{
		try{ 
			return context.openFileInput(fileLoc); 
		} catch(FileNotFoundException e){ throw e; }
	}
	
	/**
	 * Delete a file from this app's storage
	 * @param fileLoc Name of file to delete
	 * @return True if file was deleted, false otherwise
	 */
	public boolean deleteFile(String fileLoc){
		return context.deleteFile(fileLoc);
	}
	
	/**
	 * @return A list of all files stored in this app's storage
	 */
	public String[] listFiles(){
		return context.fileList();
	}
	
    /**
     * Open an asset
     * @param fileLoc Location of asset to open inside of 'assets' folder
     * @return InputStream of given asset
     * @throws IOException When asset isn't found
     */
    public InputStream openAsset(String fileLoc) throws IOException{
    	try { 
    		return assMan.open(fileLoc); 
    	} catch (IOException e) { throw e; }
    }
    
    /**
     * Open an asset's file descriptor
     * @param fileLoc Location of asset to open inside of 'assets' folder
     * @return File descriptor of asset
     * @throws IOException When asset isn't found
     */
    public AssetFileDescriptor openAssetFD(String fileLoc) throws IOException{
    	try{ 
    		return assMan.openFd(fileLoc); 
    	} catch (IOException e){ throw e; }
    }
    
    /**
     * @return Context that resources are being loaded from
     */
    public Context getContext(){
    	return context;
    }
}
