package com.bitwaffle.guts.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.offworld.entities.Player;
import com.bitwaffle.offworld.entities.dynamic.DestroyableBox;
import com.bitwaffle.offworld.entities.dynamic.DestroyableCircle;
import com.bitwaffle.offworld.renderers.Renderers;
import com.bitwaffle.offworld.rooms.Room1;

/**
 * Helper methods for Physics stuff
 * This class is pretty much just here to hold temporary debug methods for testing purposes.
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
	
	/**
	 * Temporary physics initialization
	 * @param physics Physics world to initialize
	 */
	public static void tempInit(Physics physics){
		initPlayer(physics, new Vector2(1.0f, 6.0f));
		Room1 r1 = new Room1();
		physics.setCurrentRoom(r1);
	}
	
	/**
	 * Initializes the player
	 * @param physics
	 */
	private static void initPlayer(Physics physics, Vector2 position){
		float width = 0.83062f, height = 1.8034f;
		
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(position);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width, height);
		
		FixtureDef playerFixture = new FixtureDef();
		playerFixture.shape = boxShape;
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.3f;
		playerFixture.restitution = 0.0f;
		playerFixture.filter.categoryBits = CollisionFilters.PLAYER;
		playerFixture.filter.maskBits = CollisionFilters.EVERYTHING;
		
		Game.player = new Player(Renderers.PLAYER.renderer, 6, playerBodyDef, width, height, playerFixture);
		physics.addEntity(Game.player, false);
		Render2D.camera.setTarget(Game.player);
		Render2D.camera.setMode(Camera.Modes.FOLLOW);
		Render2D.camera.setLocation(Game.player.getLocation());
		SurfaceView.touchHandler.setPlayer(Game.player);
		//DesktopGame.mouse.setPlayer(Game.player);
	}
	
	/**
	 * Make random circle-boxes
	 * @param physics World to add circles to
	 */
	public static void makeRandomCircle(Physics physics){
		float circX = Game.random.nextFloat() * 100.0f - 50.0f;
		if(circX < 1.0f) circX = 1.0f;
		float circY = Game.random.nextFloat() * 50.0f - 25.0f;
		if(circY < 1.0f) circY = 1.0f;
		float radius = Game.random.nextFloat() * 1.5f;
		if(radius < 1.0f) radius = 1.0f;
		float r = Game.random.nextFloat();
		float g = Game.random.nextFloat();
		float b = Game.random.nextFloat();
		
		BodyDef circleDef = new BodyDef();
		circleDef.type = BodyDef.BodyType.DynamicBody;
		circleDef.position.set(circX, circY);
		
		DestroyableCircle circ = new DestroyableCircle(Renderers.CIRCLE.renderer, 5, radius, circleDef, 1.0f, new float[]{r, g, b, 1.0f}){
			@Override
			public void init(World world){
				super.init(world);
				
				this.body.setAngularVelocity(Game.random.nextFloat() * 1.0f);
				
				float linX = Game.random.nextFloat() * 1.0f;
				float linY = Game.random.nextFloat() * 1.0f;
				if(Game.random.nextBoolean()) linX *= -1.0f;
				if(Game.random.nextBoolean()) linY *= -1.0f;
				this.body.setLinearVelocity(linX, linY);
			}
		};
		
		physics.addEntity(circ, true);
	}
	
	/**
	 * Make random boxes
	 */
	public static void makeRandomBox(Physics physics){
		float boxX = Game.random.nextFloat() * 100.0f - 50.0f;
		if(boxX < 1.0f) boxX = 1.0f;
		float boxY = Game.random.nextFloat() * 50.0f - 25.0f;
		if(boxY < 1.0f) boxY = 1.0f;
		float sizeX = Game.random.nextFloat() * 1.5f;
		if(sizeX < 1.0f) sizeX = 1.0f;
		float sizeY = Game.random.nextFloat() * 1.5f;
		if(sizeY < 1.0f) sizeY = 1.0f;
		float r = Game.random.nextFloat();
		float g = Game.random.nextFloat();
		float b = Game.random.nextFloat();
		
		
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
				
				this.body.setAngularVelocity(Game.random.nextFloat() * 1.0f);
				
				float linX = Game.random.nextFloat() * 1.0f;
				float linY = Game.random.nextFloat() * 1.0f;
				if(Game.random.nextBoolean()) linX *= -1.0f;
				if(Game.random.nextBoolean()) linY *= -1.0f;
				this.body.setLinearVelocity(linX, linY);
			}
		};
		physics.addEntity(box, true);
	}
}
