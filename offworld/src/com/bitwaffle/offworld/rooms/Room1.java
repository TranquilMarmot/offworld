package com.bitwaffle.offworld.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.entities.entities2d.Entity2D;
import com.bitwaffle.guts.entities.entities3d.DynamicEntity3D;
import com.bitwaffle.guts.entities.entities3d.Entity3DModelRenderer;
import com.bitwaffle.guts.graphics.shapes.polygon.Polygon;
import com.bitwaffle.guts.graphics.shapes.polygon.PolygonRenderer;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.offworld.entities.dynamic.BreakableRock;
import com.bitwaffle.offworld.entities.enemies.bat.Bat;

public class Room1 extends Room {
	/** Bounds of room */
	private static float x = 80.0f, y = 60.0f, width = 112.0f, height = 60.0f;
	
	/** Create room */
	public Room1(){
		super(x, y, width, height);
		
		Gdx.gl20.glClearColor(0.4117647058823529f, 0.592156862745098f, 0.8274509803921569f, 1.0f);

		this.addEntity(getDynamicEntity("intro-seg1", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("intro-seg2", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		this.addEntity(getDynamicEntity("intro-seg3", 5, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		//this.addEntity(getEntity("intro-gradient3", 4, new float[]{ 1.0f, 1.0f, 1.0f, 1.0f}));
		//this.addEntity(getEntity("intro-gradient1", 4, new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
		//this.addEntity(getEntity("intro-underground-background", 3, new float[]{0.0196078431372549f,0.196078431372549f, 0.4274509803921569f, 1.0f});
		
		// add random rocks
		for(int i = 0; i < 0; i++){
    		float rockX = Game.random.nextFloat() * 10.0f - 50.0f;
    		if(rockX < 1.0f) rockX = 1.0f;
    		float rockY = Game.random.nextFloat() * 150.0f + 15.0f;
    		if(rockY < 1.0f) rockY = 1.0f;
    		
    		int layer = 4;
    		
    		BreakableRock rock = new BreakableRock(new Vector2(rockX, rockY), 1.0f, layer);
    		Game.physics.addEntity(rock, true);
    		
    		/*
    		QuadRenderer rend = new QuadRenderer("box", false, true, 2.0f, 2.0f, 1.0f, 1.0f, new float[]{1.0f, 1.0f, 1.0f, 1.0f});
    		BodyDef bod = Game.resources.entityInfo.getEntityBodyDef("rock1");
    		bod.position.set(rockX, rockY);
    		bod.fixedRotation = false;
    		
    		FixtureDef fixt = new FixtureDef();
    		PhysicsHelper.setFixtureAsBox(fixt, 2.0f, 2.0f);
    		fixt.density = 2.0f;
    		
    		DestroyableEntity ent = new DestroyableEntity(rend, layer, bod, fixt, 50);
    		Game.physics.addEntity(ent, true);
    		*/
		}
		
		Bat bat = new Bat(4, new Vector2(70.0f, 19.0f));
		this.addEntity(bat);
		
		
		
		
		BodyDef diamondBody = new BodyDef();
		diamondBody.type = BodyType.DynamicBody;
		diamondBody.position.set(3.0f, 6.0f);
		diamondBody.angle = 30.0f;
		
		FixtureDef diamondFixture = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1.0f, 1.0f);
		diamondFixture.shape = shape;
		diamondFixture.density = 1.0f;
		
		DynamicEntity diamond = new DynamicEntity(null, 6, diamondBody, diamondFixture);
		DynamicEntity3D di = new DynamicEntity3D(new Entity3DModelRenderer(Game.resources.models.getModel("diamond")), diamond);
		this.addEntity(diamond);
		this.addEntity(di);
		
		
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
	private Entity2D getEntity(String name, int layer, float[] color){
		Polygon poly = Game.resources.polygons.get(name);
		Entity2D ent = new Entity2D(new PolygonRenderer(poly, color), layer);
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