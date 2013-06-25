package com.bitwaffle.offworld.camera;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.graphics2d.Camera2D.CameraMode;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * A sensor that can change how the camera is behaving
 * 
 * @author TranquilMarmot
 */
public class CameraChangeSensor extends DynamicEntity{
	/** Body this sensor is attached to */
	private Body body;
	/** Fixture this sensor is attached to */
	private Fixture fixture;
	
	private CameraMode 
		/** The mode the camera will be switched to */
		mode,
		/** Mode that camera gets switched to  */
		oldMode;
	
	/** Player that hits this sensor */
	private Player player;
	
	public CameraChangeSensor(CameraMode mode,  Shape shape, Vector2 location){
		super(null, 0, getBodyDef(location), getFixtureDef(shape));
		this.mode = mode;
	}
	
	private static FixtureDef getFixtureDef(Shape shape){
		FixtureDef def = new FixtureDef();
		def.density = 0.0f;
		def.isSensor = true;
		def.shape = shape;
		
		return def;
	}
	
	private static BodyDef getBodyDef(Vector2 position){
		BodyDef def = new BodyDef();
		def.active = true;
		def.allowSleep = true;
		def.angle = 0.0f;
		def.position.set(position);
		def.type = BodyDef.BodyType.StaticBody;
		
		return def;
	}
	
	public Fixture fixture(){ return fixture; }
	
	public void beginContact(Player player){
		if(this.player == null /*&& player.getCamera().currentMode() != this.mode*/){
			this.player = player;
			//this.oldMode = player.getCamera().currentMode();
			Game.renderer.r2D.camera.setMode(mode);
			Game.renderer.r2D.camera.setZoom(0.025f);
		}
	}
	
	public void endContact(Player player){
		//if(player == this.player){
		//	this.player = null;
		//	this.player.getCamera().setMode(oldMode);
		//}
	}
	
	
}
