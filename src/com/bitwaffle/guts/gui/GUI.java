package com.bitwaffle.guts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.util.Log;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.guts.gui.console.Console;
import com.bitwaffle.guts.gui.hud.HUD;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.guts.gui.states.movement.MovementGUIState;
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
	 * Possible states for the GUI. 
	 */
	public enum States{
		PAUSE(new PauseGUIState()),
		MOVEMENT(new MovementGUIState()),
		TITLESCREEN(new TitleScreen()),
		OPTIONS(new OptionsScreen());
		
		// Each value in this enum basically acts as a wrapper to access a GUIState
		GUIState state;
		States(GUIState state){ this.state = state; }
		protected void setParentGUI(GUI gui){ state.setParentGUI(gui); }
		protected void update(float timeStep){ state.update(timeStep); }
		protected void loseCurrentState(){ state.loseCurrentState(); }
		protected void gainCurrentState(){ state.gainCurrentState(); }
	}
	
	/** Console for interacting with game */
	public static Console console;
	
	/** List of all GUI objects */
	private ArrayList<GUIObject> objects;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<GUIObject> objectsToAdd, objectsToRemove;
	
	/** Anything that can be clicked/pressed */
	private ArrayList<Button> buttons;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/** Current state of the GUI (manages buttons and objects) */
	private States currentState;
	
	/**
	 * Create a new GUI
	 */
	public GUI(){		
		objects = new ArrayList<GUIObject>();
		objectsToRemove = new Stack<GUIObject>();
		objectsToAdd = new Stack<GUIObject>();
		
		buttons = new ArrayList<Button>();
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
	private void checkState(){
		// check if we need to switch between the pause menu and the movement keys
		if(currentState != States.TITLESCREEN && currentState != States.OPTIONS){
			if(Game.isPaused() && currentState != States.PAUSE)
				setCurrentState(States.PAUSE);
			else if(!Game.isPaused() && currentState != States.MOVEMENT)
				setCurrentState(States.MOVEMENT);
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
		if(currentState != null)
			currentState.loseCurrentState();
		
		if(newState != null)
			 newState.gainCurrentState();
		
		currentState = newState;
	}
	
	/**
	 * Draw the GUI/
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
			Log.d(LOGTAG, "Got null gui object (ignoring)");
		}
	}
	
	/**
	 * @param obj GUI Object to render
	 * @param renderer Renderer to use
	 */
	public void renderObject(GUIObject obj, Render2D renderer){
		renderer.modelview.setIdentity();
		Matrix4f.translate(new Vector3f(obj.x, obj.y, 0.0f), renderer.modelview, renderer.modelview);
		renderer.sendModelViewToShader();
		
		obj.render(renderer, false, false);
	}
}
