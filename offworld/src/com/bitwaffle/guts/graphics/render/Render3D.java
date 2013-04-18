package com.bitwaffle.guts.graphics.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities3d.Entity3D;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.glsl.GLSLShader;
import com.bitwaffle.guts.graphics.model.Material;
import com.bitwaffle.guts.graphics.render.camera.Camera3D;
import com.bitwaffle.guts.util.MathHelper;

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
	
	private Matrix4 oldModelview;
	
	private Matrix3 normal;
	
	private float[] tempMatrixArr;
	
	/** Draw distance and field-of-view to use for rendering */
	public static float drawDistance = 3000000.0f, fov =  45.0f;
	
	public Render3D(){
		initShaders();
		
		projection = new Matrix4();
		modelview = new Matrix4();
		oldModelview = new Matrix4();
		normal = new Matrix3();
		tempMatrixArr = new float[16];
		
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
		program.setUniform("Material.Kd", mat.kd()[0], mat.kd()[1], mat.kd()[2]);
		program.setUniform("Material.Ka", mat.ka()[0], mat.ka()[1], mat.ka()[2]);
		program.setUniform("Material.Ks", mat.ks()[0], mat.ks()[1], mat.ks()[2]);
		program.setUniform("Material.Shininess", mat.shininess());
	}

	/**
	 * Set the current material to the default material
	 */
	public void useDefaultMaterial(){
		program.setUniform("Material.Kd" , DEFAULT_KD[0], DEFAULT_KD[1], DEFAULT_KD[2]);
		program.setUniform("Material.Ka", DEFAULT_KA[0], DEFAULT_KA[1], DEFAULT_KA[2]);
		program.setUniform("Material.Ks", DEFAULT_KS[0], DEFAULT_KS[1], DEFAULT_KS[2]);
		program.setUniform("Material.Shininess", DEFAULT_SHINY);
	}
	
	public void setUp3DRender(){
		program.use();
		
		useDefaultMaterial();
		
		float aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		MathHelper.perspective(projection, fov, aspect, 1.0f, drawDistance);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
		modelview.idt();
	}
	
	/**
	 * Transforms the ModelView matrix to represent the camera's location and rotation
	 */
	private void translateModelviewToCamera(){
		modelview.idt();
		// translate to the camera's location
		//modelview.translate(new Vector3(Entities.camera.xOffset, Entities.camera.yOffset, -Entities.camera.zoom));
		
		modelview.translate(camera.location());

		// reverse the camera's quaternion (we want to look OUT from the camera)
		Quaternion reverse = camera.rotation().conjugate();
		//Matrix4f.mul(modelview, QuaternionHelper.toMatrix(reverse), modelview);
		reverse.toMatrix(tempMatrixArr);
		modelview = modelview.mul(new Matrix4(tempMatrixArr));
	}
	
	public void sendMatrixToShader(){
		normal.set(modelview);
		program.setUniform("ModelViewMatrix", modelview);
		program.setUniform("ProjectionMatrix", projection);
		program.setUniform("NormalMatrix", normal);
	}
	
	public void renderEntities(Iterator<Entity3D> it){
		translateModelviewToCamera();
		
		while(it.hasNext()){
			try{
				Entity3D ent = it.next();
				if(ent != null && ent.renderer != null){
					prepareToRenderEntity(ent);
					ent.renderer.render(this, ent);
				}
			} catch(ConcurrentModificationException e){
				break;
			}
		}
	}
	
	public void prepareToRenderEntity(Entity3D ent){
		float transX = camera.location().x - ent.location().x;
		float transY = camera.location().y - ent.location().y;
		float transZ = camera.location().z - ent.location().z;
		
		oldModelview.set(modelview);
		
		modelview.translate(transX, transY, transZ);
		ent.rotation().toMatrix(tempMatrixArr);
		modelview = modelview.mul(new Matrix4(tempMatrixArr));
		
		sendMatrixToShader();
		ent.renderer.render(this, ent);
		
		
		modelview.set(oldModelview);
	}
}