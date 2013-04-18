package com.bitwaffle.guts.entities;

import java.util.HashMap;

import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities3d.Entity3D;

public class EntityLayer {
	public HashMap<Integer, Entity2D> entities2D;
	
	public HashMap<Integer, Entity3D> entities3D;
	
	public EntityLayer(){
		entities2D = new HashMap<Integer, Entity2D>();
		entities3D = new HashMap<Integer, Entity3D>();
	}
}
