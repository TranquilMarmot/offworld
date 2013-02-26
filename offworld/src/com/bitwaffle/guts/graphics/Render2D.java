package com.bitwaffle.guts.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.font.BitmapFont;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.glsl.GLSLShader;
import com.bitwaffle.guts.graphics.shapes.Circle;
import com.bitwaffle.guts.graphics.shapes.Quad;
import com.bitwaffle.guts.util.MathHelper;

/**
 * This class handles all 2D rendering
 * 
 * @author TranquilMarmot
 */
public class Render2D {
	private static final String LOGTAG = "Render2D";
	
	/** Vertex shader to load on init */
	private static final String VERTEX_SHADER = "shaders/main.vert";
	/** Fragment shader to load on init */
	private static final String FRAGMENT_SHADER = "shaders/main.frag";
	
	/** Whether or not to call every entity's debug drawing method */
	public static boolean drawDebug = false;
	
	/** Camera for describing how the scene should be looked at */
	public static Camera camera;
	
	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** The modelview and projection matrices*/
	public Matrix4 modelview, projection;
	
	/** Grumpy wizards make toxic brew for the evil Queen and Jack */
	public BitmapFont font;
	
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
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		initShaders();
		
		projection = new Matrix4();
		modelview = new Matrix4();
		
		camera = new Camera();
		//SurfaceView.touchHandler.setCamera(camera);
		
		quad = new Quad(this);
		circle = new Circle(this, CIRCLE_STEP);
		font = new BitmapFont();
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
	 * Renders the 2D scene
	 * and updates the GUI and camera
	 */
	public void renderScene() {
    	// clear the screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT /*| Gdx.gl20.GL_DEPTH_BUFFER_BIT*/);
		
		program.use();
		
		if(Game.physics.entitiesExist()){
			setUpProjectionWorldCoords();
			Game.physics.renderAll(this);
		}
		
		Game.gui.render(this);
	}
	
	/**
	 * Sets up the projection matrix with an orthographic projection
	 * for drawing things in world coordinates
	 */
	public void setUpProjectionWorldCoords(){
		projection.idt();
		MathHelper.orthoM(projection, 0, Game.aspect, 0, 1, -1, 1);
		
		program.setUniformMatrix4f("Projection", projection);
	}
	
	/**
	 * Sets up the projection matrix with an orthographic projection
	 * for drawing things in screen coordinates
	 */
	public void setUpProjectionScreenCoords(){
		projection.idt();
		MathHelper.orthoM(projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
		
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
		modelview.idt();
		this.translateModelViewToCamera();
		modelview.translate(loc.x, loc.y, 0.0f);
		modelview.rotate(0.0f, 0.0f, 1.0f, angle);
		this.sendModelViewToShader();
	}
	
	/**
	 * Translates the modelview matrix to represent the camera's location
	 */
	public void translateModelViewToCamera(){
		Vector2 cameraLoc = camera.getLocation();
		float cameraAngle = camera.getAngle();
		float cameraZoom = camera.getZoom();
		
		modelview.scale(cameraZoom, cameraZoom, 1.0f);
		modelview.translate(cameraLoc.x, cameraLoc.y, 0.0f);
		modelview.rotate(0.0f, 0.0f, 1.0f, cameraAngle);
	}
	
	/**
	 * Sends the current modelview matrix to the shader
	 */
	public void sendModelViewToShader(){
		program.setUniformMatrix4f("ModelView", modelview);
	}
}
