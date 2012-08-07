package com.bitwaffle.offworld.entities.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.moguts.entities.BoxEntity;
import com.bitwaffle.offworld.interfaces.Health;

/**
 * Die, box, die!
 * 
 * @author TranquilMarmot
 */
public class DestroyableBox extends BoxEntity implements Health{
	private int health;

	public DestroyableBox(BodyDef bodyDef, float width, float height,
			float density, float[] color) {
		super(bodyDef, width, height, density, color);
		health = 100;
	}

	
	public DestroyableBox(BodyDef bodyDef, float width, float height,
			FixtureDef fixtureDef, float[] color) {
		super(bodyDef, width, height, fixtureDef, color);
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

}
