package com.bitwaffle.offworld.gui.elements.bar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.guts.graphics.graphics2d.shapes.TiledBox;

/**
 * A box with a percentage of it filled.
 * 
 * @author TranquilMarmot
 */
public class BarRenderer implements ObjectRenderer2D {
	private static final String
		BUTTON_CORNER = "buttoncorner",
		BUTTON_SEGMENT = "buttonsegment",
		BUTTON_SIDE = "buttonside",
		BUTTON_MIDDLE = "blank";
	
	/** Color of bars */
	private Color backgroundColor, fillColor;
	
	/** Box renderers */
	private TiledBox backgroundRenderer, fillRenderer;
	
	public BarRenderer(Color backgroundColor, Color fillColor, int columns, int rows, float columnWidth, float rowHeight) {
		this.backgroundColor = backgroundColor;
		this.fillColor = fillColor;
		
		backgroundRenderer = new TiledBox(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
		
		fillRenderer = new TiledBox(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
	}

	
	@Override
	public void render(Renderer renderer, Object ent){
		Bar bar = (Bar) ent;
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		renderer.r2D.setColor(backgroundColor);
		backgroundRenderer.render(renderer);
		
		if(bar.currentPercent() >= 1.0f){
			float amount = backgroundRenderer.totalWidth() - fillRenderer.totalWidth();
			renderer.modelview.translate(-amount, 0.0f, 0.0f);
			
			renderer.r2D.setColor(fillColor);
			fillRenderer.render(renderer);
		}
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
	public void updatePercent(float newPercent){
		float fillWidth = backgroundRenderer.totalWidth() * (newPercent / 100.0f);

		if(fillWidth > fillRenderer.totalWidth()){
			while(fillRenderer.totalWidth() < fillWidth)
				fillRenderer.setColumnWidth(fillRenderer.columnWidth() + 0.01f);
		} else if(fillWidth < fillRenderer.totalWidth()){
			while(fillRenderer.totalWidth() > fillWidth)
				fillRenderer.setColumnWidth(fillRenderer.columnWidth() - 0.01f);
		}
	}
	
	public void setBackgroundColor(Color c){
		backgroundColor.set(c);
	}
	
	public void setBackgroundColor(float r, float g, float b, float a){
		backgroundColor.r = r;
		backgroundColor.g = g;
		backgroundColor.b = b;
		backgroundColor.a = a;
	}
	
	public void setFillColor(Color c){
		fillColor.set(c);
	}
	
	public void setFillColor(float r, float g, float b, float a){
		fillColor.r = r;
		fillColor.g = g;
		fillColor.b = b;
		fillColor.a = a;
	}


	public Color currentBackgroundColor() {
		return backgroundColor;
	}
	
	public Color currentFillColor(){
		return fillColor;
	}
}
