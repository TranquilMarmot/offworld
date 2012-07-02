package com.bitwaffle.offworld.mguts.graphics.render;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.bitwaffle.offworld.Triangle;

public class GLRenderer implements GLSurfaceView.Renderer {
	
float r = 0, g = 0, b = 0;
	
	private Context context;
	
	public static Render3D render3D;
	
	public static int windowWidth, windowHeight;
	
	
	private Triangle triangle;
	
	public GLRenderer(Context context){
		super();
		this.context = context;
	}

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color FIXME change it back to black
        GLES20.glClearColor(0.5f, 1.0f, 0.5f, 1.0f);
        
        render3D = new Render3D(context);
        
        triangle = new Triangle();
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        render3D.renderScene();
        
        triangle.draw();
    	
    	Random rand = new Random(System.currentTimeMillis());
    	r += rand.nextFloat() / 100.0f;
    	g += rand.nextFloat() / 100.0f;
    	b += rand.nextFloat() / 100.0f;
    	
    	if(r >= 1.0f)
    		r = rand.nextFloat();
    	
    	if(g >= 1.0f)
    		g = rand.nextFloat();
    	
    	if(b >= 1.0f)
    		b = rand.nextFloat();
    	GLES20.glClearColor(r, g, b, 1.0f);
    	
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
    	windowHeight = height;
    	windowWidth = width;
        GLES20.glViewport(0, 0, width, height);
    }
}
