package com.bitwaffle.guts.entities;

import java.util.HashMap;

import com.bitwaffle.guts.threed.Entity3D;

public class EntityLayer {
	public HashMap<Integer, Entity> entities2D;
	
	public HashMap<Integer, Entity3D> entities3D;
	
	public EntityLayer(){
		entities2D = new HashMap<Integer, Entity>();
		entities3D = new HashMap<Integer, Entity3D>();
	}
}
