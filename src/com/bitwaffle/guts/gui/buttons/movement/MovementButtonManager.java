package com.bitwaffle.guts.gui.buttons.movement;

import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.gui.buttons.ButtonManager;
import com.bitwaffle.guts.gui.buttons.RectangleButton;
import com.bitwaffle.guts.gui.buttons.movement.jump.LeftJumpButton;
import com.bitwaffle.guts.gui.buttons.movement.jump.RightJumpButton;
import com.bitwaffle.guts.gui.buttons.movement.left.LeftMoveLeftButton;
import com.bitwaffle.guts.gui.buttons.movement.left.RightMoveLeftButton;
import com.bitwaffle.guts.gui.buttons.movement.right.LeftMoveRightButton;
import com.bitwaffle.guts.gui.buttons.movement.right.RightMoveRightButton;
import com.bitwaffle.guts.gui.buttons.ui.CameraButton;
import com.bitwaffle.guts.gui.buttons.ui.ConsoleButton;

/**
 * A ButtonManager that keeps track of movement buttons and their size
 * 
 * @author TranquilMarmot
 */
public class MovementButtonManager extends ButtonManager {
	/** How large movement buttons are */
	private float movementButtonWidth = 50.0f, movementButtonHeight = 50.0f;
	
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
		this.addButton(new ConsoleButton());
		
		// update to add all buttons
		this.update(1.0f / 60.0f);
	}
}
