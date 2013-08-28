package com.bitwaffle.guts.android;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.states.movement.MovementGUIState;

public class MainActivity extends AndroidApplication {
    /** Info about the screen */
    public static DisplayMetrics displayMetrics;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        cfg.hideStatusBar = true;
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        
        Display display = getWindowManager().getDefaultDisplay();
    	displayMetrics = new DisplayMetrics();
    	display.getMetrics(displayMetrics);
    	
    	// initialize window width/height variables
        Game.windowWidth = displayMetrics.widthPixels;
        Game.windowHeight = displayMetrics.heightPixels;
    	Game.aspect = (float)  Game.windowWidth /  (float) Game.windowHeight;
    	Game.renderWidth = displayMetrics.widthPixels;
    	Game.renderHeight = displayMetrics.heightPixels;
    	Game.renderAspect = (float)  Game.renderWidth /  (float) Game.renderHeight;
    	
    	// set size of buttons FIXME this should totally be elsewhere
    	MovementGUIState.buttonWidth = 37.0f *  displayMetrics.scaledDensity;
    	MovementGUIState.buttonHeight = 37.0f *  displayMetrics.scaledDensity;
        
        initialize(new AndroidGame(), cfg);
    }
    
    @Override
    public void onBackPressed(){
    	super.onBackPressed();
    }

}