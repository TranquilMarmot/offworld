package com.bitwaffle.guts.gui.buttons.movement;

import com.bitwaffle.guts.graphics.Camera;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.gui.buttons.ButtonManager;
import com.bitwaffle.guts.gui.buttons.CircleButton;
import com.bitwaffle.guts.gui.buttons.RectangleButton;
import com.bitwaffle.guts.gui.buttons.movement.jump.LeftJumpButton;
import com.bitwaffle.guts.gui.buttons.movement.jump.RightJumpButton;
import com.bitwaffle.guts.gui.buttons.movement.left.LeftMoveLeftButton;
import com.bitwaffle.guts.gui.buttons.movement.left.RightMoveLeftButton;
import com.bitwaffle.guts.gui.buttons.movement.right.LeftMoveRightButton;
import com.bitwaffle.guts.gui.buttons.movement.right.RightMoveRightButton;
import com.bitwaffle.guts.util.PhysicsHelper;
import com.bitwaffle.offworld.Game;

/**
 * A ButtonManager that keeps track of movement buttons and their size
 * 
 * @author TranquilMarmot
 */
public class MovementButtonManager extends ButtonManager {
	/** How large movement buttons are */
	private float movementButtonWidth = 50.0f, movementButtonHeight = 50.0f;
	/** How large the camera button is */
	private float cameraButtonWidth = 30.0f, cameraButtonHeight = 30.0f;
	
	/** How large the circle button is */ // FIXME temporary
	private float circleButtonRadius = 40.0f, boxButtonWidth = 40.0f, boxButtonHeight = 40.0f;
	
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
		this.addButton(new BoxEntityButton());
		this.addButton(new CircleEntityButton());
		
		// update to add all buttons
		this.update();
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
		}

		@Override
		public void update() {}
		
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
		public void render(Render2D renderer){
			Game.resources.textures.bindTexture("camera");
			super.render(renderer);
		}
		
	}
	
	// FIXME temporary button
	/**
	 * Button to make random circle entities
	 */
	class CircleEntityButton extends CircleButton{
		public CircleEntityButton() {
			super(
					Game.windowWidth - (circleButtonRadius * 3.0f),
					circleButtonRadius,
					circleButtonRadius);
			this.active[0] = 0.75f;
			this.active[1] = 0.75f;
			this.active[2] = 0.75f;
		}
		
		@Override
		public void update() {
			this.x = Game.windowWidth - (circleButtonRadius * 3.0f);
		}
		
		@Override
		protected void onRelease() {
			PhysicsHelper.makeRandomCircle(Game.physics);
			Game.resources.sounds.play("test");
		}

		@Override
		protected void onSlideRelease() {}

		@Override
		protected void onPress() {
			Game.vibration.vibrate(25);
		}
		
		@Override
		public void render(Render2D renderer){
			Game.resources.textures.bindTexture("box");
			super.render(renderer);
		}
	}
	
	// FIXME temporary button
	/**
	 * Button to make random box entities
	 */
	class BoxEntityButton extends RectangleButton{
		public BoxEntityButton() {
			super(
					Game.windowWidth - boxButtonWidth,
					boxButtonHeight,
					boxButtonWidth,
					boxButtonHeight);
			this.active[0] = 0.75f;
			this.active[1] = 0.75f;
			this.active[2] = 0.75f;
		}
		
		@Override
		public void update() {
			this.x = Game.windowWidth - boxButtonWidth;
		}
		
		@Override
		protected void onRelease() {
			PhysicsHelper.makeRandomBox(Game.physics);
			Game.resources.sounds.play("test");
		}

		@Override
		protected void onSlideRelease() {}

		@Override
		protected void onPress() {
			Game.vibration.vibrate(25);
		}
		
		@Override
		public void render(Render2D renderer){
			Game.resources.textures.bindTexture("box");
			super.render(renderer);
		}
	}
}
