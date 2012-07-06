package com.bitwaffle.offworld;

import android.app.Activity;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

import com.bitwaffle.offworld.moguts.graphics.SurfaceView;

public class MainActivity extends Activity {
	public static MainActivity main;
    private static GLSurfaceView mGLView;

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
    
    @Override
    /**
     * Android (for some fucking reason) starts everything over when the orientation of the screen is changed,
     * so android:configChanges="keyboardHidden|orientation" had to be added to the application manifest so
     * that keyboard/orientation events go through this rather than starting the whole fucking application over.
     * I guess the Bundle passed to onCreate() is supposed to save state stuff, but that isn't very helpful
     * for a fucking video game.
     */
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
}