package com.bitwaffle.offworld.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.CircleEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.offworld.interfaces.Health;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class DestroyableCircle extends CircleEntity implements Health, KryoSerializable{
	private int health;
	
	public DestroyableCircle(){
		super();
		health = 100;
	}
	
	public DestroyableCircle(EntityRenderer renderer, int layer, float radius, BodyDef bodyDef, float density, float[] color){
		super(renderer, layer, radius, bodyDef, density, color);
		health = 100;
	}

	public int currentHealth() {
		return health;
	}

	public void hurt(int amount) {
		health -= amount;
		if(health <= 0)
			Game.physics.removeEntity(this, true);
	}

	public void heal(int amount) {
		health += amount;
	}
	
	@Override
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
		
		output.writeInt(health);
	}
	
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		
		this.health = input.readInt();
	}
}
