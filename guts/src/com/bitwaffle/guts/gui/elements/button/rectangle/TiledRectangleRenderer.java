package com.bitwaffle.guts.gui.elements.button.rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.shapes.TiledBox;

public class TiledRectangleRenderer extends RectangleButtonRenderer {
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
	
	public TiledRectangleRenderer(int columns, int rows, float columnWidth, float rowHeight){
		super();
		this.rows = rows;
		this.columns = columns;
		this.columnWidth = columnWidth;
		this.rowHeight = rowHeight;
		tiledBoxRenderer = new TiledBox(columns, rows, columnWidth, rowHeight,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
	}
	
	@Override
	public void render(Renderer renderer, Object obj){
		RectangleButton rect = (RectangleButton) obj;
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		if(rect.isActive()){
			if(rect.isDown())
				renderer.r2D.setColor(downColor.r, downColor.g, downColor.b, downColor.a);
			else
				renderer.r2D.setColor(activeColor.r, activeColor.g, activeColor.b, activeColor.a);
		} else {
			renderer.r2D.setColor(inactiveColor.r, inactiveColor.g, inactiveColor.b, inactiveColor.a);
		}
		
		tiledBoxRenderer.render(renderer);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
	public float getWidth(){
		return columnWidth * columns;
	}
	
	public float getHeight(){
		return rowHeight * rows;
	}
	
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
