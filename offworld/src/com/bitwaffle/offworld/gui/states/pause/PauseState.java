package com.bitwaffle.offworld.gui.states.pause;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.offworld.gui.states.pause.buttons.LoadButton;
import com.bitwaffle.offworld.gui.states.pause.buttons.QuitButton;
import com.bitwaffle.offworld.gui.states.pause.buttons.SaveButton;

/**
 * A ButtonManager that manages the pause menu
 * 
 * @author TranquilMarmot
 */
public class PauseState extends GUIState{
	/** How many columns and rows each pause button has */
	private int buttonCols = 2, buttonRows = 3;
	
	/** How big pause menu buttons are */
	private float buttonRowWidth = 30.0f, buttonColHeight = 20.0f;
	
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

	public PauseState(GUI gui){
		super(gui);
		
		loadButton = new LoadButton(this);
		saveButton = new SaveButton(this);
		quitButton = new QuitButton(this);
		
		loadButton.toLeft = saveButton;
		
		saveButton.toRight = loadButton;
		saveButton.toUp = quitButton;
		
		quitButton.toDown = saveButton;
		
		this.addButton(loadButton);
		this.addButton(saveButton);
		this.addButton(quitButton);
		
		// update to add buttons
		this.update(1.0f/60.0f);
	}

	@Override
	protected void onGainCurrentState() {
		Game.gui.setSelectedButton(quitButton);
	}
	
	@Override
	protected void onLoseCurrentState() {
		Game.gui.setSelectedButton(null);
	}
	
	@Override
	public Button initialLeftButton() {
		return quitButton;
	}
	@Override
	public Button initialRightButton() {
		return null;
	}
	@Override
	public Button initialUpButton() {
		return quitButton;
	}
	@Override
	public Button initialDownButton() {
		return loadButton;
	}
}
