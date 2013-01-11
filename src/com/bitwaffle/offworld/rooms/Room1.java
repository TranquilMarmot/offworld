package com.bitwaffle.offworld.rooms;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.graphics.render.shapes.Polygon;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.util.PhysicsHelper;
import com.bitwaffle.offworld.renderers.PolygonRenderer;
import com.bitwaffle.offworld.renderers.Renderers;

public class Room1 extends Room {
	/** Bounds of room */
	private static float x = 1.0f, y = -15.0f, width = 17.0f, height = 21.0f;
	
	/**
	 * Create room
	 */
	public Room1(){
		super(x, y, width, height);
		
		GLES20.glClearColor(0.412f, 0.592f, 0.827f, 1.0f);
		
		this.addEntity(tut1());
		this.addEntity(tut2());
		
		// backdrop
		EntityRenderer backdropRenderer = new EntityRenderer(){
			@Override
			public void render(Render2D renderer, Entity ent,
					boolean renderDebug) {
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
				Game.resources.textures.bindTexture("tutback");
				renderer.quad.render(20.0f, 12.0f, false, true);
			}
		};
		Entity backdrop = new Entity(backdropRenderer, 1, new Vector2(0.0f, -22.0f));
		this.addEntity(backdrop);
		
		// random maker
		this.addEntity(new Entity(){
			@Override
			public void update(float timeStep){
		    	if(Game.physics.numEntities() < 30){
		        	//Random r = new Random();
		        	//if(r.nextBoolean())
		    		//	PhysicsHelper.makeRandomBox(Game.physics);
		    		//else
		    			PhysicsHelper.makeRandomRock(Game.physics);
		    	}
			}
		});
		
		// create walls
		makeWalls();
	}
	
	/**
	 * @return Left thingy
	 */
	private DynamicEntity tut1(){
		String polygonName = "tut1";
		int layer = 5;
		BodyDef bodyDef = Game.resources.entityInfo.getEntityBodyDef(polygonName);
		bodyDef.position.set(0.0f, -20.0f);
		FixtureDef fixtureDef = Game.resources.entityInfo.getEntityFixtureDef(polygonName);
		Polygon poly = Game.resources.polygons.get(polygonName);
		DynamicEntity tut = new DynamicEntity(new PolygonRenderer(poly), layer, bodyDef, fixtureDef);
		return tut;
	}
	
	/**
	 * @return Right thingy
	 */
	private DynamicEntity tut2(){
		String polygonName = "tut2";
		int layer = 5;
		BodyDef bodyDef = Game.resources.entityInfo.getEntityBodyDef(polygonName);
		bodyDef.position.set(2.0f, -20.0f);
		FixtureDef fixtureDef = Game.resources.entityInfo.getEntityFixtureDef(polygonName);
		Polygon poly = Game.resources.polygons.get(polygonName);
		DynamicEntity tut = new DynamicEntity(new PolygonRenderer(poly), layer, bodyDef, fixtureDef);
		return tut;
	}
	
	/**
	 * Makes bounds for room
	 */
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

	@Override
	public void onAddToWorld(Physics physics) {}

	@Override
	public void onRemoveFromWorld(Physics physics) {}
}