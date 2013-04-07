package com.bitwaffle.guts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.console.Console;
import com.bitwaffle.guts.gui.hud.HUD;
import com.bitwaffle.guts.gui.states.BlankState;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.options.OptionsState;
import com.bitwaffle.guts.gui.states.pause.PauseState;
import com.bitwaffle.guts.gui.states.titlescreen.TitleScreenState;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	private static final String LOGTAG = "GUI";
	/**
	 * States for the GUI. Anything can extend
	 * GUIState and be made the current state through the
	 * setCurrentState() method
	 */
	public enum States{
		NONE(new BlankState()), // nothing
		PAUSE(new PauseState()),     // displayed when the game is paused
		TITLESCREEN(new TitleScreenState()), // the title screen
		OPTIONS(new OptionsState());   // the options screen
		
		// Each value in this enum basically acts as a wrapper to access a GUIState
		GUIState state;
		States(GUIState state){ this.state = state; }
		protected void setParentGUI(GUI gui){ state.setParentGUI(gui); }
		protected void update(float timeStep){ state.update(timeStep); }
		protected void loseCurrentState(){ state.loseCurrentState(); }
		protected void gainCurrentState(){ state.gainCurrentState(); }
	}
	
	/** Console for interacting with game */
	public Console console;
	
	/** List of all GUI objects */
	private ArrayList<GUIObject> objects;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<GUIObject> objectsToAdd, objectsToRemove;
	
	/** Anything that can be clicked/pressed */
	private LinkedList<Button> buttons;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/** Current state of the GUI (manages buttons and objects) */
	private GUIState currentState;
	
	/** For when the back button is pressed, goes back to previous state*/
	private Stack<GUIState> stateStack;
	
	/**
	 * Currently selected button.
	 * This is only relevant to when the GUI is being used
	 * by the keyboard or by a controller.
	 */
	private Button selectedButton;
	
	/** Create a new GUI */
	public GUI(){		
		objects = new ArrayList<GUIObject>();
		objectsToRemove = new Stack<GUIObject>();
		objectsToAdd = new Stack<GUIObject>();
		
		buttons = new LinkedList<Button>();
		buttonsToRemove = new Stack<Button>();
		buttonsToAdd = new Stack<Button>();
		
		console = new Console();
		
		stateStack = new Stack<GUIState>();
		
		// Set GUI states to belong to this GUI
		for(States s : States.values())
			s.setParentGUI(this);
		
		// add a HUD to this GUI
		this.addObject(new HUD(this));
	}
	
	/**
	 * Update the GUI
	 * @param timeStep Time passed since last update, in seconds
	 */
	public void update(float timeStep){
		checkState();
		if(currentState != null)
			currentState.update(timeStep);
		
		updateButtons(timeStep);
		updateObjects(timeStep);
		console.update(timeStep);
	}
	
	/** Checks the state of the GUI and changes it if necessary */
	protected void checkState(){
		// check if we need to switch between the pause menu and the movement keys
		if(currentState != States.TITLESCREEN.state && currentState != States.OPTIONS.state){
			if(Game.isPaused()){
				if(currentState != States.PAUSE.state)
					setCurrentState(States.PAUSE);
			} else {
				if(currentState == States.PAUSE.state)
					setCurrentState(States.NONE);
			}
		}
	}
	
	/**
	 * Updates every button in the GUI
	 * @param timeStep Time since last update, in seconds
	 */
	private void updateButtons(float timeStep){
		while(!buttonsToRemove.isEmpty())
			buttons.remove(buttonsToRemove.pop());
		
		while(!buttonsToAdd.isEmpty())
			buttons.add(buttonsToAdd.pop());
		
		for(Button butt : buttons)
			butt.update(timeStep);
	}
	
	/**
	 * Updates every object in the GUI
	 * @param timeStep Time since last update, in seconds
	 */
	private void updateObjects(float timeStep){
		while(!objectsToRemove.isEmpty())
			objects.remove(objectsToRemove.pop());
		
		while(!objectsToAdd.isEmpty())
			objects.add(objectsToAdd.pop());
		
		for(GUIObject obj : objects)
			obj.update(timeStep);
	}
	
	/**
	 * @param o Object to add to GUI
	 */
	public void addObject(GUIObject o){
		objectsToAdd.push(o);
	}
	
	/**
	 * @param o Object to remove from GUI
	 */
	public void removeObject(GUIObject o){
		objectsToRemove.push(o);
	}
	
	/**
	 * @return An iterator that goes through every GUIObject currently in the GUI
	 */
	public Iterator<GUIObject> getObjectIterator(){
		return objects.iterator();
	}
	
	/** @param b Button to add to GUI */
	public void addButton(Button b){ 
		buttonsToAdd.push(b);
	}
	
	/** @param b Button to remove from GUI */
	public void removeButton(Button b){
		buttonsToRemove.push(b);
	}
	
	/** @return Iterator that goes through every button currently in the GUI */
	public Iterator<Button> getButtonIterator(){
		return buttons.iterator();
	}
	
	/** @param newState New state to use for GUI */
	public void setCurrentState(States newState){
		setCurrentState(newState.state);
	}
	
	/** @param newState New state to set GUI to */
	public void setCurrentState(GUIState newState){
		if(currentState != null){
			currentState.loseCurrentState();
			stateStack.push(currentState);
		}
		
		if(newState != null)
			 newState.gainCurrentState();
		
		currentState = newState;
	}
	
	/** Go to the previous state in the stack */
	public void goToPreviousState(){
		if(!stateStack.isEmpty()){
			System.out.println("doo it");
			if(currentState != null)
				currentState.loseCurrentState();
			
			currentState = stateStack.pop();
			currentState.gainCurrentState();
		}
	}
	
	/**
	 * @param state State from GUI.States
	 * @return Whether or not the given state is the curent state
	 */
	public boolean isCurrentState(States state){ return currentState == state.state; }
	
	/** @return Whether the given state is the current state */
	public boolean isCurrentState(GUIState state){ return currentState == state; }
	
	/**
	 * Draw the GUI
	 * @param renderer
	 */
	public void render(Render2D renderer) {
		renderer.setUpProjectionScreenCoords();
		renderObjects(getObjectIterator(), renderer);
		renderObjects(getButtonIterator(), renderer);
		console.render(renderer, false, false);
	}
	
	/**
	 * @param objects GUI Objects to render
	 * @param renderer Renderer to use
	 */
	private void renderObjects(Iterator<? extends GUIObject> it, Render2D renderer){
		try{
			while(it.hasNext()){
				GUIObject obj = it.next();
				
				if(obj.isVisible())
					renderObject(obj, renderer);
			}
		} catch(NullPointerException e){
			Gdx.app.error(LOGTAG, "Got null gui object (ignoring)");
		}
	}
	
	/**
	 * @param obj GUI Object to render
	 * @param renderer Renderer to use
	 */
	public void renderObject(GUIObject obj, Render2D renderer){
		renderer.modelview.idt();
		renderer.modelview.translate(obj.x, obj.y, 0.0f);
		renderer.sendModelViewToShader();
		obj.render(renderer, false, false);
	}
	
	/**
	 * Checks if the given point contains a button and, if it does,
	 * sets that button as the selected button.
	 * @param screenX X location of pointer
	 * @param screenY Y location of pointer
	 */
	public void checkForButtonSelection(float screenX, float screenY){
		// check if the mouse went off of the selected button
		if(selectedButton != null && !selectedButton.contains(screenX, screenY)){
			selectedButton.unselect();
			selectedButton = null;
		}
		
		// check every button to see if it's selected
		Iterator<Button> it = getButtonIterator();
		while (it.hasNext()) {
			Button b = it.next();
			
			if (b != selectedButton && b.isActive() && b.isVisible() && b.contains(screenX, screenY)) {
				selectedButton = b;
				b.select();
			}
		}
	}

	/**
	 * @return Whether or not a button is currently selected
	 */
	public boolean hasSelectedButton(){
		return selectedButton != null;
	}
	
	/**
	 * Press the selected button
	 */
	public void selectedButtonDown(){
		if(selectedButton != null)
			selectedButton.press();
	}
	
	/**
	 * Release the selected button
	 */
	public void selectedButtonUp(){
		if(selectedButton != null && selectedButton.isDown())
			selectedButton.release();
	}
	
	/**
	 * Set the selected button to a new button
	 * @param newButton New button to be selected.
	 */
	public void setSelectedButton(Button newButton){
		if(selectedButton != null){
			selectedButton.unselect();
			if(selectedButton.isDown())
				selectedButton.slideRelease();
		}
		selectedButton = newButton;
		if(selectedButton != null)
			selectedButton.select();
	}

	/** Selects the button to the right of the currently selected button */
	public void moveRight() {
		if(selectedButton == null)
			setSelectedButton(currentState.initialRightButton());
		else if(selectedButton.toRight != null)
			setSelectedButton(selectedButton.toRight);
	}
	
	/** Selects the button to the left of the currently selected button */
	public void moveLeft() {
		if(selectedButton == null)
			setSelectedButton(currentState.initialLeftButton());
		else if(selectedButton.toLeft != null)
			setSelectedButton(selectedButton.toLeft);
	}
	
	/** Selects the button above the currently selected button */
	public void moveUp() {
		if(selectedButton == null)
			setSelectedButton(currentState.initialUpButton());
		else if(selectedButton.toUp != null)
			setSelectedButton(selectedButton.toUp);
	}
	
	/** Selects the button below the selected button */
	public void moveDown() {
		if(selectedButton == null)
			setSelectedButton(currentState.initialDownButton());
		else if(selectedButton.toDown != null)
			setSelectedButton(selectedButton.toDown);
	}
}
