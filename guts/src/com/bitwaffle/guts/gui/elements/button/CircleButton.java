package com.bitwaffle.guts.gui.elements.button;

import com.bitwaffle.guts.graphics.Renderer;

/**
 * A circle-shaped button
 * 
 * @author TranquilMarmot
 */
public abstract class CircleButton extends Button{
	/** Radius of circle button */
	private float radius;
	
	// FIXME these are temporary
	protected float[] active = { 0.0f, 1.0f, 0.0f, 1.0f };
	protected float[] disabled = { 0.3f, 0.3f, 0.3f, 1.0f };
	protected float[] down = { 0.0f, 0.0f, 1.0f, 1.0f };
	
	public CircleButton(float x, float y, float radius) {
		super(x, y);
		this.radius = radius;
	}

	@Override
	public boolean contains(float x, float y) {
		return 
				(((x - this.x) * (x - this.x)) + 
				((y - this.y) * (y - this.y)))
			<=
				this.radius * this.radius;
	}
	
	public float getRadius(){
		return radius;
	}
	
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical) {
		if(this.isDown())
			renderer.r2D.setColor(down);
		else if(this.isActive())
			renderer.r2D.setColor(active);
		else
			renderer.r2D.setColor(disabled);
		
		renderer.r2D.circle.render(this.radius, flipHorizontal, flipVertical);
	}
	
	@Override
	public void cleanup() { }

	@Override
	protected abstract void onPress();

	@Override
	protected abstract void onRelease();

	@Override
	protected abstract void onSlideRelease();

	@Override
	public abstract void update(float timeStep);
}
