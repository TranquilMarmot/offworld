package com.bitwaffle.guts.graphics.animation;

import java.nio.Buffer;

import com.bitwaffle.guts.graphics.SubImage;

/**
 * A frame in an {@link AnimationPart}
 * 
 * @author TranquilMarmot
 */
public class Frame extends SubImage {
	/** How long to stay on this frame */
	private float length;
	
	public Frame(float length, int sheetHandle, int frameW, int frameH, Buffer texCoords){
		super(sheetHandle, frameW, frameH, texCoords);
		this.length = length;
	}
	
	/**
	 * @return How long to stay on this frame
	 */
	public float getLength(){ return length; }
	
	/**
	 * @param length New length to stay on frame
	 */
	public void setLength(float length){ this.length = length; }
}
