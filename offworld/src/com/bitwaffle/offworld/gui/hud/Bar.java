package com.bitwaffle.offworld.gui.hud;

import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.shapes.TiledBox;
import com.bitwaffle.guts.gui.GUIObject;

/**
 * A box with a percentage of it filled.
 * 
 * @author TranquilMarmot
 */
public class Bar extends GUIObject {
	private static final String
		BUTTON_CORNER = "buttoncorner",
		BUTTON_SEGMENT = "buttonsegment",
		BUTTON_SIDE = "buttonside",
		BUTTON_MIDDLE = "blank";
	
	/** Percent that bar is filled up */
	private float percent;
	
	/** Color of bars */
	private float[] backgroundColor, fillColor;
	
	/** Box renderers */
	private TiledBox backgroundRenderer, fillRenderer;
	
	public Bar(float x, float y, int columns, int rows, float columnWidth,
			float rowHeight, float[] backgroundColor, float[] fillColor) {
		super(x, y);
		this.backgroundColor = backgroundColor;
		this.fillColor = fillColor;
		
		backgroundRenderer = new TiledBox(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
		
		fillRenderer = new TiledBox(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
	}

	@Override
	public void update(float timeStep) {}
	
	/**@param percent New percent for this bar to represent */
	public void setPercent(float newPercent){
		this.percent = newPercent;
		float fillWidth = backgroundRenderer.totalWidth() * (percent / 100.0f);
		
		if(fillWidth > fillRenderer.totalWidth()){
			while(fillRenderer.totalWidth() < fillWidth)
				fillRenderer.setColumnWidth(fillRenderer.columnWidth() + 0.01f);
		} else if(fillWidth < fillRenderer.totalWidth()){
			while(fillRenderer.totalWidth() > fillWidth)
				fillRenderer.setColumnWidth(fillRenderer.columnWidth() - 0.01f);
		}
	}
	
	/** @return Current percent this bar is at */
	public float currentPercent(){
		return percent;
	}
	
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.r2D.setColor(backgroundColor);
		backgroundRenderer.render(renderer);
		
		if(percent >= 1.0f){
			float amount = backgroundRenderer.totalWidth() - fillRenderer.totalWidth();
			renderer.modelview.translate(-amount, 0.0f, 0.0f);
			
			renderer.r2D.setColor(fillColor);
			fillRenderer.render(renderer);
		}
	}
	
	public void setBackgroundColor(float r, float g, float b, float a){
		backgroundColor[0] = r;
		backgroundColor[1] = g;
		backgroundColor[2] = b;
		backgroundColor[3] = a;
	}
	
	public void setFillColor(float r, float g, float b, float a){
		fillColor[0] = r;
		fillColor[1] = g;
		fillColor[2] = b;
		fillColor[3] = a;
	}

	@Override
	public void cleanup() {
	}
}
