package com.bitwaffle.guts.graphics.glsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Convenience class for handling shaders
 * 
 * @author TranquilMarmot
 */
public class GLSLShader {
	private static final String LOGTAG = "GLSLShader";
	
	/** All the different types of shaders */
	public enum ShaderTypes {
		VERTEX(GL20.GL_VERTEX_SHADER),
		FRAGMENT(GL20.GL_FRAGMENT_SHADER);

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
		handle = Gdx.gl20.glCreateShader(type.getGLInt());
		
		if(handle == 0){
			Gdx.app.error(LOGTAG, "Error creating shader handle!");
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
		Gdx.gl20.glShaderSource(handle, source);
		Gdx.gl20.glCompileShader(handle);
		
		logString = Gdx.gl20.glGetShaderInfoLog(handle);
		//int result = Gdx.gl20.glGetProgrami(handle, Gdx.gl20.GL_COMPILE_STATUS); // FIXME this returns 16 on success sometimes?! (GL_TRUE == 1)
		
		return /*result == Gdx.Gdx.gl20.GL_TRUE || result == 16 ||*/ logString.equals("") || logString.equals("Success.\n") || logString.contains("0 errors");
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
