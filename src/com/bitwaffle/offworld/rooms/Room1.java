package com.bitwaffle.offworld.rooms;

import android.opengl.GLES20;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.physics.PhysicsHelper;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.polygon.Polygon;
import com.bitwaffle.offworld.renderers.PolygonRenderer;

public class Room1 extends Room {
	/** Bounds of room */
	private static float x = 80.0f, y = 60.0f, width = 112.0f, height = 60.0f;
	
	/**
	 * Create room
	 */
	public Room1(){
		super(x, y, width, height);
		
		GLES20.glClearColor(0.4117647058823529f, 0.592156862745098f, 0.8274509803921569f, 1.0f);

		this.addEntity(getDynamicEntity("intro-seg1", 5,new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("intro-seg2", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("intro-seg3", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		//this.addEntity(getEntity("intro-gradient3", 4, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		//this.addEntity(getEntity("intro-gradient1", 4, new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
		//this.addEntity(getEntity("intro-underground-background", 3, new float[]{0.0196078431372549f,0.196078431372549f, 0.4274509803921569f, 1.0f});
		
		// random maker
		this.addEntity(new Entity(){
			@Override
			public void update(float timeStep){
		    	if(Game.physics.numEntities() < 15){
		        	//Random r = new Random();
		        	//if(r.nextBoolean())
		    		//	PhysicsHelper.makeRandomBox(Game.physics);
		    		//else
		    			PhysicsHelper.makeRandomRock(Game.physics);
		    	}
			}
		});
		
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
	
	/**
	 * Makes bounds for room
	 */
	/*
	private void makeWalls(){
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
			new Ground(19.0f, -10.0f, 1.0f, 50.0f),
			new Ground(-17.0f, -10.0f, 1.0f, 50.0f),
			new Ground(0.0f, -34.7f, 50.0f, 1.0f)
		};
		
		for(Ground g : grounds){
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(g.x, g.y);
			
			BoxEntity ground = new BoxEntity(Renderers.BOX.renderer, 4, bodyDef, g.width, g.height, 0.0f, new float[]{0.5f, 0.5f, 0.5f, 1.0f});
			this.addEntity(ground);
		}
	}
	*/

	@Override
	public void onAddToWorld(Physics physics) {}

	@Override
	public void onRemoveFromWorld(Physics physics) {}
}