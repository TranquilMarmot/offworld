package com.bitwaffle.guts.graphics.graphics2d.shapes.quad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.guts.resources.manager.TextureManager;

/**
 * Renders an entity represented by a {@link Quad}
 * 
 * @author TranquilMarmot
 */
public class QuadRenderer implements ObjectRenderer2D {
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
	public void render(Renderer renderer, Object ent) {
		if(renderer.renderDebug){
			// only render is told to render in debug mode
			if(hasDebug)
				renderDebug(renderer, ent);
		} else {
			renderer.r2D.setColor(color);
			renderer.modelview.scale(xScale, yScale, 1.0f);
			renderer.r2D.sendMatrixToShader();
			
			// TODO maybe make blending options somewhere? per renderer?
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			// subimage has a special call that binds texture and draws it with special coords
			if(isSubImage)
				Game.resources.textures.getSubImage(textureName).render(renderer, width, height);
			
			else{
				Game.resources.textures.bindTexture(textureName);
				renderer.r2D.quad.render(width, height);
			}
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}
	
	private void renderDebug(Renderer renderer, Object ent){
		renderer.r2D.setColor(0.0f, 1.0f, 1.0f, 0.4f);
		renderer.modelview.scale(xScale, yScale, 1.0f);
		renderer.r2D.sendMatrixToShader();
		
		Game.resources.textures.bindTexture("blank");
		
		if(isSubImage){
			SubImage sub = Game.resources.textures.getSubImage(textureName);
			renderer.r2D.quad.render(sub.getRenderWidth() * width, sub.getRenderHeight() * width);
		} else{
			renderer.r2D.quad.render(width, height);
		}
	}
}
