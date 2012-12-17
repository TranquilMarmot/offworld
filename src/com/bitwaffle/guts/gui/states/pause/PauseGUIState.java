package com.bitwaffle.guts.gui.states.pause;

import java.util.Iterator;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.button.RectangleButton;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.pause.buttons.DebugButton;
import com.bitwaffle.guts.gui.states.pause.buttons.LoadButton;
import com.bitwaffle.guts.gui.states.pause.buttons.QuitButton;
import com.bitwaffle.guts.gui.states.pause.buttons.SaveButton;

/**
 * A ButtonManager that manages the pause menu
 * 
 * @author TranquilMarmot
 */
public class PauseGUIState extends GUIState{
	/** How big pause menu buttons are */
	private float pauseMenuButtonWidth = 80.0f, pauseMenuButtonHeight = 40.0f;
	
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
		
		Iterator<Button> it = this.getButtonIterator();
		while(it.hasNext()){
			Button butt = it.next();
			
			if(butt instanceof RectangleButton)
				((RectangleButton) butt).setSize(pauseMenuButtonWidth, pauseMenuButtonHeight);
		}
	}
	
	public PauseGUIState(GUI gui){
		super(gui);
		
		this.addButton(new DebugButton(this));
		this.addButton(new LoadButton(this));
		this.addButton(new SaveButton(this));
		//this.addButton(new SwarmButton(this));
		this.addButton(new QuitButton(this));
		
		// update to add buttons
		this.update(1.0f/60.0f);
	}
}
