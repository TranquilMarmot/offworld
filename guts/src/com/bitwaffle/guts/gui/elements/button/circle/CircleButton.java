package com.bitwaffle.guts.gui.elements.button.circle;

import com.bitwaffle.guts.gui.elements.button.Button;

/**
 * A circle-shaped button
 * 
 * @author TranquilMarmot
 */
public abstract class CircleButton extends Button{
	/** Radius of circle button */
	private float radius;
	
	public CircleButton(CircleButtonRenderer renderer, float x, float y, float radius) {
		super(renderer, x, y);
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
	public float getWidth(){ return radius; }
	
	@Override
	public float getHeight(){ return radius; }
	
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
