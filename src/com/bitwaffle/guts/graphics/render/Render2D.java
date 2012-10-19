package com.bitwaffle.guts.graphics.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import android.opengl.Matrix;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.Camera;
import com.bitwaffle.guts.graphics.render.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.render.glsl.GLSLShader;
import com.bitwaffle.guts.graphics.render.shapes.Circle;
import com.bitwaffle.guts.graphics.render.shapes.Quad;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.util.MathHelper;

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
		camera.update(1.0f / 60.0f);
		
		program.use();
		
		setUpProjectionWorldCoords();
		Game.physics.renderAll(this);
		
		setUpProjectionScreenCoords();
		// draw some debug info TODO move this somewhere else!
		float[] debugTextColor = new float[]{ 0.3f, 0.3f, 0.3f, 1.0f };
		String str =
				"Version " + Game.VERSION + "\n" +
				"FPS: " + Game.currentFPS + "\n" +
				"Ents: " + Game.physics.numEntities();
		Game.resources.font.drawString(str, this, 82, 20, 0.15f, debugTextColor);
		// draw pause text FIXME temp
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
		
		program.setUniformMatrix4f("Projection", projection);
	}
	
	/**
	 * Renders every entity in the given iterator
	 * @param it Iterator that goes through Entity objects needing to be rendered
	 */
	public void renderEntities(Iterator<? extends Entity> it){
		while(it.hasNext()){
			try{
				Entity ent = it.next();
				if(ent != null && ent.renderer != null){
					prepareToRenderEntity(ent);
					ent.renderer.render(this, ent, drawDebug);
				}
			} catch(ConcurrentModificationException e){
				break;
			}
		}
	}
	
	/**
	 * Prepares the modelview matrix to render an entity
	 * @param ent Entity to prepare to render
	 */
	public void prepareToRenderEntity(Entity ent){
		Vector2 loc = ent.getLocation();
		float angle = MathHelper.toDegrees(ent.getAngle());
		
		// mainpulate the modelview matrix to draw the entity
		Matrix.setIdentityM(modelview, 0);
		this.translateModelViewToCamera();
		Matrix.translateM(modelview, 0, loc.x, loc.y, 0.0f);
		Matrix.rotateM(modelview, 0, angle, 0.0f, 0.0f, 1.0f);
		this.sendModelViewToShader();
	}
	
	/**
	 * Translates the modelview matrix to represent the camera's location
	 */
	public void translateModelViewToCamera(){
		Vector2 cameraLoc = camera.getLocation();
		float cameraAngle = camera.getAngle();
		float cameraZoom = camera.getZoom();
		
		Matrix.scaleM(modelview, 0, cameraZoom, cameraZoom, 1.0f);
		Matrix.translateM(modelview, 0, cameraLoc.x, cameraLoc.y, 0.0f);
		Matrix.rotateM(modelview, 0, cameraAngle, 0.0f, 0.0f, 1.0f);
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
					this.sendModelViewToShader();
					
					butt.render(this);
				}
			}
		} catch(NullPointerException e){
			Log.v("GUI", "Got null button (ignoring)");
		}
	}
	
	/**
	 * Sends the current modelview matrix to the shader
	 */
	public void sendModelViewToShader(){
		program.setUniformMatrix4f("ModelView", modelview);
	}
}
