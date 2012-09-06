package com.bitwaffle.offworld;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

import com.bitwaffle.moguts.device.SurfaceView;
import com.swarmconnect.SwarmActivity;

/**
 * public static void main(String[] args)!!!
 * (ugh that's so ingrained in my brain)
 * 
 * @author TranquilMarmot
 */
public class MainActivity extends SwarmActivity {
	/**
	 * This is VERY important! It holds on to the 'Game' object that
	 * takes care of all the physics, rendering, etc. etc.
	 */
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
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // TODO make this an option somewhere
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // initialize window width/height variables
        Display display = getWindowManager().getDefaultDisplay();
        Game.windowWidth = display.getWidth();
        Game.windowHeight = display.getHeight();
        
       // getResources().getDisplayMetrics().

        /*
         * Create a SurfaceView instance and set it as
         * the ContentView for this Activity.
         * NOTE: This SurfaceView holds on to the Game object
         * and handles pretty much EVERYTHING in the game
         */
        mGLView = new SurfaceView(this);
        setContentView(mGLView);
    }
    
    @Override
    public void onBackPressed(){
    	Game.togglePause();
    }
}