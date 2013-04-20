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
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.render.render2d.camera.Camera2D;
import com.bitwaffle.guts.input.controller.player.OuyaPlayerControllerListener;
import com.bitwaffle.guts.input.controller.player.XboxPlayerControllerListener;
import com.bitwaffle.guts.input.listeners.player.PlayerInputListener;
import com.bitwaffle.offworld.entities.player.Player;
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
		//initPlayer(physics, new Vector2(1.0f, 6.0f));
		Room1 r1 = new Room1();
		physics.setCurrentRoom(r1);
	}
	
	// FIXME get this out of here!!!
	/**
	 * Initializes the player
	 * @param physics
	 */
	public static void initPlayer(Physics physics, Vector2 position, int playerNumber, boolean takeControl){
		Game.players[playerNumber] = new Player(6, position);
		physics.addEntity(Game.players[playerNumber], false);
	
		// TODO have each player press start
		
		if(takeControl){	
			Game.renderer.r2D.camera.setTarget(Game.players[playerNumber]);
			Game.renderer.r2D.camera.setMode(Camera2D.Modes.FOLLOW);
			Game.renderer.r2D.camera.setLocation(Game.players[playerNumber].getLocation());
			
			for(Controller con : Controllers.getControllers()){
				if(con.getName().equals(Ouya.ID)){
					con.addListener(new OuyaPlayerControllerListener(Game.players[playerNumber]));
				}else if(con.getName().contains("XBOX 360")){
					con.addListener(new XboxPlayerControllerListener(Game.players[playerNumber]));
				}
			}
			
			// add player control listener
			Game.input.multiplexer.addProcessor(new PlayerInputListener(Game.players[playerNumber], Game.renderer.r2D.camera));
		}
	}
}
