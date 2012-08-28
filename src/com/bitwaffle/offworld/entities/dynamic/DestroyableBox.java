package com.bitwaffle.offworld.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.moguts.entities.dynamic.BoxEntity;
import com.bitwaffle.moguts.graphics.render.renderers.Renderers;
import com.bitwaffle.offworld.interfaces.Health;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Die, box, die!
 * This is basically just a BoxEntity that implements Health, and so
 * has a health int.
 * 
 * @author TranquilMarmot
 */
public class DestroyableBox extends BoxEntity implements Health, KryoSerializable{
	private int health;
	
	public DestroyableBox(){
		super();
		health = 100;
	}

	public DestroyableBox(Renderers renderer, BodyDef bodyDef, float width, float height,
			float density, float[] color) {
		super(renderer, bodyDef, width, height, density, color);
		health = 100;
	}

	
	public DestroyableBox(Renderers renderer, BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef, float[] color) {
		super(renderer, bodyDef, width, height, fixtureDef, color);
		health = 100;
	}


	public int currentHealth() {
		return health;
	}


	public void hurt(int amount) {
		health -= amount;
		if(health <= 0)
			this.removeFlag = true;
	}


	public void heal(int amount) {
		health += amount;
	}
	
	@Override
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		
		// read health
		this.health = input.readInt();
	}
	
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
		
		// write health
		output.writeInt(this.health);
	}

}
