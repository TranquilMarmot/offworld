package com.bitwaffle.offworld.mguts.graphics.glsl;

import android.opengl.GLES20;

public class GLSLProgram {
	private int handle;
	private boolean linked;
	private String logString;
	
	public GLSLProgram(){
		handle = GLES20.glCreateProgram();
		
		if(handle == 0)
			System.out.println("Error creating shader program!");
		
		linked = false;
	}
	
	public void addShader(GLSLShader shader){
		GLES20.glAttachShader(handle, shader.getHandle());
	}
	
	public boolean link(){
		if(linked)
			return true;
		if(handle <= 0)
			return false;
		
		GLES20.glLinkProgram(handle);
		
		logString = GLES20.glGetProgramInfoLog(handle);
		
		// check for any errors while linking
		if(!logString.equals("")){
			System.err.println("Failed to link program! " + logString);
			linked = false;
		} else
			linked = true;
		
		return linked;
	}
	
	public void use(){
		if(handle <= 0 || !linked)
			return;
		GLES20.glUseProgram(handle);
	}
	
	public String log() { return logString; }
	public int getHandle() { return handle; }
	public boolean isLinked() { return linked; }
}
