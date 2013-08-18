package com.bitwaffle.guts;

import java.io.PrintStream;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.input.Input;
import com.bitwaffle.guts.net.Net;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.resources.Resources;
import com.bitwaffle.guts.util.MathHelper;

/**
 * Base game class.
 * 
 * @author TranquilMarmot
 */
public abstract class Game implements ApplicationListener {
	public static Resources resources;
	public static Renderer renderer;
	public static GUI gui;
	public static Physics physics;
	public static Input input;
	public static Net net;
	
	/** Whether or not the game is paused (physics isn't stepped when this is true) */
	private static boolean paused = false;
	
	/** These change whenever the screen size is changed. */
	public static int windowWidth, windowHeight;
	
	/** The aspect ratio of the window */
	public static float aspect = (float) windowWidth / (float) windowHeight;
	
	/** Random generator */
	public static Random random;
	
	/** Where to print out info for the game (System.out by default, gets set to ConsoleOutputStream to print to console) */
	public static PrintStream out = System.out;
	
	/** Current frames per second (at the moment, counts rendering and physics loop per second) */
	public static int currentFPS = 60;
	/** Used to count up to a second for FPS */
	private float counter = 0.0f;
	/** Used to count frames for FPS */
	private int frameCount = 0;
	
	/** How much to step the world each time (Box2D prefers 1/60) */
	private final float FIXED_TIMESTEP = 1.0f / 60.0f;
	/** Maximum number of allowed steps per frame */
	private final int MAX_STEPS = 5;
	/** Used to know how much time has passed */
	private float timeStepAccum;
	/** Used to know how much time has passed */
	private long previousTime;
	
	@Override
    public void create () {
    	random = new Random(TimeUtils.millis());
		
    	initGDX();
    	physics = initPhysics();
		resources = initResources();
		resources.init();
		renderer = initRenderer();
        gui = initGUI();
        input = initInput();
        net = initNet();
    }
	
	protected void initGDX(){
		Gdx.gl20.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
	}
	
	protected abstract Physics initPhysics();
	protected abstract Net initNet();
	protected abstract Input initInput();
	protected abstract GUI initGUI();
	
	protected Resources initResources(){
		return new Resources();
	}
	
	protected Renderer initRenderer(){
		return new Renderer();
	}

	@Override
    public void render () {
		update();
		renderer.renderScene();
    }
    
    protected void update(){
    	long currentTime = TimeUtils.millis();
    	
    	if(previousTime == 0.0f)
    		previousTime = currentTime;
    	
    	// get the current time
		long timeBeforeLoop = currentTime;
		
    	// subtract and convert to seconds 
		float deltaTime = (float)(timeBeforeLoop - previousTime) / 1000.0f;
		
		// set previousTime for next iteration
		previousTime = timeBeforeLoop;
		
		// add the change in time to the accumulator, then find out how many steps we need to do
		timeStepAccum += deltaTime;
		float steps = (float)Math.floor((double)(timeStepAccum / FIXED_TIMESTEP));
		
		// only touch the accumulator if necessary
		if(steps > 0)
			timeStepAccum -= steps * FIXED_TIMESTEP;
		
		// clamp steps and iterate, updating everything by FIXED_TIMESTEP here
		for(int i = 0; i < Math.min(steps, MAX_STEPS); i++){
			// only step physics sim if not paused
			if(!paused && physics != null)
					physics.update(FIXED_TIMESTEP);
			
			// update cameras
			if(renderer != null)
				renderer.update(FIXED_TIMESTEP);
			
			// update GUI
			if(gui != null)
				gui.update(FIXED_TIMESTEP);
			
			if(net != null)
				net.update(FIXED_TIMESTEP);
		}
		
		updateFPS(deltaTime);
    }
    
    /** @return Whether or not the game is currently paused */
    public static boolean isPaused(){
    	return paused;
    }
    
    public static void togglePause () {
    	paused = !paused;
    }
    
	@Override
	public void pause() {
		if(!paused)
			togglePause();
		
	}

	@Override
	public void resume() {
		// TODO anything in here?
	}

    public void resize (int width, int height) {
    	Game.windowWidth = width;
    	Game.windowHeight = height;
    	Game.aspect = (float)width / (float) height;
    	
    	// set viewport and projection matrix
    	Gdx.gl20.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
    	MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);

    	// setting the zoom resizes the camera's view of the world
    	if(renderer.camera != null)
    		renderer.camera.setZoom(renderer.camera.zoom);
    }

    public void dispose () {
    	// nothing... yet
    }
    
    /**
     * Update the FPS counter
     * @param timeBeforeLoop Time in milliseconds before doing everything
     */
    private void updateFPS(float deltaTime){
    	counter += deltaTime;
    	frameCount++;
    	
    	// if the counter is above 1, it means a second has passed
    	if(counter >= 1.0){
    		currentFPS = frameCount;
    		frameCount = 0;
    		counter -= 1.0;
    	}
    }
}