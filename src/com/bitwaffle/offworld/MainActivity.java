package com.bitwaffle.offworld;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.bitwaffle.offworld.mguts.graphics.SurfaceView;

public class MainActivity extends Activity {
    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new SurfaceView(this);
        setContentView(mGLView);
    }
}