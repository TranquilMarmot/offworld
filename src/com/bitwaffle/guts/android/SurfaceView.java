package com.bitwaffle.guts.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.bitwaffle.guts.android.input.TouchHandler;
import com.bitwaffle.guts.graphics.render.Render2D;

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
	 * contains static instances of everything that runs the game.
	 */
	public static Game game;
	
	/** Handles any touch events */
	public static TouchHandler touchHandler;
	
    public SurfaceView(Context context){
        super(context);
        
        // Create an OpenGL ES 2.0 context
        this.setEGLContextClientVersion(2);
        
        touchHandler = new TouchHandler(Game.player, Render2D.camera);
        
        game = new Game();
        Game.setContext(context);
        this.setRenderer(game);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
    	// simply send the event to the touch handler
    	if(touchHandler != null)
    		return touchHandler.touchEvent(e);
    	else
    		return false;
    }
}