package com.bitwaffle.guts.gui.elements.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.font.BitmapFont;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.guts.gui.elements.button.rectangle.TiledRectangleRenderer;

/**
 * A button that displays text
 * 
 * @author TranquilMarmot
 */
public class TextButtonRenderer extends TiledRectangleRenderer {
	/** Text being displayed on button */
	private String text;
	
	/** Scale that text is rendererd at */
	private float textScale;
	
	/** Color to render text in (black by default) */
	private float[] textColor = {0.0f, 0.0f, 0.0f, 1.0f};
	
	public TextButtonRenderer(Color activeColor, Color downColor, Color inactiveColor, Color selectedColor, String text, float textScale, int columns, int rows, float columnWidth, float rowHeight) {
		super(activeColor, downColor, inactiveColor, selectedColor, columns, rows, columnWidth, rowHeight);
		
		this.text = text;
		
		this.textScale = (this.getWidth() + this.getHeight()) / (10.0f * textScale);
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
	public void render(Renderer renderer, Object ent){
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		renderBackground(renderer, ent);
		renderText(renderer, ent);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
	public void renderBackground(Renderer renderer, Object ent){
		super.render(renderer, ent);
	}
	
	public void renderText(Renderer renderer, Object ent){
		GUIObject obj = (GUIObject) ent;
		float stringWidth = renderer.r2D.font.stringWidth(text, textScale) - (BitmapFont.FONT_GLYPH_WIDTH * textScale);
		float stringHeight = renderer.r2D.font.stringHeight(text, textScale);
		
		// FIXME I don't... wat? I was having issues centering the text... this sort of works
		if(stringHeight == (BitmapFont.FONT_GLYPH_HEIGHT * textScale))
			stringHeight = -((BitmapFont.FONT_GLYPH_HEIGHT * textScale) * 2.0f);
		else
			stringHeight -= ((BitmapFont.FONT_GLYPH_HEIGHT * textScale) * 2.0f);
		
		
		renderer.r2D.font.drawString(text, renderer, obj.x - (stringWidth / 2.0f), obj.y - (stringHeight / 2.0f), textScale, textColor);
	}

}
