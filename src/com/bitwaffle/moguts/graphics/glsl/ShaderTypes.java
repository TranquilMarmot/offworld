package com.bitwaffle.moguts.graphics.glsl;

import android.opengl.GLES20;

public enum ShaderTypes {
	VERTEX(GLES20.GL_VERTEX_SHADER),
	FRAGMENT(GLES20.GL_FRAGMENT_SHADER);

	private int glInt;
	private ShaderTypes(int glInt){
		this.glInt = glInt;
	}
	
	public int getGLInt(){
		return glInt;
	}
}
