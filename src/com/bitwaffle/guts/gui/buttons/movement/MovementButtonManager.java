package com.bitwaffle.guts.gui.buttons.movement;

import android.content.DialogInterface;
import android.opengl.GLES20;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.input.TextInput;
import com.bitwaffle.guts.graphics.Camera;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.gui.buttons.ButtonManager;
import com.bitwaffle.guts.gui.buttons.RectangleButton;
import com.bitwaffle.guts.gui.buttons.movement.jump.LeftJumpButton;
import com.bitwaffle.guts.gui.buttons.movement.jump.RightJumpButton;
import com.bitwaffle.guts.gui.buttons.movement.left.LeftMoveLeftButton;
import com.bitwaffle.guts.gui.buttons.movement.left.RightMoveLeftButton;
import com.bitwaffle.guts.gui.buttons.movement.right.LeftMoveRightButton;
import com.bitwaffle.guts.gui.buttons.movement.right.RightMoveRightButton;

/**
 * A ButtonManager that keeps track of movement buttons and their size
 * 
 * @author TranquilMarmot
 */
public class MovementButtonManager extends ButtonManager {
	/** How large movement buttons are */
	private float movementButtonWidth = 50.0f, movementButtonHeight = 50.0f;
	/** How large the camera button is */
	private float cameraButtonWidth = 32.0f, cameraButtonHeight = 32.0f;
	
	/** How large the console button is */
	private float consoleButtonWidth = 32.0f, consoleButtonHeight = 32.0f;
	
	/**
	 * Set the size of movement buttons
	 * @param buttonWidth New width of movement buttons
	 * @param buttonHeight New height of movement buttons
	 */
	public void setMovementButtonSize(float buttonWidth, float buttonHeight){
		this.movementButtonHeight = buttonHeight;
		this.movementButtonWidth = buttonWidth;
		
		for(Button butt : buttons){
			if(butt instanceof RectangleButton)
				((RectangleButton) butt).setSize(movementButtonWidth, movementButtonHeight);
		}
	}
	
	/** @return Current height of movement buttons */
	public float movementButtonHeight() { return movementButtonHeight; }
	/** @return Current height of movement buttons */
	public float movementButtonWidth() { return movementButtonWidth; }
	
	public MovementButtonManager(){
		super();
		
		// MoveLeftButtons
		this.addButton(new LeftMoveLeftButton(this));
		this.addButton(new RightMoveLeftButton(this));
		
		// MoveRightButtons
		this.addButton(new LeftMoveRightButton(this));
		this.addButton(new RightMoveRightButton(this));
		
		// JumpButtons
		this.addButton(new LeftJumpButton(this));
		this.addButton(new RightJumpButton(this));
		
		// MiscButtons
		this.addButton(new CameraButton());
		this.addButton(new ConsoleInputButton());
		
		// update to add all buttons
		this.update(1.0f / 60.0f);
	}
	
	// TODO should this be moved elsewhere?
	/**
	 * Button to change camera modes
	 */
	class CameraButton extends RectangleButton{
		public CameraButton() {
			super(
					cameraButtonWidth + 1.0f,
					cameraButtonHeight,
					cameraButtonWidth,
					cameraButtonHeight);
			
			this.active[0] = 0.1f;
			this.active[1] = 0.75f;
			this.active[2] = 0.1f;
		}

		@Override
		public void update(float timeStep) {}
		
		@Override
		protected void onRelease() {
			Camera.Modes mode = Render2D.camera.currentMode();
			if(mode == Camera.Modes.FOLLOW)
				Render2D.camera.setMode(Camera.Modes.FREE);
			else
				Render2D.camera.setMode(Camera.Modes.FOLLOW);
		}
		
		@Override
		protected void onSlideRelease() {}
		@Override
		protected void onPress() {
			Game.vibration.vibrate(25);
		}
		
		@Override
		public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
			renderer.program.setUniform("vColor", 0.5f, 0.5f, 0.5f, this.isDown() ? 1.0f : 0.5f);
			
			if(Render2D.camera.currentMode() == Camera.Modes.FREE)
				Game.resources.textures.bindTexture("camera-free");
			else
				Game.resources.textures.bindTexture("camera");
			renderer.quad.draw(cameraButtonWidth, cameraButtonHeight);
			GLES20.glDisable(GLES20.GL_BLEND);
		}
		
	}
	
	// FIXME this button needs to be moved/
	/**
	 * Button to do text input to console
	 */
	class ConsoleInputButton extends RectangleButton{
		public ConsoleInputButton(){
			super(
					cameraButtonWidth + 1.0f + (consoleButtonWidth * 2.0f) + 5.0f,
					consoleButtonHeight,
					consoleButtonWidth,
					consoleButtonHeight);
			this.active[0] = 0.1f;
			this.active[1] = 0.75f;
			this.active[2] = 0.1f;
		}

		@Override
		public void update(float timeStep) {
			//this.x = (Game.windowWidth / 2) - consoleButtonWidth;
		}

		@Override
		protected void onRelease() {
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
								Game.console.putCharacter(c);
							Game.console.submit();
							Game.console.hide();
						}
					}.start();
				}
			};
			
			// hide console on cancellation of input
	    	input.alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(Game.console.isOn())
						Game.console.hide();
				}
			});
			
			
			input.askForInput();
		}

		@Override
		protected void onSlideRelease() {
			// slide-releasing this button toggles the console
			if(Game.console.isOn())
				Game.console.hide();
			else
				Game.console.show();
		}

		@Override
		protected void onPress() {}
		
		@Override
		public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
			renderer.program.setUniform("vColor", 0.5f, 0.5f, 0.5f, this.isDown() ? 1.0f : 0.5f);
			
			Game.resources.textures.bindTexture("console");
			renderer.quad.draw(cameraButtonWidth, cameraButtonHeight);
			GLES20.glDisable(GLES20.GL_BLEND);
		}
	}
}
