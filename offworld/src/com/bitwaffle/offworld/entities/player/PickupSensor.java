package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.offworld.interfaces.Item;

public class PickupSensor {
	
	private Player player;
	
	/** Actual fixture this sensor is attached to */
	private Fixture fixture;
	
	private float radius = 2.0f;
	
	public PickupSensor(Player player){
		this.player = player;
		
		FixtureDef pickupSensorDef = new FixtureDef();
		pickupSensorDef.isSensor = true;
		
		CircleShape circ = new CircleShape();
		circ.setRadius(radius);
		pickupSensorDef.shape = circ;
		
		// create fixture and set pointer back to here
		fixture = player.body.createFixture(pickupSensorDef);
		fixture.setUserData(this);
	}
	
	public void reportContact(DynamicEntity ent){
		if(ent instanceof Item){
			player.backpack.addItem((Item)ent);
		}
	}
	
	public Fixture fixture(){
		return fixture;
	}
}
