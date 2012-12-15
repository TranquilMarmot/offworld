package com.bitwaffle.guts.gui.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.gui.GUIObject;
import com.bitwaffle.guts.gui.button.Button;

/**
 * Each GUIState mantains a list of buttons. Generally, the GUI
 * holds on to instances of each state, of which it only uses one at a time.
 * This makes it easy to toggle between menus (simply change the active GUIState)
 * 
 * @author TranquilMarmot
 */
public class GUIState {
	/** GUI this state belongs to */
	protected GUI gui;
	/** Whether or not this state is the current state of the GUI */
	private boolean isCurrentState;
	
	/** List of buttons */
	private ArrayList<Button> buttons;
	/** Used to avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/** List of all GUI objects */
	private ArrayList<GUIObject> objects;
	/** Used to add/remove GUI objects and avoid ConcurrentModificationExceptions */
	private Stack<GUIObject> objectsToAdd, objectsToRemove;
	
	/**
	 * Create a new GUI state
	 */
	public GUIState(GUI gui){
		this.gui = gui;
		this.isCurrentState = false;
		
		buttons = new ArrayList<Button>();
		buttonsToAdd = new Stack<Button>();
		buttonsToRemove = new Stack<Button>();
		
		objects = new ArrayList<GUIObject>();
		objectsToRemove = new Stack<GUIObject>();
		objectsToAdd = new Stack<GUIObject>();
	}
	
	/**
	 * Called when this state becomes the current state of a GUI
	 * At this point, all of this state's buttons and objects have been added to the GUI
	 * @param gui GUI state is becoming current state of
	 */
	public void gainCurrentState(){
		Iterator<Button> butts = this.getButtonIterator();
		while(butts.hasNext())
			gui.addButton(butts.next());
		
		Iterator<GUIObject> objs = this.getObjectIterator();
		while(objs.hasNext())
			gui.addObject(objs.next());
		
		this.isCurrentState = true;
	}
	
	/**
	 * Called when this state gets removes from a GUI
	 * @param gui GUI state is being removed from
	 */
	public void loseCurrentState(){
		Iterator<Button> it = this.getButtonIterator();
		while(it.hasNext())
			gui.removeButton(it.next());
		
		Iterator<GUIObject> it2 = this.getObjectIterator();
		while(it2.hasNext())
			gui.removeObject(it2.next());
		
		this.isCurrentState = true;
	}
	
	/**
	 * Hides all buttons owned by this manager
	 */
	public void hideAll(){
		for(Button butt : buttons)
			butt.hide();
		
		for(GUIObject obj : objects)
			obj.hide();
	}
	
	/**
	 * Shows all buttons owned by this manager
	 */
	public void showAll(){
		for(Button butt : buttons)
			butt.show();
		
		for(GUIObject obj : objects)
			obj.show();
	}
	
	/**
	 * Updates all the buttons being managed by this manager
	 */
	public void update(float timeStep){
		updateButtons(timeStep);
		updateObjects(timeStep);
	}
	
	/**
	 * Updates every button in the GUI
	 * @param timeStep Time since last update, in seconds
	 */
	private void updateButtons(float timeStep){
		while(!buttonsToRemove.isEmpty()){
			Button butt = buttonsToRemove.pop();
			buttons.remove(butt);
			if(this.isCurrentState)
				gui.removeButton(butt);
		}
		
		while(!buttonsToAdd.isEmpty()){
			Button butt = buttonsToAdd.pop();
			buttons.add(butt);
			if(this.isCurrentState)
				gui.addButton(butt);
		}
	}
	
	/**
	 * Updates every object in the GUI
	 * @param timeStep Time since last update, in seconds
	 */
	private void updateObjects(float timeStep){
		while(!objectsToRemove.isEmpty()){
			GUIObject obj = objectsToRemove.pop();
			objects.remove(obj);
			if(this.isCurrentState)
				gui.removeObject(obj);
		}
		
		while(!objectsToAdd.isEmpty()){
			GUIObject obj = objectsToAdd.pop();
			objects.add(obj);
			if(this.isCurrentState)
				gui.addObject(obj);
		}
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
		objectsToAdd.push(o);
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
}
