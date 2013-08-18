package com.bitwaffle.offworld.gui.elements.toolbox;

import com.bitwaffle.offworld.entities.player.Player;

/**
 * An in-game menu for accessing maps, inventory, etc.
 * 
 * @author TranquilMarmot
 */
public class Toolbox {
	/** The player that this toolbox belongs to */
	private Player player;
	
	public Toolbox(Player player){
		this.player = player;
	}
}
