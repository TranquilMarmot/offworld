package com.bitwaffle.offworld.gui.elements.bar;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.gui.elements.GUIObject;

public class Bar extends GUIObject {
	
	/** Percent that bar is filled up */
	private float percent;
	
	public Bar(BarRenderer renderer, float x, float y){
		super(renderer, x, y);
	}
	
	
	public Bar(float x, float y, int columns, int rows, float columnWidth, float rowHeight, Color backgroundColor, Color fillColor){
		super(new BarRenderer(backgroundColor, fillColor, columns, rows, columnWidth, rowHeight), x, y);
	}
	
	/** @return Current percent this bar is at */
	public float currentPercent(){
		return percent;
	}
	
	public void setPercent(float newPercent){
		this.percent = newPercent;
		((BarRenderer)renderer).updatePercent(newPercent);
	}

	@Override
	public void update(float timeStep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
}
