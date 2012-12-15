package com.bitwaffle.guts.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.FloatMath;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;

import com.bitwaffle.guts.android.input.KeyboardManager;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.input.KeyBindings;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.resources.Resources;
import com.bitwaffle.offworld.entities.Player;
import com.swarmconnect.SwarmActivity;

/**
 * Main Activity!
 * This class handles calling the rendering methods,
 * as well as stepping the physics sim (for now).
 * This class also has a static instance of Physics, which
 * should be used whenever interacting with the physics world.
 * 
 * @author TranquilMarmot
 */
public class Game extends SwarmActivity implements GLSurfaceView.Renderer {
	/** Current version of the game */
	public static final String VERSION = "0.0.5 (pre-alpha)";
	
	/** Current context */
	public static Context context;
	
	/**
	 * This is VERY important! It holds on to the 'Game' object that
	 * takes care of all the physics, rendering, etc. etc.
	 */
    private static GLSurfaceView surfaceView;
	
	/** Resource manager */
	public static Resources resources;
	
	/** 2D Renderer */
	private Render2D render2D;
	
	/** The graphical user interface */
	public static GUI gui;
	
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
	
	/** How much to step the world each time (Box2D prefers 1/60) */
	private final float FIXED_TIMESTEP = 1.0f / 60.0f;
	
	/** Maximum number of allowed steps per frame */
	private final int MAX_STEPS = 5;
	
	/** Used to know how much time has passed */
	private float timeStepAccum = 0;
	
	/** Used to know how much time has passed */
	private long previousTime;
	
	 @Override
    /**
     * What to do when the activity is created
     * (this is overriding the Activity class)
     * NOTE:
     * Since this app is pure OpenGL, this is the only activity.
     * Whenever the screen's orientation gets changed, this gets called
     * so be careful with any initialization caused by creating the SurfaceView.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO make this an option somewhere
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // initialize window width/height variables
        Display display = getWindowManager().getDefaultDisplay();
        Game.windowWidth = display.getWidth();
        Game.windowHeight = display.getHeight();
        
       // getResources().getDisplayMetrics().

        /*
         * Create a SurfaceView instance and set it as
         * the ContentView for this Activity.
         * NOTE: This SurfaceView holds on to the Game object
         * and handles pretty much EVERYTHING in the game
         */
        surfaceView = new SurfaceView(this);
        this.setContentView(surfaceView);
    }

	/**
	 * What to do when the OpenGL surface is created
	 * (overrides GLSufraceView.Renderer)
	 */
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    	previousTime = Game.getTime();
    	
		resources.init();
		
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        render2D = new Render2D();
        gui = new GUI();
        
        /*
         * Only initialize physics engine if it doesn't exist yet
         * Whenever the screen's orientation is called, the WHOLE application
         * starts over- so physics can get re-initialized if we're not careful.
         */
        if(physics == null){
        	physics = new Physics();
        	//PhysicsHelper.temp(physics);
        }
        
        //physics.addEntity(new GLSLSandbox());
    }
    
    private void update(){
		// get the current time
		long currentTime = getTime();
		
    	// subtract and convert to seconds 
		float deltaTime = (currentTime - previousTime) / 1000.0f;
		
		// set previousTime for next iteration
		previousTime = currentTime;
		
		// add the change in time to the accumulator, then find out how many steps we need to do
		timeStepAccum += deltaTime;
		float steps = FloatMath.floor(timeStepAccum / FIXED_TIMESTEP);
		
		// only touch the accumulator if necessary
		if(steps > 0)
			timeStepAccum -= steps * FIXED_TIMESTEP;
		
		// clamp steps and iterate however many times
		// update everything by FIXED_TIMESTEP here
		for(int i = 0; i < Math.min(steps, MAX_STEPS); i++){
			// Step the physics sim
			if(!paused)
				physics.update(FIXED_TIMESTEP);
			
			gui.update(FIXED_TIMESTEP);
		}
		
    	// check for pausing
		if(KeyBindings.SYS_PAUSE.pressedOnce()){
			if(GUI.console.isOn())
				GUI.console.hide();
			else
				Game.togglePause();
		}
    }
    
    /**
     * Draws a frame and steps the physics sim
     */
    public void onDrawFrame(GL10 unused) {
    	// used for FPS counting
    	long timeBeforeLoop = SystemClock.elapsedRealtime();
    	
    	// update everything
    	update();
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
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	return KeyboardManager.keyDown(keyCode, event);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
    	return KeyboardManager.keyUp(keyCode, event);
    }
    
    public static void togglePause(){
    	paused = !paused;
    }
    
    public static boolean isPaused(){
    	return paused;
    }
    
    public static long getTime(){
    	return SystemClock.elapsedRealtime();
    }
}
