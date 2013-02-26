package com.bitwaffle.guts.gui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.button.RectangleButton;

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
		
		Gdx.input.getTextInput(new TextInputListener(){
			@Override
			public void input(String input) {
				for(char c : input.toCharArray())
					Game.gui.console.putCharacter(c);
				Game.gui.console.submit();
				Game.gui.console.hide();
			}

			@Override
			public void canceled() {}
		}, "Enter text to send to console (/? for help)", "");
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
			Game.gui.console.toggle();
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
	protected void onDrag(float dx, float dy){}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		renderer.program.setUniform("vColor", 0.5f, 0.5f, 0.5f, this.isDown() ? 1.0f : 0.5f);
		
		Game.resources.textures.bindTexture("console");
		renderer.quad.render(consoleButtonWidth, consoleButtonHeight);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}