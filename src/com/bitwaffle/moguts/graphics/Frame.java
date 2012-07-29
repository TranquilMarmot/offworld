package com.bitwaffle.moguts.graphics;

public class Frame {
	private int index, handle, width, height;
	private float time;
	
	public Frame(int index, int handle, float time, int width, int height){
		this.index = index;
		this.handle = handle;
		this.time = time;
		this.width = width;
		this.height = height;
	}
	
	public float getTime(){
		return time;
	}
	
	public int getIndex(){
		return index;
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
