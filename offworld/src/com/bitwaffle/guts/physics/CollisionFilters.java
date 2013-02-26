package com.bitwaffle.guts.physics;

import com.badlogic.gdx.Gdx;

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
	
	public static short getFilter(String filt){
		if(filt.equalsIgnoreCase("EVERYTHING"))
			return EVERYTHING;
		else if(filt.equalsIgnoreCase("ENTITY"))
			return ENTITY;
		else if(filt.equalsIgnoreCase("GROUND"))
			return GROUND;
		else if(filt.equalsIgnoreCase("PLAYER"))
			return PLAYER;
		else if(filt.equalsIgnoreCase("BULLET"))
			return BULLET;
		else if(filt.equalsIgnoreCase("PICKUP"))
			return PICKUP;
		else if(filt.equalsIgnoreCase("NOTHING"))
			return NOTHING;
		else{
			Gdx.app.error("CollisionFilters", "ERROR! Got unknown filter string (using NOTHING; got " + filt +  ")");
			return NOTHING;
		}
	}
}
