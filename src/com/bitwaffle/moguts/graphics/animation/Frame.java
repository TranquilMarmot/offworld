package com.bitwaffle.moguts.graphics.animation;

/**
 * A frame in an {@link Animation}
 * 
 * @author TranquilMarmot
 */
public class Frame {
	/** Handle for this frame's texture */
	private int handle;
	
	/** Width and height, in pixels */
	private int width, height;
	
	/** How long to stay on this frame */
	private float length;
	
	/**
	 * Create a new frame
	 * @param handle Handle of texture for frame
	 * @param length How long to stay on the frame
	 * @param width Width of frame, in pixels
	 * @param height Height of frame, in pixels
	 */
	public Frame(int handle, float length, int width, int height){
		this.handle = handle;
		this.length = length;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return How long to stay on this frame
	 */
	public float getLength(){ return length; }
	
	/**
	 * @return Handle for this frame's texture
	 */
	public int getHandle(){ return handle; }
	
	/**
	 * @return Width of this frame, in pixels
	 */
	public int getWidth(){ return width; }
	
	/**
	 * @return Height of this frame, in pixels
	 */
	public int getHeight(){ return height; }
}
