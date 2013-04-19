package com.bitwaffle.guts.entities.entities3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;

public class Entity3D {
	public Entity3DRenderer renderer;
	
	protected Vector3 location;
	
	protected Quaternion rotation;
	private float yrot = 0.0f, xrot = 0.0f; // FIXME temp
	
	private Integer hash;
	
	private int layer;
	
	public Entity3D(Entity3DRenderer renderer, int layer, Vector3 location, Quaternion rotation){
		this.renderer = renderer;
		this.layer = layer;
		this.location = location;
		this.rotation = rotation;
	}
	
	public void update(float timeStep) {
		if(Gdx.input.isKeyPressed(Input.Keys.UP))		
			yrot -= 0.85f;
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))			
			xrot -= 0.85f;
		
		rotation.setEulerAngles(xrot, yrot, 0.0f);
		
		if(Game.players[0] != null){
			Vector2 playerLoc = Game.players[0].getLocation();
			this.location.x = playerLoc.x;
			this.location.y = playerLoc.y;
			System.out.println("ent loc: " + location);
		}
	}
	
	public void cleanup(){}
	
	public Vector3 location(){
		return location;
	}
	
	public Quaternion rotation(){
		return rotation;
	}
	
	public int getLayer(){
		return layer;
	}
	
	public void setHash(int hash){
		this.hash = hash;
	}
	
	@Override
	public int hashCode(){
		if(hash == null)
			return super.hashCode();
		else
			return hash;
	}
}
