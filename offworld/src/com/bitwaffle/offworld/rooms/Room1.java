package com.bitwaffle.offworld.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.camera.CameraMode;
import com.bitwaffle.guts.graphics.graphics2d.shapes.polygon.Polygon;
import com.bitwaffle.guts.graphics.graphics2d.shapes.polygon.PolygonRenderer;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.offworld.OffworldGame;
import com.bitwaffle.offworld.camera.CameraChangeSensor;
import com.bitwaffle.offworld.entities.BreakableRock;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;
import com.bitwaffle.offworld.entities.pickups.diamond.Diamond;
import com.bitwaffle.offworld.entities.player.input.ControlInfo.SplitScreenSection;
import com.bitwaffle.offworld.physics.OffworldPhysics;


public class Room1 extends Room {
	/** Bounds of room */
	private static float x = 80.0f, y = 60.0f, width = 112.0f, height = 60.0f;
	
	/** Create room */
	public Room1(){
		super(x, y, width, height);
		
		Gdx.gl20.glClearColor(0.4117647058823529f, 0.592156862745098f, 0.8274509803921569f, 1.0f);
		//Gdx.gl20.glClearColor(0.40625f, 0.46875f, 0.78125f, 1.0f);
		
		OffworldPhysics.initPlayer(Game.physics, new Vector2(86.1816f * 3.0f, 24.6180f * 3.0f), 0, true);
		//OffworldPhysics.initPlayer(Game.physics, new Vector2(86.1816f * 3.0f, 20.6180f * 3.0f), 1, false);
		//OffworldPhysics.initPlayer(Game.physics, new Vector2(86.1816f * 3.0f, 10.6180f * 3.0f), 2, false);
		//OffworldPhysics.initPlayer(Game.physics, new Vector2(86.1816f * 3.0f, 4.6180f * 3.0f), 3, false);
		//OffworldGame.players[1].controlInfo.controlledByMouse = false;
		//OffworldGame.players[2].controlInfo.controlledByMouse = false;
		//OffworldGame.players[3].controlInfo.controlledByMouse = false;
		
		// 4 player
		OffworldGame.players[0].controlInfo.screenSection = SplitScreenSection.FULL;
		//OffworldGame.players[1].controlInfo.screenSection = SplitScreenSection.TOP_RIGHT_QUARTER;
		//OffworldGame.players[2].controlInfo.screenSection = SplitScreenSection.BOTTOM_LEFT_QUARTER;
		//OffworldGame.players[3].controlInfo.screenSection = SplitScreenSection.BOTTOM_RIGHT_QUARTER;
		
		/* 3 player
		OffworldGame.players[0].controlInfo.screenSection = SplitScreenSection.TOP_HALF;
		OffworldGame.players[1].controlInfo.screenSection = SplitScreenSection.BOTTOM_LEFT_QUARTER;
		OffworldGame.players[2].controlInfo.screenSection = SplitScreenSection.BOTTOM_RIGHT_QUARTER;
		*/

		this.addGeometry(getDynamicEntity("cave-1-seg1", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addGeometry(getDynamicEntity("cave-1-seg2", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addGeometry(getDynamicEntity("cave-1-platform1", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addGeometry(getDynamicEntity("cave-1-platform2", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		/*
		this.addEntity(getDynamicEntity("cave-1-seg3", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("cave-1-seg4", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("cave-1-platform-1", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("cave-1-platform-2", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("cave-1-platform-3", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("cave-1-platform-4", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		*/
		
		// add random rocks
		for(int i = 0; i < 10; i++){
    		float rockX = Game.random.nextFloat() * 10.0f - 50.0f;
    		if(rockX < 1.0f) rockX = 1.0f;
    		float rockY = Game.random.nextFloat() * 150.0f + 15.0f;
    		if(rockY < 1.0f) rockY = 1.0f;
    		
    		int layer = 4;
    		
    		BreakableRock rock = new BreakableRock(new Vector2(rockX, rockY), 1.0f, layer);
    		this.addEntity(rock);
		}
		
		
		// diamonds
		for(int i = 0; i < 10; i++){
    		//float diamondX = Game.random.nextFloat() * 10.0f - 50.0f;
    		//if(Game.random.nextBoolean()) diamondX = -diamondX;
    		//float diamondY = Game.random.nextFloat() * 15.0f + 15.0f;
    		//if(Game.random.nextBoolean()) diamondY = -diamondY;
    		
    		float diamondX = 86.1816f * 3.0f, diamondY = 27.6180f * 3.0f;
    		
    		float rotation = Game.random.nextFloat() * 10.0f;
    		
    		int layer = 4;
    		
    		Diamond diamond = new Diamond(layer, new Vector2(diamondX, diamondY), rotation);
    		this.addEntity(diamond);
		}
		
		
		// bats
		for(int i = 0; i < 15; i++){
    		float batX = 20.0f + (Game.random.nextFloat() * 2.75f);
    		float batY = -14.20f + (Game.random.nextFloat());
    		//if(Game.random.nextBoolean()) batX = -batX;
    		//if(Game.random.nextBoolean()) batY = -batY;
    		
    		Bat bat = new Bat(4, new Vector2(batX, batY));
    		this.addEntity(bat);
		}
		
		addCameraSensor();
		
		/*
		float batX = 20.0f;
		float batY = -14.20f;
		
		Bat bat = new Bat(4, new Vector2(batX, batY));
		bat.sleeping = true;
		this.addEntity(bat);
		*/
		
		Game.net.startServer();
		
		// create walls
		//makeWalls();
	}
	
	private DynamicEntity getDynamicEntity(String name, int layer, float[] color){
		BodyDef bodyDef = Game.resources.entityInfo.getEntityBodyDef(name);
		bodyDef.position.set(0.0f, 0.0f);
		FixtureDef fixtureDef = Game.resources.entityInfo.getEntityFixtureDef(name);
		Polygon poly = Game.resources.polygons.get(name);
		DynamicEntity tut = new DynamicEntity(new PolygonRenderer(poly, color), layer, bodyDef, fixtureDef);
		return tut;
	}
	
	@SuppressWarnings("unused")
	private Entity getEntity(String name, int layer, float[] color){
		Polygon poly = Game.resources.polygons.get(name);
		Entity ent = new Entity(new PolygonRenderer(poly, color), layer);
		return ent;
	}
	
	private void addCameraSensor(){
		CameraMode mode = new CameraMode(OffworldGame.players[0].getCamera()){
			@Override public void update(float timeStep) {}
		};
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10.0f, 10.0f);
		Vector2 loc = new Vector2(370.0229f, -16.357243f);
		mode.target.set(new Vector2(-340.0229f, 26.357243f));
		mode.interpolate = false;
		mode.zoom = 0.025f;
		CameraChangeSensor sensor = new CameraChangeSensor(mode, shape, loc);
		this.addEntity(sensor);
	}

	@Override
	public void onAddToWorld(Physics physics) {}

	@Override
	public void onRemoveFromWorld(Physics physics) {}
}