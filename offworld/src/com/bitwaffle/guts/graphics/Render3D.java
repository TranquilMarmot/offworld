package com.bitwaffle.guts.graphics;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.camera.Camera3D;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.glsl.GLSLShader;

public class Render3D {
private static final String LOGTAG = "Render3D";
	
	/** Default shaders to use */
	private static final String
		VERTEX_SHADER = "shaders/3d.vert",
		FRAGMENT_SHADER = "shaders/3d.frag";
	
	/** Default materials */
	private static final float[]
			DEFAULT_KD = {0.5f, 0.5f, 0.5f},
			DEFAULT_KA = {0.5f, 0.5f, 0.5f},
			DEFAULT_KS = {0.8f, 0.8f, 0.8f};
	/** Default shininess of material */
	private static final float DEFAULT_SHINY = 50.0f;
	
	/** Whether or not to call every entity's debug drawing method */
	public static boolean drawDebug = false;
	
	/** Camera for describing how the scene should be looked at */
	public static Camera3D camera;
	
	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** The modelview and projection matrices*/
	public Matrix4 modelview, projection, mvp;
	
	public Render3D(){
		initShaders();
		
		projection = new Matrix4();
		modelview = new Matrix4();
		mvp = new Matrix4();
		
		camera = new Camera3D();
	}
	
	private void initShaders(){
		GLSLShader vert = new GLSLShader(GLSLShader.ShaderTypes.VERTEX);
		GLSLShader frag = new GLSLShader(GLSLShader.ShaderTypes.FRAGMENT);
		try {
			InputStream vertexStream = Game.resources.openAsset(VERTEX_SHADER);
			if (!vert.compileShaderFromStream(vertexStream))
				Gdx.app.error(LOGTAG, "Error compiling vertex shader! Result: "
						+ vert.log());
			vertexStream.close();
			
			InputStream fragmentStream = Game.resources.openAsset(FRAGMENT_SHADER);
			if (!frag.compileShaderFromStream(fragmentStream))
				Gdx.app.error(LOGTAG, "Error compiling fragment shader! Result: "
						+ frag.log());
			fragmentStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		program = new GLSLProgram();
		program.addShader(vert);
		program.addShader(frag);
		if (!program.link())
			Gdx.app.error(LOGTAG, "Error linking program!\n" + program.log());
	}
	
	/**
	 * Sets the current material being used for rendering
	 * @param mat Material to use
	 */
	public void setCurrentMaterial(Material mat){
		program.setUniform("Material.Kd" , mat.getKd());
		program.setUniform("Material.Ka", mat.getKa());
		program.setUniform("Material.Ks", mat.getKs());
		program.setUniform("Material.Shininess", mat.getShininess());
	}

	/**
	 * Set the current material to the default material
	 */
	public void useDefaultMaterial(){
		program.setUniform("Material.Kd" , DEFAULT_KD[0], DEFAULT_KD[1], DEFAULT_KD[2], DEFAULT_KD[3]);
		program.setUniform("Material.Ka", DEFAULT_KA[0], DEFAULT_KA[1], DEFAULT_KA[2], DEFAULT_KA[3]);
		program.setUniform("Material.Ks", DEFAULT_KS[0], DEFAULT_KS[1], DEFAULT_KS[2], DEFAULT_KS[3]);
		program.setUniform("Material.Shininess", DEFAULT_SHINY);
	}
}
