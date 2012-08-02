package com.bitwaffle.moguts.graphics.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.Entities;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.moguts.graphics.glsl.GLSLShader;
import com.bitwaffle.moguts.graphics.shapes.Quad;
import com.bitwaffle.moguts.gui.GUI;
import com.bitwaffle.moguts.gui.button.Button;
import com.bitwaffle.offworld.Game;

/**
 * This class handles all 2D rendering
 * 
 * @author TranquilMarmot
 */
public class Render2D {
	/** Tag to use when logging */
	private static final String LOGTAG = "Render2D";
	
	/** Vertex shader to load on init */
	private static final String VERTEX_SHADER = "shaders/main.vert";
	/** Fragment shader to load on init */
	private static final String FRAGMENT_SHADER = "shaders/main.frag";
	
	/** Initial values for camera */
	private static final float DEFAULT_CAMX = 245.0f, DEFAULT_CAMY = 75.0f, DEFAULT_CAMZ = 0.04f;
	
	/** Camera for describing how the scene should be looked at */
	public static Camera camera;
	
	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** The modelview and projection matrices*/
	public float[] modelview, projection;
	
	/** The graphical user interface */
	public static GUI gui;
	
	/** Used for ALL quad rendering! Whenever something needs to be drawn, this quad's draw() method should be called */
	public Quad quad;

	/**
	 * Create a new 2D renderer
	 * @param context Context for things being rendered
	 */
	public Render2D() {
		initShaders();
		
		projection = new float[16];
		modelview = new float[16];
		
		camera = new Camera(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY), DEFAULT_CAMZ);
		
		quad = new Quad(this);
		
		gui = new GUI();
	}

	/**
	 * Initializes the vertex and fragment shaders and then links them to the program
	 */
	private void initShaders() {
		GLSLShader vert = new GLSLShader(GLSLShader.ShaderTypes.VERTEX);
		GLSLShader frag = new GLSLShader(GLSLShader.ShaderTypes.FRAGMENT);
		try {
			InputStream vertexStream = Game.resources.openAsset(VERTEX_SHADER);
			if (!vert.compileShaderFromStream(vertexStream))
				Log.e(LOGTAG, "Error compiling vertex shader! Result: "
						+ vert.log());
			vertexStream.close();
			
			InputStream fragmentStream = Game.resources.openAsset(FRAGMENT_SHADER);
			if (!frag.compileShaderFromStream(fragmentStream))
				Log.e(LOGTAG, "Error compiling fragment shader! Result: "
						+ frag.log());
			fragmentStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		program = new GLSLProgram();
		program.addShader(vert);
		program.addShader(frag);
		if (!program.link())
			Log.e(LOGTAG, "Error linking program! " + program.log());
	}

	/**
	 * Renders the 2D scene
	 */
	public void renderScene() {
		gui.update();
		
		program.use();
		
		camera.update(1.0f/60.0f);
		// FIXME this doesn't need to be called every frame... does it?
		setUpProjectionMatrix();
		renderEntities(Game.physics.getPassiveEntityIterator());
		renderEntities(Game.physics.getDynamicEntityIterator());
		
		setUpProjectionGUI();
		renderGUI(gui);
	}
	
	/**
	 * Sets up the projection matrix with an orthographic projection
	 */
	private void setUpProjectionMatrix(){
		Matrix.setIdentityM(projection, 0);
		Matrix.orthoM(projection, 0, 0, Game.aspect, 0, 1, -1, 1);
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
			float angle = toDegrees(ent.getAngle());
			
			// mainpulate the modelview matrix to draw the entity
			Matrix.setIdentityM(modelview, 0);
			Matrix.scaleM(modelview, 0, camera.getZoom(), camera.getZoom(), 1.0f);
			Matrix.translateM(modelview, 0, loc.x + cam.x, loc.y + cam.y, 0.0f);
			Matrix.rotateM(modelview, 0, angle, 0.0f, 0.0f, 1.0f);
			program.setUniformMatrix4f("ModelView", modelview);
			
			ent.render(this);
		}
	}
	
	/**
	 * Sets up the projection matrix for drawing the GUI
	 */
	private void setUpProjectionGUI(){
		Matrix.setIdentityM(projection, 0);
		Matrix.orthoM(projection, 0, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
		
		program.setUniformMatrix4f("Projection", projection);
	}
	
	/**
	 * Renders the given GUI
	 * @param gui GUI to render
	 */
	private void renderGUI(GUI gui){
		Iterator<Button> it = gui.getButtonIterator();
		
		while(it.hasNext()){
			Button butt = it.next();
			
			Matrix.setIdentityM(modelview, 0);
			Matrix.translateM(modelview, 0, butt.x, butt.y, 0.0f);
			program.setUniformMatrix4f("ModelView", modelview);
			
			butt.draw(this);
		}
		
		// draw FPS counter TODO move this somewhere else!
		Game.resources.font.drawString("FPS: " + Game.currentFPS + "\nEnts: " + Game.physics.numDynamicEntities(), this, 80, 20, 0.15f);
	}
	
	/**
	 * @return Current modelview matrix
	 */
	public float[] currentModelview(){
		return modelview;
	}
	
	/**
	 * @return Current projection matrix
	 */
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
