package com.bitwaffle.moguts.screen;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.bitwaffle.moguts.Game;

/**
 * Implementation of android.opengl.GLSurfaceView.
 * Handles initializing the GLRenderer and the TouchHandler
 * (all touch events go through here)
 * 
 * @author TranquilMarmot
 */
public class SurfaceView extends GLSurfaceView {
	/** 
	 * Handles all the rendering (NOTE: GLRenderer
	 * contains static instances of Render2D and Render3D,
	 * which should be referenced whenever doing graphics stuff.
	 * It's also got a static instance of Physics which should be
	 * used when interacting with the physics world) 
	 */
	public static Game renderer;
	
	/** Handles any touch events */
	public static TouchHandler touchHandler;
	
    public SurfaceView(Context context){
        super(context);
        
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        touchHandler = new TouchHandler();
        
        renderer = new Game(context);
        setRenderer(renderer);  
  }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
    	// simply send the event to the touch handler
    	return touchHandler.touchEvent(e);
    }
}