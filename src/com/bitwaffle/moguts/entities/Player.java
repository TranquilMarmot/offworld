package com.bitwaffle.moguts.entities;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player extends BoxEntity {
	private static float[] defaultColor = { 0.0f, 1.0f, 0.0f, 1.0f };
	
	private Random r;
	
	public Player(BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef) {
		super(bodyDef, width, height, fixtureDef, defaultColor);
		
		r = new Random();
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		this.color[0] = r.nextFloat();
		this.color[1] = r.nextFloat();
		this.color[2] = r.nextFloat();
	}
	
	public void jump(){
		
	}

}
