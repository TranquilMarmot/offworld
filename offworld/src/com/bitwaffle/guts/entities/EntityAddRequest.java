package com.bitwaffle.guts.entities;

/**
 * Request to add an entity to the game
 * 
 * @author TranquilMarmot
 */
public class EntityAddRequest {
	/** Entity being added */
	public Entity ent;
	/** Hash to add entity with */
	public int hash;
	
	/**
	 * @param ent Entity being added
	 * @param hash Hash to use when adding entity
	 */
	public EntityAddRequest(Entity ent, int hash){
		this.ent = ent;
		this.hash = hash;
		ent.setHashCode(hash);
	}
}
