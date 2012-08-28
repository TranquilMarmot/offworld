package com.bitwaffle.moguts.gui.buttons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import android.util.Log;

import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.buttons.movement.jump.JumpButton;
import com.bitwaffle.moguts.gui.buttons.movement.jump.LeftJumpButton;
import com.bitwaffle.moguts.gui.buttons.movement.jump.RightJumpButton;
import com.bitwaffle.moguts.gui.buttons.movement.left.LeftMoveLeftButton;
import com.bitwaffle.moguts.gui.buttons.movement.left.MoveLeftButton;
import com.bitwaffle.moguts.gui.buttons.movement.left.RightMoveLeftButton;
import com.bitwaffle.moguts.gui.buttons.movement.right.LeftMoveRightButton;
import com.bitwaffle.moguts.gui.buttons.movement.right.MoveRightButton;
import com.bitwaffle.moguts.gui.buttons.movement.right.RightMoveRightButton;
import com.bitwaffle.moguts.gui.buttons.pause.DebugButton;
import com.bitwaffle.moguts.gui.buttons.pause.LoadButton;
import com.bitwaffle.moguts.gui.buttons.pause.SaveButton;
import com.bitwaffle.moguts.gui.buttons.pause.SwarmButton;
import com.bitwaffle.moguts.util.PhysicsHelper;
import com.bitwaffle.offworld.Game;

/**
 * Temporary class for creating buttons
 * 
 * @author TranquilMarmot
 */
public class ButtonManager {
	// TODO use multiple ButtonManagers in GUI- one for normal buttons and one for pause buttons
	// (each set of buttons will be defined by its own ButtonManager)
	
	/** List of buttons */
	private ArrayList<Button> buttons;
	/** Used to avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/** Alpha values for buttons */
	public float activeAlpha = 0.3f, pressedAlpha = 0.6f;
	
	/** How large movement buttons are */
	private float movementButtonWidth = 50.0f, movementButtonHeight = 50.0f;
	/** How large the camera button is */
	private float cameraButtonWidth = 30.0f, cameraButtonHeight = 30.0f;
	/** How large the circle button is */
	private float circleButtonRadius = 40.0f, boxButtonWidth = 40.0f, boxButtonHeight = 40.0f;
	/** How big pause menu buttons are */
	private float pauseMenuButtonWidth = 70.0f, pauseMenuButtonHeight = 40.0f;
	
	/** Buttons to move left */
	private MoveLeftButton leftMoveLeft, rightMoveLeft;
	/** Buttons to move right */
	private MoveRightButton leftMoveRight, rightMoveRight;
	/** Buttons to jump */
	private JumpButton leftJump, rightJump;
	
	/** Button to save game */
	private SaveButton save;
	/** Button to load game */
	private LoadButton load;
	/** Butotn to toggle debug */
	private DebugButton debug;
	/** Button to show swarm */
	private SwarmButton swarm;
	
	/** Button to toggle camera mode */
	private CameraButton cameraButton;
	/** Button to add box entity */
	private BoxEntityButton boxButton;
	/** Button to add circle entity */
	private CircleEntityButton circleButton;
	
	/**
	 * Create a new button manager
	 */
	public ButtonManager(){
		buttons = new ArrayList<Button>();
		buttonsToAdd = new Stack<Button>();
		buttonsToRemove = new Stack<Button>();
		
		// left movement buttons
		this.addButton(leftMoveLeft = new LeftMoveLeftButton(this));
		this.addButton(leftMoveRight = new LeftMoveRightButton(this));
		this.addButton(leftJump = new LeftJumpButton(this));
		
		// right movement buttons
		this.addButton(rightMoveLeft = new RightMoveLeftButton(this));
		this.addButton(rightMoveRight = new RightMoveRightButton(this));
		this.addButton(rightJump = new RightJumpButton(this));
		
		// misc buttons
		this.addButton(boxButton = new BoxEntityButton());
		this.addButton(circleButton = new CircleEntityButton());
		this.addButton(cameraButton = new CameraButton());
		
		// pause menu buttons
		this.addButton(save = new SaveButton(this));
		this.addButton(load = new LoadButton(this));
		this.addButton(swarm = new SwarmButton(this));
		this.addButton(debug = new DebugButton(this));
		save.hide();
		load.hide();
		swarm.hide();
		debug.hide();
		
		// update to add all buttons
		this.update();
	}
	
	/**
	 * Shows/hides the pause menu 
	 */
	public void togglePauseMenu(){
		// hide buttons not on the pause menu
		if(Game.isPaused()){
			leftMoveLeft.hide();
			leftMoveRight.hide();
			leftJump.hide();
			
			rightMoveLeft.hide();
			rightMoveRight.hide();
			rightJump.hide();
			
			cameraButton.hide();
			boxButton.hide();
			circleButton.hide();
			
			save.show();
			load.show();
			debug.show();
			swarm.show();
		// show buttons not on the pause menu
		} else{
			leftMoveLeft.show();
			leftMoveRight.show();
			leftJump.show();
			
			rightMoveLeft.show();
			rightMoveRight.show();
			rightJump.show();
			
			cameraButton.show();
			boxButton.show();
			circleButton.show();
			
			save.hide();
			load.hide();
			debug.hide();
			swarm.hide();
		}
	}
	
	/**
	 * @return An iterator that goes through every button
	 */
	public Iterator<Button> getButtonIterator(){
		return buttons.iterator();
	}
	
	/**
	 * Add a button to this button manager
	 * @param obj Button to add
	 */
	public void addButton(Button butt){
		buttonsToAdd.add(butt);
	}
	
	/**
	 * Remove a button from this button manager
	 * @param obj Button to remove from this button manager
	 */
	public void removeButton(Button butt){
		buttonsToRemove.add(butt);
	}
	
	/**
	 * Updates all the buttons being managed by this manager
	 */
	public void update(){
		// remove any buttons to remove
		while(!buttonsToRemove.isEmpty()){
			Button butt = buttonsToRemove.pop();
			butt.cleanup();
			buttons.remove(butt);
		}
		
		// add any buttons to add
		while(!buttonsToAdd.isEmpty())
			buttons.add(buttonsToAdd.pop());
		
		Iterator<Button> it = getButtonIterator();
		
		// update every button
		try{
			while(it.hasNext())
				it.next().update();
		} catch(NullPointerException e){
			Log.e("Buttons", "Got null button (ignoring)");
		}
	}
	
	/** @return Current pause menu button width */
	public float pauseMenuButtonWidth(){ return pauseMenuButtonWidth; }
	/** @return Current pause menu button height */
	public float pauseMenuButtonHeight(){ return pauseMenuButtonHeight; }
	
	/**
	 * Set the size of pause menu buttons
	 * @param width New width of buttons
	 * @param height New height of buttons
	 */
	public void setPauseMenuButtonSize(float width, float height){
		this.pauseMenuButtonWidth = width;
		this.pauseMenuButtonHeight = height;
		
		save.width = width;
		save.height = height;
		
		load.width = width;
		load.height = height;
		
		swarm.width = width;
		swarm.height = height;
		
		debug.width = width;
		debug.height = height;
	}
	
	/**
	 * Set current active button alpha
	 * @param alpha New alpha value for active buttons
	 */
	public void setActiveAlpha(float alpha){ this.activeAlpha = alpha; }
	
	/**
	 * Set current pressed button alpha
	 * @param alpha New alpha value for pressed buttons
	 */
	public void setPressedAlpha(float alpha){ this.pressedAlpha = alpha; }
	
	/** @return Current alpha value of active buttons */
	public float activeAlpha(){ return activeAlpha; }
	/** @return Current alpha value of pressed buttons */
	public float pressedAlpha(){ return pressedAlpha; }
	
	/**
	 * Set the size of movement buttons
	 * @param buttonWidth New width of movement buttons
	 * @param buttonHeight New height of movement buttons
	 */
	public void setMovementButtonSize(float buttonWidth, float buttonHeight){
		this.movementButtonHeight = buttonHeight;
		this.movementButtonWidth = buttonWidth;
		
		leftMoveLeft.width = movementButtonWidth;
		leftMoveLeft.height = movementButtonHeight;
		
		rightMoveLeft.width = movementButtonWidth;
		rightMoveLeft.height = movementButtonHeight;
		
		leftJump.width = movementButtonWidth;
		leftJump.height = movementButtonHeight;
	}
	
	/** @return Current height of movement buttons */
	public float movementButtonHeight() { return movementButtonHeight; }
	/** @return Current height of movement buttons */
	public float movementButtonWidth() { return movementButtonWidth; }
	
	/**
	 * Button to change camera modes
	 */
	class CameraButton extends RectangleButton{
		public CameraButton() {
			super(cameraButtonWidth + 1.0f, cameraButtonHeight, cameraButtonWidth, cameraButtonHeight);
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
	
	/**
	 * Button to make random circle entities
	 */
	class CircleEntityButton extends CircleButton{
		public CircleEntityButton() {
			super(Game.windowWidth - (circleButtonRadius * 3.0f), circleButtonRadius, circleButtonRadius);
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
	
	/**
	 * Button to make random box entities
	 */
	class BoxEntityButton extends RectangleButton{
		public BoxEntityButton() {
			super(Game.windowWidth - boxButtonWidth, boxButtonHeight, boxButtonWidth, boxButtonHeight);
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
