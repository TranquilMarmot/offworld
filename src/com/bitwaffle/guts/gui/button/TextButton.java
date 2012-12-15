package com.bitwaffle.guts.gui.button;

import com.bitwaffle.guts.graphics.render.Render2D;

public abstract class TextButton extends RectangleButton {
	private String text;
	
	private float textScale;
	
	public TextButton(String text, float x, float y, float width, float height) {
		super(x, y, width, height);
		
		this.text = text;
		
		textScale = width / 100.0f;
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		super.render(renderer, flipHorizontal, flipVertical);
		
		this.width = renderer.font.stringWidth(text, textScale);
		this.height = renderer.font.stringHeight(text, textScale);
		
		renderer.program.setUniform("vColor", 0.0f, 0.0f, 0.0f, 1.0f);
		renderer.font.drawString(text, renderer, x - (width / 2.0f), y + height, textScale);
	}

}
