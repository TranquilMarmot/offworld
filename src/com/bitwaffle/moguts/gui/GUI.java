package com.bitwaffle.moguts.gui;

import java.util.Iterator;

import com.bitwaffle.moguts.gui.buttons.Button;
import com.bitwaffle.moguts.gui.buttons.ButtonManager;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	// FIXME use an EntityList for this? Maybe rename EntityList :P (or just make GUIObject extend entity?)
	// TODO button list is separate at the moment because they get checked on every touch event, probably also need a non-pressable UI list
	public static ButtonManager buttons;
	
	public GUI(){
		buttons = new ButtonManager();
	}
	
	public void update(){
		buttons.update();
	}
	
	public Iterator<Button> getButtonIterator(){
		return buttons.getButtonIterator();
	}
	
	
}
