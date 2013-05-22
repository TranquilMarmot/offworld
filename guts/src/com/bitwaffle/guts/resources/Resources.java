package com.bitwaffle.guts.resources;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bitwaffle.guts.resources.manager.EntityInfoManager;
import com.bitwaffle.guts.resources.manager.ModelManager;
import com.bitwaffle.guts.resources.manager.PolygonManager;
import com.bitwaffle.guts.resources.manager.SoundManager;
import com.bitwaffle.guts.resources.manager.TextureManager;

/**
 * Manages resources for the game. All resources should be acquired through this.
 * Also holds a reference to a Textures object that keeps track of texture names/handles.
 * 
 * @author TranquilMarmot
 */
public class Resources {	
	public TextureManager textures;
	public SoundManager sounds;
	public PolygonManager polygons;
	public EntityInfoManager entityInfo;
	public ModelManager models;
	
	/** Whether or not resources have been initialized */
	private boolean initialized = false;
	
	/** This must be called AFTER OpenGL has been initialized */
	public void init(){
		if(!initialized){
			textures = new TextureManager();
			sounds = new SoundManager();
			polygons = new PolygonManager();
			entityInfo = new EntityInfoManager();
			models = new ModelManager();
		}
		
		initialized = true;
	}
	
    /**
     * Open an asset as a stream
     * @param fileLoc Location of asset to open inside of 'assets' folder
     * @return InputStream of given asset
     * @throws IOException When asset isn't found
     */
    public InputStream openAsset(String fileLoc) throws IOException{
    	return getFileHandle(fileLoc).read();
    }

    /**
     * Get a LibGDX FileHandle from a file.
     * @param fileLocation Location of file inside of 'assets' folder
     */
	public FileHandle getFileHandle(String fileLocation) {
		return Gdx.files.internal(fileLocation);
	}
}
