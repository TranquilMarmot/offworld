package com.bitwaffle.guts.graphics.graphics2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.glsl.GLSLProgram;
import com.bitwaffle.guts.graphics.graphics2d.font.BitmapFont;
import com.bitwaffle.guts.graphics.graphics2d.shapes.circle.Circle;
import com.bitwaffle.guts.graphics.graphics2d.shapes.quad.Quad;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Handles all 2D rendering
 * 
 * @author TranquilMarmot
 */
public class Render2D {
	private static final String LOGTAG = "Render2D";
	
	/** Shader files */
	private static final String
		VERTEX_SHADER = "shaders/main.vert",
		FRAGMENT_SHADER = "shaders/main.frag";
			
	/** Names of attributes and uniforms in shaders */
	private static final String
		POSITION_ATTRIB = "vPosition",
		TEXCOORD_ATTRIB = "vTexCoord",
		MVP_UNIFORM = "MVP",
		COLOR_UNIFORM = "vColor";
	
	/** Renderer owning this 2D renderer */
	private Renderer renderer;
	
	/** The program to use for 2D rendering */
	public GLSLProgram program;
	
	/** The modelview and projection matrices*/
	private Matrix4 mvp;
	
	/** Grumpy wizards make toxic brew for the evil Queen and Jack */
	public BitmapFont font;
	
	/** Used for ALL quad rendering! Whenever something needs to be drawn, this quad's draw() method should be called */
	public Quad quad;
	
	/** Used for ALL circle rendering! */
	public Circle circle;
	
	/** How many steps to take when constructing circle's geometry (lower numbers == more vertices) */
	private static final float CIRCLE_STEP = 15.0f;

	/** @param renderer Renderer owning this Render2D instance */
	public Render2D(Renderer renderer) {
		this.renderer = renderer;
		
		program = GLSLProgram.getProgram(VERTEX_SHADER, FRAGMENT_SHADER, LOGTAG);
		mvp = new Matrix4();
		//camera = new Camera2D();
		
		quad = new Quad(renderer);
		circle = new Circle(renderer, CIRCLE_STEP);
		font = new BitmapFont();
	}
	
	/** @return Handle for vertex position shader attribute */
	public int getVertexPositionHandle(){ return program.getAttribLocation(POSITION_ATTRIB); }
	
	/** @return Handle for vertex texture coordinate shader attribute */
	public int getTexCoordHandle(){ return program.getAttribLocation(TEXCOORD_ATTRIB); }
	
	/** @param color Color to set renderer to draw in */
	public void setColor(Color color){ setColor(color.r, color.g, color.b, color.a); };
	
	/** @param color Color to set renderer to draw in, must have four floats [r,g,b,a] */
	public void setColor(float[] color){ setColor(color[0], color[1], color[2], color[3]); }
	
	/** Set the color the renderer is drawing in */
	public void setColor(float r, float g, float b, float a){
		renderer.r2D.program.setUniform(COLOR_UNIFORM, r, g, b, a);
	}
	
	/** Change the projection matrix to render in world coords */
	public void switchTo2DWorldCoords(){
		program.use();
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		
		MathHelper.orthoM(renderer.projection, 0, Game.renderAspect, 0, 1, -1, 1000);
	}
	
	/** Change the projection matrix to render in screen coords (for GUI) */
	public void switchTo2DScreenCoords(){
		program.use();
		Gdx.gl20.glDisable(GL20.GL_DEPTH_TEST);
		
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
	}
	
	/** Prepares the modelview matrix to render an entity */
	public void prepareToRenderEntity(Entity ent){
		Vector2 loc = ent.getLocation();
		float angle = MathHelper.toDegrees(ent.getAngle());
		
		// mainpulate the modelview matrix to draw the entity
		renderer.modelview.idt();
		translateModelViewToCamera();
		renderer.modelview.translate(loc.x, loc.y, 0.0f);
		renderer.modelview.rotate(0.0f, 0.0f, 1.0f, angle);
		sendMatrixToShader();
	}
	
	/** Translates the modelview matrix to represent the camera's location */
	public void translateModelViewToCamera(){
		Vector2 cameraLoc = renderer.camera.getLocation();
		float cameraAngle = renderer.camera.getAngle();
		float cameraZoom = renderer.camera.zoom;
		
		renderer.modelview.scale(cameraZoom, cameraZoom, 1.0f);
		renderer.modelview.translate(cameraLoc.x, cameraLoc.y, 0.0f);
		renderer.modelview.rotate(0.0f, 0.0f, 1.0f, cameraAngle);
	}
	
	/** Sends the current modelview matrix to the shader */
	public void sendMatrixToShader(){
		mvp.set(renderer.projection);
		mvp.mul(renderer.modelview);
		program.setUniform(MVP_UNIFORM, mvp);
	}
	

}
