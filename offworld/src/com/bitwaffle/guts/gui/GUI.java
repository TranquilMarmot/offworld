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
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.options.OptionsScreen;
import com.bitwaffle.guts.gui.states.pause.PauseGUIState;
import com.bitwaffle.guts.gui.states.titlescreen.TitleScreen;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	private static final String LOGTAG = "GUI";
	/**
	 * States for the GUI. Optionally, you can create your own
	 * GUIState and make it the current state through the
	 * setCurrentState() method
	 */
	public enum States{
		NONE(new GUIState(){ protected void onGainCurrentState() {} protected void onLoseCurrentState() {}}), // nothing
		PAUSE(new PauseGUIState()),     // displayed when the game is paused
		TITLESCREEN(new TitleScreen()), // the title screen
		OPTIONS(new OptionsScreen());   // the options screen
		
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
	protected ArrayList<GUIObject> objects;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<GUIObject> objectsToAdd, objectsToRemove;
	
	/** Anything that can be clicked/pressed */
	protected LinkedList<Button> buttons;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/** Current state of the GUI (manages buttons and objects) */
	protected GUIState currentState;
	
	public Button selectedButton;
	
	/**
	 * Create a new GUI
	 */
	public GUI(){		
		objects = new ArrayList<GUIObject>();
		objectsToRemove = new Stack<GUIObject>();
		objectsToAdd = new Stack<GUIObject>();
		
		buttons = new LinkedList<Button>();
		buttonsToRemove = new Stack<Button>();
		buttonsToAdd = new Stack<Button>();
		
		console = new Console();
		
		// All of the GUI states 
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
	
	/**
	 * Checks the state of the GUI and changes it if necessary
	 */
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
	
	/**
	 * @param b Button to add to GUI
	 */
	public void addButton(Button b){
		buttonsToAdd.push(b);
	}
	
	/**
	 * @param b Button to remove from GUI
	 */
	public void removeButton(Button b){
		buttonsToRemove.push(b);
	}
	
	/**
	 * @return Iterator that goes through every button currently in the GUI
	 */
	public Iterator<Button> getButtonIterator(){
		return buttons.iterator();
	}
	
	/**
	 * Set the current state of the GUI
	 * @param newState New state to use
	 */
	public void setCurrentState(States newState){
		setCurrentState(newState.state);
	}
	
	/**
	 * @param newState New state to set GUI to
	 */
	public void setCurrentState(GUIState newState){
		if(currentState != null)
			currentState.loseCurrentState();
		
		if(newState != null)
			 newState.gainCurrentState();
		
		currentState = newState;
	}
	
	/**
	 * Checks if the given state in the enum is the current state
	 * @param state State to check for
	 * @return Whether or not the given state is the curent state
	 */
	public boolean isCurrentState(States state){
		return currentState == state.state;
	}
	
	/**
	 * @return Whether the given state is the current state
	 */
	public boolean isCurrentState(GUIState state){
		return currentState == state;
	}
	
	/**
	 * Draw the GUI
	 * @param renderer
	 */
	public void render(Render2D renderer) {
		renderer.setUpProjectionScreenCoords();
		renderObjects(getObjectIterator(), renderer);
		renderObjects(getButtonIterator(), renderer);
		
		if(console.isVisible())
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
				
				if(obj.isVisible()){
					renderObject(obj, renderer);
				}
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
	 * Selects the button to the right of the currently selected button
	 */
	public void selectRight() {
		if(selectedButton != null && selectedButton.toRight != null){
			selectedButton.unselect();
			selectedButton = selectedButton.toRight;
			selectedButton.select();
		}
	}
	
	/**
	 * Selects the button to the left of the currently selected button
	 */
	public void selectLeft() {
		if(selectedButton != null && selectedButton.toLeft != null){
			selectedButton.unselect();
			selectedButton = selectedButton.toLeft;
			selectedButton.select();
		}
	}
	
	/**
	 * Selects the button above the currently selected button
	 */
	public void selectUp() {
		if(selectedButton != null && selectedButton.toUp != null){
			selectedButton.unselect();
			selectedButton = selectedButton.toUp;
			selectedButton.select();
		}
	}
	
	/**
	 * Selects the button below the selected button
	 */
	public void selectDown() {
		if(selectedButton != null && selectedButton.toDown != null){
			selectedButton.unselect();
			selectedButton = selectedButton.toDown;
			selectedButton.select();
		}	
	}
}
