package com.bitwaffle.guts.entities.passive;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * A decal that disappears after a certain time period
 * 
 * @author TranquilMarmot
 */
public class Decal extends Entity{
	/** How long the decal lives and a timer */
	float timeToLive, timeLived;
	
	/**
	 * Blank constructor for serialization
	 */
	public Decal(){
		super();
	}
	
	/**
	 * Create a new decal
	 * @param renderer Renderer to render decal with
	 * @param layer Which layer to render the decal on
	 * @param location Location of decal
	 * @param rotation Rotation of decal
	 * @param timeToLive How long the decal will live for
	 */
	public Decal(EntityRenderer renderer, int layer, Vector2 location, float rotation, float timeToLive){
		super(renderer, layer, location, rotation);
		this.timeToLive = timeToLive;
		timeLived = 0.0f;
	}
	
	@Override
	public void update(float timeStep){
		// update timer and set remove flag if done
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
