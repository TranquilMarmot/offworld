package com.bitwaffle.offworld.interfaces;

public interface Health {
	public int currentHealth();
	public void hurt(int amount);
	public void heal(int amount);
}
