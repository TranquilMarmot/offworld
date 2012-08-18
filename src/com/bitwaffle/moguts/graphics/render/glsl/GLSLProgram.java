package com.bitwaffle.moguts.graphics.render.glsl;

import java.util.HashMap;

import android.opengl.GLES20;

/**
 * A convenience class for handling shader programs
 * 
 * @author TranquilMarmot
 */
public class GLSLProgram {
	/** Handle for the program */
	private int handle;
	
	/** Whether or not the program has been linked */
	private boolean linked;
	
	/** Contains any error info if any methods return false */
	private String logString;
	
	/** Maps every uniform/attribute to it's location in the shader */
	private HashMap<String, Integer> uniformMap, attribMap;
	
	/**
	 * Creates a handle for a new GLSL program.
	 * Shaders still need to be added with addShader(),
	 * and the program still needs to be linked with link()
	 * before being use()'d
	 */
	public GLSLProgram(){
		handle = GLES20.glCreateProgram();
		
		if(handle == 0)
			System.err.println("Error creating shader program!");
		
		linked = false;
	}
	
	/**
	 * Add a shader to this program. link() must be called after adding shaders.
	 * @param shader Shader to add to program
	 */
	public void addShader(GLSLShader shader){
		GLES20.glAttachShader(handle, shader.getHandle());
	}
	
	/**
	 * Link the added shaders to the program
	 * @return Whether or not the program successfully linked. If false is returned, then log() will return the error message.
	 */
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
		
		buildUniformMap();
		buildAttribMap();
		
		return linked;
	}
	
	/**
	 * Use this program
	 */
	public void use(){
		if(handle <= 0 || !linked)
			return;
		GLES20.glUseProgram(handle);
	}
	
	/** If any method returns false, this will return an error string */
	public String log() { return logString; }
	
	/** @return Handle for this program */
	public int getHandle() { return handle; }
	
	/** @return Whether or not this program is linked */
	public boolean isLinked() { return linked; }
	
	/**
	 * Bind an attribute to a given location
	 * @param location Location to bind
	 * @param name Name of attribute
	 */
	public void bindAttribLocation(int location, String name){
		GLES20.glBindAttribLocation(handle, location, name);
	}
	
	/**
	 * @param name Uniform to get location of
	 * @return Handle for uniform
	 */
	public int getUniformLocation(String name){
		try{
			return uniformMap.get(name);
		} catch (NullPointerException e){
			return GLES20.glGetUniformLocation(handle, name);
		}
	}
	
	/**
	 * @return Number of uniforms active for this program
	 */
	public int getNumUniforms(){
		int[] numUniforms = new int[1];
		GLES20.glGetProgramiv(handle, GLES20.GL_ACTIVE_UNIFORMS, numUniforms, 0);
		return numUniforms[0];
	}
	
	/**
	 * @return Length of the longest active uniform's name
	 */
	private int getMaxUniformNameLength(){
		int[] maxLen = new int[1];
		GLES20.glGetProgramiv(handle, GLES20.GL_ACTIVE_UNIFORM_MAX_LENGTH, maxLen, 0);
		return maxLen[0];
	}
	
	/**
	 * @return A list of every active uniform in the program
	 */
	public String[] getUniformList(){
		int numUniforms = getNumUniforms();
		int maxLen = getMaxUniformNameLength();
		String[] ret = new String[numUniforms];
		
		// will contain the name of the uniform
		byte[] str = new byte[maxLen];
		
		// how long the name of the uniform is
		int[] strLen = new int[1];
		
		// not sure what the point of this is
		int[] size = new int[1];
		
		// not sure how to get what the type is from the int
		int[] type = new int[1];
		
		for(int i = 0; i < numUniforms; i++){
			GLES20.glGetActiveUniform(handle, i, maxLen, strLen, 0, size, 0, type, 0, str, 0);
			ret[i] = (new String(str)).substring(0, strLen[0]);
		}
		
		return ret;
	}
	
	/**
	 * Builds up the HashMap containing the location of every uniform
	 */
	private void buildUniformMap(){
		if(uniformMap == null)
			uniformMap = new HashMap<String, Integer>();
		
		for(String uni : getUniformList())
			uniformMap.put(uni, this.getUniformLocation(uni));
	}
	
	/**
	 * @param name Attribute to get location of
	 * @return Handle for attribute
	 */
	public int getAttribLocation(String name){
		try{
			return attribMap.get(name);
		} catch (NullPointerException e){
			return GLES20.glGetAttribLocation(handle, name);
		}
	}
	
	/**
	 * @return Number of active attributes for this program
	 */
	public int getNumAttribs(){
		int[] numAttribs = new int[1];
		GLES20.glGetProgramiv(handle, GLES20.GL_ACTIVE_ATTRIBUTES, numAttribs, 0);
		return numAttribs[0];
	}
	
	/**
	 * @return Length of the longest active attribute's name
	 */
	private int getMaxAttribNameLength(){
		int[] maxLen = new int[1];
		GLES20.glGetProgramiv(handle, GLES20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, maxLen, 0);
		return maxLen[0];
	}
	
	/**
	 * @return A list of every active attribute in the program
	 */
	public String[] getAttribList(){
		int numAttribs = getNumAttribs();
		int maxLen = getMaxAttribNameLength();
		String[] ret = new String[numAttribs];
		
		// will contain the name of the uniform
		byte[] str = new byte[maxLen];
		
		// how long the name of the uniform is
		int[] strLen = new int[1];
		
		// not sure what the point of this is
		int[] size = new int[1];
		
		// not sure how to get what the type is from the int
		int[] type = new int[1];
		
		for(int i = 0; i < numAttribs; i++){
			GLES20.glGetActiveAttrib(handle, i, maxLen, strLen, 0, size, 0, type, 0, str, 0);
			ret[i] = (new String(str)).substring(0, strLen[0]);
		}
		
		return ret;
	}
	
	/**
	 * Builds up the HashMap containing the location of every attribute
	 */
	private void buildAttribMap(){
		if(attribMap == null)
			attribMap = new HashMap<String, Integer>();
		
		for(String attrib : getAttribList())
			attribMap.put(attrib, this.getAttribLocation(attrib));
	}
	
	/**
	 * Set a 3f uniform
	 * @param name Name of uniform to set
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setUniform(String name, float x, float y, float z){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform3f(loc, x, y, z);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	/**
	 * Set a 4f uniform
	 * @param name Name of uniform to set
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void setUniform(String name, float x, float y, float z, float w){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform4f(loc, x, y, z, w);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	/**
	 * Set a uniform matrix from an array
	 * @param name Name of uniform to set
	 * @param m A 4x4 matrix (16 floats)
	 */
	public void setUniformMatrix4f(String name, float[] m){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniformMatrix4fv(loc, 1, false, m, 0);
		} else {
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	/**
	 * Set a single float uniform
	 * @param name Name of uniform to set
	 * @param val
	 */
	public void setUniform(String name, float val){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform1f(loc, val);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	/**
	 * Set a single integer uniform
	 * @param name Name of uniform to set
	 * @param val
	 */
	public void setUniform(String name, int val){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform1i(loc, val);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	/**
	 * Set a single boolean uniform
	 * @param name Name of uniform to set
	 * @param val
	 */
	public void setUniform(String name, boolean val){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform1i(loc, val ? 1 : 0);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
}
