package com.bitwaffle.guts.entities.passive;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities2d.Entity2DRenderer;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.glsl.GLSLShader;
import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * This is a port of a Javascript/HTML5/WebGL sandbox that can be found
 * at http://glsl.heroku.com/
 * 
 * All of the code involved in porting was in the file
 * https://github.com/mrdoob/glsl-sandbox/blob/master/static/index.html
 * 
 * @author TranquilMarmot
 */
public class GLSLSandbox extends Entity2D {
	public static final String LOGTAG = "GLSLSandbox";
	
	/** Vertex shader to load on init */
	private static final String VERTEX_SHADER = "shaders/sandbox.vert";
	/** Fragment shader to load on init */
	private static final String DEFAULT_FRAG_SHADER = "shaders/sandbox/pulseblob.frag";
	
	/** What does all the rendering */
	private SandboxRenderer renderer;
	
	/** Create a new GLSL sandbox, with the default fragment shader */
	public GLSLSandbox() {
		this(DEFAULT_FRAG_SHADER);
	}
	
	/**
	 * Create a new GLSL sandbox with a given shader
	 * @param fragShader Location of shader file
	 */
	public GLSLSandbox(String fragShader){
		super(null, 2);
		renderer = new SandboxRenderer(fragShader);
		super.renderer = renderer;
	}
	
	@Override
	public void update(float timeStep) {
		renderer.update(timeStep);
	}
	
	@Override
	public void cleanup() {}
	
	private class SandboxRenderer implements Entity2DRenderer{
		/** Number of coordinates per vertex */
		private static final int COORDS_PER_VERTEX = 3;
		
		/** Six indices because we've got 2 triangles */
		private static final int NUM_INDICES = 6;
		
		/** Buffers to hold position data */
		private Buffer posBuffer;
		
		/** The shader program to use */
		private GLSLProgram program;
		
		/** Time accumulator */
		private float time;
		
		/** OpenGL handles */
		private int positionHandle;
		
		/**
		 * @param fragShader Location of fragment shader to load
		 */
		public SandboxRenderer(String fragShader){
			initShaders(fragShader);
			initQuad();
			time = 0.0f;
		}
		
		@Override
		public void render(Render2D renderer, Entity2D ent, boolean renderDebug) {
			// use program and set uniforms
			program.use();
			program.setUniform("time", time);
			program.setUniform("resolution", Game.windowWidth, Game.windowHeight);

			// bind quad
			Gdx.gl20.glEnableVertexAttribArray(positionHandle);
			Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, posBuffer);
			
			// draw quad
			Gdx.gl20.glDrawArrays(GL20.GL_TRIANGLES, 0, NUM_INDICES);
			
			// un-bind quad
			Gdx.gl20.glDisableVertexAttribArray(positionHandle);
			
			// go back to normal renderer program
			renderer.program.use();
		}
		
		/**
		 * @param timeStep Time since last update, in seconds
		 */
		public void update(float timeStep){
			time += timeStep;
		}
		
		/** Initializes the vertex and fragment shaders and then links them to the program */
		private void initShaders(String fragShader) {
			GLSLShader vert = new GLSLShader(GLSLShader.ShaderTypes.VERTEX);
			GLSLShader frag = new GLSLShader(GLSLShader.ShaderTypes.FRAGMENT);
			try {
				// vertex shader
				InputStream vertexStream = Game.resources.openAsset(VERTEX_SHADER);
				if (!vert.compileShaderFromStream(vertexStream))
					Gdx.app.error(LOGTAG, "Error compiling vertex shader! Result: " + vert.log());
				vertexStream.close();
				
				// fragment shader
				InputStream fragmentStream = Game.resources.openAsset(fragShader);
				if (!frag.compileShaderFromStream(fragmentStream))
					Gdx.app.error(LOGTAG, "Error compiling fragment shader! Result: " + frag.log());
				fragmentStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// create program, add shaders and link
			program = new GLSLProgram();
			program.addShader(vert);
			program.addShader(frag);
			if (!program.link())
				Gdx.app.error(LOGTAG, "Error linking program!\n" + program.log());
		}
		
		/** Initializes the Quad used for rendering */
		private void initQuad(){
			positionHandle = program.getAttribLocation("position");
			
			float[] coords = {
					-1.0f, -1.0f, 0.0f,
					1.0f, -1.0f, 0.0f,
					1.0f, 1.0f, 0.0f,
					
					1.0f, 1.0f, 0.0f,
					-1.0f, 1.0f, 0.0f,
					-1.0f, -1.0f, 0.0f
			};
			posBuffer = BufferUtils.newByteBuffer(coords.length * 4);
			BufferUtils.copy(coords, posBuffer, coords.length, 0);
			posBuffer.rewind();
		}
	}
}
