package com.bitwaffle.guts.gui.button;

import org.lwjgl.util.vector.Matrix4f;
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
	
	/** Used to preserve modelview between transitions */
	private Matrix4f oldModelview;

	public StretchButton(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		oldModelview = new Matrix4f();
	}

	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		float segmentWidth = this.width / 3.0f;
		float segmentHeight = this.height / 2.0f;
		
		// top-left corner
		Matrix4f.load(renderer.modelview, oldModelview);
		renderer.modelview.translate(new Vector2f(-segmentWidth * 3.0f, -segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight, false, false);
		
		
		// top-right corner
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.modelview.translate(new Vector2f(segmentWidth * 3.0f, -segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight, true, false);
		
		
		// bottom-left corner
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.modelview.translate(new Vector2f(-segmentWidth * 3.0f, segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight + 1.0f, true, true);
		
		
		// bottom-right corner
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.modelview.translate(new Vector2f(segmentWidth * 3.0f, segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttoncorner").render(renderer.quad, segmentWidth, segmentHeight + 1.0f, false, true);
		
		// top segment 1
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.modelview.translate(new Vector2f(-segmentWidth, -segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttonsegment").render(renderer.quad, segmentWidth, segmentHeight, false, false);
		
		// top segment 3
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.modelview.translate(new Vector2f(segmentWidth, -segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttonsegment").render(renderer.quad, segmentWidth, segmentHeight, false, false);
		
		
		// bottom segment 1
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.modelview.translate(new Vector2f(-segmentWidth, segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttonsegment").render(renderer.quad, segmentWidth, segmentHeight+ 1.0f, false, true);
		
		// bottom segment 3
		Matrix4f.load(oldModelview, renderer.modelview);
		renderer.modelview.translate(new Vector2f(segmentWidth, segmentHeight));
		renderer.sendModelViewToShader();
		Game.resources.textures.getSubImage("buttonsegment").render(renderer.quad, segmentWidth, segmentHeight + 1.0f, false, true);
	}
}
