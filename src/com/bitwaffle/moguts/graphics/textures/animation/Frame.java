package com.bitwaffle.moguts.graphics.textures.animation;

import java.nio.FloatBuffer;

/**
 * A frame in an {@link Animation}
 * 
 * @author TranquilMarmot
 */
public class Frame {
	/** Contains the sprite's location in the sprite sheet */
	private FloatBuffer texCoordBuff;
	
	/** How long to stay on this frame */
	private float length;
	

	/**
	 * Create a new frame in an animation
	 * @param length How long to stay on this frame for
	 * @param texCoordBuff Texture coordinates for frame in animation sprite sheet
	 */
	public Frame(float length, FloatBuffer texCoordBuff){
		this.length = length;
		this.texCoordBuff = texCoordBuff;
	}
	
	/**
	 * @return How long to stay on this frame
	 */
	public float getLength(){ return length; }
	
	/**
	 * @return FloatBuffer to use when drawing quad
	 */
	public FloatBuffer getTexCoordBuffer(){ return texCoordBuff; }
}
