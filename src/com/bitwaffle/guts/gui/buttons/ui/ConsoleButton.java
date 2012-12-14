package com.bitwaffle.guts.gui.buttons.ui;

import android.content.DialogInterface;
import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.input.TextInput;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.buttons.RectangleButton;

/**
 * Button to do text input to console
 */
public class ConsoleButton extends RectangleButton{
	/** How large the console button is */
	public static float consoleButtonWidth = 32.0f, consoleButtonHeight = 32.0f;
	
	/** How far apart the taps have to be to be counted as a double tap, in seconds */
	private static final float DOUBLE_TAP_TIMEOUT = 0.3f;
	
	/** Used to time taps for double tapping*/
	private float timeSinceTap;
	
	/** Used to check if a tap is the first tap or not */
	private boolean firstTap;
	
	public ConsoleButton(){
		super(
				CameraButton.cameraButtonWidth + 1.0f + (consoleButtonWidth * 2.0f) + 5.0f,
				consoleButtonHeight,
				consoleButtonWidth,
				consoleButtonHeight);
		this.active[0] = 0.1f;
		this.active[1] = 0.75f;
		this.active[2] = 0.1f;
		
		timeSinceTap = 0.0f;
		firstTap = false;
	}
	
	/**
	 * Asks for input and sends it to the console
	 */
	private void askForInput(){
		TextInput input = new TextInput("Console Input", "Enter text to send to console (/? for help)"){
			@Override
			public void parseInput(final String input) {
				/*
				 * If you tell the console to submit inside of here, it counts it as the main thread
				 * On Android, you can't do any networking in the main thread. So, if a networking-related command is given,
				 * Android will throw an exception when the command is executed. So, we execute it in its own thread.
				 */
				new Thread(){
					@Override
					public void run(){
						for(char c : input.toCharArray())
							GUI.console.putCharacter(c);
						GUI.console.submit();
						GUI.console.hide();
					}
				}.start();
			}
		};
		
		// hide console on cancellation of input
    	input.alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//if(GUI.console.isOn())
				//	GUI.console.hide();
			}
		});
		
		
		input.askForInput();
	}

	@Override
	public void update(float timeStep) {
		// update time
		if(timeSinceTap < DOUBLE_TAP_TIMEOUT)
			timeSinceTap += timeStep;
		else
			timeSinceTap = DOUBLE_TAP_TIMEOUT + 1; // to avoid needless addition, make it just over
	}

	@Override
	protected void onRelease() {
		// double tap
		if(!firstTap && timeSinceTap < DOUBLE_TAP_TIMEOUT){
			askForInput();
			firstTap = true;
			timeSinceTap = 0.0f;
			
		// single tap
		} else {
			GUI.console.toggle();
			firstTap = true;
			timeSinceTap = 0.0f;
		}
	}
	
	@Override
	protected void onPress() {
		firstTap = false;
	}

	@Override
	protected void onSlideRelease() {}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
		renderer.program.setUniform("vColor", 0.5f, 0.5f, 0.5f, this.isDown() ? 1.0f : 0.5f);
		
		Game.resources.textures.bindTexture("console");
		renderer.quad.draw(consoleButtonWidth, consoleButtonHeight);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}