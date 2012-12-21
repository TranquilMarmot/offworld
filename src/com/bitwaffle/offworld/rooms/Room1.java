package com.bitwaffle.offworld.rooms;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.android.SurfaceView;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.BoxEntity;
import com.bitwaffle.guts.entities.dynamic.PolygonEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.graphics.render.shapes.Polygon;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.util.PhysicsHelper;
import com.bitwaffle.guts.util.PolygonLoader;
import com.bitwaffle.offworld.entities.CollisionFilters;
import com.bitwaffle.offworld.entities.Player;
import com.bitwaffle.offworld.renderers.Renderers;
import com.bitwaffle.guts.graphics.camera.Camera;

public class Room1 extends Room {

	@Override
	public void addToWorld(Physics physics) {
		addTut(physics);
		addTut2(physics);
		
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
		physics.addDynamicEntity(Game.player);
		Render2D.camera.setTarget(Game.player);
		Render2D.camera.setMode(Camera.Modes.FOLLOW);
		Render2D.camera.setLocation(Game.player.getLocation());
		SurfaceView.touchHandler.setPlayer(Game.player);
		
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
		physics.addEntity(backdrop);
		
		Entity staticbackdrop = new Entity(Renderers.BACKDROP.renderer, 0);
		physics.addEntity(staticbackdrop);
		
		
		physics.addEntity(new Entity(){
			@Override
			public void update(float timeStep){
		    	if(Game.physics.numEntities() < 50){
		        	Random r = new Random();
		    		if(r.nextBoolean())
		    			PhysicsHelper.makeRandomBox(Game.physics);
		    		else
		    			PhysicsHelper.makeRandomCircle(Game.physics);	
		    	}
			}
		});
		
		temp1(physics);
	}
	
	private void addTut(Physics physics){
		Polygon poly = PolygonLoader.loadPolygon("rooms/tut.obj", "rooms/tut-collision.obj", 4.0f, 4.0f);
		int layer = 5;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0.0f, -20.0f);
		//bodyDef.type = BodyDef.BodyType.StaticBody;
		float density = 0.0f;
		float friction = 1.0f;
		float restitution = 0.01f;
		boolean isSensor = false;
		PolygonEntity tut = new PolygonEntity(poly, layer, bodyDef, density, friction, restitution, isSensor){
			@Override
			public void renderPolygon(Render2D renderer){
				Game.resources.textures.bindTexture("tut");
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
				super.renderPolygon(renderer);
			}
		};
		physics.addDynamicEntity(tut);
		Render2D.camera.setTarget(tut);
	}
	
	private void addTut2(Physics physics){
		Polygon poly = PolygonLoader.loadPolygon("rooms/tut2.obj", "rooms/tut2-collision.obj", 4.0f, 4.0f);
		int layer = 5;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(2.0f, -20.0f);
		//bodyDef.type = BodyDef.BodyType.StaticBody;
		float density = 0.0f;
		float friction = 1.0f;
		float restitution = 0.01f;
		boolean isSensor = false;
		PolygonEntity tut = new PolygonEntity(poly, layer, bodyDef, density, friction, restitution, isSensor){
			@Override
			public void renderPolygon(Render2D renderer){
				Game.resources.textures.bindTexture("tut");
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
				super.renderPolygon(renderer);
			}
		};
		physics.addDynamicEntity(tut);
		Render2D.camera.setTarget(tut);
	}

	@Override
	public void removeFromWorld(Physics physics) {
		// TODO Auto-generated method stub
		
	}
	
	private void temp1(Physics physics){
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
			new Ground(0.0f, -60.0f, 45.0f, 1.0f),
			// left
			new Ground(-46.0f, -31.0f, 1.0f, 30.0f),
			// right
			new Ground(46.0f, -31.0f, 1.0f, 30.0f),
			// stairs
			new Ground(-35.0f, -58.0f, 10.0f, 1.0f),
			new Ground(-37.0f, -56.0f, 8.0f, 1.0f),
			new Ground(-39.0f, -54.0f, 6.0f, 1.0f),
			new Ground(-41.0f, -52.0f, 4.0f, 1.0f),
			// floating
			new Ground(-22.0f, -50.0f, 8.0f, 0.5f),
			new Ground(2.0f, -50.0f, 8.0f, 0.5f),
		};
		
		for(Ground g : grounds){
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(g.x, g.y);
			
			BoxEntity ground = new BoxEntity(Renderers.BOX.renderer, 4, bodyDef, g.width, g.height, 0.0f, new float[]{0.5f, 0.5f, 0.5f, 1.0f});
			physics.addDynamicEntity(ground);
		}
	}
}