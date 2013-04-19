package com.bitwaffle.guts.gui.button;

import com.bitwaffle.guts.graphics.render.render2d.Render2D;

/**
 * A box-shaped button
 * 
 * @author TranquilMarmot
 */
public abstract class RectangleButton extends Button {
	/** Width and height of button */
	protected float width, height;
	
	/**
	 * Create a new rectangular button
	 * @param x X location of button
	 * @param y Y location of button
	 * @param width How wide the button is
	 * @param height How tall the button is
	 */
	public RectangleButton(float x, float y, float width, float height) {
		super(x, y);
		this.height = height;
		this.width = width;
	}

	@Override
	public boolean contains(float touchX, float touchY) {
		return
				touchY > this.y - this.height &&  // top
				touchY < this.y + this.height && // bottom
				touchX > this.x - this.width && // left
				touchX < this.x + this.width;  // right
	}

	@Override
	public abstract void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical);
	
	public float getWidth(){ return width; }
	public float getHeight(){ return height; }
	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
	}

	@Override
	public void cleanup() {}

	@Override
	protected abstract void onRelease();

	@Override
	protected abstract void onSlideRelease();
	
	@Override
	protected abstract void onPress();
}
