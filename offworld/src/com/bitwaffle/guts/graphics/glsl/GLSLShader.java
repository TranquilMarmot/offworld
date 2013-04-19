package com.bitwaffle.guts.graphics.glsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;

/**
 * Convenience class for handling shaders
 * 
 * @author TranquilMarmot
 */
public class GLSLShader {
	private static final String LOGTAG = "GLSLShader";
	
	/**
	 * OpenGL ES requires some extra definitions in the shader.
	 * This gets prepended to any shaders being sent to an OpenGL ES context.
	 */
	private static final String GLES_PREFIX = "#define mediump float\n\n";
	
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
	
	/** @see ShaderTypes */
	public GLSLShader(ShaderTypes type){
		handle = Gdx.gl20.glCreateShader(type.getGLInt());
		
		if(handle == 0)
			Gdx.app.error(LOGTAG, "Error creating shader handle!");
	}
	
	/**
	 * Compiles a shader from an InputStream
	 * @param stream Stream to compile from (should be a plaintext file)
	 */
	public boolean compileShaderFromStream(InputStream stream){
		// check if we're using OpenGL ES 
		String prefix = "";
		ApplicationType type = Gdx.app.getType();
		if(type == ApplicationType.Android || type == ApplicationType.iOS || type == ApplicationType.WebGL)
			prefix = GLES_PREFIX;
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 8);
			String source = prefix;
			String line;
			while ((line = reader.readLine()) != null)
				source += line + "\n";
			reader.close();
			stream.close();
			return compileShaderFromString(source);
		} catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/** Compile a shader from a String */
	public boolean compileShaderFromString(String source){
		Gdx.gl20.glShaderSource(handle, source);
		Gdx.gl20.glCompileShader(handle);
		
		logString = Gdx.gl20.glGetShaderInfoLog(handle);
		//int result = Gdx.gl20.glGetProgrami(handle, Gdx.gl20.GL_COMPILE_STATUS); // FIXME this returns 16 on success sometimes?! (GL_TRUE == 1)
		
		return /*result == Gdx.Gdx.gl20.GL_TRUE || result == 16 ||*/ logString.equals("") || logString.equals("Success.\n") || logString.contains("0 errors");
	}
	
	/** @return String containing any errors */
	public String log() { return logString; }
	
	/** @return Handle for shader */
	public int getHandle() { return handle; }
}
