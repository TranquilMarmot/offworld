package com.bitwaffle.guts.graphics.render.glsl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUIObject;
import com.bitwaffle.guts.util.BufferUtils;

public class GLSLSandbox extends GUIObject {
	public static final String LOGTAG = "GLSLSandbox";
	
	/** Vertex shader to load on init */
	private static final String VERTEX_SHADER = "shaders/sandbox.vert";
	/** Fragment shader to load on init */
	private static final String FRAGMENT_SHADER = "shaders/waves.frag";
	
	private static final int COORDS_PER_VERTEX = 3;
	
	/** Buffers to hold data */
	private FloatBuffer vertBuffer;
	
	private GLSLProgram program;
	
	private float previousTime, time;
	
	private int texture, positionHandle;
	
	public GLSLSandbox() {
		super(0.0f, 0.0f);
		previousTime = (float)Game.getTime();
		initShaders();
		createTarget(Game.windowWidth, Game.windowHeight);
	}

	@Override
	public void update() {
		float currentTime = (float)Game.getTime();
		
		time += currentTime - previousTime;
		previousTime = currentTime;
		
		//if(time >= 3141.5f)
		//	time -= 3141.5f;
	}

	@Override
	public void render(Render2D renderer, boolean flipHorizontal,
			boolean flipVertical) {
		program.use();
		program.setUniform("time", time / 1000.0f);
		program.setUniform("resolution", Game.windowWidth, Game.windowHeight);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertBuffer);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
		
		 GLES20.glDisableVertexAttribArray(positionHandle);
		
		renderer.program.use();
		renderer.sendModelViewToShader();
	}

	@Override
	public void cleanup() {}
	
	/**
	 * Initializes the vertex and fragment shaders and then links them to the program
	 */
	private void initShaders() {
		GLSLShader vert = new GLSLShader(GLSLShader.ShaderTypes.VERTEX);
		GLSLShader frag = new GLSLShader(GLSLShader.ShaderTypes.FRAGMENT);
		try {
			InputStream vertexStream = Game.resources.openAsset(VERTEX_SHADER);
			if (!vert.compileShaderFromStream(vertexStream))
				Log.e(LOGTAG, "Error compiling vertex shader! Result: "
						+ vert.log());
			vertexStream.close();
			
			InputStream fragmentStream = Game.resources.openAsset(FRAGMENT_SHADER);
			if (!frag.compileShaderFromStream(fragmentStream))
				Log.e(LOGTAG, "Error compiling fragment shader! Result: "
						+ frag.log());
			fragmentStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		program = new GLSLProgram();
		program.addShader(vert);
		program.addShader(frag);
		if (!program.link())
			Log.e(LOGTAG, "Error linking program!\n" + program.log());
	}
	
	private void createTarget(float width, float height){
		int[] texturebuffers = new int[1];
		GLES20.glGenTextures(1, texturebuffers, 0);
		texture = texturebuffers[0];
		
		float[] coords = {
				-1.0f * width, -1.0f * height, 0.0f,
				1.0f * width, -1.0f * height, 0.0f,
				1.0f * width, 1.0f * height, 0.0f,
				
				1.0f * width, 1.0f * height, 0.0f,
				-1.0f * width, 1.0f * height, 0.0f,
				-1.0f * width, -1.0f * height, 0.0f
		};
		
		
		
		
		vertBuffer = BufferUtils.getFloatBuffer(coords.length);
		vertBuffer.put(coords);
		vertBuffer.rewind();
		
		positionHandle = program.getAttribLocation("position");
		
		// set position info
		GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertBuffer);

		// set up texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, (int)width, (int)height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
		
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

		// clean up
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

}
