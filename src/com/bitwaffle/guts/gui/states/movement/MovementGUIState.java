package com.bitwaffle.guts.gui.states.movement;

import java.util.Iterator;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.button.RectangleButton;
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
	private float movementButtonWidth = 50.0f, movementButtonHeight = 50.0f;
	/** Alpha values for buttons */
	protected float activeAlpha = 0.3f, pressedAlpha = 0.6f;
	
	
	/**
	 * Set the size of movement buttons
	 * @param buttonWidth New width of movement buttons
	 * @param buttonHeight New height of movement buttons
	 */
	public void setMovementButtonSize(float buttonWidth, float buttonHeight){
		this.movementButtonHeight = buttonHeight;
		this.movementButtonWidth = buttonWidth;
		
		Iterator<Button> it = this.getButtonIterator();
		while(it.hasNext()){
			Button butt = it.next();
			if(butt instanceof RectangleButton)
				((RectangleButton) butt).setSize(movementButtonWidth, movementButtonHeight);
		}
	}
	
	/** @return Current height of movement buttons */
	public float movementButtonHeight() { return movementButtonHeight; }
	/** @return Current height of movement buttons */
	public float movementButtonWidth() { return movementButtonWidth; }
	
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
	
	public MovementGUIState(GUI gui){
		super(gui);
		
		// MoveLeftButtons
		this.addButton(new LeftMoveLeftButton(this));
		this.addButton(new RightMoveLeftButton(this));
		
		// MoveRightButtons
		this.addButton(new LeftMoveRightButton(this));
		this.addButton(new RightMoveRightButton(this));
		
		// JumpButtons
		this.addButton(new LeftJumpButton(this));
		this.addButton(new RightJumpButton(this));
		
		// update to add all buttons
		this.update(1.0f / 60.0f);
	}
}
