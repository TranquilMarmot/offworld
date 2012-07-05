package com.bitwaffle.offworld.moguts.graphics.render;

import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.Matrix;

import com.bitwaffle.offworld.Quad;
import com.bitwaffle.offworld.Triangle;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLShader;
import com.bitwaffle.offworld.moguts.graphics.glsl.ShaderTypes;

public class Render2D {
	private static final String VERTEX_SHADER = "game/shaders/main.vert";
	private static final String FRAGMENT_SHADER = "game/shaders/main.frag";

	public GLSLProgram program;
	//private 
	
	float[] modelview, projection;

	public static float fov = 45.0f;
	public static float drawDistance = 1000.0f;

	@SuppressWarnings("unused")
	private Context context;
	private AssetManager assets;

	// FIXME temp
	//private Triangle triangle;
	private Quad quad;
	
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
		
		projection = new float[16];
		modelview = new float[16];
		Matrix.frustumM(projection, 0, -oldAspect, oldAspect, -1, 1, 3, 7);
		Matrix.setLookAtM(modelview, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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
	
	/*
	public int getVertexPositionHandle(){
		
	}
	
	public int getColorUniformHandle(){
		
	}
	*/
	
	float ang1 = 0.0f, ang2 = 0.0f, ang3 = 0.0f, ang4 = 0.0f;

	public void renderScene() {
		float aspect = (float) GLRenderer.windowWidth
				/ (float) GLRenderer.windowHeight;
		
		if(aspect != oldAspect){
			oldAspect = aspect;
			 Matrix.frustumM(projection, 0, -aspect, aspect, -1, 1, 3, 7);
		}
		
		Matrix.rotateM(modelview, 0, 2, 1.0f, 0.0f, 0.0f);
		
		program.setUniformMatrix4f("ModelView", modelview);
		program.setUniformMatrix4f("Projection", projection);
		
		// FIXME temp
		//if(triangle == null)
		//	triangle = new Triangle();
		if(quad == null)
			quad = new Quad();
		
		program.use();
		
		Random r = new Random();
		
		float[] oldmv = modelview.clone();
		Matrix.rotateM(modelview, 0, ang1, r.nextFloat(), r.nextFloat(), r.nextFloat());
		ang1++;
		program.setUniformMatrix4f("ModelView", modelview);
		quad.draw();
		
		modelview = oldmv.clone();
		Matrix.rotateM(modelview, 0, ang2, r.nextFloat(), r.nextFloat(), r.nextFloat());
		ang2++;
		program.setUniformMatrix4f("ModelView", modelview);
		quad.draw();
		
		modelview = oldmv.clone();
		Matrix.rotateM(modelview, 0, ang3, r.nextFloat(), r.nextFloat(), r.nextFloat());
		ang3--;
		program.setUniformMatrix4f("ModelView", modelview);
		quad.draw();
		
		modelview = oldmv.clone();
		Matrix.rotateM(modelview, 0, ang4, r.nextFloat(), r.nextFloat(), r.nextFloat());
		ang4--;
		program.setUniformMatrix4f("ModelView", modelview);
		quad.draw();
		
		modelview = oldmv.clone();
		
		//triangle.draw();
		
	}
}
