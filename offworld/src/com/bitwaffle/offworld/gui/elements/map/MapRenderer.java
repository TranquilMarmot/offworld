package com.bitwaffle.offworld.gui.elements.map;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButtonRenderer;
import com.bitwaffle.guts.physics.Room;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Renders a {@link Map} and keeps track of some helpful info
 * 
 * @author TranquilMarmot
 */
public class MapRenderer extends RectangleButtonRenderer{
	/** Color of the map's background  */
	public Color backgroundColor = new Color(0.5f, 0.5f, 0.5f, 0.05f);
	
	/** How big the map is being rendered at */
	private int mapRenderWidth = Game.renderWidth, mapRenderHeight = Game.renderHeight;
	
	/** Location of the map's viewport */
	private int viewXOffset = 0, viewYOffset = 0;
	
	@Override
	public void render(Renderer renderer, Object obj) {
		Map map = (Map) obj;
		Room room = Game.physics.currentRoom();
		if(room == null)
			return;
		Player player = map.toolbox.getPlayer();
		
		// save width/height so they can be restored after rendering
		int oldRenderWidth = Game.renderWidth, oldRenderHeight = Game.renderHeight;
		
		// reset view offsets
		viewXOffset = 0;
		viewYOffset = 0;
		
		// set render size / view offset based on player's screen location
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
		
		// update all our variables
		Game.renderWidth = Game.renderWidth - map.borderWidth;
		Game.renderHeight = Game.renderHeight - map.borderHeight;
		Game.renderAspect = (float) Game.renderWidth / (float) Game.renderHeight;
		viewXOffset += (map.borderWidth / 2) + map.xOffset;
		viewYOffset += (map.borderHeight / 2) + map.yOffset;
		
		// set the player's camera's zoom temporarily to get the size of the world
		float oldZoom = player.getCamera().currentMode().zoom;
		player.getCamera().zoom = map.mapZoom;
		Vector2 worldWindowSize = player.getCamera().getWorldWindowSize();
		
		// set the viewport and porjection matrix to match our new variables
		Gdx.gl.glViewport(viewXOffset, viewYOffset, Game.renderWidth, Game.renderHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.renderAspect, 0, 1, -1, 1);
		renderer.modelview.idt();
		
		// enable blending
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		// draw background box
		renderer.r2D.setColor(backgroundColor);
		Game.resources.textures.bindTexture("blank");
		renderer.r2D.quad.render(worldWindowSize.x, worldWindowSize.y);
		
		// scale and translate to how we want to render our map
		renderer.modelview.scale(map.mapZoom, map.mapZoom, 1.0f);
		renderer.modelview.translate(
				-player.getLocation().x + worldWindowSize.x + map.scrollX,
				-player.getLocation().y + worldWindowSize.y + map.scrollY,
				0.0f
		);
		renderer.r2D.sendMatrixToShader();
		
		// render all of the static geometry in the room
		renderer.renderDebug = true;
		Iterator<Entity> geomIt = room.getStaticGeometryIterator();
		while(geomIt.hasNext()){
			Entity ent = geomIt.next();
			ent.renderer.render(renderer, ent);
		}
		
		// draw player marker
		renderer.modelview.translate(player.getLocation().x, player.getLocation().y, 0.0f);
		renderer.r2D.setColor(1.0f, 0.0f, 0.0f, 0.5f);
		renderer.r2D.quad.render(player.getWidth(), player.getHeight());
		
		renderer.renderDebug = false;
		
		// update variables so things know how big the map is
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
	
	public float viewXOffset(){ return viewXOffset; }
	public float viewYOffset(){ return viewYOffset; }
	
	public float mapRenderWidth(){ return mapRenderWidth; }
	public float mapRenderHeight(){ return mapRenderHeight; }
}
