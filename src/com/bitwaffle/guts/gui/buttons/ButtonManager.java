package com.bitwaffle.guts.gui.buttons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import android.util.Log;

/**
 * Each ButtonManager mantains a list of buttons. Generally, the GUI
 * holds a couple of ButtonManagers, of which it only uses one at a time.
 * This makes it easy to toggle between menus (simply change the active ButtonManager)
 * 
 * @author TranquilMarmot
 */
public class ButtonManager {
	/** List of buttons */
	protected ArrayList<Button> buttons;
	/** Used to avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	/** Alpha values for buttons */ // TODO should these be elsewhere?
	private float activeAlpha = 0.3f, pressedAlpha = 0.6f;
	
	/**
	 * Create a new button manager
	 */
	public ButtonManager(){
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
	public void update(){
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
				it.next().update();
		} catch(NullPointerException e){
			Log.e("Buttons", "Got null button (ignoring)");
		}
	}
	
	/**
	 * Set current active button alpha
	 * @param alpha New alpha value for active buttons
	 */
	public void setActiveAlpha(float alpha){ this.activeAlpha = alpha; }
	
	/**
	 * Set current pressed button alpha
	 * @param alpha New alpha value for pressed buttons
	 */
	public void setPressedAlpha(float alpha){ this.pressedAlpha = alpha; }
	
	/** @return Current alpha value of active buttons */
	public float activeAlpha(){ return activeAlpha; }
	/** @return Current alpha value of pressed buttons */
	public float pressedAlpha(){ return pressedAlpha; }
}
