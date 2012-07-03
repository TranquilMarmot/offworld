package com.bitwaffle.offworld;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

import com.bitwaffle.offworld.moguts.graphics.SurfaceView;

public class MainActivity extends Activity {
	public static MainActivity main;
    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO make this an option somewhere
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        //        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new SurfaceView(this);
        setContentView(mGLView);
    }

}