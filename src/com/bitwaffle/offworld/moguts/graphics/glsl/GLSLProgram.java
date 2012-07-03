package com.bitwaffle.offworld.moguts.graphics.glsl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import android.opengl.GLES20;

public class GLSLProgram {
	private int handle;
	private boolean linked;
	private String logString;
	
	private FloatBuffer matrix4fBuffer, matrix3fBuffer;
	
	public GLSLProgram(){
		handle = GLES20.glCreateProgram();
		
		if(handle == 0)
			System.out.println("Error creating shader program!");
		
		linked = false;
		
		matrix4fBuffer = ByteBuffer.allocateDirect(16 * 4).asFloatBuffer();
		matrix3fBuffer = ByteBuffer.allocateDirect(9 * 4).asFloatBuffer();
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
	
	public void bindAttribLocation(int location, String name){
		GLES20.glBindAttribLocation(handle, location, name);
	}
	
	
	public void setUniform(String name, float x, float y, float z){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform3f(loc, x, y, z);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	public void setUniform(String name, Vector3f v){
		this.setUniform(name, v.x, v.y, v.z);
		
	}
	
	public void setUniform(String name, float x, float y, float z, float w){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform4f(loc, x, y, z, w);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	public void setUniform(String name, Vector4f v){
		this.setUniform(name, v.x, v.y, v.z, v.w);
	}
	
	public void setUniform(String name, Matrix4f m){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			matrix4fBuffer.clear();
			m.store(matrix4fBuffer);
			matrix4fBuffer.rewind();
			GLES20.glUniformMatrix4fv(loc, 1, false, matrix4fBuffer);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	public void setUniform(String name, Matrix3f m){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			matrix3fBuffer.clear();
			m.store(matrix3fBuffer);
			matrix3fBuffer.rewind();
			GLES20.glUniformMatrix3fv(loc, 1, false, matrix3fBuffer);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	public void setUniformMatrix4f(String name, float[] m){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniformMatrix4fv(loc, 1, false, m, 0);
		} else {
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	public void setUniform(String name, float val){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform1f(loc, val);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	public void setUniform(String name, int val){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform1i(loc, val);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	public void setUniform(String name, boolean val){
		int loc = getUniformLocation(name);
		if(loc >= 0){
			GLES20.glUniform1i(loc, val ? 1 : 0);
		} else{
			System.out.println("Uniform variable " + name + " not found!");
		}
	}
	
	/*
	public void printActiveUniforms(){
		int nUniforms, location, maxLen;
		String name;
		
		maxLen = GLES20.glGetProgram(handle, GLES20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
		nUniforms = GLES20.glGetProgram(handle, GLES20.GL_ACTIVE_UNIFORMS);
		
		System.out.println("\n Active Uniforms");
		System.out.println("------------------------------------------------");
		for(int i = 0; i < nUniforms; i++){
			name = GLES20.glGetActiveUniform(handle, i, maxLen);
			location = GLES20.glGetUniformLocation(handle, name);
			System.out.println("  " + location + " | " + name);
		}
	}
	
	public void printActiveAttribs(){
		int location, maxLength, nAttribs;
		String name;
		
		maxLength = GL20.glGetProgram(handle, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
		nAttribs = GL20.glGetProgram(handle, GL20.GL_ACTIVE_ATTRIBUTES);
		
		System.out.println("\n Active Atributes");
		System.out.println("------------------------------------------------");
		for(int i = 0; i < nAttribs; i++){
			name = GL20.glGetActiveAttrib(handle, i, maxLength);
			location = GL20.glGetAttribLocation(handle, name);
			System.out.println(" " + location + " | " + name);
		}
	}
	*/
	
	public int getUniformLocation(String name){
		return GLES20.glGetUniformLocation(handle, name);
	}
}
