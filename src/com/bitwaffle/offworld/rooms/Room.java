package com.bitwaffle.offworld.rooms;

import com.bitwaffle.guts.physics.Physics;

public abstract class Room {
	
	public abstract void addToWorld(Physics physics);
	
	public abstract void removeFromWorld(Physics physics);
}
