package com.bitwaffle.moguts.graphics.render;

import java.io.IOException;
import java.util.Iterator;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.Entities;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.moguts.graphics.glsl.GLSLShader;
import com.bitwaffle.moguts.gui.GUI;
import com.bitwaffle.moguts.gui.button.Button;

/**
 * This class handles all 2D rendering
 * 
 * @author TranquilMarmot
 */
public class Render2D {
	/** Vertex shader to load on init */
	private static final String VERTEX_SHADER = "game/shaders/main.vert";
	/** Fragment shader to load on init */
	private static final String FRAGMENT_SHADER = "game/shaders/main.frag";
	
	/** Initial values for camera */
	private static final float DEFAULT_CAMX = 245.0f, DEFAULT_CAMY = 75.0f, DEFAULT_CAMZ = 0.004f;

	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** Camera for describing how the scene should be looked at */
	public Camera camera;
	
	/** The modelview and projection matrices*/
	float[] modelview, projection;

	@SuppressWarnings("unused")
	private Context context;
	/** Used for loading assets */
	private AssetManager assets;
	
	/** The graphical user interface */
	public GUI gui;

	/**
	 * Create a new 2D renderer
	 * @param context Context for things being rendered
	 */
	public Render2D(Context context) {
		this.context = context;
		assets = context.getAssets();
		
		initShaders();
		
		projection = new float[16];
		modelview = new float[16];
		
		camera = new Camera(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY), DEFAULT_CAMZ);
	}

	/**
	 * Initializes the vertex and fragment shaders and then links them to the program
	 */
	private void initShaders() {
		GLSLShader vert = new GLSLShader(GLSLShader.ShaderTypes.VERTEX);
		GLSLShader frag = new GLSLShader(GLSLShader.ShaderTypes.FRAGMENT);
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

	/**
	 * Renders the 2D scene
	 */
	public void renderScene() {
		//FIXME wtf?!
		if(gui == null)
			gui = new GUI();
		gui.update();
		
		program.use();
		
		setUpProjectionMatrix();
		renderEntities(Game.physics.entities.getPassiveEntityIterator());
		renderEntities(Game.physics.entities.getDynamicEntityIterator());
		
		setUpProjectionGUI();
		renderGUI(gui);
	}
	
	/**
	 * Sets up the projection matrix with an orthographic projection
	 */
	private void setUpProjectionMatrix(){
		Matrix.setIdentityM(projection, 0);
		Matrix.orthoM(projection, 0, 0, Game.aspect, 0, 1, -1, 1);
		//Matrix.scaleM(projection, 0, camera.getZoom(), camera.getZoom(), 1.0f);
		Matrix.rotateM(projection, 0, camera.getAngle(), 0.0f, 0.0f, 1.0f);
		
		program.setUniformMatrix4f("Projection", projection);
	}
	
	/**
	 * Renders entities from the given entity list
	 * @param entities Entity list to render
	 * @see Entities
	 */
	private void renderEntities(Iterator<?> it){
		Vector2 cam = camera.getLocation();
		
		// iterate through every entity
		while(it.hasNext()){
			Entity ent = (Entity) it.next();
			
			// figure out the location and the angle of what we're rendering
			Vector2 loc = ent.getLocation();
			//float angle = (float)Math.toDegrees((double)ent.getAngle());
			float angle = toDegrees(ent.getAngle());
			
			// mainpulate the modelview matrix to draw the entity (zooming is done by scaling the projection matrix)
			Matrix.setIdentityM(modelview, 0);
			Matrix.scaleM(modelview, 0, camera.getZoom(), camera.getZoom(), 1.0f);
			Matrix.translateM(modelview, 0, loc.x + cam.x, loc.y + cam.y, 0.0f);
			Matrix.rotateM(modelview, 0, angle, 0.0f, 0.0f, 1.0f);
			program.setUniformMatrix4f("ModelView", modelview);
			
			ent.render();
		}
	}
	
	private void setUpProjectionGUI(){
		Matrix.setIdentityM(projection, 0);
		Matrix.orthoM(projection, 0, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
		
		program.setUniformMatrix4f("Projection", projection);
	}
	
	private void renderGUI(GUI gui){
		Iterator<Button> it = gui.getIterator();
		
		while(it.hasNext()){
			Button butt = it.next();
			
			Matrix.setIdentityM(modelview, 0);
			Matrix.translateM(modelview, 0, butt.x, butt.y, 0.0f);
			program.setUniformMatrix4f("ModelView", modelview);
			
			butt.draw();
		}
	}
	
	public float[] currentModelview(){
		return modelview;
	}
	
	public float[] currenProjection(){
		return projection;
	}
	
	/**
	 * Convert radians to degrees- with floats!
	 * (AKA Why the hell does everything in java.lang.math use doubles?)
	 * @param radians
	 * @return
	 */
	public static float toDegrees(float radians){
		return radians * 180.0f / 3.14159265f;
	}
	

	/**
	 * Returns a string with a readable matrix
	 * @param mat 4x4 matrix to format string with
	 * @return Formatted string representing given matrix
	 */
	public static String matrixToString(float[] mat){
		return String.format("%f %f %f %f\n%f %f %f %f\n %f %f %f %f\n %f %f %f %f\n",
				mat[0], mat[1], mat[2], mat[3], mat[4], mat[5], mat[6], mat[7], mat[8], 
				mat[9], mat[10], mat[11], mat[12], mat[13], mat[14], mat[15]);
	}
}
