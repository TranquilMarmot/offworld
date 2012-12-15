package com.bitwaffle.guts.gui.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.guts.gui.button.Button;

import android.util.Log;

/**
 * Each GUIState mantains a list of buttons. Generally, the GUI
 * holds on to instances of each state, of which it only uses one at a time.
 * This makes it easy to toggle between menus (simply change the active GUIState)
 * 
 * @author TranquilMarmot
 */
public class GUIState {
	/** List of buttons */
	protected ArrayList<Button> buttons;
	/** Used to avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/**
	 * Create a new button manager
	 */
	public GUIState(){
		buttons = new ArrayList<Button>();
		buttonsToAdd = new Stack<Button>();
		buttonsToRemove = new Stack<Button>();
	}
	
	/**
	 * Hides all buttons owned by this manager
	 */
	public void hideAll(){
		for(Button butt : buttons)
			butt.hide();
	}
	
	/**
	 * Shows all buttons owned by this manager
	 */
	public void showAll(){
		for(Button butt : buttons)
			butt.show();
	}
	
	/**
	 * @return An iterator that goes through every button this manager owns
	 */
	public Iterator<Button> getButtonIterator(){
		return buttons.iterator();
	}
	
	/**
	 * Add a button to this button manager
	 * @param obj Button to add
	 */
	public void addButton(Button butt){
		buttonsToAdd.add(butt);
	}
	
	/**
	 * Remove a button from this button manager
	 * @param obj Button to remove from this button manager
	 */
	public void removeButton(Button butt){
		buttonsToRemove.add(butt);
	}
	
	/**
	 * Updates all the buttons being managed by this manager
	 */
	public void update(float timeStep){
		// remove any buttons to remove
		while(!buttonsToRemove.isEmpty()){
			Button butt = buttonsToRemove.pop();
			butt.cleanup();
			buttons.remove(butt);
		}
		
		// add any buttons to add
		while(!buttonsToAdd.isEmpty())
			buttons.add(buttonsToAdd.pop());
		
		// update every button
		try{
			Iterator<Button> it = getButtonIterator();
			while(it.hasNext())
				it.next().update(timeStep);
		} catch(NullPointerException e){
			Log.e("Buttons", "Got null button (ignoring)");
		}
	}
}
