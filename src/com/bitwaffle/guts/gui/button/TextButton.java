package com.bitwaffle.guts.gui.button;

import com.bitwaffle.guts.graphics.font.BitmapFont;
import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * A button that displays text
 * 
 * @author TranquilMarmot
 */
public abstract class TextButton extends StretchButton {
	/** Text being displayed on button */
	private String text;
	
	/** Scale that text is rendererd at */
	private float textScale;
	
	/** Color to render text in (black by default) */
	private float[] textColor = {0.0f, 0.0f, 0.0f, 1.0f};
	
	public TextButton(String text, float x, float y, float width, float height) {
		super(x, y, width, height);
		
		this.text = text;
		
		// FIXME is there a better way to determine this? Maybe based on text length?
		// Maybe get width + height of string, then scale that down to fit inside button w/h
		// But maybe it would better if all text buttons are the same size? (Or maybe a minimum size?)
		// Could also have scrolling text!
		textScale = (width + height) / 225.0f;
	}
	
	public void setTextColor(float r, float g, float b, float a){
		textColor[0] = r;
		textColor[1] = g;
		textColor[2] = b;
		textColor[3] = a;
	}
	
	public float[] currentTextColor(){
		return textColor;
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		renderBackground(renderer, flipHorizontal, flipVertical);
		renderText(renderer, flipHorizontal, flipVertical);
	}
	
	public void renderBackground(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		super.render(renderer, flipHorizontal, flipVertical);
	}
	
	public void renderText(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		float stringWidth = renderer.font.stringWidth(text, textScale) - (BitmapFont.FONT_GLYPH_WIDTH * textScale);
		float stringHeight = renderer.font.stringHeight(text, textScale);
		
		renderer.font.drawString(text, renderer, x - (stringWidth / 2.0f), y + stringHeight, textScale, textColor);
	}

}
