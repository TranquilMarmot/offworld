package com.bitwaffle.guts.graphics.glsl;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
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
	private FloatBuffer mat4buff;
	
	//private IntBuffer tempBuff;
	
	/** Maps every uniform/attribute to it's location in the shader */
	//private HashMap<String, Integer> uniformMap, attribMap;
	
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
		//tempBuff = BufferUtils.newIntBuffer(1);
	}
	
	/**
	 * Add a shader to this program. link() must be called after adding shaders.
	 * @param shader Shader to add to program
	 */
	public void addShader(GLSLShader shader){
		Gdx.gl20.glAttachShader(handle, shader.getHandle());
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
		
		Gdx.gl20.glLinkProgram(handle);
		
		logString = Gdx.gl20.glGetProgramInfoLog(handle);
		//tempBuff.rewind();
		//Gdx.gl20.glGetShaderiv(handle, GL20.GL_LINK_STATUS, tempBuff);
		//tempBuff.rewind();
		//int linkStatus = tempBuff.get();
		
		// check for any errors while linking
		linked = /*linkStatus == GL20.GL_TRUE || linkStatus == 54 || linkStatus == 16 || */logString.equals("") || logString.contains("Link was successful.\n");
		
		//buildUniformMap();
		//buildAttribMap();
		
		return linked;
	}
	
	/**
	 * Use this program
	 */
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
	
	/**
	 * Bind an attribute to a given location
	 * @param location Location to bind
	 * @param name Name of attribute
	 */
	public void bindAttribLocation(int location, String name){
		Gdx.gl20.glBindAttribLocation(handle, location, name);
	}
	
	/**
	 * @param name Uniform to get location of
	 * @return Handle for uniform
	 */
	public int getUniformLocation(String name){
		return Gdx.gl20.glGetUniformLocation(handle, name);
	}
	
	/**
	 * @return Number of uniforms active for this program
	 */
	/*
	public int getNumUniforms(){
		tempBuff.rewind();
		Gdx.gl20.glGetProgramiv(handle, GL20.GL_ACTIVE_UNIFORMS, tempBuff);
		tempBuff.rewind();
		return tempBuff.get();
	}
	*/
	
	/**
	 * @return Length of the longest active uniform's name
	 */
	/*
	private int getMaxUniformNameLength(){
		tempBuff.rewind();
		Gdx.gl20.glGetProgramiv(handle, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH, tempBuff);
		return tempBuff.get();
	}
	*/
	
	/**
	 * @return A list of every active uniform in the program
	 */
	/*
	public String[] getUniformList(){
		int numUniforms = getNumUniforms();
		int maxLen = getMaxUniformNameLength();
		String[] ret = new String[numUniforms];
		
		for(int i = 0; i < numUniforms; i++)
			ret[i] = Gdx.gl20.glGetActiveUniform(handle, i, maxLen);
		
		Gdx.gl20.glGetActiveUniform(program, index, size, type)
		
		return ret;
	}
	*/
	
	/**
	 * Builds up the HashMap containing the location of every uniform
	 */
	/*
	private void buildUniformMap(){
		if(uniformMap == null)
			uniformMap = new HashMap<String, Integer>();
		
		for(String uni : getUniformList())
			uniformMap.put(uni, this.getUniformLocation(uni));
	}
	*/
	
	/**
	 * @param name Attribute to get location of
	 * @return Handle for attribute
	 */
	public int getAttribLocation(String name){
		return Gdx.gl20.glGetAttribLocation(handle, name);
	}
	
	/**
	 * @return Number of active attributes for this program
	 */
	/*
	public int getNumAttribs(){
		return Gdx.gl20.glGetProgrami(handle, Gdx.gl20.GL_ACTIVE_ATTRIBUTES);
	}
	*/
	
	/**
	 * @return Length of the longest active attribute's name
	 */
	/*
	private int getMaxAttribNameLength(){
		return Gdx.gl20.glGetProgrami(handle, Gdx.gl20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
	}
	*/
	
	/**
	 * @return A list of every active attribute in the program
	 */
	/*
	public String[] getAttribList(){
		int numAttribs = getNumAttribs();
		int maxLen = getMaxAttribNameLength();
		String[] ret = new String[numAttribs];
		
		for(int i = 0; i < numAttribs; i++)
			ret[i] = Gdx.gl20.glGetActiveAttrib(handle, i, maxLen);
		
		return ret;
	}
	*/
	
	/**
	 * Builds up the HashMap containing the location of every attribute
	 */
	/*
	private void buildAttribMap(){
		if(attribMap == null)
			attribMap = new HashMap<String, Integer>();
		
		for(String attrib : getAttribList())
			attribMap.put(attrib, this.getAttribLocation(attrib));
	}
	*/
	
	/**
	 * Set a 2f uniform
	 * @param name Name of uniform to set
	 * @param x
	 * @param y
	 */
	public void setUniform(String name, float x, float y){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			Gdx.gl20.glUniform2f(loc, x, y);
		} else{
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
		}
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
			Gdx.gl20.glUniform3f(loc, x, y, z);
		} else{
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
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
			Gdx.gl20.glUniform4f(loc, x, y, z, w);
		} else{
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
		}
	}
	
	/**
	 * Set a uniform matrix from an array
	 * @param name Name of uniform to set
	 * @param m A 4x4 matrix (16 floats)
	 */
	public void setUniformMatrix4f(String name, Matrix4 m){
		int loc = getUniformLocation(name);
		mat4buff.clear();
		
		for(float f : m.getValues())
			mat4buff.put(f);
		
		mat4buff.rewind();
		if(loc >= 0){
			Gdx.gl20.glUniformMatrix4fv(loc, 1, false, mat4buff);
		} else {
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
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
			Gdx.gl20.glUniform1f(loc, val);
		} else{
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
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
			Gdx.gl20.glUniform1i(loc, val);
		} else{
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
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
			Gdx.gl20.glUniform1i(loc, val ? 1 : 0);
		} else{
			Gdx.app.error(LOGTAG, "Uniform variable " + name + " not found!");
		}
	}
}
