package com.bitwaffle.moguts.gui.button;

import android.opengl.GLES20;

import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.shapes.Quad;
import com.bitwaffle.moguts.resources.Textures;

/**
 * A box-shaped button
 * 
 * @author TranquilMarmot
 */
public class RectangleButton extends Button {
	Quad q;
	
	/** width and height of button */
	private float width, height;
	
	// FIXME these are temporary (should be images!)
	private float[] active = { 0.0f, 1.0f, 0.0f, 1.0f };
	private float[] disabled = { 0.3f, 0.3f, 0.3f, 1.0f };
	private float[] down = { 0.0f, 0.0f, 1.0f, 1.0f };
	
	/**
	 * Create a new rectangular button
	 * @param x X location of button
	 * @param y Y location of button
	 * @param width How wide the button is
	 * @param height How tall the button is
	 */
	public RectangleButton(float x, float y, float width, float height) {
		super(x, y);
		q = new Quad(width, height);
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
	public void update() {
	}

	@Override
	public void draw() {
		if(this.isDown())
			Game.render2D.program.setUniform("vColor", down[0], down[1], down[2], down[3]);
		else if(this.isActive())
			Game.render2D.program.setUniform("vColor", active[0], active[1], active[2], active[3]);
		else
			Game.render2D.program.setUniform("vColor", disabled[0], disabled[1], disabled[2], disabled[3]);
		
		//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Textures.arrowTex);
		q.draw();
	}

	@Override
	public void cleanup() {
		
	}
}
