package com.bitwaffle.guts.graphics.render.glsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.opengl.GLES20;

/**
 * Convenience class for handling shaders
 * 
 * @author TranquilMarmot
 */
public class GLSLShader {
	/** All the different types of shaders */
	public enum ShaderTypes {
		VERTEX(GLES20.GL_VERTEX_SHADER),
		FRAGMENT(GLES20.GL_FRAGMENT_SHADER);

		private int glInt;
		private ShaderTypes(int glInt){ this.glInt = glInt; }
		public int getGLInt(){ return glInt; }
	}
	
	/** Handle for shader */
	private int handle;
	
	/** String that will contain any errors */
	private String logString;
	
	/**
	 * Create a new shader
	 * @param type Type of shader
	 * @see ShaderTypes
	 */
	public GLSLShader(ShaderTypes type){
		handle = GLES20.glCreateShader(type.getGLInt());
		
		if(handle == 0){
			System.out.println("Error creating shader handle!");
		}
	}
	
	/**
	 * Compiles a shader from an InputStream
	 * @param stream Stream to compile from (should be a plaintext file)
	 * @return Whether or not the shader compiled successfully
	 */
	public boolean compileShaderFromStream(InputStream stream){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 8);
			String source = "";
			String line;
			while ((line = reader.readLine()) != null) {
				source += line + "\n";
			}
			reader.close();
			stream.close();
			return compileShaderFromString(source);
		} catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Compile a shader from a String
	 * @param source String containing source code for shader
	 * @return Whether or not the shader compiled correctly
	 */
	public boolean compileShaderFromString(String source){
		GLES20.glShaderSource(handle, source);
		GLES20.glCompileShader(handle);
		
		logString = GLES20.glGetShaderInfoLog(handle);
		
		return logString.equals("") || logString.equals("Success.\n") || logString.contains("0 errors");
	}
	
	/**
	 * @return String containing any errors
	 */
	public String log() { return logString; }
	
	/**
	 * @return Handle for shader
	 */
	public int getHandle() { return handle; }
}
