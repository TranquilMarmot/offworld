package com.bitwaffle.offworld.gui.states.titlescreen;

import com.badlogic.gdx.math.Matrix4;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.GUIObject;

public class SpinningPizza extends GUIObject {
	
	float pizzaAngle;
	
	float pizzaSize;
	
	float xOffset, yOffset;

	Matrix4 oldModelview;
	
	public SpinningPizza(float pizzaSize, float xOffset, float yOffset) {
		super(0.0f, 0.0f);
		this.pizzaSize = pizzaSize;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		pizzaAngle = 0.0f;
		oldModelview = new Matrix4();
		
		this.update(1.0f / 60.0f);
	}

	@Override
	public void update(float timeStep) {
		pizzaAngle += timeStep * 5.0f;
		
		this.x = ((float)Game.windowWidth / 2.0f) + xOffset;
		this.y = ((float)Game.windowHeight / 2.0f) + yOffset;
	}

	@Override
	public void render(Renderer renderer, boolean flipHorizontal,
			boolean flipVertical) {
		oldModelview.set(renderer.modelview);
		renderer.modelview.rotate(0.0f, 0.0f, 1.0f, pizzaAngle);
		renderer.r2D.sendMatrixToShader();
		
		renderer.r2D.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		Game.resources.textures.bindTexture("mainmenupizza");
		renderer.r2D.quad.render(pizzaSize, pizzaSize);
		
		
		renderer.modelview.set(oldModelview);
		renderer.r2D.sendMatrixToShader();
		Game.resources.textures.bindTexture("mainmenubanner");
		renderer.r2D.quad.render(pizzaSize + 5.0f, pizzaSize + 5.0f);
		
	}

	@Override
	public void cleanup() {
	}

}
