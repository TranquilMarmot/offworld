package com.bitwaffle.moguts.util;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.moguts.device.SurfaceView;
import com.bitwaffle.moguts.entities.dynamic.BoxEntity;
import com.bitwaffle.moguts.entities.dynamic.DynamicEntity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.graphics.render.Renderers;
import com.bitwaffle.moguts.physics.Physics;
import com.bitwaffle.offworld.Game;
import com.bitwaffle.offworld.entities.Player;
import com.bitwaffle.offworld.entities.dynamic.DestroyableBox;
import com.bitwaffle.offworld.entities.dynamic.DestroyableCircle;

/**
 * Helper methods for Physics stuff
 * 
 * @author TranquilMarmot
 */
public class PhysicsHelper {
	/**
	 * Get a FixtureDef from a Fixture
	 * @param fixt Fixture to get def from
	 * @return FixtureDef representing Fixture
	 */
	public static FixtureDef getFixtureDef(Fixture fixt){
		FixtureDef def = new FixtureDef();
		
		def.density = fixt.getDensity();
		def.friction = fixt.getFriction();
		def.isSensor = fixt.isSensor();
		def.restitution = fixt.getRestitution();
		def.shape = fixt.getShape();
		
		Filter filter = fixt.getFilterData();
		def.filter.categoryBits = filter.categoryBits;
		def.filter.groupIndex = filter.groupIndex;
		def.filter.maskBits = filter.maskBits;
		
		return def;
	}
	
	/**
	 * Get a BodyDef from a Body
	 * @param bod Body to get def from
	 * @return BodyDef representing body
	 */
	public static BodyDef getBodyDef(Body bod){
		BodyDef def = new BodyDef();
		
		def.active = bod.isActive();
		def.allowSleep = bod.isSleepingAllowed();
		def.angle = bod.getAngle();
		def.angularDamping = bod.getAngularDamping();
		def.angularVelocity = bod.getAngularVelocity();
		def.awake = bod.isAwake();
		def.bullet = bod.isBullet();
		def.fixedRotation = bod.isFixedRotation();
		def.gravityScale = bod.getGravityScale();
		def.linearDamping = bod.getLinearDamping();
		def.linearVelocity.set(bod.getLinearVelocity());
		def.position.set(bod.getPosition());
		def.type = bod.getType();
		
		return def;
	}
	
	/**
	 * Get a dynamic entity from a fixture
	 * @param fixture Fixture to get entity from
	 * @return DynamicEntity from fixture
	 */
	public static DynamicEntity getDynamicEntity(Fixture fixture){
		return (DynamicEntity)fixture.getBody().getUserData();
	}
	
	public static void makeRandomCircle(Physics physics){
		Random randy = new Random();
		float circX = randy.nextFloat() * 100.0f - 50.0f;
		if(circX < 1.0f) circX = 1.0f;
		float circY = randy.nextFloat() * 50.0f - 25.0f;
		if(circY < 1.0f) circY = 1.0f;
		float radius = randy.nextFloat() * 1.5f;
		if(radius < 1.0f) radius = 1.0f;
		float r = randy.nextFloat();
		float g = randy.nextFloat();
		float b = randy.nextFloat();
		
		BodyDef circleDef = new BodyDef();
		circleDef.type = BodyDef.BodyType.DynamicBody;
		circleDef.position.set(circX, circY);
		
		DestroyableCircle circ = new DestroyableCircle(Renderers.CIRCLE, radius, circleDef, 1.0f, new float[]{r, g, b, 1.0f}){
			@Override
			public void init(World world){
				super.init(world);
				
				Random randy = new Random();
				this.body.setAngularVelocity(randy.nextFloat() * 1.0f);
				
				float linX = randy.nextFloat() * 1.0f;
				float linY = randy.nextFloat() * 1.0f;
				if(randy.nextBoolean()) linX *= -1.0f;
				if(randy.nextBoolean()) linY *= -1.0f;
				this.body.setLinearVelocity(linX, linY);
			}
		};
		
		physics.addEntity(circ);
	}
	
	/**
	 * Make random boxes
	 */
	public static void makeRandomBox(Physics physics){
		Random randy = new Random();
		float boxX = randy.nextFloat() * 100.0f - 50.0f;
		if(boxX < 1.0f) boxX = 1.0f;
		float boxY = randy.nextFloat() * 50.0f - 25.0f;
		if(boxY < 1.0f) boxY = 1.0f;
		float sizeX = randy.nextFloat() * 1.5f;
		if(sizeX < 1.0f) sizeX = 1.0f;
		float sizeY = randy.nextFloat() * 1.5f;
		if(sizeY < 1.0f) sizeY = 1.0f;
		float r = randy.nextFloat();
		float g = randy.nextFloat();
		float b = randy.nextFloat();
		
		
		BodyDef boxDef = new BodyDef();
		boxDef.type = BodyDef.BodyType.DynamicBody;
		boxDef.position.set(boxX, boxY);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(sizeX, sizeY);
		
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.shape = boxShape;
		boxFixture.density = 1.0f;
		boxFixture.friction = 0.3f;
		boxFixture.restitution = 0.3f;
		
		DestroyableBox box = new DestroyableBox(Renderers.BOX, boxDef, sizeX, sizeY, boxFixture, new float[]{r, g, b, 1.0f}){
			@Override
			// give it a random spin and speed on init
			public void init(World world){
				super.init(world);
				
				Random randy = new Random();
				this.body.setAngularVelocity(randy.nextFloat() * 1.0f);
				
				float linX = randy.nextFloat() * 1.0f;
				float linY = randy.nextFloat() * 1.0f;
				if(randy.nextBoolean()) linX *= -1.0f;
				if(randy.nextBoolean()) linY *= -1.0f;
				this.body.setLinearVelocity(linX, linY);
			}
		};
		physics.addEntity(box);
	}
	
	/**
	 *  FIXME this initialization method is only temporary
	 */
	public static void temp(Physics physics){
		// ground
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -50.0f);
		
		BoxEntity ground = new BoxEntity(Renderers.BOX, groundBodyDef, 1000.0f, 1.0f, 0.0f, new float[]{0.5f, 0.5f, 0.5f, 1.0f});
		physics.addEntity(ground);
		
		// player
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(0.0f, -15.0f);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.83062f, 1.8034f);
		
		FixtureDef playerFixture = new FixtureDef();
		playerFixture.shape = boxShape;
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.3f;
		playerFixture.restitution = 0.0f;
		
		Game.player = new Player(Renderers.PLAYER, playerBodyDef, 0.83062f, 1.8034f, playerFixture);
		physics.addEntity(Game.player);
		Render2D.camera.follow(Game.player);
		SurfaceView.touchHandler.setPlayer(Game.player);
		
		for(int i = 0; i < 35; i++)
			PhysicsHelper.makeRandomBox(physics);
		for(int i = 0; i < 35; i++)
			PhysicsHelper.makeRandomCircle(physics);
	}
}
