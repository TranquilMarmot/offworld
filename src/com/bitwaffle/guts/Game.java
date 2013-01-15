package com.bitwaffle.guts;

import android.app.Activity;
import android.util.FloatMath;

import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.input.KeyBindings;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.resources.Resources;
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
// Android version has to extend Activity
public abstract class Game extends Activity {
	/** Current version of the game */
	public static final String VERSION = "0.0.6.0 (pre-alpha)";
	
	/** Resource manager */
	public static Resources resources;
	
	/** 2D Renderer */
	protected Render2D render2D;
	
	/** The graphical user interface */
	public static GUI gui;
	
	/** Physics world */
	public static Physics physics;
	
	/** The player */
	public static Player player;
	
	/** Whether or not the game is paused */
	private static boolean paused = false;
	
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
	
	/**
	 * These change whenever the screen size is changed.
	 * The values that they are defined as will be the initial size of the window.
	 */
	public static int windowWidth = 1337, windowHeight = 920;
	
	/** The aspect ratio of the window */
	public static float aspect = (float) windowWidth / (float) windowHeight;
	
	/**
	 * Initializes everything
	 */
	protected void init(){
		if(physics == null)
			physics = new Physics();
		
		// Moved out of here since android resources requires context
		//resources = new Resources();
		//resources.init();
        
        render2D = new Render2D();
    	
		gui = new GUI();
		gui.setCurrentState(GUI.States.TITLESCREEN);
	}
	
	/**
	 * Updates everything and renders the scene
	 */
	protected void updateAndRender(){
		// update everything
		update();
		// render the scene
		render2D.renderScene();
	}
	
	/**
	 * Updates everything!
	 */
	protected void update(){
    	// get the current time
		long timeBeforeLoop = getTime();
		
    	// subtract and convert to seconds 
		float deltaTime = (float)(timeBeforeLoop - previousTime) / 1000.0f;
		
		// set previousTime for next iteration
		previousTime = timeBeforeLoop;
		
		// add the change in time to the accumulator, then find out how many steps we need to do
		timeStepAccum += deltaTime;
		float steps = FloatMath.floor((timeStepAccum / FIXED_TIMESTEP));
		
		// only touch the accumulator if necessary
		if(steps > 0)
			timeStepAccum -= steps * FIXED_TIMESTEP;
		
		// clamp steps and iterate, updating everything by FIXED_TIMESTEP here
		for(int i = 0; i < Math.min(steps, MAX_STEPS); i++){
			// Step the physics sim
			if(!paused && physics != null){
				physics.update(FIXED_TIMESTEP);
			
				if(Render2D.camera != null)
					Render2D.camera.update(FIXED_TIMESTEP);
			}
			
			if(gui != null)
				gui.update(FIXED_TIMESTEP);
		}
		
    	// check for pausing
		if(KeyBindings.SYS_PAUSE.pressedOnce()){
			if(GUI.console.isOn())
				GUI.console.hide();
			else
				togglePause();
		}
		
		updateFPS(timeBeforeLoop);
	}
	
	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public abstract long getTime();
	
    /**
     * Toggles between paused and not paused
     */
    public static void togglePause(){
    	paused = !paused;
    }
    
    /**
     * @return Whether or not the game is currently pause
     */
    public static boolean isPaused(){
    	return paused;
    }
    
    /**
     * Update the FPS counter
     * @param timeBeforeLoop Time in milliseconds before doing everything
     */
    private void updateFPS(long timeBeforeLoop){
    	long elapsedTime = getTime() - timeBeforeLoop;
    	counter += elapsedTime;
    	frameCount++;
    	
    	// if the counter is above 1000, it means a second has passed
    	if(counter >= 1000.0){
    		currentFPS = frameCount;
    		frameCount = 0;
    		counter -= 1000.0;
    	}
    }
}
