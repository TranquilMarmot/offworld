package com.bitwaffle.moguts.graphics.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.bitwaffle.moguts.physics.Physics;

/**
 * Implementation of the GLSurfaceView.renderer class.
 * This class handles calling the rendering methods, as well as stepping the physics sim (for now)
 * 
 * @author TranquilMarmot
 */
public class GLRenderer implements GLSurfaceView.Renderer {
	/** Context for loading resources */
	private Context context;
	
	/** 3D Renderer */
	public static Render3D render3D;
	
	/** 2D Renderer*/
	public static Render2D render2D;
	
	/** Physics world */
	public static Physics physics;
	
	/** Current height and width of the window */
	public static volatile int windowWidth, windowHeight;
	
	/** Current aspect ratio (windowWidth / windowHeight) */
	public static volatile float aspect;
	
	/**
	 * Create a new renderer instance
	 * @param context Context to use for laoding resources
	 */
	public GLRenderer(Context context){
		super();
		this.context = context;
	}

	/**
	 * Initializes everything
	 */
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        //render3D = new Render3D(context);
        render2D = new Render2D(context);
        physics = new Physics();
    }
    
    /**
     * Draws a frame and steps the physics sim
     */
    public void onDrawFrame(GL10 unused) {
    	// clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        //render3D.renderScene();
        
        // render 2D scene
        render2D.renderScene();

        // step physics sim TODO make this have a variable timestep? (look up an article "fixing your timestep")
    	physics.update();	
    }

    /**
     * Changes windowWidth/windowHeight/aspect whenever the screen size changes
     * Also resizes the GL viewport
     */
    public void onSurfaceChanged(GL10 unused, int width, int height) {
    	windowHeight = height;
    	windowWidth = width;
    	aspect = (float) width /  (float) height;
        GLES20.glViewport(0, 0, width, height);
    }
}
