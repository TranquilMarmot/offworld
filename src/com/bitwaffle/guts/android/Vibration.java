package com.bitwaffle.guts.android;

import android.content.Context;
import android.os.Vibrator;

/**
 * Handles all vibration
 * 
 * @author TranquilMarmot
 */
public class Vibration {
	/** Actual vibrator */
	private Vibrator vib;
	
	/** Whether or not vibration is enabled */
	private boolean vibrationEnabled = true;
	
	/**
	 * Create a new Vibration handler
	 * @param context Context for vibrator
	 */
	public Vibration(Context context){
		vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	/**
	 * Vibrate the device; only works if vibrationEnabled is true
	 * @param milliseconds How long to vibrate for
	 */
    public void vibrate(long milliseconds){
    	if(vibrationEnabled)
    		vib.vibrate(milliseconds);
    }
    
    /**
     * Vibrate the device; only works if vibrationEnabled is true
     * @param pattern Pattern to vibrate in
     * @param repeat How many times to repeat pattern
     */
    public void vibrate(long[] pattern, int repeat){
    	if(vibrationEnabled)
    		vib.vibrate(pattern, repeat);
    }
    
    /**
     * Cancel any current vibration
     */
    public void cancelVibration(){
    	vib.cancel();
    }
    
    /**
     * @return Whether or not vibration is enabled
     */
    public boolean vibrationEnabled(){
    	return vibrationEnabled;
    }
    
    /**
     * Enable vibration
     */
    public void enableVibration(){
    	vibrationEnabled = true;
    }
    
    /**
     * Disable vibration
     */
    public void disableVibration(){
    	vibrationEnabled = false;
    }
}
