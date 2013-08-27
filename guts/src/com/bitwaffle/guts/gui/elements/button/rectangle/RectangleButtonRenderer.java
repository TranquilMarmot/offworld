package com.bitwaffle.guts.gui.elements.button.rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;

public class RectangleButtonRenderer implements ObjectRenderer2D {
	/** Color button gets rendered as */
	public Color activeColor, downColor, inactiveColor;
	
	/** Texture used to render button */
	private String texture;
	
	private boolean isSubImage = false;
	
	public boolean flipHorizontal = false, flipVertical = false;
	
	public RectangleButtonRenderer(){
		this("blank", new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f));
	}
	
	public RectangleButtonRenderer(Color activeColor, Color downColor) {
		this("blank", activeColor, downColor);
	}
	
	public RectangleButtonRenderer(String texture, Color activeColor, Color downColor) {
		this(texture, false, activeColor, downColor);
	}
	
	public RectangleButtonRenderer(String texture, boolean isSubImage, Color activeColor, Color downColor) {
		this.isSubImage = isSubImage;
		this.texture = texture;
		this.activeColor = activeColor;
		this.downColor = downColor;
	}
	
	@Override
	public void render(Renderer renderer, Object ent){
		RectangleButton rect = (RectangleButton) ent;
		
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
		
		if(isSubImage){
			Game.resources.textures.getSubImage(texture).render(renderer, rect.width, rect.height, flipHorizontal, flipVertical);
		} else {
			Game.resources.textures.bindTexture(texture);
			renderer.r2D.quad.render(rect.width, rect.height, flipHorizontal, flipVertical);
		}
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}
