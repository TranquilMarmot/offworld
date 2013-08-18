package com.bitwaffle.guts.gui.elements.button;

import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.shapes.TiledBox;

/**
 * A button that stretches in segments that can be any size
 * 
 * @author TranquilMarmot
 */
public abstract class TiledButton extends RectangleButton {
	private static final String
		BUTTON_CORNER = "buttoncorner",
		BUTTON_SEGMENT = "buttonsegment",
		BUTTON_SIDE = "buttonside",
		BUTTON_MIDDLE = "blank";
	
	/** Width/height of each column/row */
	private float columnWidth, rowHeight;
	
	/** How many rows/columns this button has */
	private int rows, columns;
	
	/** Used to render button */
	public TiledBox tiledBoxRenderer;
	
	public TiledButton(float x, float y, int columns, int rows, float columnWidth, float rowHeight){
		super(x, y, (columns * columnWidth), (rows * rowHeight));
		this.rows = rows;
		this.columns = columns;
		this.columnWidth = columnWidth;
		this.rowHeight = rowHeight;
		tiledBoxRenderer = new TiledBox(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
	}
	
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		tiledBoxRenderer.render(renderer);
	}
	
	@Override
	public float getWidth(){ return columnWidth * columns; }
	
	@Override
	public float getHeight(){ return rowHeight * rows; }
	
	public void setColumns(int columns){
		this.columns = columns;
		tiledBoxRenderer.setColumns(columns);
	}
	
	public void setRows(int rows){
		this.rows = rows;
		tiledBoxRenderer.setRows(rows);
	}
	
	public void setColumnWidth(float width){
		this.columnWidth = width;
		tiledBoxRenderer.setColumnWidth(width);
	}
	
	public void setRowHeight(float height){
		this.rowHeight = height;
		tiledBoxRenderer.setRowHeight(height);
	}
}
