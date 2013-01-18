package com.bitwaffle.guts.gui.states.pause;

import java.util.Iterator;

import com.bitwaffle.guts.gui.button.Button;
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
	/** How many columns and rows each pause button has */
	private int buttonCols = 2, buttonRows = 3;
	
	/** How big pause menu buttons are */
	private float buttonRowWidth = 30.0f, buttonColHeight = 20.0f;
	
	public int buttonCols(){ return buttonCols; }
	public int buttonRows(){ return buttonRows; }
	public float buttonRowWidth(){ return buttonRowWidth; }
	public float buttonColHeight(){ return buttonColHeight; }
	
	/**
	 * Set the size of pause menu buttons
	 * @param width New width of buttons
	 * @param height New height of buttons
	 */
	public void setPauseMenuButtonSize(float width, float height){
		//this.pauseMenuButtonWidth = width;
		//this.pauseMenuButtonHeight = height;
		
		Iterator<Button> it = this.getButtonIterator();
		while(it.hasNext()){
			//Button butt = it.next();
			
			//if(butt instanceof RectangleButton)
			//	((RectangleButton) butt).setSize(pauseMenuButtonWidth, pauseMenuButtonHeight);
			// TODO
		}
	}
	
	public PauseGUIState(){
		super();
		
		this.addButton(new DebugButton(this));
		this.addButton(new LoadButton(this));
		this.addButton(new SaveButton(this));
		//this.addButton(new SwarmButton(this));
		this.addButton(new QuitButton(this));
		
		// update to add buttons
		this.update(1.0f/60.0f);
	}
}
