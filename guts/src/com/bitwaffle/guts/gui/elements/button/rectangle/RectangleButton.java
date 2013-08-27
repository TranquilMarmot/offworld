package com.bitwaffle.guts.gui.elements.button.rectangle;

import com.bitwaffle.guts.gui.elements.button.Button;

/**
 * A box-shaped button
 * 
 * @author TranquilMarmot
 */
public abstract class RectangleButton extends Button {
	/** Width and height of button */
	protected float width, height;
	
	/**
	 * @param x X location of button
	 * @param y Y location of button
	 * @param width How wide the button is
	 * @param height How tall the button is
	 */
	public RectangleButton(RectangleButtonRenderer renderer, float x, float y, float width, float height) {
		super(renderer, x, y);
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
	public float getWidth(){ return width; }
	
	@Override
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
