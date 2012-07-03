package com.bitwaffle.offworld.moguts.graphics.render;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.Matrix;

import com.bitwaffle.offworld.Triangle;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLShader;
import com.bitwaffle.offworld.moguts.graphics.glsl.ShaderTypes;

public class Render2D {
	private static final String VERTEX_SHADER = "game/shaders/main.vert";
	private static final String FRAGMENT_SHADER = "game/shaders/main.frag";

	public GLSLProgram program;
	
	float[] mv, proj;

	public static float fov = 45.0f;
	public static float drawDistance = 1000.0f;

	@SuppressWarnings("unused")
	private Context context;
	private AssetManager assets;

	// FIXME temp
	private Triangle triangle;
	
	private float oldAspect;

	public Render2D(Context context) {
		this.context = context;
		assets = context.getAssets();
		initShaders();

		oldAspect = (float) GLRenderer.windowWidth
				/ (float) GLRenderer.windowHeight;

		//projection = MatrixHelper.perspective(fov, oldAspect, 1.0f, drawDistance);
		//projection = MatrixHelper.frustum(-oldAspect, oldAspect, -1.0f, 1.0f, 3.0f, 7.0f);
		//modelview = new Matrix4f();
		
		proj = new float[16];
		mv = new float[16];
		Matrix.frustumM(proj, 0, -oldAspect, oldAspect, -1, 1, 3, 7);
		Matrix.setLookAtM(mv, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}

	private void initShaders() {
		GLSLShader vert = new GLSLShader(ShaderTypes.VERTEX);
		GLSLShader frag = new GLSLShader(ShaderTypes.FRAGMENT);
		try {
			if (!vert.compileShaderFromStream(assets.open(VERTEX_SHADER)))
				System.err.println("Error compiling vertex shader! Result: "
						+ vert.log());
			if (!frag.compileShaderFromStream(assets.open(FRAGMENT_SHADER)))
				System.err.println("Error compiling vertex shader! Result: "
						+ frag.log());
		} catch (IOException e) {
			e.printStackTrace();
		}
		program = new GLSLProgram();
		program.addShader(vert);
		program.addShader(frag);
		if (!program.link())
			System.err.println("Error linking program! " + program.log());
	}

	public int getProgramHandle() {
		return program.getHandle();
	}

	public void renderScene() {
		float aspect = (float) GLRenderer.windowWidth
				/ (float) GLRenderer.windowHeight;
		
		if(aspect != oldAspect){
			oldAspect = aspect;
			 Matrix.frustumM(proj, 0, -aspect, aspect, -1, 1, 3, 7);
		}
		
		Matrix.rotateM(mv, 0, 2, 1.0f, 0.0f, 0.0f);
		
		program.setUniformMatrix4f("ModelView", mv);
		program.setUniformMatrix4f("Projection", proj);
		
		// FIXME temp
		if(triangle == null)
			triangle = new Triangle();
		program.use();
		triangle.draw();
	}
}
