package com.bitwaffle.guts.graphics.animation;

import java.nio.Buffer;

/**
 * A frame in an {@link AnimationPart}
 * 
 * @author TranquilMarmot
 */
public class Frame {
	/** Contains the sprite's location in the sprite sheet (basically, the location of this frame in the sheet) */
	private Buffer texCoordBuff;
	
	/** How long to stay on this frame */
	private float length;
	
	/** Size of this frame, in pixels */
	private int frameW, frameH;
	

	/**
	 * Create a new frame in an animation
	 * @param length How long to stay on this frame for
	 * @param sourceHeight 
	 * @param sourceWidth 
	 * @param frameH 
	 * @param frameW 
	 * @param texCoordBuff Texture coordinates for frame in animation sprite sheet
	 */
	public Frame(float length, int frameW, int frameH, Buffer texCoordBuff){
		this.length = length;
		this.texCoordBuff = texCoordBuff;
		this.frameW = frameW;
		this.frameH = frameH;
	}
	
	/**
	 * @return How long to stay on this frame
	 */
	public float getLength(){ return length; }
	
	/**
	 * @return Width to render this frame at
	 */
	public float getRenderWidth(){
		return (float)this.frameW / (float)this.frameH;
	}
	
	/**
	 * @return Height to render this frame on
	 */
	public float getRenderHeight(){
		return 1.0f;
	}
	
	/**
	 * @return FloatBuffer to use when drawing quad
	 */
	public Buffer getTexCoordBuffer(){ return texCoordBuff; }
}
