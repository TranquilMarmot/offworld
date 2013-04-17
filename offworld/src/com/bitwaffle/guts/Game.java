package com.bitwaffle.guts;

import java.io.PrintStream;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.Render3D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.input.Input;
import com.bitwaffle.guts.net.Net;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.resources.Resources;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Base game class.
 * 
 * @author TranquilMarmot
 */
public abstract class Game implements ApplicationListener {
	/** Current version of the game */
	public static final String VERSION = "0.0.6.8 (pre-alpha)";
	
	/** Resource manager */
	public static Resources resources;
	
	/** 2D Renderer */
	protected Render2D render2D;
	
	/** 3D Renderer */
	protected Render3D render3D;
	
	/** The graphical user interface */
	public static GUI gui;
	
	/** Physics world */
	public static Physics physics;
	
	/** Local players */
	public static Player[] players = new Player[4];
	
	/** Whether or not the game is paused */
	private static boolean paused = false;
	
	/**
	 * These change whenever the screen size is changed.
	 * The values that they are defined as will be the initial size of the window.
	 */
	public static int windowWidth = 1337, windowHeight = 920;
	
	/** The aspect ratio of the window */
	public static float aspect = (float) windowWidth / (float) windowHeight;
	
	/** Random generator */
	public static Random random;
	
	/** Where to print out info for the game (System.out by default, gets set to ConsoleOutputStream to print to console) */
	public static PrintStream out = System.out;
	
	/** Current frames per second (at the moment, counts rendering and physics updates per second) */
	public static int currentFPS = 60;
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
	
	/** Handles all input to the game */
	public static Input input;
	
	public static Net net;
	
	@Override
    public void create () {
    	random = new Random(this.getTime());
		
    	initGDX();
    	initPhysics();
		initResources();
		initRenderer();
        initGUI();
        input = new Input();
        
        net = new Net();
    }
	
	protected void initGDX(){
		Gdx.graphics.setTitle("Offworld " + VERSION);
		Gdx.gl20.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
	}
	
	protected void initPhysics(){
		if(physics == null)
			physics = new Physics();
	}
	
	protected void initResources(){
		resources = new Resources();
		resources.init();
	}
	
	protected void initRenderer(){
		render2D = new Render2D();
		render3D = new Render3D();
	}
	
	protected void initGUI(){
		gui = new GUI();
		gui.setCurrentState(GUI.States.TITLESCREEN);
	}

	@Override
    public void render () {
		// update everything
		update();
		// render the scene
		render2D.renderScene();
    }
    
    protected void update(){
    	// get the current time
		long timeBeforeLoop = getTime();
		
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
			// Step the physics sim and camera
			if(!paused){
				if(physics != null)
					physics.update(FIXED_TIMESTEP);
			
				if(Render2D.camera != null)
					Render2D.camera.update(FIXED_TIMESTEP);
			}
			
			// GUI gets updated even if we're not paused
			if(gui != null)
				gui.update(FIXED_TIMESTEP);
		}
		
		updateFPS(timeBeforeLoop);
		
		net.update();
    }
    
	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public abstract long getTime();
    
    /**
     * @return Whether or not the game is currently pause
     */
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
    	Gdx.gl20.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
    }

    public void dispose () {
    	// nothing... yet
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