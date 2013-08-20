package com.bitwaffle.guts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.elements.console.Console;
import com.bitwaffle.guts.gui.states.GUIState;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public abstract class GUI {
	private static final String LOGTAG = "GUI";
	
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
	}
	
	/**
	 * Update the GUI
	 * @param timeStep Time passed since last update, in seconds
	 */
	public void update(float timeStep){
		if(currentState != null)
			currentState.update(timeStep);
		
		updateButtons(timeStep);
		updateObjects(timeStep);
		console.update(timeStep);
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
	
	/** @param o Object to add to GUI */
	public void addObject(GUIObject o){ objectsToAdd.push(o); }
	
	/** @param o Object to remove from GUI */
	public void removeObject(GUIObject o){ objectsToRemove.push(o); }
	
	/** @return An iterator that goes through every GUIObject currently in the GUI */
	public Iterator<GUIObject> getObjectIterator(){ return objects.iterator(); }
	
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
			if(currentState != null)
				currentState.loseCurrentState();
			
			currentState = stateStack.pop();
			currentState.gainCurrentState();
		}
	}
	
	/** @return Whether the given state is the current state */
	public boolean isCurrentState(GUIState state){ return currentState == state; }
	
	/** Draw the GUI */
	public void render(Renderer renderer) {
		renderObjects(getObjectIterator(), renderer);
		renderObjects(getButtonIterator(), renderer);
		console.render(renderer, false, false);
	}
	
	/** Render all objects in an iterator */
	private void renderObjects(Iterator<? extends GUIObject> it, Renderer renderer){
		while(it.hasNext()){
			GUIObject obj = it.next();
			if(obj == null){
				Gdx.app.error(LOGTAG, "Got null gui object during rendering (ignoring)");
				continue;
			}
			
			if(obj.isVisible())
				renderObject(obj, renderer);
		}
	}
	
	/** Renders a GUI object */
	public void renderObject(GUIObject obj, Renderer renderer){
		renderer.modelview.idt();
		renderer.modelview.translate(obj.x, obj.y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		obj.render(renderer, false, false);
	}
	
	/**
	 * Checks for a button at the given screen coordinates.
	 * @return Button at location, null if no button
	 */
	public Button buttonAt(float screenX, float screenY) {
		// check every button
		Iterator<Button> it = getButtonIterator();
		while (it.hasNext()) {
			Button b = it.next();
			if (b.isActive() && b.isVisible() && b.contains(screenX, screenY)) 
				return b;
		}
		return null;
	}
	
	/**
	 * Checks if the given point contains a button and, if it does,
	 * sets that button as the selected button.
	 */
	public void checkForButtonSelection(float screenX, float screenY){
		// check if the mouse went off of the selected button
		if(selectedButton != null && !selectedButton.contains(screenX, screenY)){
			selectedButton.unselect();
			selectedButton = null;
		}
		
		// check if there's a button at the given spot
		Button b = buttonAt(screenX, screenY);
		if(b != null && b != selectedButton){
			selectedButton = b;
			b.select();
		}
	}

	/** @return Whether or not a button is currently selected */
	public boolean hasSelectedButton(){
		return selectedButton != null;
	}
	
	/** Press the selected button */
	public void selectedButtonDown(){
		if(selectedButton != null)
			selectedButton.press();
	}
	
	/** Release the selected button */
	public void selectedButtonUp(){
		if(selectedButton != null && selectedButton.isDown())
			selectedButton.release();
	}
	
	/** Set the selected button to a new button */
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
