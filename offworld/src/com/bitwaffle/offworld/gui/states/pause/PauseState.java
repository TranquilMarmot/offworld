package com.bitwaffle.offworld.gui.states.pause;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.offworld.gui.states.pause.buttons.ContinueButton;
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
	/** Button to un-pause game */
	private ContinueButton contButton;
	
	public int buttonCols(){ return buttonCols; }
	public int buttonRows(){ return buttonRows; }
	public float buttonRowWidth(){ return buttonRowWidth; }
	public float buttonColHeight(){ return buttonColHeight; }

	public PauseState(GUI gui){
		super(gui);
		
		loadButton = new LoadButton(this);
		saveButton = new SaveButton(this);
		quitButton = new QuitButton(this);
		contButton = new ContinueButton(this);
		
		contButton.toRight = quitButton;
		contButton.toDown = saveButton;
		
		loadButton.toLeft = saveButton;
		loadButton.toUp = quitButton;
		
		saveButton.toRight = loadButton;
		saveButton.toUp = contButton;
		
		quitButton.toDown = loadButton;
		quitButton.toLeft = contButton;
		
		this.addButton(loadButton);
		this.addButton(saveButton);
		this.addButton(quitButton);
		this.addButton(contButton);
		
		// update to add buttons
		this.update(1.0f/60.0f);
	}

	@Override
	protected void onGainCurrentState() {
		Game.gui.setSelectedButton(contButton);
	}
	
	@Override
	protected void onLoseCurrentState() {
		Game.gui.setSelectedButton(null);
	}
	
	@Override
	public Button initialLeftButton() {
		return contButton;
	}
	@Override
	public Button initialRightButton() {
		return quitButton;
	}
	@Override
	public Button initialUpButton() {
		return contButton;
	}
	@Override
	public Button initialDownButton() {
		return saveButton;
	}
}
