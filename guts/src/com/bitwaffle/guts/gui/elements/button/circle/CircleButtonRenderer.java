package com.bitwaffle.guts.gui.elements.button.circle;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;

public class CircleButtonRenderer implements ObjectRenderer2D {
	
	/** Colors button gets rendered in */
	public Color 
		active = new Color(0.0f, 1.0f, 0.0f, 1.0f), 
		disabled = new Color(0.3f, 0.3f, 0.3f, 1.0f), 
		down = new Color(0.0f, 0.0f, 1.0f, 1.0f);
	
	public boolean flipHorizontal = false, flipVertical = false;
	
	private String texture;
	
	public CircleButtonRenderer(String texture){}
	
	public CircleButtonRenderer(String texture, Color active, Color disabled, Color down){
		this.active = active;
		this.disabled = disabled;
		this.down = down;
		this.texture = texture;
	}

	@Override
	public void render(Renderer renderer, Object ent) {
		CircleButton button = (CircleButton) ent;
		if(button.isDown())
			renderer.r2D.setColor(down);
		else if(button.isActive())
			renderer.r2D.setColor(active);
		else
			renderer.r2D.setColor(disabled);
		
		Game.resources.textures.bindTexture(texture);
		
		renderer.r2D.circle.render(button.getRadius(), flipHorizontal, flipVertical);
	}

}
