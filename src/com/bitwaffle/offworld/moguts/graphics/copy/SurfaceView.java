package com.bitwaffle.offworld.moguts.graphics.copy;

import com.bitwaffle.offworld.moguts.graphics.render.GLRenderer;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class SurfaceView extends GLSurfaceView {
	public static GLRenderer renderer;
	
    public SurfaceView(Context context){
        super(context);
        
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        renderer = new GLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);  
  }
}