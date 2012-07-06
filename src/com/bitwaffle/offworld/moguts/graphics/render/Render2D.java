package com.bitwaffle.offworld.moguts.graphics.render;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.offworld.moguts.entity.Entity;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLShader;
import com.bitwaffle.offworld.moguts.graphics.glsl.ShaderTypes;
import com.bitwaffle.offworld.moguts.physics.Physics;

public class Render2D {
	private static final String VERTEX_SHADER = "game/shaders/main.vert";
	private static final String FRAGMENT_SHADER = "game/shaders/main.frag";

	public GLSLProgram program;
	//private 
	
	float[] modelview, projection, oldModelview;

	public static float fov = 45.0f;
	public static float drawDistance = 1000.0f;

	@SuppressWarnings("unused")
	private Context context;
	private AssetManager assets;
	
	private float oldAspect;

	public Render2D(Context context) {
		this.context = context;
		assets = context.getAssets();
		initShaders();

		oldAspect = GLRenderer.aspect;
		
		projection = new float[16];
		modelview = new float[16];
		oldModelview = new float[16];
		//Matrix.frustumM(projection, 0, -oldAspect, oldAspect, -1, 1, 3, 7);
		//Matrix.setLookAtM(modelview, 0, 0, 0, -4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		Matrix.setIdentityM(projection, 0);
		Matrix.setIdentityM(modelview, 0);
		program.setUniformMatrix4f("Projection", projection);
		program.setUniformMatrix4f("ModelView", modelview);
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
	
	float camZ = -1.0f;

	public void renderScene() {
		//GLRenderer.render2D.program.setUniform("vColor", 0.0f, 1.0f, 0.0f, 1.0f);
		
		if(oldAspect != GLRenderer.aspect){
			oldAspect = GLRenderer.aspect;
			Matrix.frustumM(projection, 0, -oldAspect, oldAspect, -1, 1, 3, 7);
			//Matrix.frustumM(projection, 0, -oldAspect, oldAspect, -1, 1, 1, 2);
		}
		
		program.setUniformMatrix4f("Projection", projection);
		program.setUniformMatrix4f("ModelView", modelview);
		
		
		program.use();
		
		Random r = new Random();
		Iterator<Entity> it = Physics.entities.getIterator();
		while(it.hasNext()){
			GLRenderer.render2D.program.setUniform("vColor", r.nextFloat(), r.nextFloat(), r.nextFloat(), 1.0f);
			
			oldModelview = modelview.clone();
			
			Entity ent = it.next();
			
			Vector2 loc = ent.getLocation();
			
			
			Matrix.translateM(modelview, 0, loc.x, loc.y, camZ);
			
			program.setUniformMatrix4f("ModelView", modelview);
			
			ent.render();
			
			modelview = oldModelview.clone();
		}
		
		//camZ -= 0.05;
	}
	
	public static void printMatrix(float[] mat){
		System.out.printf("%f %f %f %f\n%f %f %f %f\n %f %f %f %f\n %f %f %f %f\n",
				mat[0], mat[1], mat[2], mat[3], mat[4], mat[5], mat[6], mat[7], mat[8], 
				mat[9], mat[10], mat[11], mat[12], mat[13], mat[14], mat[15]);
	}
}
