package com.bitwaffle.guts.graphics.render.render3d;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity;
import com.bitwaffle.guts.entities.entities2d.EntityRenderer3D;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.shapes.model.Material;
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
	public boolean drawDebug = false;
	
	/** Camera for describing how the scene should be looked at */
	public Camera3D camera;
	
	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** The modelview and projection matrices*/
	public Matrix4 modelview, projection;
	
	private Matrix3 normal;
	
	private float[] tempMatrixArr;
	
	/** Draw distance and field-of-view to use for rendering */
	public static float drawDistance = 1000.0f;
	
	/** List of lights */
	private ArrayList<Light> lights;
	
	public Render3D(){
		program = GLSLProgram.getProgram(VERTEX_SHADER, FRAGMENT_SHADER, LOGTAG);
		
		projection = new Matrix4();
		modelview = new Matrix4();
		normal = new Matrix3();
		tempMatrixArr = new float[16];
		
		lights = new ArrayList<Light>();
		Vector3 lightLoc = new Vector3(0.0f, 10.0f, -10.0f);
		Vector3 lightIntensity = new Vector3(0.9f, 0.9f, 0.9f);
		lights.add(new Light(lightLoc, lightIntensity));
		
		camera = new Camera3D();
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
	
	public void switchTo3DRender(){
		program.use();
		
		useDefaultMaterial();
		setUpLights();
		
		//MathHelper.perspective(projection, fov, aspect, 1.0f, drawDistance);
		MathHelper.orthoM(projection, 0, Game.aspect, 0, 1, -1, drawDistance);
		
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDisable(GL20.GL_BLEND); 
	}
	
	/**
	 * Transforms the ModelView matrix to represent the camera's location and rotation
	 */
	private void translateModelviewToCamera(){
		modelview.idt();
		
		float zoom = Game.renderer.render2D.camera.getZoom();
		modelview = modelview.scale(zoom, zoom, zoom);
		
		modelview = modelview.translate(camera.getLocation());

		// reverse the camera's quaternion (we want to look OUT from the camera)
		Quaternion reverse = camera.getRotation().conjugate();
		reverse.toMatrix(tempMatrixArr);
		modelview = modelview.mul(new Matrix4(tempMatrixArr));
	}
	
	public void sendMatrixToShader(){
		normal.set(modelview);
		program.setUniform("ModelViewMatrix", modelview);
		program.setUniform("ProjectionMatrix", projection);
		program.setUniform("NormalMatrix", normal);
	}
	
	public void prepareToRenderEntity(Entity ent){
		Vector2 entLoc = ent.getLocation();
		
		EntityRenderer3D rend = (EntityRenderer3D) ent.renderer;
		
		modelview.idt();
		translateModelviewToCamera();
		
		modelview.translate(new Vector3(entLoc.x, entLoc.y, rend.z));
		rend.rotation.toMatrix(tempMatrixArr);
		modelview = modelview.mul(new Matrix4(tempMatrixArr));
		
		sendMatrixToShader();
	}
	
	
	/**
	 * Sets up lights for rendering
	 */
	private void setUpLights(){
		// FIXME only one light supported right now!
		if(lights.size() > 1)
			System.out.println("More than one light! Multiple lighting not yet implemented.");
		Light l = lights.iterator().next();
		
		// crazy quaternion and vector math to get the light into world coordinates
		Quaternion reverse = new Quaternion(camera.getRotation()).conjugate();
		Vector3 rotated = MathHelper.rotateVectorByQuaternion(l.location(), reverse);
		
		// set uniforms
		program.setUniform("Light.LightPosition", rotated.x, rotated.y, rotated.z, 0.0f);
		program.setUniform("Light.LightIntensity", l.intensity().x, l.intensity().y, l.intensity().z);
		program.setUniform("Light.LightEnabled", true);
	}
}
