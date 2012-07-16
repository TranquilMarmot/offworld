package com.bitwaffle.moguts;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.bitwaffle.moguts.entities.Player;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.graphics.render.Render3D;
import com.bitwaffle.moguts.physics.Physics;

/**
 * Implementation of the GLSurfaceView.renderer class.
 * This class handles calling the rendering methods,
 * as well as stepping the physics sim (for now).
 * This class has static instances of Render2D and Render3D,
 * which should be referenced whenever doing graphics things.
 * This class also has a static instance of Physics, which
 * should be used whenever interacting with the physics world.
 * 
 * @author TranquilMarmot
 */
public class Game implements GLSurfaceView.Renderer {
	/** Context for loading resources */
	private Context context;
	
	/** 3D Renderer */
	public static Render3D render3D;
	
	/** 2D Renderer*/
	public static Render2D render2D;
	
	/** Physics world */
	public static Physics physics;
	
	/** The player */
	public static Player player;
	
	/** 
	 * If currentFPS is greater than this, then physics
	 * is ticked with <code>1 / currentFPS</code>. If currentFPS is below
	 * this, then physics gets ticked with <code>1 / MIN_TIMESTEP_FPS</code>
	 */
	//private static final int MIN_TIMESTEP_FPS = 30, MAX_TIMESTEP_FPS = 60;
	
	/** Current height and width of the window */
	public static volatile int windowWidth, windowHeight;
	
	/** Current aspect ratio (windowWidth / windowHeight) */
	public static volatile float aspect;
	
	/** Current frames per second (at the moment, counts rendering and physics) */
	public static int currentFPS = 30;
	
	/** Used to count up to a second for FPS */
	private long counter;
	/** Used to count frames for FPS */
	private int frameCount = 0;
	
	/**
	 * Create a new renderer instance
	 * @param context Context to use for laoding resources
	 */
	public Game(Context context){
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
        physics.temp2();
    }
    
    /**
     * Draws a frame and steps the physics sim
     */
    public void onDrawFrame(GL10 unused) {
    	long timeBeforeLoop = System.currentTimeMillis();
    	
        /*
         * Step the physics sim
         * (see comment above MIN_TIMESTEP_FPS for more info)
         */
        // FIXME this timestep makes me a sad panda!
       // if(currentFPS < MIN_TIMESTEP_FPS)
        //	physics.update(1.0f / MIN_TIMESTEP_FPS);
       // else if(currentFPS > MAX_TIMESTEP_FPS)
        //	physics.update(1.0f / MAX_TIMESTEP_FPS);
       // else
        	physics.update(1.0f / 60.0f);
    	
    	// clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        //render3D.renderScene();
        
        // render 2D scene
        render2D.renderScene();
    	
    	updateFPS(timeBeforeLoop);
    }
    
    /**
     * Update the FPS counter
     * @param timeBeforeLoop Time in milliseconds before doing everything
     */
    private void updateFPS(long timeBeforeLoop){
    	long elapsedTime = System.currentTimeMillis() - timeBeforeLoop;
    	counter += elapsedTime;
    	frameCount++;
    	
    	// if the counter is above 1000, it means a second has passed
    	if(counter >= 1000.0){
    		Log.v("FPS", "" + frameCount);
    		currentFPS = frameCount;
    		frameCount = 0;
    		counter -= 1000.0;
    	}
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
