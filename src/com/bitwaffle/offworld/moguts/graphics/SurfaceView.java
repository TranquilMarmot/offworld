package com.bitwaffle.offworld.moguts.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.offworld.moguts.graphics.render.GLRenderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class SurfaceView extends GLSurfaceView {
	public static GLRenderer renderer;
	private float mPreviousX, mPreviousY;
	
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
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                
                Vector2 camLoc = GLRenderer.render2D.camera.getLocation();
                camLoc.x += dx / 75.0f;
                camLoc.y -= dy / 75.0f;
                GLRenderer.render2D.camera.setLocation(camLoc);
                
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}