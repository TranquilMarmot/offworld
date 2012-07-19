package com.bitwaffle.offworld;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

import com.bitwaffle.moguts.device.SurfaceView;

/**
 * public static void main(String[] args)!!!
 * (ugh that's so ingrained in my brain)
 * 
 * @author TranquilMarmot
 */
public class MainActivity extends Activity {
    private static GLSurfaceView mGLView;

    @Override
    /**
     * What to do when the activity is created
     * NOTE:
     * Since this app is pure OpenGL, this is the only activity.
     * Whenever the screen's orientation gets changed, this gets called
     * so be careful with any initialization caused by creating the SurfaceView.
     */
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