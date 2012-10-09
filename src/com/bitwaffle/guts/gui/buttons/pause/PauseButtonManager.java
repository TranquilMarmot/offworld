package com.bitwaffle.guts.gui.buttons.pause;

import com.bitwaffle.guts.gui.buttons.Button;
import com.bitwaffle.guts.gui.buttons.ButtonManager;
import com.bitwaffle.guts.gui.buttons.RectangleButton;

/**
 * A ButtonManager that manages the pause menu
 * 
 * @author TranquilMarmot
 */
public class PauseButtonManager extends ButtonManager{
	/** How big pause menu buttons are */
	private float pauseMenuButtonWidth = 70.0f, pauseMenuButtonHeight = 40.0f;
	
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
		
		for(Button butt : this.buttons){
			if(butt instanceof RectangleButton)
				((RectangleButton) butt).setSize(pauseMenuButtonWidth, pauseMenuButtonHeight);
		}
	}
	
	public PauseButtonManager(){
		super();
		
		this.addButton(new DebugButton(this));
		this.addButton(new LoadButton(this));
		this.addButton(new SaveButton(this));
		this.addButton(new SwarmButton(this));
		
		// update to add buttons
		this.update();
	}
}
