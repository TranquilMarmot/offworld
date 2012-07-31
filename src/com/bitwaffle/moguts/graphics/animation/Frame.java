package com.bitwaffle.moguts.graphics.animation;

public class Frame {
	private int handle, width, height;
	private float length;
	
	public Frame(int handle, float length, int width, int height){
		this.handle = handle;
		this.length = length;
		this.width = width;
		this.height = height;
	}
	
	public float getLength(){
		return length;
	}
	
	public int getHandle(){
		return handle;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}
