package com.bitwaffle.guts.resources.manager;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.graphics.graphics2d.animation.Animation;
import com.bitwaffle.guts.graphics.graphics2d.shapes.quad.SubImage;

/**
 * Manages initializing and loading textures, and keeps
 * a HashMap containing all the texture names mapped to their
 * respective handles
 * 
 * @author TranquilMarmot
 */
public class TextureManager {
	private static final String LOGTAG = "TextureManager";
	/** Hashes texture names to their int GL handles */
	private HashMap<String, Integer> textures;
	
	/** Images inside of other images */
	private HashMap<String, SubImage> subImages;
	
	/** Keeps track of animations */
	private HashMap<String, Animation> animations;
	
	/** Create a new texture manager */
	public TextureManager(){
		textures = new HashMap<String, Integer>();
		animations = new HashMap<String, Animation>();
		subImages = new HashMap<String, SubImage>();
	}
	
	/** Bind a texture to GL_TEXTURE_2D */
	public void bindTexture(String textureName){
		Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, textures.get(textureName));
	}
	
	public SubImage getSubImage(String subImage){ return subImages.get(subImage); }
	
	/** Get the handle for a texture */
	public int getTextureHandle(String textureName){
		return textures.get(textureName);
	}
	
	/**
	 * Get an instance of an animation
	 * Whatever class is handling the animation needs
	 * to update and bind its current frame on draw.
	 * This way, multiple instance of the same animation
	 * can be going and be on different frames.
	 * @param animationName Name of animation (from XML resource file)
	 * @return Instance of animation
	 */
	public Animation getAnimation(String animationName){
		return new Animation(animations.get(animationName));
	}
	
	/**
	 * Add a texture to the map of textures.
	 * If a texture with the given name already exists in the map,
	 * a warning will be printed and the value will be overwritten
	 * @param name Name of texture
	 * @param handle OpenGL handle to use for texture
	 */
	public void addTexture(String name, int handle){
		if(textures.containsKey("name"))
			Gdx.app.debug(LOGTAG, "WARNING texture with name " + name + " already exists! Overwriting...");
		textures.put(name, handle);
		
	}
	
	/**
	 * Add an animation to the map of animations.
	 * If an animation with the given name already exists in the map,
	 * a warning will be printed and the value will be overwritten
	 * @param name Name of animation
	 * @param animation Animation to put in map
	 */
	public void addAnimation(String name, Animation animation){
		if(animations.containsKey(name))
			Gdx.app.debug(LOGTAG, "WARNING animation with name " + name + " already exists! Overwriting...");
		animations.put(name, animation);
	}
	
	public void addSubImage(String name, SubImage image){
		if(subImages.containsKey(name))
			Gdx.app.debug(LOGTAG, "WARNING sub-image with name " + name + " already exists! Overwriting...");
		subImages.put(name, image);
	}
}
