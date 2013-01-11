package com.bitwaffle.guts.gui.states.titlescreen;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.gui.GUIObject;

public class SpinningPizza extends GUIObject {
	
	float pizzaAngle;
	
	float pizzaSize;
	
	float xOffset, yOffset;

	Matrix4f oldModelview;
	
	public SpinningPizza(float pizzaSize, float xOffset, float yOffset) {
		super(0.0f, 0.0f);
		this.pizzaSize = pizzaSize;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		pizzaAngle = 0.0f;
		oldModelview = new Matrix4f();
		
		this.update(1.0f / 60.0f);
	}

	@Override
	public void update(float timeStep) {
		pizzaAngle += timeStep / 5.0f;
		
		this.x = ((float)Game.windowWidth / 2.0f) + xOffset;
		this.y = ((float)Game.windowHeight / 2.0f) + yOffset;
	}

	@Override
	public void render(Render2D renderer, boolean flipHorizontal,
			boolean flipVertical) {
		Matrix4f.load(renderer.modelview, oldModelview);
		renderer.modelview.rotate(pizzaAngle, new Vector3f(0.0f, 0.0f, 1.0f));
		renderer.sendModelViewToShader();
		
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		Game.resources.textures.bindTexture("mainmenupizza");
		renderer.quad.render(pizzaSize, pizzaSize);
		
		
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.sendModelViewToShader();
		Game.resources.textures.bindTexture("mainmenubanner");
		renderer.quad.render(pizzaSize + 5.0f, pizzaSize + 5.0f);
		
	}

	@Override
	public void cleanup() {
	}

}
