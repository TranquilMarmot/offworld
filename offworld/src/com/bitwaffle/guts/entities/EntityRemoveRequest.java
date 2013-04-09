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
	
	/**
	 * @param layer Layer to remove entity from
	 * @param hash Hash of entity being removed
	 */
	public EntityRemoveRequest(int layer, int hash){
		this.layer = layer;
		this.hash = hash;
	}
}
