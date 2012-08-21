package com.bitwaffle.offworld;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

import com.bitwaffle.moguts.device.SurfaceView;
import com.bitwaffle.moguts.swarm.LoginListener;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmActivity;

/**
 * public static void main(String[] args)!!!
 * (ugh that's so ingrained in my brain)
 * 
 * @author TranquilMarmot
 */
public class MainActivity extends SwarmActivity {
	/** ID for swarm */
	private static final int SWARM_ID = 1374;
	/** API key for swarm*/
	private static final String SWARM_KEY = "c853c5b0cc55a0d380366f35e3c7f8b0";
	
	
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

        /*
         * Create a SurfaceView instance and set it as
         * the ContentView for this Activity.
         * NOTE: This SurfaceView holds on to the Game object
         * and handles pretty much EVERYTHING in the game
         */
        mGLView = new SurfaceView(this);
        setContentView(mGLView);
        
        // intialize swarm
        Swarm.init(this, SWARM_ID, SWARM_KEY, new LoginListener());
    }
    
    @Override
    public void onBackPressed(){
    	// TODO make this do the pause menu
    }
}