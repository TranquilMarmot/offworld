package com.bitwaffle.guts.graphics.graphics2d.animation;

import java.nio.Buffer;

import com.bitwaffle.guts.graphics.graphics2d.shapes.quad.SubImage;

/**
 * A frame in an {@link AnimationPart}
 * 
 * @author TranquilMarmot
 */
public class Frame extends SubImage {
	/** How long to stay on this frame, in seconds  */
	private float length;
	
	public Frame(float length, int sheetHandle, int frameW, int frameH, Buffer texCoords){
		super(sheetHandle, frameW, frameH, texCoords);
		this.length = length;
	}
	
	/** @return How long to stay on this frame, in seconds */
	public float getLength(){ return length; }
	
	/** @param length How long to stay on frame, in seconds */
	public void setLength(float length){ this.length = length; }
}
