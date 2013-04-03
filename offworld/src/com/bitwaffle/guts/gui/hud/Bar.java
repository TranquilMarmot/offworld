package com.bitwaffle.guts.gui.hud;

import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.GUIObject;
import com.bitwaffle.guts.gui.TiledBoxRenderer;

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
	private TiledBoxRenderer backgroundRenderer, fillRenderer;
	
	public Bar(float x, float y, int columns, int rows, float columnWidth,
			float rowHeight, float[] backgroundColor, float[] fillColor) {
		super(x, y);
		this.backgroundColor = backgroundColor;
		this.fillColor = fillColor;
		
		backgroundRenderer = new TiledBoxRenderer(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
		
		fillRenderer = new TiledBoxRenderer(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
	}

	@Override
	public void update(float timeStep) {}
	
	/**@param percent New percent for this bar to represent */
	public void setPercent(float newPercent){
		float oldPercent = percent;
		this.percent = newPercent;
		
		float fillWidth = backgroundRenderer.totalWidth() * (percent / 100.0f);
		
		if((newPercent - oldPercent) < 0.0f){
			while(fillRenderer.totalWidth() < fillWidth)
				fillRenderer.setColumnWidth(fillRenderer.columnWidth() + 0.01f);
		} else if((newPercent - oldPercent) > 0.0f){
			while(fillRenderer.totalWidth() > fillWidth)
				fillRenderer.setColumnWidth(fillRenderer.columnWidth() - 0.01f);
		}
	}
	
	/** @return Current percent this bar is at */
	public float currentPresent(){
		return percent;
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.program.setUniform("vColor", backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
		backgroundRenderer.render(renderer);
		
		float amount = backgroundRenderer.totalWidth() - fillRenderer.totalWidth();
		renderer.modelview.translate(-amount, 0.0f, 0.0f);
		
		renderer.program.setUniform("vColor", fillColor[0], fillColor[1], fillColor[2], fillColor[3]);
		fillRenderer.render(renderer);
	}

	@Override
	public void cleanup() {
	}
}
