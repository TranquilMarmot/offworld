package com.bitwaffle.moguts.entities.passive;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Renderers;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Decal extends Entity{
	float timeToLive, timeLived;
	
	public Decal(){
		super();
	}
	
	public Decal(Renderers renderer, int layer, Vector2 location, float rotation, float timeToLive){
		super(renderer, layer, location, rotation);
		this.timeToLive = timeToLive;
		timeLived = 0.0f;
	}
	
	@Override
	public void update(float timeStep){
		timeLived += timeStep;
		if(timeLived >= timeToLive)
			this.removeFlag = true;
	}
	
	@Override
	public void write(Kryo kryo, Output output){
		super.write(kryo, output);
		
		output.writeFloat(timeToLive);
		output.writeFloat(timeLived);
	}
	
	@Override
	public void read(Kryo kryo, Input input){
		super.read(kryo, input);
		
		this.timeToLive = input.readFloat();
		this.timeLived = input.readFloat();
	}
}
