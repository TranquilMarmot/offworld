package com.bitwaffle.guts.gui.elements.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;

/**
 * A rectangle button that gets rendered with a given texture
 * with blending turned on.
 * 
 * @author TranquilMarmot
 */
public abstract class TransparentRectangleButton extends RectangleButton {
	
	/*
	 * TODO
	 * - Subimage support?
	 */
	
	/** Color button gets rendered as */
	public Color activeColor, downColor, inactiveColor;
	
	/** Texture used to render button */
	private String texture;
	
	public TransparentRectangleButton(float x, float y, float width, float height, String texture, Color activeColor, Color downColor) {
		super(x, y, width, height);
		this.activeColor = activeColor;
		this.downColor = downColor;
		this.inactiveColor = new Color(activeColor);
		this.texture = texture;
	}
	
	public TransparentRectangleButton(float x, float y, float width, float height, String texture, Color color) {
		this(x, y, width, height, texture, color, new Color(color));
	}

	@Override
	public void render(Renderer renderer, boolean flipHorizontal,
			boolean flipVertical) {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		if(this.isActive()){
			if(this.isDown())
				renderer.r2D.setColor(downColor.r, downColor.g, downColor.b, downColor.a);
			else
				renderer.r2D.setColor(activeColor.r, activeColor.g, activeColor.b, activeColor.a);
		} else {
			renderer.r2D.setColor(inactiveColor.r, inactiveColor.g, inactiveColor.b, inactiveColor.a);
		}
		
		renderer.modelview.rotate(0.0f, 0.0f, 1.0f, this.angle);
		
		Game.resources.textures.bindTexture(texture);
		renderer.r2D.quad.render(this.width, this.height, flipHorizontal, flipVertical);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}
