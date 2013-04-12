package com.bitwaffle.guts.resources;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bitwaffle.guts.resources.entityinfo.EntityInfoManager;

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
	
	/** Whether or not resources have been initialized */
	public boolean init = false;
	
	/** This must be called AFTER OpenGL has been initialized */
	public void init(){
		textures = new TextureManager();
		sounds = new SoundManager();
		polygons = new PolygonManager();
		entityInfo = new EntityInfoManager();
		
		// load base resource file (loads vital resources)
		ResourceLoader.loadResourceFile("base.res");
		
		init = true;
	}
	
    /**
     * Open an asset
     * @param fileLoc Location of asset to open inside of 'assets' folder
     * @return InputStream of given asset
     * @throws IOException When asset isn't found
     */
    public InputStream openAsset(String fileLoc) throws IOException{
    	return getFileHandle(fileLoc).read();
    }

	public FileHandle getFileHandle(String fileLocation) {
		return Gdx.files.internal(fileLocation);
	}
}
