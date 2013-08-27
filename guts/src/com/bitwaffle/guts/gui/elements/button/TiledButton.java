package com.bitwaffle.guts.gui.elements.button;

import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.guts.gui.elements.button.rectangle.TiledRectangleRenderer;

/**
 * A button that stretches in segments that can be any size
 * 
 * @author TranquilMarmot
 */
public abstract class TiledButton extends RectangleButton {
	public TiledButton(float x, float y, int columns, int rows, float columnWidth, float rowHeight){
		super(
			new TiledRectangleRenderer(columns, rows, columnWidth, rowHeight),
			x, y, (columns * columnWidth), (rows * rowHeight)
		);
	}
	
	@Override
	public float getWidth(){ return ((TiledRectangleRenderer)renderer).getWidth(); }
	
	@Override
	public float getHeight(){ return ((TiledRectangleRenderer)renderer).getHeight(); }
	

}
