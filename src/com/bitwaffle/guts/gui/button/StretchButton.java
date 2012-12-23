package com.bitwaffle.guts.gui.button;

import org.lwjgl.util.vector.Vector2f;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * A button that stretches in segments that can be any size
 * 
 * @author TranquilMarmot
 */
public abstract class StretchButton extends RectangleButton {
	// TODO segments should be drawn in a for loop based on the ratio of width-height
	// I'm thinking it might be as simple as 2:1 ratio = 2 width segments, 1 height segment

	/*
	public StretchButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	*/
	
	private float segmentWidth, segmentHeight;
	private int rows, columns;
	
	public StretchButton(float x, float y, int rows, int columns, float segmentWidth, float segmentHeight){
		super(x, y, rows * segmentWidth, columns * segmentHeight);
		this.rows = rows;
		this.columns = columns;
		this.segmentWidth = segmentWidth;
		this.segmentHeight = segmentHeight;
	}
	

	public void render(Render2D renderer){
		/*
		 * If there's only one row, just draw 
		 */
		/* draw top row */
		// translate to top left corner
		
		renderer.modelview.translate(new Vector2f(-segmentWidth * columns, -segmentHeight * (rows / 2.0f)));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight, false, false);
	}


	public void render(Render2D renderer, boolean flipVertical, boolean flipHorizontal){
		float segmentWidth = this.width / 3.0f;
		float segmentHeight = this.height / 2.0f;
		
		// translate to top row
		renderer.modelview.translate(new Vector2f(0.0f, -segmentHeight));
		renderer.sendModelViewToShader();
		
		// top middle segment
		Game.resources.textures.getSubImage("buttonsegment").render(renderer.quad, segmentWidth, segmentHeight, false, false);
		
		// top-left corner
		renderer.modelview.translate(new Vector2f(-segmentWidth * 2.0f, 0.0f));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight, false, false);
		
		// bottom-left corner
		renderer.modelview.translate(new Vector2f(0.0f, (segmentHeight * 2.0f) - (segmentHeight / 100.0f)));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight, true, true);
		
		// bottom middle segment
		renderer.modelview.translate(new Vector2f(segmentWidth * 2.0f, 0.0f));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttonsegment").render(renderer.quad, segmentWidth, segmentHeight, false, true);
		
		// bottom-right corner
		renderer.modelview.translate(new Vector2f(segmentWidth * 2.0f, 0.0f));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight, false, true);
		
		// top-right corner
		renderer.modelview.translate(new Vector2f(0.0f, -segmentHeight * 2.0f));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight, true, false);
	}
}
