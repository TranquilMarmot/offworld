package com.bitwaffle.moguts.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player extends BoxEntity {
	private static float[] defaultColor = { 0.0f, 1.0f, 0.0f, 1.0f };
	
	public Player(BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef) {
		super(bodyDef, width, height, fixtureDef, defaultColor);
	}

}
