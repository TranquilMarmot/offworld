package com.bitwaffle.guts.gui.button;

import com.bitwaffle.guts.graphics.font.BitmapFont;
import com.bitwaffle.guts.graphics.render.Renderer;

/**
 * A button that displays text
 * 
 * @author TranquilMarmot
 */
public abstract class TextButton extends TiledButton {
	/** Text being displayed on button */
	private String text;
	
	/** Scale that text is rendererd at */
	private float textScale;
	
	/** Color to render text in (black by default) */
	private float[] textColor = {0.0f, 0.0f, 0.0f, 1.0f};
	
	public TextButton(String text, float textScale, float x, float y, int rows, int columns, float rowWidth, float columnHeight) {
		super(x, y, rows, columns, rowWidth, columnHeight);
		
		this.text = text;
		
		this.textScale = (width + height) / (10.0f * textScale);
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
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		renderBackground(renderer, flipHorizontal, flipVertical);
		renderText(renderer, flipHorizontal, flipVertical);
	}
	
	public void renderBackground(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		super.render(renderer, flipHorizontal, flipVertical);
	}
	
	public void renderText(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		float stringWidth = renderer.r2D.font.stringWidth(text, textScale) - (BitmapFont.FONT_GLYPH_WIDTH * textScale);
		float stringHeight = renderer.r2D.font.stringHeight(text, textScale);
		
		// FIXME I don't... wat? I was having issues centering the text... this sort of works
		if(stringHeight == (BitmapFont.FONT_GLYPH_HEIGHT * textScale))
			stringHeight = -((BitmapFont.FONT_GLYPH_HEIGHT * textScale) * 2.0f);
		else
			stringHeight -= ((BitmapFont.FONT_GLYPH_HEIGHT * textScale) * 2.0f);
		
		
		renderer.r2D.font.drawString(text, renderer, x - (stringWidth / 2.0f), y - (stringHeight / 2.0f), textScale, textColor);
	}

}
