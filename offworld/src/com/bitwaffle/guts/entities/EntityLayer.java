package com.bitwaffle.guts.entities;

import java.util.HashMap;

import com.bitwaffle.guts.entities.entities2d.Entity2D;

public class EntityLayer {
	public HashMap<Integer, Entity2D> entities2D;
	
	public EntityLayer(){
		entities2D = new HashMap<Integer, Entity2D>();
	}
}
