package com.bitwaffle.offworld.moguts.graphics.glsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.opengl.GLES20;

public class GLSLShader {
	private int handle;
	private String logString;
	
	public GLSLShader(ShaderTypes type){
		handle = GLES20.glCreateShader(type.getGLInt());
		
		if(handle == 0){
			System.out.println("Error creating shader handle!");
		}
	}
	
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
	
	public boolean compileShaderFromString(String source){
		GLES20.glShaderSource(handle, source);
		GLES20.glCompileShader(handle);
		
		logString = GLES20.glGetShaderInfoLog(handle);
		
		return logString.equals("Success.\n");
	}
	
	public String log() { return logString; }
	public int getHandle() { return handle; }
}
