package com.bitwaffle.guts.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.physics.Physics;

/**
 * Implementation of android.opengl.GLSurfaceView.
 * Handles initializing the GLRenderer and the TouchHandler
 * (all touch events go through here)
 * 
 * @author TranquilMarmot
 */
public class SurfaceView extends GLSurfaceView {
	/** 
	 * Handles all the rendering (NOTE: {@link Game}
	 * contains static instances of {@link Render2D},
	 * which should be referenced whenever doing rendering.
	 * It's also got a static instance of {@link Physics} which should be
	 * used when interacting with the physics world) 
	 */
	private Game game;
	
	/** Handles any touch events */
	public static TouchHandler touchHandler;
	
    public SurfaceView(Context context){
        super(context);
        
        // Create an OpenGL ES 2.0 context
        this.setEGLContextClientVersion(2);
        
        touchHandler = new TouchHandler(Game.player, Render2D.camera);
        
        game = new Game(context);
        this.setRenderer(game);  
  }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
    	try{
    		// simply send the event to the touch handler
    		return touchHandler.touchEvent(e);
    	}catch (NullPointerException err){
    		return false;
    	}
    }
}