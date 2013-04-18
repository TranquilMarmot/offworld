package com.bitwaffle.guts.graphics.glsl;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.BufferUtils;

/**
 * A convenience class for handling shader programs
 * 
 * @author TranquilMarmot
 */
public class GLSLProgram {
	private static final String LOGTAG = "GLSLProgram";
	
	/** Handle for the program */
	private int handle;
	
	/** Whether or not the program has been linked */
	private boolean linked;
	
	/** Contains any error info if any methods return false */
	private String logString;
	
	/** Used to store matrix before sending it to opengl */
	private FloatBuffer mat4buff, mat3buff;
	
	/**
	 * Creates a handle for a new GLSL program.
	 * Shaders still need to be added with addShader(),
	 * and the program still needs to be linked with link()
	 * before being use()'d
	 */
	public GLSLProgram(){
		handle = Gdx.gl20.glCreateProgram();
		
		if(handle == 0)
			System.err.println("Error creating shader program!");
		
		linked = false;
		
		mat4buff = BufferUtils.newFloatBuffer(16);
		mat3buff = BufferUtils.newFloatBuffer(9);
	}
	
	public void addShader(GLSLShader shader){
		Gdx.gl20.glAttachShader(handle, shader.getHandle());
	}
	
	/** @return Whether or not the program successfully linked. If false is returned, then log() will return the error message. */
	public boolean link(){
		if(linked)
			return true;
		if(handle <= 0)
			return false;
		
		Gdx.gl20.glLinkProgram(handle);
		
		logString = Gdx.gl20.glGetProgramInfoLog(handle);
		
		// check for any errors while linking
		linked = /*linkStatus == GL20.GL_TRUE || linkStatus == 54 || linkStatus == 16 || */logString.equals("") || logString.contains("Link was successful.\n");
		
		return linked;
	}
	
	/** Use this program */
	public void use(){
		/*
		 * Checking if the program is linked before using it
		 * was causing some issues. There's no standard for what gets returned
		 * by the log when glLinkProgram gets called, so it's impossible to check
		 * for all cases. As such, it's very difficult to determine whether or not
		 * the program got linked successfully, so checking for linkage here would
		 * just made the program not run. 
		 */
		if(handle <= 0 /*|| !linked*/)
			return;
		Gdx.gl20.glUseProgram(handle);
	}
	
	/** If any method returns false, this will return an error string */
	public String log() { return logString; }
	
	/** @return Handle for this program */
	public int getHandle() { return handle; }
	
	/** @return Whether or not this program is linked */
	public boolean isLinked() { return linked; }
	
	/** Bind an attribute to a given location */
	public void bindAttribLocation(int location, String name){
		Gdx.gl20.glBindAttribLocation(handle, location, name);
	}
	
	/** @param name Uniform to get location of */
	public int getUniformLocation(String name){
		return Gdx.gl20.glGetUniformLocation(handle, name);
	}
	
	/** @return Handle for given attribute */
	public int getAttribLocation(String name){
		return Gdx.gl20.glGetAttribLocation(handle, name);
	}
	
	/** Set a 2f uniform */
	public void setUniform(String name, float x, float y){
		int loc = getUniformLocation(name);
		if(loc >= 0)
			Gdx.gl20.glUniform2f(loc, x, y);
		else
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
	}
	
	/** Set a 3f uniform */
	public void setUniform(String name, float x, float y, float z){
		int loc = getUniformLocation(name);
		if(loc >= 0)
			Gdx.gl20.glUniform3f(loc, x, y, z);
		else
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
	}
	
	/** Set a 4f uniform */
	public void setUniform(String name, float x, float y, float z, float w){
		int loc = getUniformLocation(name);
		if(loc >= 0)
			Gdx.gl20.glUniform4f(loc, x, y, z, w);
		else
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
	}
	
	/** Set a uniform matrix from an array */
	public void setUniform(String name, Matrix4 m){
		int loc = getUniformLocation(name);
		mat4buff.clear();
		
		for(float f : m.getValues())
			mat4buff.put(f);
		
		mat4buff.rewind();
		if(loc >= 0)
			Gdx.gl20.glUniformMatrix4fv(loc, 1, false, mat4buff);
		else
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
	}
	
	public void setUniform(String name, Matrix3 m) {
		int loc = getUniformLocation(name);
		mat3buff.clear();
		
		for(float f : m.getValues())
			mat3buff.put(f);
		
		mat3buff.rewind();
		if(loc >= 0)
			Gdx.gl20.glUniformMatrix3fv(loc, 1, false, mat3buff);
		else 
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
		
	}
	
	/** Set a single float uniform */
	public void setUniform(String name, float val){
		int loc = getUniformLocation(name);
		if(loc >= 0)
			Gdx.gl20.glUniform1f(loc, val);
		else
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
	}
	
	/** Set a single integer uniform */
	public void setUniform(String name, int val){
		int loc = getUniformLocation(name);
		if(loc >= 0)
			Gdx.gl20.glUniform1i(loc, val);
		else
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
	}
	
	/** Set a single boolean uniform */
	public void setUniform(String name, boolean val){
		int loc = getUniformLocation(name);
		if(loc >= 0)
			Gdx.gl20.glUniform1i(loc, val ? 1 : 0);
		else
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
	}
}
