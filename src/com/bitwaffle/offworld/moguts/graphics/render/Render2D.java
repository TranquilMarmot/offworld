package com.bitwaffle.offworld.moguts.graphics.render;

import java.io.IOException;
import java.util.Iterator;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.offworld.moguts.entity.Entities;
import com.bitwaffle.offworld.moguts.entity.Entity;
import com.bitwaffle.offworld.moguts.graphics.Camera;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.offworld.moguts.graphics.glsl.GLSLShader;
import com.bitwaffle.offworld.moguts.graphics.glsl.ShaderTypes;
import com.bitwaffle.offworld.moguts.physics.Physics;

public class Render2D {
	private static final String VERTEX_SHADER = "game/shaders/main.vert";
	private static final String FRAGMENT_SHADER = "game/shaders/main.frag";

	public GLSLProgram program;
	
	public Camera camera;
	//private 
	
	float[] modelview, projection;

	@SuppressWarnings("unused")
	private Context context;
	private AssetManager assets;

	public Render2D(Context context) {
		this.context = context;
		assets = context.getAssets();
		initShaders();
		
		projection = new float[16];
		modelview = new float[16];
		
		camera = new Camera(new Vector2(10.0f, 5.0f), 0.05f);
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
	
	float camX = 10.0f, camY = 5.0f, camZ = 0.0f;

	public void renderScene() {
		program.use();
		
		setUpProjectionMatrix();
		
		renderEntities(Physics.entities);
	}
	
	private void setUpProjectionMatrix(){
		Matrix.setIdentityM(projection, 0);
		Matrix.orthoM(projection, 0, 0, GLRenderer.aspect, 0, 1, -1, 1);
		Matrix.scaleM(projection, 0, camera.getZoom(), camera.getZoom(), 1.0f);
		
		program.setUniformMatrix4f("Projection", projection);
	}
	
	private void renderEntities(Entities entities){
		Vector2 cam = camera.getLocation();
		
		Iterator<Entity> it = Physics.entities.getIterator();
		while(it.hasNext()){
			Entity ent = it.next();
			
			Vector2 loc = ent.getLocation();
			float angle = (float)Math.toDegrees((double)ent.getAngle());
			
			Matrix.setIdentityM(modelview, 0);
			Matrix.translateM(modelview, 0, loc.x + cam.x, loc.y + cam.y, 0.0f);
			Matrix.rotateM(modelview, 0, angle, 0.0f, 0.0f, 1.0f);
			
			program.setUniformMatrix4f("ModelView", modelview);
			
			ent.render();
		}
	}
	
	public static void printMatrix(float[] mat){
		System.out.printf("%f %f %f %f\n%f %f %f %f\n %f %f %f %f\n %f %f %f %f\n",
				mat[0], mat[1], mat[2], mat[3], mat[4], mat[5], mat[6], mat[7], mat[8], 
				mat[9], mat[10], mat[11], mat[12], mat[13], mat[14], mat[15]);
	}
}
