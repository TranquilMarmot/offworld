package com.bitwaffle.offworld.entities.dynamic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.physics.callbacks.FirstHitRayCastCallback;
import com.bitwaffle.guts.polygon.PolygonRenderer;
import com.bitwaffle.offworld.interfaces.Health;

public class BreakableRock extends DynamicEntity implements Health {
	/** Possible rocks */
	private static String[] rockNames = {"rock1", "rock2", "rock3", "rock4"};
	/** @return Random name from rockNames array */
	private static String getRandomName(){ return rockNames[Game.random.nextInt(rockNames.length)]; }
	
	/**
	 * @return Random gray color 
	 */
	private static float[] getRandomColor(){
		int max = 6, min = 4;
		float color = ((float)Game.random.nextInt(max - min + 1) + min) / 10.0f;
		return new float[]{color, color, color, 1.0f};
	}
	
	/** Scale of this rock, in regards to geometry and rendering data (1.0 == normal) */
	private float scale;
	
	/** How much health this rock has left */
	private int health;
	
	/** Color of this rock */
	private float[] color;
	
	/**
	 * This constructor calls the superclass's constructor
	 * @param renderer
	 * @param layer
	 * @param bodyDef
	 * @param fixtureDef
	 */
	private BreakableRock(EntityRenderer renderer, int layer, BodyDef bodyDef, FixtureDef fixtureDef){
		super(renderer, layer, bodyDef, fixtureDef);
	}
	
	/**
	 * Create a new breakable rock
	 * @param location Location to put rock at
	 * @param scale Scale to create rock at
	 * @param layer Layer to put rock on
	 */
	public BreakableRock(Vector2 location, float scale, int layer){
		this(getRandomName(), location, scale, getRandomColor(), layer);
	}
	
	/**
	 * Create a new breakable rock
	 * @param location Location to put rock at
	 * @param scale Scale to create rock at
	 * @param color Color to draw rock as
	 * @param layer Layer to put rock on
	 */
	public BreakableRock(Vector2 location, float scale, float[] color, int layer){
		this(getRandomName(), location, scale, color, layer);
	}
	
	/**
	 * Create a new breakable rock
	 * @param name Name of model to use
	 * @param location Location to put rock at
	 * @param scale Scale to create rock at
	 * @param color Color to draw rock as
	 * @param layer Layer to put rock on
	 */
	public BreakableRock(String name, Vector2 location, float scale, float[] color, int layer){
		this(
			new PolygonRenderer(Game.resources.polygons.get(name), scale, color),
			layer,
			getBodyDef(name, location),
			getFixtureDef(name, scale)
		);
		this.health = 20;
		this.scale = scale;
		this.color = color;
	}
	
	/**
	 * Gets the body def for a given rock at a given location
	 * @param name Name of rock to get def for
	 * @param location Location to set def to
	 * @return Def from given rock set to given location
	 */
	private static BodyDef getBodyDef(String name, Vector2 location){
		BodyDef bodDef = Game.resources.entityInfo.getEntityBodyDef(name);
		bodDef.position.set(location);
		return bodDef;
	}
	
	/**
	 * Gets the fixture def for a given rock at a given scale
	 * @param name Name of rock to get def for
	 * @param scale Scale to create rock at
	 * @return Def from given rock at given scale
	 */
	private static FixtureDef getFixtureDef(String name, float scale){
		FixtureDef fixtDef = Game.resources.entityInfo.getEntityFixtureDef(name);
		fixtDef.shape = Game.resources.polygons.get(name).getShape(scale);
		return fixtDef;
	}

	@Override
	public void hurt(int amount) {
		health -= amount;
		
		// if no more health, remove from world and add smaller rocks
		if(health <= 0){
			float newScale = this.scale * 0.75f;
			
			if(newScale > 0.3f){
				for(int i = 0; i < 3; i++){
					Vector2 loc = new Vector2(this.getLocation()); // location of new rock
					float factor = Game.random.nextBoolean() ? 5.0f : -5.0f; // factor for how much to move the new rocks TODO magic numbers
					loc.x += (Game.random.nextFloat() * factor) * newScale;
					loc.y += (Game.random.nextFloat() * factor) * newScale;
					
					FirstHitRayCastCallback callback = new FirstHitRayCastCallback();
					Game.physics.rayCast(callback, this.location, loc);
					
					// only add if there's nothing there
					if(callback.getHit() == null){
						Game.physics.addEntity(new BreakableRock(loc, newScale, this.color, this.getLayer()), true);
					} else {
						// randomly step in every direction by a different amount until there's nothing at the given spot
						// TODO formalize this some more- pick a direction, check if there's anything there, if there is, switch direction
						for(int j = 0; j < 15; j++){ // TODO magic numbers
							loc.x -= (Game.random.nextFloat() * factor) * newScale * (Game.random.nextBoolean() ? j : -j);
							Game.physics.rayCast(callback, this.location, loc);
							
							if(callback.getHit() == null){
								Game.physics.addEntity(new BreakableRock(loc, newScale, this.color, this.getLayer()), true);
								break;
							} else {
								loc.y -= (Game.random.nextFloat() * factor) * newScale * (Game.random.nextBoolean() ? j : -j);
								Game.physics.rayCast(callback, this.location, loc);
								
								if(callback.getHit() == null){
									Game.physics.addEntity(new BreakableRock(loc, newScale, this.color, this.getLayer()), true);
									break;
								}
									
							}
						}
					}
				}
			}
			
			Game.physics.removeEntity(this, true);
		}
	}

	@Override
	public void heal(int amount) { health += amount; }
	@Override
	public int currentHealth() { return this.health; }
	
}
