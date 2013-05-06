package com.bitwaffle.guts.gui.states.movement;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.movement.buttons.jump.LeftJumpButton;
import com.bitwaffle.guts.gui.states.movement.buttons.jump.RightJumpButton;
import com.bitwaffle.guts.gui.states.movement.buttons.left.LeftMoveLeftButton;
import com.bitwaffle.guts.gui.states.movement.buttons.left.RightMoveLeftButton;
import com.bitwaffle.guts.gui.states.movement.buttons.right.LeftMoveRightButton;
import com.bitwaffle.guts.gui.states.movement.buttons.right.RightMoveRightButton;

/**
 * A ButtonManager that keeps track of movement buttons and their size
 * 
 * @author TranquilMarmot
 */
public class MovementGUIState extends GUIState {
	/** How large movement buttons are */
	public static float buttonWidth = 50.0f, buttonHeight = 50.0f;
	/** Alpha values for buttons */
	public static float activeAlpha = 0.3f, pressedAlpha = 0.6f;
	
	public MovementGUIState(GUI gui){
		super(gui);
		
		// MoveLeftButtons
		this.addButton(new LeftMoveLeftButton());
		this.addButton(new RightMoveLeftButton());
		
		// MoveRightButtons
		this.addButton(new LeftMoveRightButton());
		this.addButton(new RightMoveRightButton());
		
		// JumpButtons
		this.addButton(new LeftJumpButton());
		this.addButton(new RightJumpButton());
		
		// update to add all buttons
		this.update(1.0f / 60.0f);
	}

	@Override
	protected void onGainCurrentState() {
	}

	@Override
	protected void onLoseCurrentState() {
	}

	@Override
	public Button initialLeftButton() {
		return null;
	}

	@Override
	public Button initialRightButton() {
		return null;
	}

	@Override
	public Button initialUpButton() {
		return null;
	}

	@Override
	public Button initialDownButton() {
		return null;
	}
}
