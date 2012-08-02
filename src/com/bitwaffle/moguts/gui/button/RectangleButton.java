package com.bitwaffle.moguts.gui.button;

import com.bitwaffle.moguts.graphics.render.Render2D;

/**
 * A box-shaped button
 * 
 * @author TranquilMarmot
 */
public abstract class RectangleButton extends Button {
	/** width and height of button */
	private float width, height;
	
	// FIXME these are temporary-ish
	protected float[] active = { 0.0f, 1.0f, 0.0f, 1.0f };
	protected float[] disabled = { 0.3f, 0.3f, 0.3f, 1.0f };
	protected float[] down = { 0.0f, 0.0f, 1.0f, 1.0f };
	
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
	public void draw(Render2D renderer, boolean flipHorizontal, boolean flipVertical) {
		if(this.isDown())
			renderer.program.setUniform("vColor", down[0], down[1], down[2], down[3]);
		else if(this.isActive())
			renderer.program.setUniform("vColor", active[0], active[1], active[2], active[3]);
		else
			renderer.program.setUniform("vColor", disabled[0], disabled[1], disabled[2], disabled[3]);
		
		renderer.quad.draw(renderer, this.width, this.height, flipHorizontal, flipVertical);
	}

	@Override
	public void cleanup() {}

	@Override
	public abstract void update();

	@Override
	protected abstract void onRelease();

	@Override
	protected abstract void onSlideRelease();
	
	@Override
	protected abstract void onPress();
}
