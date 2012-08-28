package com.bitwaffle.offworld;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.bitwaffle.moguts.device.Vibration;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.physics.Physics;
import com.bitwaffle.moguts.resources.Resources;
import com.bitwaffle.moguts.util.PhysicsHelper;
import com.bitwaffle.offworld.entities.Player;

/**
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
	/** Resource manager */
	public static Resources resources;
	
	/** 2D Renderer */
	private Render2D render2D;
	
	/** Physics world */
	public static Physics physics;
	
	/** The player */
	public static Player player;
	
	/** Whether or not the game is paused */
	private static boolean paused = false;
	
	/** Current height and width of the window */
	public static volatile int windowWidth, windowHeight;
	
	/** Current aspect ratio (windowWidth / windowHeight) */
	public static volatile float aspect;
	
	/** Buzz buzz*/
	public static Vibration vibration;
	
	/** Current frames per second (at the moment, counts rendering and physics updates per second) */
	public static volatile int currentFPS = 60;
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
		resources = new Resources(context);
		vibration = new Vibration(context);
	}

	/**
	 * Initializes everything
	 */
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		resources.init();
		
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        render2D = new Render2D();
        
        /*
         * Only initialize physics engine if it doesn't exist yet
         * Whenever the screen's orientation is called, the WHOLE application
         * starts over- so physics can get re-initialized if we're not careful.
         */
        if(physics == null){
        	physics = new Physics();
        	PhysicsHelper.temp(physics);
        }
    }
    
    /**
     * Draws a frame and steps the physics sim
     */
    public void onDrawFrame(GL10 unused) {
    	// used for FPS counting
    	long timeBeforeLoop = SystemClock.elapsedRealtime();

    	// Step the physics sim
    	if(!paused)
    		physics.update();
    	
    	// clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT /*| GLES20.GL_DEPTH_BUFFER_BIT*/);
        
        // render 2D scene
        render2D.renderScene();
        
    	updateFPS(timeBeforeLoop);
    }
    
    /**
     * Update the FPS counter
     * @param timeBeforeLoop Time in milliseconds before doing everything
     */
    private void updateFPS(long timeBeforeLoop){
    	long elapsedTime = SystemClock.elapsedRealtime() - timeBeforeLoop;
    	counter += elapsedTime;
    	frameCount++;
    	
    	// if the counter is above 1000, it means a second has passed
    	if(counter >= 1000.0){
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
    
    public static void togglePause(){
    	paused = !paused;
    }
    
    public static boolean isPaused(){
    	return paused;
    }
}
