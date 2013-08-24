package com.bitwaffle.offworld.gui.elements.map;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.gui.elements.toolbox.Toolbox;

public class Map extends GUIObject {
	
	private Toolbox toolbox;

	public Map(float x, float y, Toolbox toolbox) {
		super(x, y);
		this.toolbox = toolbox;
	}

	@Override
	public void update(float timeStep) {
	}

	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical) {
		Room room = Game.physics.currentRoom();
		Iterator<Entity> geomIt = room.getStaticGeometryIterator();
		
		int xOffset = 0, yOffset = 0;
		int oldWindowWidth = Game.windowWidth, oldWindowHeight = Game.windowHeight;
		
		Player player = toolbox.getPlayer();
		switch(player.controlInfo.screenSection){
		case FULL:
			break;
		case TOP_HALF:
			Game.windowHeight /= 2;
			yOffset = Game.windowHeight;
			break;
		case BOTTOM_HALF:
			Game.windowHeight /= 2;
			break;
		case TOP_LEFT_QUARTER:
			Game.windowWidth /= 2;
			Game.windowHeight /= 2;
			yOffset = Game.windowHeight;
			break;
		case BOTTOM_LEFT_QUARTER:
			Game.windowWidth /= 2;
			Game.windowHeight /= 2;
			break;
		case BOTTOM_RIGHT_QUARTER:
			Game.windowWidth /= 2;
			Game.windowHeight /= 2;
			xOffset = Game.windowWidth;
			break;
			
		case TOP_RIGHT_QUARTER:
			Game.windowWidth /= 2;
			Game.windowHeight /= 2;
			xOffset = Game.windowWidth;
			yOffset = Game.windowHeight;
			break;
		}
		
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		Gdx.gl.glViewport(xOffset, yOffset, Game.windowWidth, Game.windowHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
		
		renderer.modelview.scale(5.0f, 5.0f, 1.0f);
		renderer.modelview.rotate(0.0f, 0.0f, 1.0f, 180.0f);
		renderer.modelview.rotate(0.0f, 1.0f, 0.0f, 180.0f);
		renderer.modelview.translate(-player.getLocation().x, -player.getLocation().y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		while(geomIt.hasNext()){
			Entity ent = geomIt.next();
			ent.renderer.render(renderer, ent, true);
		}
		
		renderer.modelview.translate(player.getLocation().x, player.getLocation().y, 0.0f);
		renderer.r2D.setColor(1.0f, 0.0f, 0.0f, 0.5f);
		renderer.r2D.quad.render(player.getWidth(), player.getHeight());
		
		Game.windowHeight = oldWindowHeight;
		Game.windowWidth = oldWindowWidth;
		Game.aspect = (float) Game.windowWidth / (float) Game.windowHeight;
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
	}

	@Override
	public void cleanup() {
		
	}
}
