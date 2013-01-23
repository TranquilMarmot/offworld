package com.bitwaffle.guts.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.input.KeyboardManager;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.states.movement.MovementGUIState;
import com.bitwaffle.guts.resources.Resources;

/**
 * Main Activity!
 * Heart of the game on Android devices
 * 
 * @author TranquilMarmot
 */
public class AndroidGame extends Game implements GLSurfaceView.Renderer {
	/** Current context */
	public static Context context;
	
	/**
	 * This is VERY important! It holds on to the 'Game' object that
	 * takes care of all the physics, rendering, etc. etc.
	 */
    private static GLSurfaceView surfaceView;
    
    /** Info about the screen */
    public static DisplayMetrics displayMetrics;
	
	/** Buzz buzz*/
	public static Vibration vibration;
	
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // initialize window width/height variables
        Display display = getWindowManager().getDefaultDisplay();
        Game.windowWidth = display.getWidth();
        Game.windowHeight = display.getHeight();
    	Game.aspect = (float)  Game.windowWidth /  (float) Game.windowHeight;
    	
    	displayMetrics = new DisplayMetrics();
    	display.getMetrics(displayMetrics);
    	MovementGUIState.buttonWidth = 37.0f *  displayMetrics.scaledDensity;
    	MovementGUIState.buttonHeight = 37.0f *  displayMetrics.scaledDensity;

        /*
         * Create a SurfaceView instance and set it as
         * the ContentView for this Activity.
         * NOTE: This SurfaceView holds on to the actual Game object
         */
        surfaceView = new SurfaceView(this);
        this.setContentView(surfaceView);
    }

	/**
	 * What to do when the OpenGL surface is created
	 * (overrides GLSufraceView.Renderer)
	 */
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    	super.init();
    	
    	resources = new Resources(context);
    	resources.init();
    }
    
    /**
     * Sets the current context to use for this game
     * @param context New context to use for game
     */
    public static void setContext(Context context){
        AndroidGame.context = context;
		resources = new Resources(context);
		vibration = new Vibration(context);
    }
    /**
     * Draws a frame and steps the physics sim
     */
    public void onDrawFrame(GL10 unused) {
    	super.updateAndRender();
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
        
        if(Render2D.camera != null)
        	Render2D.camera.getWorldWindowSize();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	return KeyboardManager.keyDown(keyCode, event);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
    	return KeyboardManager.keyUp(keyCode, event);
    }
    
    /**
     * @return Current time
     */
    @Override
    public long getTime(){
    	return SystemClock.elapsedRealtime();
    }

    /**
     * Ends the game... immediately
     */
	public static void endGame() {
		System.exit(0);
	}
}
