package com.bitwaffle.offworld.entities;

public class CollisionFilters {
	public static final short NOTHING = 0;
	public static final short ENTITY = bit(0);
	public static final short GROUND = bit(1);
	public static final short PLAYER = bit(2);
	public static final short BULLET = bit(3);
	public static final short PICKUP = bit(4);

	public static final short EVERYTHING = (short)(ENTITY | GROUND | PLAYER | BULLET | GROUND | PICKUP); 

	private static short bit(int x) {
		return (short) (1 << x);
	}
}
