package com.bitwaffle.guts.entities.entities2d;

/**
 * Request to remove an entity from the game.
 * 
 * @author TranquilMarmot
 */
public class Entity2DRemoveRequest {
	/** Layer to remove entity from */
	public int layer;
	/** Hash of entity being removed */
	public int hash;
}
