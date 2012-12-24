package com.bitwaffle.guts.util;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.offworld.entities.CollisionFilters;
import com.bitwaffle.offworld.entities.Player;
import com.bitwaffle.offworld.entities.dynamic.DestroyableBox;
import com.bitwaffle.offworld.entities.dynamic.DestroyableCircle;
import com.bitwaffle.offworld.renderers.Renderers;
import com.bitwaffle.offworld.rooms.Room1;

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
		
		DestroyableCircle circ = new DestroyableCircle(Renderers.CIRCLE.renderer, 5, radius, circleDef, 1.0f, new float[]{r, g, b, 1.0f}){
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
		
		physics.addEntity(circ, true);
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
		
		DestroyableBox box = new DestroyableBox(Renderers.BOX.renderer, 5, boxDef, sizeX, sizeY, boxFixture, new float[]{r, g, b, 1.0f}){
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
		physics.addEntity(box, true);
	}
	
	public static void temp(Physics physics){
		initPlayer(physics);
		Room1 r1 = new Room1();
		physics.setCurrentRoom(r1);
	}
	
	private static void initPlayer(Physics physics){
		// player
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(1.0f, -30.0f);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.83062f, 1.8034f);
		
		FixtureDef playerFixture = new FixtureDef();
		playerFixture.shape = boxShape;
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.3f;
		playerFixture.restitution = 0.0f;
		playerFixture.filter.categoryBits = CollisionFilters.PLAYER;
		playerFixture.filter.maskBits = CollisionFilters.EVERYTHING;
		
		Game.player = new Player(Renderers.PLAYER.renderer, 6, playerBodyDef, 0.83062f, 1.8034f, playerFixture);
		physics.addEntity(Game.player, false);
		Render2D.camera.setTarget(Game.player);
		Render2D.camera.setMode(Camera.Modes.FOLLOW);
		Render2D.camera.setLocation(Game.player.getLocation());
		SurfaceView.touchHandler.setPlayer(Game.player);
		//Game.mouse.setPlayer(Game.player);
	}
	
	public static void temp1(Physics physics){
		// backdrop
		Entity backdrop = new Entity(Renderers.BACKDROP.renderer, 0);
		physics.addEntity(backdrop, false);
		
		// player
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(0.0f, -25.0f);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.83062f, 1.8034f);
		
		FixtureDef playerFixture = new FixtureDef();
		playerFixture.shape = boxShape;
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.3f;
		playerFixture.restitution = 0.0f;
		playerFixture.filter.categoryBits = CollisionFilters.PLAYER;
		playerFixture.filter.maskBits = CollisionFilters.EVERYTHING;
		
		Game.player = new Player(Renderers.PLAYER.renderer, 6, playerBodyDef, 0.83062f, 1.8034f, playerFixture);
		physics.addEntity(Game.player, false);
		Render2D.camera.setTarget(Game.player);
		Render2D.camera.setMode(Camera.Modes.FOLLOW);
		Render2D.camera.setLocation(Game.player.getLocation());
		//SurfaceView.touchHandler.setPlayer(Game.player);
		
		
		class Ground {
			float x, y, width, height;
			
			public Ground(float x, float y, float width, float height){
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
			}
		}
		
		Ground[] grounds = new Ground[]{
			// bottom
			new Ground(0.0f, -30.0f, 45.0f, 1.0f),
			// left
			new Ground(-46.0f, -1.0f, 1.0f, 30.0f),
			// right
			new Ground(46.0f, -1.0f, 1.0f, 30.0f),
			// stairs
			new Ground(-35.0f, -28.0f, 10.0f, 1.0f),
			new Ground(-37.0f, -26.0f, 8.0f, 1.0f),
			new Ground(-39.0f, -24.0f, 6.0f, 1.0f),
			new Ground(-41.0f, -22.0f, 4.0f, 1.0f),
			// floating
			new Ground(-22.0f, -20.0f, 8.0f, 0.5f),
			new Ground(2.0f, -20.0f, 8.0f, 0.5f),
		};
		
		for(Ground g : grounds){
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(g.x, g.y);
			
			BoxEntity ground = new BoxEntity(Renderers.BOX.renderer, 4, bodyDef, g.width, g.height, 0.0f, new float[]{0.5f, 0.5f, 0.5f, 1.0f});
			physics.addEntity(ground, false);
		}
		
		/*
		physics.addEntity(new Entity(){
			@Override
			public void update(float timeStep){
		    	if(Game.physics.numEntities() < 100){
		        	Random r = new Random();
		    		if(r.nextBoolean())
		    			PhysicsHelper.makeRandomBox(Game.physics);
		    		else
		    			PhysicsHelper.makeRandomCircle(Game.physics);	
		    	}
			}
		});
		*/
	}
	
	/**
	 *  FIXME this initialization method is only temporary
	 */
	public static void temp2(Physics physics){
		// backdrop
		Entity backdrop = new Entity(Renderers.BACKDROP.renderer, 0);
		physics.addEntity(backdrop, false);
		
		// ground
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0.0f, -30.0f);
		
		BoxEntity ground = new BoxEntity(Renderers.BOX.renderer, 5, groundBodyDef, 1000.0f, 1.0f, 0.0f, new float[]{0.5f, 0.5f, 0.5f, 1.0f});
		physics.addEntity(ground, false);
		
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
		playerFixture.filter.categoryBits = CollisionFilters.PLAYER;
		playerFixture.filter.maskBits = CollisionFilters.EVERYTHING;
		
		Game.player = new Player(Renderers.PLAYER.renderer, 6, playerBodyDef, 0.83062f, 1.8034f, playerFixture);
		physics.addEntity(Game.player, false);
		Render2D.camera.setTarget(Game.player);
		//SurfaceView.touchHandler.setPlayer(Game.player);
		
		for(int i = 0; i < 35; i++)
			PhysicsHelper.makeRandomBox(physics);
		for(int i = 0; i < 35; i++)
			PhysicsHelper.makeRandomCircle(physics);
	}
}
