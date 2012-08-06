package com.bitwaffle.offworld.entities.passive;

import android.opengl.Matrix;

import com.bitwaffle.moguts.entities.Entity;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.offworld.Game;

/**
 * A static background that doesn't move.
 * Generally, fills the entire screen and is
 * drawn before anything else.
 * 
 * @author TranquilMarmot
 */
public class StaticBackground extends Entity{
	@Override
	public void update(float timeStep) {}

	@Override
	public void render(Render2D renderer) {
		// FIXME why the fuck is it so hard to render a background?!
		float width = Game.windowWidth / 75.0f;
		float height = Game.windowHeight / 75.0f;
		Matrix.translateM(renderer.modelview, 0, width - (width / 4.5f), height - (height / 10.0f), 0);
		renderer.program.setUniformMatrix4f("ModelView", renderer.modelview);
		
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(renderer, width, height, false, false);
	}

	@Override
	public void cleanup() {}
}
