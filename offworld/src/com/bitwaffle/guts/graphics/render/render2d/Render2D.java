package com.bitwaffle.guts.graphics.render.render2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.graphics.font.BitmapFont;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.render.render2d.camera.Camera2D;
import com.bitwaffle.guts.graphics.shapes.circle.Circle;
import com.bitwaffle.guts.graphics.shapes.quad.Quad;
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
	public Camera2D camera;
	
	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** The modelview and projection matrices*/
	public Matrix4 modelview, projection, mvp;
	
	/** Grumpy wizards make toxic brew for the evil Queen and Jack */
	public BitmapFont font;
	
	/** Used for ALL quad rendering! Whenever something needs to be drawn, this quad's draw() method should be called */
	public Quad quad;
	
	/** Used for ALL circle rendering! */
	public Circle circle;
	
	/** How many steps to take when constructing circle's geometry (lower numbers == more vertices) */
	private static final float CIRCLE_STEP = 15.0f;

	public Render2D() {
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		program = GLSLProgram.getProgram(VERTEX_SHADER, FRAGMENT_SHADER, LOGTAG);
		
		projection = new Matrix4();
		modelview = new Matrix4();
		mvp = new Matrix4();
		
		camera = new Camera2D();
		
		quad = new Quad(this);
		circle = new Circle(this, CIRCLE_STEP);
		font = new BitmapFont();
	}
	
	public void switchTo2DWorldCoords(){
		program.use();
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		
		MathHelper.orthoM(projection, 0, Game.aspect, 0, 1, -1, 1000);
	}
	
	public void switchTo2DScreenCoords(){
		program.use();
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		
		MathHelper.orthoM(projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1000);
	}
	
	/** Prepares the modelview matrix to render an entity */
	public void prepareToRenderEntity(Entity2D ent){
		Vector2 loc = ent.getLocation();
		float angle = MathHelper.toDegrees(ent.getAngle());
		
		// mainpulate the modelview matrix to draw the entity
		modelview.idt();
		this.translateModelViewToCamera();
		modelview.translate(loc.x, loc.y, 0.0f);
		modelview.rotate(0.0f, 0.0f, 1.0f, angle);
		this.sendMatrixToShader();
	}
	
	/** Translates the modelview matrix to represent the camera's location */
	public void translateModelViewToCamera(){
		Vector2 cameraLoc = camera.getLocation();
		float cameraAngle = camera.getAngle();
		float cameraZoom = camera.getZoom();
		
		modelview.scale(cameraZoom, cameraZoom, 1.0f);
		modelview.translate(cameraLoc.x, cameraLoc.y, 0.0f);
		modelview.rotate(0.0f, 0.0f, 1.0f, cameraAngle);
	}
	
	/** Sends the current modelview matrix to the shader */
	public void sendMatrixToShader(){
		mvp.set(projection);
		mvp.mul(modelview);
		program.setUniform("MVP", mvp);
	}
}
