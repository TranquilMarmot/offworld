package com.bitwaffle.guts.physics;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.input.controller.player.OuyaPlayerControllerListener;
import com.bitwaffle.guts.input.controller.player.XboxPlayerControllerListener;
import com.bitwaffle.guts.input.listeners.player.PlayerInputListener;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.entities.player.PlayerRenderer;
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
	 * Gets a box shape with a given width and height
	 * @param width Width of box to get
	 * @param height Height of box to get
	 * @param density Desity to give box
	 * @return Box.
	 */
	public static void setFixtureAsBox(FixtureDef fixture, float width, float height){
		PolygonShape box = new PolygonShape();
		box.setAsBox(width, height);
		fixture.shape = box;
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
	
	// FIXME get this out of here!!!
	/**
	 * Initializes the player
	 * @param physics
	 */
	private static void initPlayer(Physics physics, Vector2 position){
		float width = 0.52062f, height = 1.8034f;
		
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(width, height);
		
		FixtureDef playerFixture = new FixtureDef();
		playerFixture.shape = boxShape;
		playerFixture.density = 1.0f;
		playerFixture.friction = 0.3f;
		playerFixture.restitution = 0.0f;
		playerFixture.filter.categoryBits = CollisionFilters.PLAYER;
		playerFixture.filter.maskBits = CollisionFilters.EVERYTHING;
		
		Game.players[0] = new Player(new PlayerRenderer(), 6, playerBodyDef, width, height, playerFixture);
		physics.addEntity(Game.players[0], false);
		Render2D.camera.setTarget(Game.players[0]);
		Render2D.camera.setMode(Camera.Modes.FOLLOW);
		Render2D.camera.setLocation(Game.players[0].getLocation());
		
		// TODO have each player press start
		for(Controller con : Controllers.getControllers()){
			if(con.getName().equals(Ouya.ID))
				con.addListener(new OuyaPlayerControllerListener(Game.players[0]));
			else if(con.getName().contains("XBOX 360"))
				con.addListener(new XboxPlayerControllerListener(Game.players[0]));
		}
		
		// swap button press listener for player control listener
		Game.input.multiplexer.addProcessor(new PlayerInputListener(Game.players[0], Render2D.camera));
		
		
		playerBodyDef.position.set(position);
		//SurfaceView.touchHandler.setPlayer(Game.player);
		//DesktopGame.mouse.setPlayer(Game.player); TODO
	}
}
