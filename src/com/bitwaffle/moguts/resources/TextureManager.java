package com.bitwaffle.moguts.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.util.XMLHelper;

/**
 * Manages initializing and loading textures, and keeps
 * a HashMap containing all the texture names mapped to their
 * respective handles
 * 
 * @author TranquilMarmot
 */
public class TextureManager {
	/** Hashes texture names to their int GL handles */
	private HashMap<String, Integer> textures;
	
	/**
	 * Create a new texture manager
	 */
	public TextureManager(){
		textures = new HashMap<String, Integer>();
		// TODO this should be done on a per-room (level?) basis
		try {
			parseXML(Game.resources.openAsset("resourcelists/textures.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Bind a texture to GL_TEXTURE_2D
	 * @param textureName Name of texture from XML resource file
	 */
	public void bindTexture(String textureName){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures.get(textureName));
	}
	
	/**
	 * Get the handle for a texture
	 * @param textureName Name of texture from XML resource file
	 * @return Handle of texture (if it exists)
	 */
	public int getTextureHandle(String textureName){
		return textures.get(textureName);
	}
	
	/**
	 * Initialize a texture
	 * 
	 * @param in InputStream from texture's file
	 * @param minFilter What to use for GL_TEXTURE_MIN_FILTER
	 * @param magFilter What to use for GL_TEXTURE_MAG_FILTER
	 * @return Handle for newly created texture
	 */
	private int initTexture(InputStream in, int minFilter, int magFilter){
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeStream(in);
		
		int[] handles = new int[1];
		
		GLES20.glGenTextures(1, handles, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[0]);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR)
			Log.e("Render2D", "Error loading texture! " + GLES20.glGetString(error));
		
		bitmap.recycle();
		
		return handles[0];
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
					loadTexture((Element) nodes.item(i));
			}
		} else {
			Log.e("XML", "Error parsing XML in TextureManager! Either there was nothing in the given file or the parser simply just didn't want to work");
		}
	}
	
	/**
	 * Load a texture from an XML Element
	 * @param ele Element to parse
	 */
	private void loadTexture(Element ele){
		String name = ele.getAttribute("name");
		String path = XMLHelper.getString(ele, "path");
		
		// TODO make sure this doesn't crash when there's no specified filters
		
		int minFilter = GLES20.GL_NEAREST;
		String minFilterString = XMLHelper.getString(ele, "minFilter");
		if(minFilterString.equals("GL_LINEAR"))
			minFilter = GLES20.GL_LINEAR;
		else if(minFilterString.equals("GL_NEAREST"))
			minFilter = GLES20.GL_NEAREST;
		
		int magFilter = GLES20.GL_NEAREST;
		String magFilterString = XMLHelper.getString(ele, "magFilter");
		if(magFilterString.equals("GL_LINEAR"))
			magFilter = GLES20.GL_LINEAR;
		else if(magFilterString.equals("GL_NEAREST"))
			magFilter = GLES20.GL_NEAREST;
		
		try {
			InputStream in = Game.resources.openAsset(path);
			textures.put(name, initTexture(in, minFilter, magFilter));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
