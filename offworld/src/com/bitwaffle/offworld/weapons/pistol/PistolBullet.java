package com.bitwaffle.offworld.weapons.pistol;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.dynamic.BoxEntity;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.entity.passive.Decal;
import com.bitwaffle.guts.graphics.graphics2d.shapes.quad.QuadRenderer;
import com.bitwaffle.guts.physics.CollisionFilters;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.interfaces.Health;

public class PistolBullet extends BoxEntity {
	private static final float SCALE = 0.125f;
	
	/** How much damage the bullet does */ // TODO should this be defined somewhere else? In the constructor?
	private int DAMAGE = 10;
	
	/** The owner of this bullet. The bullet can never hit its owner (see the physics.CollisionHandler) */
	private DynamicEntity owner;
	
	public PistolBullet(DynamicEntity owner, float x, float y, float angle, float speed){
		super(getRenderer(),
				5,
				getBodyDef(x, y, angle, speed),
				getBulletWidth(), getBulletHeight(),
				getFixtureDef());
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
		float width = getBulletWidth();
		float height = getBulletWidth();
		boxShape.setAsBox(width, height);
		return boxShape;
	}
	
	private static QuadRenderer getRenderer(){
		float xScale = SCALE;
		float yScale = SCALE;
		float width = 1.0f;
		float height = 1.0f;
		
		
		return new QuadRenderer("pistolbullet", true, true, xScale, yScale, width, height, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
	}
	
	private static float getBulletWidth(){
		return Game.resources.textures.getSubImage("pistolbullet").getRenderWidth() * SCALE;
	}
	
	private static float getBulletHeight(){
		return Game.resources.textures.getSubImage("pistolbullet").getRenderHeight() * SCALE;
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
				new QuadRenderer("spark", true, false, 0.4f, 0.4f, 0.631f, 1.0f, new float[]{1.0f, 1.0f, 1.0f, 1.0f}),
				5,
				point,
				/*MathHelper.toRadians(*/normal.angle()/*) FIXME? */,
				0.1f),
				true
		);
		
		Game.physics.removeEntity(this, true);	
		
	}
}
