package com.bitwaffle.moguts.gui.button;

import android.util.Log;

import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.shapes.Quad;

public class RectangleButton extends Button {
	Quad q;
	
	private float width, height;
	
	private float[] active = { 0.0f, 1.0f, 0.0f, 1.0f };
	private float[] disabled = { 0.3f, 0.3f, 0.3f, 1.0f };
	private float[] down = { 0.0f, 0.0f, 1.0f, 1.0f };
	
	public RectangleButton(float x, float y, float width, float height) {
		super(x, y);
		q = new Quad(width, height);
		this.height = height;
		this.width = width;
	}

	@Override
	public void actionPerformed() {
		Log.d("Button", "butt press!");
	}

	@Override
	public boolean checkForPress(float touchX, float touchY) {
		//boolean pressed = r.contains(touchX, touchY);
		boolean pressed = this.contains(touchX, touchY);
		
		//Log.d("Button", "Attempted press at: " + x + " " + y + (pressed ? " success!" : " fail"));
		
		/*
		if(pressed){
			isDown = true;
			wasDown = true;
		} else{
			isDown = false;
		}*/
		
		return pressed;
	}
	
	private boolean contains(float touchX, float touchY){
		return
				touchY > this.y - this.height &&  // top
				touchY < this.y + this.height && // bottom
				touchX > this.x - this.width && // left
				touchX < this.x + this.width;  // right
	}

	@Override
	public void update() {
		//if(!isDown && wasDown){
		//	actionPerformed();
		//	wasDown = false;
		//}
		
		// TODO figure out how to know when button is released
	}

	@Override
	public void draw() {
		if(isDown)
			Game.render2D.program.setUniform("vColor", down[0], down[1], down[2], down[3]);
		else if(isActive)
			Game.render2D.program.setUniform("vColor", active[0], active[1], active[2], active[3]);
		else
			Game.render2D.program.setUniform("vColor", disabled[0], disabled[1], disabled[2], disabled[3]);
		
		q.draw();
	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public void press() {
		// TODO Auto-generated method stub
		isDown = true;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		isDown = false;
		actionPerformed();
	}

}
