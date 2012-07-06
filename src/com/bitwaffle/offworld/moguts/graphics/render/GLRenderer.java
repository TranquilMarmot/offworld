package com.bitwaffle.offworld.moguts.graphics.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.bitwaffle.offworld.moguts.physics.Physics;

public class GLRenderer implements GLSurfaceView.Renderer {
	
float r = 0, g = 0, b = 0;
	
	private Context context;
	
	public static Render3D render3D;
	
	public static Render2D render2D;
	
	public static Physics physics;
	
	public static volatile int windowWidth, windowHeight;
	public static volatile float aspect;
	
	public GLRenderer(Context context){
		super();
		this.context = context;
	}

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color FIXME change it back to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        //render3D = new Render3D(context);
        render2D = new Render2D(context);
        physics = new Physics();
    }
    
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        
        //render3D.renderScene();
        
        render2D.renderScene();
    	
    	physics.update();	
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
    	windowHeight = height;
    	windowWidth = width;
    	aspect = (float) width /  (float) height;
        GLES20.glViewport(0, 0, width, height);
    }
}
