package com.bitwaffle.offworld.gui;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.offworld.gui.elements.DebugText;

public class OffworldGUI extends GUI {
	
	public OffworldGUI(){
		super();
		this.addObject(new DebugText());
	}

}
