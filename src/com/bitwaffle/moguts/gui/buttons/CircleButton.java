package com.bitwaffle.moguts.gui.buttons;

import com.bitwaffle.moguts.graphics.render.Render2D;

/**
 * A circle-shaped button
 * 
 * @author TranquilMarmot
 */
public abstract class CircleButton extends Button{
	/** Radius of circle button */
	private float radius;
	
	// FIXME these are temporary-ish
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
	public void draw(Render2D renderer, boolean flipHorizontal,
			boolean flipVertical) {
		if(this.isDown())
			renderer.program.setUniform("vColor", down[0], down[1], down[2], down[3]);
		else if(this.isActive())
			renderer.program.setUniform("vColor", active[0], active[1], active[2], active[3]);
		else
			renderer.program.setUniform("vColor", disabled[0], disabled[1], disabled[2], disabled[3]);
		
		renderer.circle.render(this.radius, flipHorizontal, flipVertical);
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
	public abstract void update();
}
