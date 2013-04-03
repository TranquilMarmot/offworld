package com.bitwaffle.offworld.interfaces;

/**
 * Something that can get hurt
 * 
 * @author TranquilMarmot
 */
public interface Health {
	/** @return Current health of object */
	public float currentHealth();
	
	/** @param amount Amount to hurt object */
	public void hurt(float amount);
	
	/** @param amount Amount to heal object */
	public void heal(float amount);
}
