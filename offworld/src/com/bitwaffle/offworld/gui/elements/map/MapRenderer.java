package com.bitwaffle.offworld.gui.elements.map;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.Player;

public class MapRenderer implements ObjectRenderer2D{
	
	public int mapRenderWidth = Game.renderWidth, mapRenderHeight = Game.renderHeight;
	public int viewXOffset = 0, viewYOffset = 0;
	
	@Override
	public void render(Renderer renderer, Object obj) {
		Map map = (Map) obj;
		
		Room room = Game.physics.currentRoom();
		if(room == null)
			return;
		Iterator<Entity> geomIt = room.getStaticGeometryIterator();
		
		int oldRenderWidth = Game.renderWidth, oldRenderHeight = Game.renderHeight;
		
		Player player = map.toolbox.getPlayer();
		switch(player.controlInfo.screenSection){
		case FULL:
			break;
		case TOP_HALF:
			Game.renderHeight = Game.windowHeight / 2;
			viewYOffset = Game.renderHeight;
			break;
		case BOTTOM_HALF:
			Game.renderHeight = Game.windowHeight / 2;
			break;
		case TOP_LEFT_QUARTER:
			Game.renderWidth = Game.windowWidth / 2;
			Game.renderHeight = Game.windowHeight / 2;
			viewYOffset = Game.renderHeight;
			break;
		case BOTTOM_LEFT_QUARTER:
			Game.renderWidth = Game.windowWidth / 2;
			Game.renderHeight = Game.windowHeight / 2;
			break;
		case BOTTOM_RIGHT_QUARTER:
			Game.renderWidth = Game.windowWidth / 2;
			Game.renderHeight = Game.windowHeight / 2;
			viewXOffset = Game.renderWidth;
			break;
			
		case TOP_RIGHT_QUARTER:
			Game.renderWidth = Game.windowWidth / 2;
			Game.renderHeight = Game.windowHeight / 2;
			viewXOffset = Game.renderWidth;
			viewYOffset = Game.renderHeight;
			break;
		}
		
		Game.renderWidth = Game.renderWidth - map.width;
		Game.renderHeight = Game.renderHeight - map.height;
		Game.renderAspect = (float) Game.renderWidth / (float) Game.renderHeight;
		

		//renderer.modelview.translate(0.0f , -(viewYOffset * 2.0f) + (mapRenderHeight * 2.0f) , 0.0f);
		//renderer.modelview.translate(mapRenderWidth, mapRenderHeight, 0.0f);
		renderer.modelview.translate(viewXOffset + map.xOffset + (map.width / 2) + (Game.renderWidth), viewYOffset + map.yOffset + (map.height / 2) - (Game.renderHeight / 2.0f), 0.0f);
		//map.box.render(renderer);
		
		
		Gdx.gl.glViewport(viewXOffset + map.xOffset + (map.width / 2), viewYOffset + map.yOffset + (map.height / 2), Game.renderWidth, Game.renderHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.renderAspect, 0, 1, -1, 1);
		
		float oldZoom = player.getCamera().currentMode().zoom;
		player.getCamera().zoom = map.mapZoom;
		Vector2 worldWindowSize = player.getCamera().getWorldWindowSize();
		
		renderer.modelview.idt();
		renderer.modelview.scale(map.mapZoom, map.mapZoom, 1.0f);
		renderer.modelview.translate(-player.getLocation().x + worldWindowSize.x, -player.getLocation().y + worldWindowSize.y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		renderer.renderDebug = true;
		while(geomIt.hasNext()){
			Entity ent = geomIt.next();
			ent.renderer.render(renderer, ent);
		}
		
		renderer.modelview.translate(player.getLocation().x, player.getLocation().y, 0.0f);
		renderer.r2D.setColor(1.0f, 0.0f, 0.0f, 0.5f);
		renderer.r2D.quad.render(player.getWidth(), player.getHeight());
		
		renderer.renderDebug = false;
		
		mapRenderWidth = Game.renderWidth;
		mapRenderHeight = Game.renderHeight;
		
		// set render size/zoom back to what it was
		Game.renderWidth = oldRenderWidth;
		Game.renderHeight = oldRenderHeight;
		Game.renderAspect = (float) Game.renderWidth / (float) Game.renderHeight;
		player.getCamera().zoom = oldZoom;
		
		// set projection back to screen-space orthographic projection for GUI
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
	}
}
