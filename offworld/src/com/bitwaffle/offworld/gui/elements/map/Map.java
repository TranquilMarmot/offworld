package com.bitwaffle.offworld.gui.elements.map;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.OffworldGame;

public class Map extends GUIObject {

	public Map(float x, float y) {
		super(x, y);
	}

	@Override
	public void update(float timeStep) {
	}

	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical) {
		Room room = Game.physics.currentRoom();
		Iterator<Entity> geomIt = room.getStaticGeometryIterator();
		
		Game.windowHeight /= 2;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		Gdx.gl.glViewport(0, Game.windowHeight, Game.windowWidth, Game.windowHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
		
		renderer.modelview.scale(10.0f, 10.0f, 1.0f);
		renderer.modelview.rotate(0.0f, 0.0f, 1.0f, 180.0f);
		renderer.modelview.rotate(0.0f, 1.0f, 0.0f, 180.0f);
		renderer.modelview.translate(-OffworldGame.players[0].getLocation().x, -OffworldGame.players[0].getLocation().y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		while(geomIt.hasNext()){
			Entity ent = geomIt.next();
			ent.renderer.render(renderer, ent, true);
		}
		
		Game.windowHeight *= 2;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
	}

	@Override
	public void cleanup() {
		
	}
	
}
