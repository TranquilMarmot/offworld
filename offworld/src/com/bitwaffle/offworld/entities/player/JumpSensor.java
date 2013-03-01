package com.bitwaffle.offworld.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.physics.CollisionFilters;

/**
 * A sensor that gets attached to a player so that the game knows when
 * the player is standing on something and can jump.
 * 
 * The sensor keeps track of how many points of contact it has with other objects.
 * Generally, if there's one contact, the player can jump.
 * The number of contacts gets modified by the ContactHandler.
 * 
 * @author TranquilMarmot
 */
public class JumpSensor {
	/** The actual fixture this sensor is attached to */
	private Fixture fixture;
	
	/** How large the sensor is */
	private float width = 0.5f, height = 0.25f;
	
	/** Sensor location, relative to player's center */
	private float x, y;
	
	/** Current number of contacts sensor has with things */
	private int numContacts;

	/**
	 * Creates a new jump sensor fixture and adds it to the given player's body
	 * @param player Player to add sensor to
	 */
	public JumpSensor(Player player){
		FixtureDef jumpSensorDef = new FixtureDef();
		jumpSensorDef.isSensor = true;
		this.x = 0.0f;
		this.y = -player.getHeight();
		
		// create box shape
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(width, height, new Vector2(x, y), 0.0f);
		jumpSensorDef.shape = sensorShape;
		
		// set filters
		jumpSensorDef.filter.groupIndex = CollisionFilters.PLAYER;
		jumpSensorDef.filter.maskBits = CollisionFilters.EVERYTHING;
		jumpSensorDef.filter.categoryBits = CollisionFilters.PLAYER;
		
		// create fixture and set pointer back to here
		fixture = player.body.createFixture(jumpSensorDef);
		fixture.setUserData(this);
		
		numContacts = 0;
	}
	
	/**
	 * Called by {@link ContactHandler} when sensor hits something
	 */
	public void beginContact(){
		numContacts++;
	}
	
	/**
	 * Called by {@link ContactHandler} when sensor stops hitting something
	 */
	public void endContact(){
		numContacts--;
	}
	
	/**
	 * @return Current number of contacts this sensor has with the world
	 */
	public int numContacts(){
		return numContacts;
	}
	
	/**
	 * @return Fixture this sensor is attached to
	 */
	public Fixture fixture(){
		return fixture;
	}
	
	/**
	 * @return Width of sensor
	 */
	public float getWidth(){
		return width;
	}
	
	/**
	 * @return Height of sensor
	 */
	public float getHeight(){
		return height;
	}
	
	/**
	 * @return X location of sensor, relative to player's center
	 */
	public float getX(){
		return x;
	}
	
	/**
	 * @return Y location of sensor, relative to player's center
	 */
	public float getY(){
		return y;
	}

}
