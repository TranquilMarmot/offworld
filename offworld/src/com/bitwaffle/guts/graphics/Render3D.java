package com.bitwaffle.guts.graphics;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.camera.Camera3D;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.glsl.GLSLShader;
import com.bitwaffle.guts.graphics.model.Material;

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
	public Matrix4 modelview, projection;
	
	private Matrix3 normal;
	
	public Render3D(){
		initShaders();
		
		projection = new Matrix4();
		modelview = new Matrix4();
		normal = new Matrix3();
		
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
		program.setUniform("Material.Kd", mat.kd()[0], mat.kd()[1], mat.kd()[2], mat.kd()[3]);
		program.setUniform("Material.Ka", mat.ka()[0], mat.ka()[1], mat.ka()[2], mat.ka()[3]);
		program.setUniform("Material.Ks", mat.ks()[0], mat.ks()[1], mat.ks()[2], mat.ks()[3]);
		program.setUniform("Material.Shininess", mat.shininess());
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
	
	public void setUp3DRender(){
		program.use();
		
		useDefaultMaterial();
		
		float aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		
		modelview.idt();
	}
	
	/**
	 * Transforms the ModelView matrix to represent the camera's location and rotation
	 */
	private void transformToCamera(){
		// translate to the camera's location
		//modelview.translate(new Vector3(Entities.camera.xOffset, Entities.camera.yOffset, -Entities.camera.zoom));
		
		modelview.translate(camera.location());

		// reverse the camera's quaternion (we want to look OUT from the camera)
		Quaternion reverse = camera.rotation().conjugate();
		//Matrix4f.mul(modelview, QuaternionHelper.toMatrix(reverse), modelview);
		float[] tmp = new float[16];
		reverse.toMatrix(tmp);
		Matrix4 tempMat = new Matrix4(tmp);
	}
	
	public void sendMatrixToShader(){
		normal.set(modelview);
		program.setUniform("ModelViewMatrix", modelview);
		program.setUniform("ProjectionMatrix", projection);
		program.setUniform("NormalMatrix", normal);
	}
}
