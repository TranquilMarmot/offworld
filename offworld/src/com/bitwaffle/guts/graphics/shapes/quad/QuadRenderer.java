package com.bitwaffle.guts.graphics.shapes.quad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.graphics.EntityRenderer;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.SubImage;
import com.bitwaffle.guts.resources.TextureManager;

/**
 * Renders an entity represented by a {@link Quad}
 * 
 * @author TranquilMarmot
 */
public class QuadRenderer implements EntityRenderer {
	/** Name of texture to use for this quad */
	private String textureName;
	
	/**
	 * See the {@link TextureManager} for more details.
	 * If an image is a subimage, then it gets rendered with calculated texture coords (the image is defined as part of a sheet)
	 * Else, the texture gets bound and drawn with normal texture coords (0,0|1,0|1,1|0,1)
	 */
	private boolean isSubImage;
	
	/**
	 * Whether or not to draw this quad when debug rendering is enabled
	 */
	private boolean hasDebug;
	
	/** How the modelview matrix gets scaled before rendeing */
	private float xScale, yScale;
	
	/** Actual width/height that quad gets rendered at */
	private float width, height;
	
	/** Color quad gets rendered at */
	private float[] color;
	
	/**
	 * @param textureName Name of texture to use for to render this quad
	 * @param isSubImage If an image is defined in a sprite sheet, it is a sub image and uses special texture coords. Otherwise, the given texture is bound and used for the whole quad.
	 * @param hasDebug Whether or not to draw this quad when debug drawing is enabled (draws it semi-transparent at the given size)
	 * @param xScale How much to scale the modelview matrix on the X axis before drawing
	 * @param yScale How much to scale the modelview matrix on the Y axis before drawing
	 * @param width Width to draw quad at
	 * @param height Height to draw quad at
	 * @param color Color to use when drawing quad
	 */
	public QuadRenderer(String textureName, boolean isSubImage, boolean hasDebug, float xScale, float yScale, float width, float height, float[] color){
		this.textureName = textureName;
		this.isSubImage = isSubImage;
		this.hasDebug = hasDebug;
		this.xScale = xScale;
		this.yScale = yScale;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		if(renderDebug){
			// only render is told to render in debug mode
			if(hasDebug)
				renderDebug(renderer, ent);
		} else {
			renderer.program.setUniform("vColor", color[0], color[1], color[2], color[3]);
			renderer.modelview.scale(xScale, yScale, 1.0f);
			renderer.sendModelViewToShader();
			
			// TODO maybe make blending options somewhere? per renderer?
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			// subimage has a special call that binds texture and draws it with special coords
			if(isSubImage)
				Game.resources.textures.getSubImage(textureName).render(renderer, width, height);
			
			else{
				Game.resources.textures.bindTexture(textureName);
				renderer.quad.render(width, height);
			}
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}
	
	private void renderDebug(Render2D renderer, Entity ent){
		renderer.program.setUniform("vColor", 0.0f, 1.0f, 1.0f, 0.4f);
		renderer.modelview.scale(xScale, yScale, 1.0f);
		renderer.sendModelViewToShader();
		
		Game.resources.textures.bindTexture("blank");
		
		if(isSubImage){
			SubImage sub = Game.resources.textures.getSubImage(textureName);
			renderer.quad.render(sub.getRenderWidth() * width, sub.getRenderHeight() * width);
		} else{
			renderer.quad.render(width, height);
		}
	}
}
