package com.bitwaffle.guts.entities.entities3d;

public class Entity3DAddRequest {
	public Entity3D ent;
	
	public int hash;
	
	public Entity3DAddRequest(Entity3D ent, int hash){
		this.ent = ent;
		this.hash = hash;
		ent.setHash(hash);
	}
}
