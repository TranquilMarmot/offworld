package com.bitwaffle.guts.entities;

/**
 * Request to remove an entity from the game.
 * 
 * @author TranquilMarmot
 */
public class EntityRemoveRequest {
	/** Layer to remove entity from */
	public int layer;
	/** Hash of entity being removed */
	public int hash;
}
