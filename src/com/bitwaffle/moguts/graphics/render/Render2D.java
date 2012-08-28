package com.bitwaffle.moguts.graphics.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.device.SurfaceView;
import com.bitwaffle.moguts.entities.Entities;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.render.glsl.GLSLProgram;
import com.bitwaffle.moguts.graphics.render.glsl.GLSLShader;
import com.bitwaffle.moguts.graphics.render.renderers.Renderers;
import com.bitwaffle.moguts.gui.GUI;
import com.bitwaffle.moguts.gui.buttons.Button;
import com.bitwaffle.moguts.util.MathHelper;
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
	
	/** Whether or not to call every entity's debug drawing method */
	public static boolean drawDebug = false;
	
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
	
	/** Used for ALL circle rendering! */
	public Circle circle;
	
	/** How many steps to take when constructing circle's geometry (lower numbers == more vertices) */
	private static final float CIRCLE_STEP = 15.0f;

	/**
	 * Create a new 2D renderer
	 * @param context Context for things being rendered
	 */
	public Render2D() {
		initShaders();
		
		projection = new float[16];
		modelview = new float[16];
		
		camera = new Camera(new Vector2(DEFAULT_CAMX, DEFAULT_CAMY), DEFAULT_CAMZ);
		SurfaceView.touchHandler.setCamera(camera);
		
		quad = new Quad(this);
		circle = new Circle(this, CIRCLE_STEP);
		
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
			Log.e(LOGTAG, "Error linking program!\n" + program.log());
	}

	/**
	 * Renders the 2D scene
	 * and updates the GUI and camera
	 */
	public void renderScene() {
		gui.update();
		
		program.use();
		
		setUpProjectionScreenCoords();
		Renderers.BACKGROUND.render(this, null);
		
		setUpProjectionWorldCoords();
		renderEntities(Game.physics.getPassiveEntityIterator());
		renderEntities(Game.physics.getDynamicEntityIterator());
		
		setUpProjectionScreenCoords();
		// draw pause text
		if(Game.isPaused()){
			String pauseString = "Hello. This is a message to let you know that\nthe game is paused. Have a nice day.";
			float scale = 0.3f;
			float stringWidth = Game.resources.font.stringWidth(pauseString, scale);
			float stringHeight = Game.resources.font.stringHeight(pauseString, scale);
			float textX = ((float)Game.windowWidth / 2.0f) - (stringWidth / 2.0f);
			float textY = ((float)Game.windowHeight / 2.0f) - (stringHeight / 2.0f);
			Game.resources.font.drawString(pauseString, this, textX, textY, scale);
		}
		renderGUI(gui);
	}
	
	/**
	 * Sets up the projection matrix with an orthographic projection
	 * for drawing things in world coordinates
	 */
	private void setUpProjectionWorldCoords(){
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
	private void renderEntities(Iterator<? extends Entity> it){
		Vector2 cam = camera.getLocation();
		
		// iterate through every entity
		while(it.hasNext()){
			Entity ent;
			try{
				ent = (Entity) it.next();
			} catch(ConcurrentModificationException e){
				break;
			}
			
			if(ent != null){
				// figure out the location and the angle of what we're rendering
				Vector2 loc = ent.getLocation();
				float angle = MathHelper.toDegrees(ent.getAngle());
				
				// mainpulate the modelview matrix to draw the entity
				Matrix.setIdentityM(modelview, 0);
				Matrix.scaleM(modelview, 0, camera.getZoom(), camera.getZoom(), 1.0f);
				Matrix.translateM(modelview, 0, loc.x + cam.x, loc.y + cam.y, 0.0f);
				Matrix.rotateM(modelview, 0, angle, 0.0f, 0.0f, 1.0f);
				this.sendModelViewToShader();
				
				if(ent.renderer != null){
					ent.renderer.render(this, ent);
					if(drawDebug){
						Matrix.setIdentityM(modelview, 0);
						Matrix.scaleM(modelview, 0, camera.getZoom(), camera.getZoom(), 1.0f);
						Matrix.translateM(modelview, 0, loc.x + cam.x, loc.y + cam.y, 0.0f);
						Matrix.rotateM(modelview, 0, angle, 0.0f, 0.0f, 1.0f);
						this.sendModelViewToShader();
						ent.renderer.renderDebug(this, ent);
					}
				}
			}
		}
	}
	
	/**
	 * Sets up the projection matrix for drawing
	 * things in screen coordinates
	 */
	private void setUpProjectionScreenCoords(){
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
		
		try{
			while(it.hasNext()){
				Button butt = it.next();
				
				if(butt.isVisible()){
					Matrix.setIdentityM(modelview, 0);
					Matrix.translateM(modelview, 0, butt.x, butt.y, 0.0f);
					program.setUniformMatrix4f("ModelView", modelview);
					
					butt.render(this);
				}
			}
		} catch(NullPointerException e){
			Log.v("GUI", "Got null button (ignoring)");
		}
		
		// draw FPS counter TODO move this somewhere else!
		float[] debugTextColor = new float[]{ 0.0f, 0.0f, 0.0f, 1.0f };
		Game.resources.font.drawString("FPS: " + Game.currentFPS + "\nEnts: " + Game.physics.numDynamicEntities(), this, 80, 20, 0.15f, debugTextColor);
	}
	
	/**
	 * Sends the current modelview matrix to the shader
	 */
	public void sendModelViewToShader(){
		program.setUniformMatrix4f("ModelView", modelview);
	}
}
