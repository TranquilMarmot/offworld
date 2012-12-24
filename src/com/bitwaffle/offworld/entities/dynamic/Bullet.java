package com.bitwaffle.offworld.entities.dynamic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.entities.passive.Decal;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.CollisionFilters;
import com.bitwaffle.offworld.interfaces.Health;
import com.bitwaffle.offworld.renderers.Renderers;

public class Bullet extends BoxEntity {
	static float[] color = { 1.0f, 1.0f, 1.0f, 1.0f };
	
	public static float SCALE = 0.25f;
	public static float WIDTH = 1.0f * SCALE, HEIGHT = 0.379f * SCALE;
	private int DAMAGE = 10;
	
	private DynamicEntity owner;
	
	public Bullet(DynamicEntity owner, float x, float y, float angle, float speed){
		super(Renderers.BULLET.renderer, 5, getBodyDef(x, y, angle, speed), WIDTH, HEIGHT, getFixtureDef(), color);
		this.angle = angle;
		this.owner = owner;
	}
	
	/**
	 * @return Who shot this bullet? WHO, DAMN IT?!
	 */
	public DynamicEntity getOwner(){
		return owner;
	}
	
	/**
	 * Get the body def of a bullet
	 * @param x Initial X location
	 * @param y Initial Y location
	 * @param angle Angle bullet is facing, in degrees
	 * @param speed Speed bullet is moving at
	 * @return Body def for bullet
	 */
	private static BodyDef getBodyDef(float x, float y, float angle, float speed){
		BodyDef boxDef = new BodyDef();
		boxDef.type = BodyDef.BodyType.DynamicBody;
		boxDef.position.set(x, y);
		boxDef.angularVelocity = 0.0f;
		boxDef.angle = MathHelper.toRadians(angle);
		Vector2 spd = new Vector2(speed, 0.0f);
		spd.rotate(angle);
		boxDef.linearVelocity.set(spd);
		boxDef.bullet = true;
		return boxDef;
	}
	
	/**
	 * @return FixtureDef of bullet
	 */
	private static FixtureDef getFixtureDef(){
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.shape = getShape();
		boxFixture.density = 10.0f;
		boxFixture.friction = 0.3f;
		boxFixture.restitution = 0.5f;
		boxFixture.filter.categoryBits = CollisionFilters.BULLET;
		boxFixture.filter.maskBits = (short)(CollisionFilters.ENTITY | CollisionFilters.GROUND | CollisionFilters.BULLET | CollisionFilters.PICKUP);
		return boxFixture;
	}
	
	/**
	 * @return Shape of bullet
	 */
	private static Shape getShape(){
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(WIDTH, HEIGHT);
		return boxShape;
	}
	
	/**
	 * @return How much damage this bullet does
	 */
	public int getDamage(){
		return DAMAGE;
	}
	
	/**
	 * What happens when a bullet hits something
	 * @param contact Contact of hit
	 * @param bullet Bullet entity
	 * @param hit What the bullet is hitting
	 */
	public void collision(Contact contact, DynamicEntity hit){
		if(hit instanceof Health)
			((Health)hit).hurt(this.getDamage());
		
		Vector2 point = contact.getWorldManifold().getPoints()[0];
		Vector2 normal = contact.getWorldManifold().getNormal();
		
		// add spark decal at hit
		Game.physics.addEntity(new Decal(
				Renderers.SPARK.renderer,
				5,
				point,
				MathHelper.toRadians(normal.angle()),
				0.1f),
				true
		);
		
		Game.physics.removeEntity(this, true);
	}
}
