package com.bitwaffle.offworld.gui;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.offworld.gui.hud.HUD;

public class OffworldGUI extends GUI {
	
	public OffworldGUI(){
		super();
		this.addObject(new HUD(this));
		this.addObject(new DebugText());
	}

}
