package com.bitwaffle.guts.graphics.shapes;

import com.badlogic.gdx.math.Matrix4;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Renderer;

/**
 * A box that tiles a given segment, side and middle subimages to create a box.
 * 
 * @author TranquilMarmot
 */
public class TiledBox {
	/** Width/height of each column/row */
	private float columnWidth, rowHeight;
	
	/** How many rows/columns this button has */
	private int rows, columns;
	
	/** Strings of sub-images to render with */
	private String cornerSubImage, segmentSubImage, sideSubImage, middleSubImage;
	
	private static Matrix4 tempMat = new Matrix4();
	
	public void setRows(int rows){ this.rows = rows; }
	public int rows(){ return rows; }
	
	public void setColumns(int columns){ this.columns = columns; }
	public int columns(){ return columns; }
	
	public void setColumnWidth(float width){ this.columnWidth = width; }
	public float columnWidth(){ return columnWidth; }
	
	public void setRowHeight(float height){ this.rowHeight = height; }
	public float rowHeight(){ return rowHeight; }
	
	public float totalWidth(){ return this.columns * this.columnWidth; }
	public float totalHeight(){ return this.rows * this.rowHeight; }
	
	public TiledBox(int columns, int rows, float columnWidth, float rowHeight,
			String cornerSubImage, String segmentSubImage, String sideSubImage, String middleSubImage){
		this.rows = rows;
		this.columns = columns;
		this.columnWidth = columnWidth;
		this.rowHeight = rowHeight;
		this.cornerSubImage = cornerSubImage;
		this.segmentSubImage = segmentSubImage;
		this.sideSubImage = sideSubImage;
		this.middleSubImage = middleSubImage;
	}
	
	public void render(Renderer renderer){
		// save the modelview for the next draw
		tempMat.set(renderer.modelview);
		
		/* draw top row */
		// top-left corner
		renderer.modelview.translate(-columnWidth * (columns - 1), -rowHeight * (rows - 1), 0.0f);
		renderer.r2D.sendMatrixToShader();
		Game.resources.textures.getSubImage(cornerSubImage).render(renderer, columnWidth, rowHeight, false, false);
		
		// top row segments
		for(int i = 0; i < columns - 2; i++){
			renderer.modelview.translate((columnWidth * 2.0f), 0.0f, 0.0f);
			renderer.r2D.sendMatrixToShader();
			Game.resources.textures.getSubImage(segmentSubImage).render(renderer, columnWidth, rowHeight, false, false);
		}
		
		// top-right corner
		renderer.modelview.translate((columnWidth * 2.0f), 0.0f, 0.0f);
		renderer.r2D.sendMatrixToShader();
		Game.resources.textures.getSubImage(cornerSubImage).render(renderer, columnWidth, rowHeight, true, false);
		
		/* draw middle rows */
		for(int i = 0; i < rows - 2; i++){
			// translate to row
			renderer.modelview.translate(-(columnWidth * 2.0f) * (columns - 1), (rowHeight * 2.0f), 0.0f);
			renderer.r2D.sendMatrixToShader();
			Game.resources.textures.getSubImage(sideSubImage).render(renderer, columnWidth, rowHeight, false, false);
			
			for(int j = 0; j < columns - 2; j++){
				renderer.modelview.translate(columnWidth * 2.0f, 0.0f, 0.0f);
				renderer.r2D.sendMatrixToShader();
				Game.resources.textures.bindTexture(middleSubImage);
				renderer.r2D.quad.render(columnWidth, rowHeight);	
			}
			
			renderer.modelview.translate((columnWidth * 2.0f), 0.0f, 0.0f);
			renderer.r2D.sendMatrixToShader();
			Game.resources.textures.getSubImage(sideSubImage).render(renderer, columnWidth, rowHeight, true, false);
		}
		
		/* draw bottom row */
		// bottom-right corner
		renderer.modelview.translate(0.0f, (rowHeight * 2.0f), 0.0f);
		renderer.r2D.sendMatrixToShader();
		Game.resources.textures.getSubImage(cornerSubImage).render(renderer, columnWidth, rowHeight, false, true);
		
		// bottom row segments
		for(int i = 0; i < columns - 2; i++){
			renderer.modelview.translate(-(columnWidth * 2.0f), 0.0f, 0.0f);
			renderer.r2D.sendMatrixToShader();
			Game.resources.textures.getSubImage(segmentSubImage).render(renderer, columnWidth, rowHeight, false, true);
		}
		
		// bottom-right corner
		renderer.modelview.translate(-(columnWidth * 2.0f), 0.0f, 0.0f);
		renderer.r2D.sendMatrixToShader();
		Game.resources.textures.getSubImage(cornerSubImage).render(renderer, columnWidth, rowHeight, true, true);
		
		// reset the modelview to what it was before all this nonsense
		renderer.modelview.set(tempMat);
	}
}
