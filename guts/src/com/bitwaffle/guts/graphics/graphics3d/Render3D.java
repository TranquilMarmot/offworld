package com.bitwaffle.guts.graphics.graphics3d;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.graphics3d.model.Material;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Handles rendering things in THREEEE DIMMEEEENSIONS!
 * 
 * @author TranquilMarmot
 */
public class Render3D {
	private static final String LOGTAG = "Render3D";
	
	/** Shader files to use */
	private static final String
		VERTEX_SHADER = "shaders/3d.vert",
		FRAGMENT_SHADER = "shaders/3d.frag";
	
	/** Names of attributes in shaders */
	private static final String
		POSITION_ATTRIB = "VertexPosition",
		TEXCOORD_ATTRIB = "VertexTexCoord",
		NORMAL_ATTRIB = "VertexNormal";
	
	/** Default materials */
	private static final float[]
			DEFAULT_KD = {0.5f, 0.5f, 0.5f},
			DEFAULT_KA = {0.5f, 0.5f, 0.5f},
			DEFAULT_KS = {0.8f, 0.8f, 0.8f};
	/** Default shininess of material */
	private static final float DEFAULT_SHINY = 50.0f;
	
	/** Renderer owning this 3D renderer */
	private Renderer renderer;
	
	/** Camera for describing how the scene should be looked at */
	public Camera3D camera;
	
	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** Matrix for normal calculations (same as modelview, without rotations) */
	private Matrix3 normal;
	
	/** Temporary array for storing matrix data */
	private float[] tempMatrixArr;
	
	/** Draw distance and field-of-view to use for rendering */
	public static float drawDistance = 1000.0f;
	
	/** List of lights */
	private ArrayList<Light> lights;
	
	public Render3D(Renderer renderer){
		this.renderer = renderer;
		
		program = GLSLProgram.getProgram(VERTEX_SHADER, FRAGMENT_SHADER, LOGTAG);
		
		normal = new Matrix3();
		tempMatrixArr = new float[16];
		
		lights = new ArrayList<Light>();
		Vector3 lightLoc = new Vector3(0.0f, 0.0f, 10.0f);
		Vector3 lightIntensity = new Vector3(0.9f, 0.9f, 0.9f);
		lights.add(new Light(lightLoc, lightIntensity));
		
		camera = new Camera3D();
	}
	
	public int getVertexPositionHandle(){ return program.getAttribLocation(POSITION_ATTRIB); }
	
	public int getVertexNormalHandle(){ return program.getAttribLocation(NORMAL_ATTRIB); }
	
	public int getTexCoordHandle(){ return program.getAttribLocation(TEXCOORD_ATTRIB); }
	
	/** Sets the current material being used for rendering */
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
		sendLightsToShader();
		
		//MathHelper.perspective(projection, fov, aspect, 1.0f, drawDistance);
		MathHelper.orthoM(renderer.projection, 0, Game.aspect, 0, 1, -1, drawDistance);
		
		Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl20.glDisable(GL20.GL_BLEND); 
	}
	
	/**
	 * Transforms the ModelView matrix to represent the camera's location and rotation
	 */
	private void translateModelviewToCamera(){
		renderer.modelview.idt();
		
		float zoom = Game.renderer.r2D.camera.getZoom();
		renderer.modelview = renderer.modelview.scale(zoom, zoom, zoom);
		
		renderer.modelview = renderer.modelview.translate(camera.getLocation());

		// reverse the camera's quaternion (we want to look OUT from the camera)
		Quaternion reverse = camera.getRotation().conjugate();
		reverse.toMatrix(tempMatrixArr);
		renderer.modelview = renderer.modelview.mul(new Matrix4(tempMatrixArr));
	}
	
	/** Sends the matrices to the shader */
	public void sendMatrixToShader(){
		normal.set(renderer.modelview);
		program.setUniform("ModelViewMatrix", renderer.modelview);
		program.setUniform("ProjectionMatrix", renderer.projection);
		program.setUniform("NormalMatrix", normal);
	}
	
	/** 
	 * Translates and rotates the camera to be at the given entity.
	 * Entity's renderer MUST extend EntityRenderer3D!!
	 */
	public void prepareToRenderEntity(Entity ent){
		Vector2 entLoc = ent.getLocation();
		float entAngle = ent.getAngle();
		
		EntityRenderer3D rend = (EntityRenderer3D) ent.renderer;
		
		renderer.modelview.idt();
		translateModelviewToCamera();
		
		// translate to entity with z value from renderer
		renderer.modelview.translate(new Vector3(entLoc.x, entLoc.y, rend.z));
		
		// rotate to match entity's angle
		renderer.modelview.rotate(0.0f, 0.0f, 1.0f, MathHelper.toDegrees(entAngle));
		
		// add any rotation from renderer
		renderer.modelview = renderer.modelview.mul(rend.view);
		
		sendMatrixToShader();
	}
	
	public void setLight(int light, Light l){
		lights.set(light, l);
	}
	
	
	/** Sets up lights for rendering */
	private void sendLightsToShader(){
		// FIXME only one light supported right now!
		if(lights.size() > 1)
			System.out.println("More than one light! Multiple lighting not yet implemented.");
		Light l = lights.iterator().next();
		
		// crazy quaternion and vector math to get the light into world coordinates
		//Quaternion reverse = new Quaternion(camera.getRotation()).conjugate();
		//Vector3 rotated = MathHelper.rotateVectorByQuaternion(l.location(), reverse);
		
		// set uniforms
		//program.setUniform("Light.LightPosition", rotated.x, rotated.y, rotated.z, 0.0f);
		program.setUniform("Light.LightPosition", l.location().x, l.location().y, l.location().z, 0.0f);
		program.setUniform("Light.LightIntensity", l.intensity().x, l.intensity().y, l.intensity().z);
		program.setUniform("Light.LightEnabled", true);
	}
}
