package com.bitwaffle.moguts.resources;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;

/**
 * Manages resources for the game. All resources should be acquired through this.
 * Also holds a reference to a Textures object that keeps track of texture names/handles.
 * 
 * @author TranquilMarmot
 */
public class Resources {
	/** Ha. Haha. HA! */
	private AssetManager assMan;
	
	/** Texture manager */
	public TextureManager textures;
	
	/**
	 * Create a new resource manager
	 * @param assetMan Asset manager to get assets from
	 */
	public Resources(AssetManager assetMan){
		this.assMan = assetMan;
	}
	
	/**
	 * This must be called AFTER OpenGL has been initialized
	 */
	public void init(){
		textures = new TextureManager();
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
		} catch (IOException e) {
			throw e;
		}
    }
}
