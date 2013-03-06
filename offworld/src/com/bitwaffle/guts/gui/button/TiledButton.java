package com.bitwaffle.guts.gui.button;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;

/**
 * A button that stretches in segments that can be any size
 * 
 * @author TranquilMarmot
 */
public abstract class TiledButton extends RectangleButton {
	/** Width/height of each column/row */
	private float columnWidth, rowHeight;
	
	/** How many rows/columns this button has */
	private int rows, columns;
	
	public TiledButton(float x, float y, int columns, int rows, float columnWidth, float rowHeight){
		super(x, y, (columns * columnWidth), (rows * rowHeight));
		this.rows = rows;
		this.columns = columns;
		this.columnWidth = columnWidth;
		this.rowHeight = rowHeight;
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		//float 
		/* draw top row */
		// top-left corner
		renderer.modelview.translate(-columnWidth * (columns - 1), -rowHeight * (rows - 1), 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer, columnWidth, rowHeight, false, false);
		
		// top row segments
		for(int i = 0; i < columns - 2; i++){
			renderer.modelview.translate((columnWidth * 2.0f), 0.0f, 0.0f);
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("buttonsegment").render(renderer, columnWidth, rowHeight, false, false);
		}
		
		// top-right corner
		renderer.modelview.translate((columnWidth * 2.0f), 0.0f, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer, columnWidth, rowHeight, true, false);
		
		/* draw middle rows */
		for(int i = 0; i < rows - 2; i++){
			// translate to row
			renderer.modelview.translate(-(columnWidth * 2.0f) * (columns - 1), (rowHeight * 2.0f), 0.0f);
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("buttonside").render(renderer, columnWidth, rowHeight, false, false);
			
			for(int j = 0; j < columns - 2; j++){
				renderer.modelview.translate(columnWidth * 2.0f, 0.0f, 0.0f);
				renderer.sendModelViewToShader();
				Game.resources.textures.bindTexture("blank");
				renderer.quad.render(columnWidth, rowHeight);	
			}
			
			renderer.modelview.translate((columnWidth * 2.0f), 0.0f, 0.0f);
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("buttonside").render(renderer, columnWidth, rowHeight, true, false);
		}
		
		/* draw bottom row */
		// bottom-right cornerl
		renderer.modelview.translate(0.0f, (rowHeight * 2.0f), 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer, columnWidth, rowHeight, false, true);
		
		// top row segments
		for(int i = 0; i < columns - 2; i++){
			renderer.modelview.translate(-(columnWidth * 2.0f), 0.0f, 0.0f);
			renderer.sendModelViewToShader();
			Game.resources.textures.getSubImage("buttonsegment").render(renderer, columnWidth, rowHeight, false, true);
		}
		
		// bottom-right corner
		renderer.modelview.translate(-(columnWidth * 2.0f), 0.0f, 0.0f);
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer, columnWidth, rowHeight, true, true);
	}
}
