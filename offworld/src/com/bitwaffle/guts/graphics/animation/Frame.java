package com.bitwaffle.guts.graphics.animation;

import java.nio.Buffer;

/**
 * A frame in an {@link Animation}
 * 
 * @author TranquilMarmot
 */
public class Frame {
	/** Contains the sprite's location in the sprite sheet */
	private Buffer texCoordBuff;
	
	/** How long to stay on this frame */
	private float length;
	
	private int frameW, frameH;
	
	@SuppressWarnings("unused")
	private int sourceWidth, sourceHeight;
	

	/**
	 * Create a new frame in an animation
	 * @param length How long to stay on this frame for
	 * @param sourceHeight 
	 * @param sourceWidth 
	 * @param frameH 
	 * @param frameW 
	 * @param frameBuff Texture coordinates for frame in animation sprite sheet
	 */
	public Frame(float length, int frameW, int frameH, int sourceWidth, int sourceHeight, Buffer frameBuff){
		this.length = length;
		this.texCoordBuff = frameBuff;
		this.frameW = frameW;
		this.frameH = frameH;
		this.sourceWidth = sourceWidth;
		this.sourceHeight = sourceHeight;
	}
	
	/**
	 * @return How long to stay on this frame
	 */
	public float getLength(){ return length; }
	
	public float getRenderWidth(){
		return (float)this.frameW / (float)this.frameH;
	}
	
	public float getRenderHeight(){
		return 1.0f;
	}
	
	/**
	 * @return FloatBuffer to use when drawing quad
	 */
	public Buffer getTexCoordBuffer(){ return texCoordBuff; }
}
