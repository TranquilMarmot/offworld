package com.bitwaffle.guts.gui.states.pause;

import java.util.Iterator;

import com.bitwaffle.guts.Game;
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
	
	/** Button to activate/deactivate debug mode */
	private DebugButton debugButton;
	/** Button to load saved game */
	private LoadButton loadButton;
	/** Button to go back to main menu */
	private QuitButton quitButton;
	/** Button to save game */
	private SaveButton saveButton;
	
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
		
		debugButton = new DebugButton(this);
		loadButton = new LoadButton(this);
		saveButton = new SaveButton(this);
		quitButton = new QuitButton(this);
		
		debugButton.toLeft = quitButton;
		debugButton.toDown = loadButton;
		loadButton.toUp = debugButton;
		loadButton.toLeft = saveButton;
		saveButton.toRight = loadButton;
		saveButton.toUp = quitButton;
		quitButton.toRight = debugButton;
		quitButton.toDown = saveButton;
		
		this.addButton(debugButton);
		this.addButton(loadButton);
		this.addButton(saveButton);
		this.addButton(quitButton);
		
		// update to add buttons
		this.update(1.0f/60.0f);
	}
	
	@Override
	public void gainCurrentState(){
		super.gainCurrentState();
		
		Game.gui.selectedButton = quitButton;
		quitButton.select();
	}
}
