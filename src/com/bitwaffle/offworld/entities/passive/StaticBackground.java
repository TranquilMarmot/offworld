package com.bitwaffle.offworld.entities.passive;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

/**
 * A background.
 * 
 * @author TranquilMarmot
 */
public class StaticBackground extends Entity{
	float width, height;
	
	public StaticBackground(float x, float y, float width, float height){
		super(new Vector2(x, y));
		this.width = width;
		this.height = height;
	}

	@Override
	public void update(float timeStep) {
		if(Render2D.camera != null){
			Vector2 cameraLoc = Render2D.camera.getLocation();
			float cameraZoom = Render2D.camera.getZoom();
			this.location.set((cameraLoc.x * cameraZoom) + this.location.x, (cameraLoc.y * cameraZoom) + this.location.y);
		}
	}

	@Override
	public void render(Render2D renderer) {
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(renderer, width, height, true, true);
	}

	@Override
	public void cleanup() {
	}

}
