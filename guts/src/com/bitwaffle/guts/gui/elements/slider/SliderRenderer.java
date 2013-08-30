package com.bitwaffle.guts.gui.elements.slider;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButtonRenderer;

public class SliderRenderer extends RectangleButtonRenderer {

	@Override
	public void render(Renderer renderer, Object ent) {
		Slider slider = (Slider) ent;
		
		float 
			trackX = slider.centerX() - slider.x,
			trackY = slider.centerY() - slider.y;
		renderer.modelview.translate(trackX, trackY, 0.0f);
		renderer.r2D.sendMatrixToShader();
		Game.resources.textures.bindTexture("blank");
		renderer.r2D.quad.render(slider.trackWidth(), slider.trackHeight());
		renderer.modelview.translate(-trackX, -trackY, 0.0f);
		renderer.r2D.sendMatrixToShader();
		
		super.render(renderer, ent);
	}

}
