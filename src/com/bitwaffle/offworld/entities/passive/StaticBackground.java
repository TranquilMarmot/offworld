package com.bitwaffle.offworld.entities.passive;

import android.opengl.Matrix;

import com.badlogic.gdx.math.Vector2;
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
	float width, height;
	
	float xOffset, yOffset;
	
	public StaticBackground(float x, float y, float width, float height){
		super(new Vector2(x, y));
		this.width = width;
		this.height = height;
		this.xOffset = x;
		this.yOffset = y;
	}

	@Override
	public void update(float timeStep) {}

	@Override
	public void render(Render2D renderer) {
		// FIXME why the fuck is it so hard to render a background?!
		Matrix.translateM(renderer.modelview, 0, width + xOffset, height + yOffset, 0);
		renderer.program.setUniformMatrix4f("ModelView", renderer.modelview);
		
		Game.resources.textures.bindTexture("background");
		renderer.quad.draw(renderer, width, height);
	}

	@Override
	public void cleanup() {}
}
