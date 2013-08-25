package com.bitwaffle.offworld.gui.elements.map;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
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
	
	private int xOffset = 10, yOffset = -10;
	private int width = 100, height = 100;
	
	float mapZoom = 0.0155f;

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
		
		int viewXOffset = 0, viewYOffset = 0;
		int oldRenderWidth = Game.renderWidth, oldRenderHeight = Game.renderHeight;
		
		Player player = toolbox.getPlayer();
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
		
		Game.renderWidth = Game.renderWidth - width;
		Game.renderHeight = Game.renderHeight - height;
		
		Game.renderAspect = (float) Game.renderWidth / (float) Game.renderHeight;
		Gdx.gl.glViewport(viewXOffset + xOffset + (width / 2), viewYOffset + yOffset + (height / 2), Game.renderWidth, Game.renderHeight);
		//MathHelper.orthoM(renderer.projection, 0, Game.renderWidth, Game.renderHeight, 0, -1, 1);
		MathHelper.orthoM(renderer.projection, 0, Game.renderAspect, 0, 1, -1, 1);
		
		float oldZoom = player.getCamera().currentMode().zoom;
		player.getCamera().zoom = mapZoom;
		Vector2 worldWindowSize = player.getCamera().getWorldWindowSize();
		
		renderer.modelview.idt();
		renderer.modelview.scale(mapZoom, mapZoom, 1.0f);
		renderer.modelview.translate(-player.getLocation().x + worldWindowSize.x, -player.getLocation().y + worldWindowSize.y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		while(geomIt.hasNext()){
			Entity ent = geomIt.next();
			ent.renderer.render(renderer, ent, true);
		}
		
		renderer.modelview.translate(player.getLocation().x, player.getLocation().y, 0.0f);
		renderer.r2D.setColor(1.0f, 0.0f, 0.0f, 0.5f);
		renderer.r2D.quad.render(player.getWidth(), player.getHeight());
		
		Game.renderWidth = oldRenderWidth;
		Game.renderHeight = oldRenderHeight;
		player.getCamera().zoom = oldZoom;
		
		// set projection back to screen-space orthographic projection for GUI
		Gdx.gl.glViewport(0, 0, Game.windowWidth, Game.windowHeight);
		MathHelper.orthoM(renderer.projection, 0, Game.windowWidth, Game.windowHeight, 0, -1, 1);
	}

	@Override
	public void cleanup() {
		
	}
}
