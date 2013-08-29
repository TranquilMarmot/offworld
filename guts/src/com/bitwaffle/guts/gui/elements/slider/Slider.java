package com.bitwaffle.guts.gui.elements.slider;

import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;

/**
 * A button that can be slid between any two points.
 * 
 * @author TranquilMarmot
 */
public class Slider extends RectangleButton {
	
	/** Center location of the track */
	private float centerX, centerY;
	
	/** How large the track is rendered*/
	private float trackWidth, trackHeight;
	
	/** Minimum and maximum values that thumb can slide between */
	private float minValue, maxValue;
	
	/** Different directions slider can move in */
	public static enum SlideOrientation{
		HORIZONTAL,
		VERTICAL
	};
	
	/** Which way this slider moves */
	SlideOrientation orientation;

	
	public Slider(
			SlideOrientation orientation, 
			float centerX, float centerY, 
			float trackWidth, float trackHeight, 
			float thumbWidth, float thumbHeight) {
		
		super(new SliderRenderer(), centerX, centerY, thumbWidth, thumbHeight);
		this.centerX = centerX;
		this.centerY = centerY;
		this.trackWidth = trackWidth;
		this.trackHeight = trackHeight;
		this.orientation = orientation;
	}
	
	public float centerX(){ return centerX; }
	public float centerY(){ return centerY; }
	public void setCenterX(float newX){ this.centerX = newX;}
	public void setCenterY(float newY){ this.centerY = newY; }
	public float trackWidth(){ return trackWidth; }
	public float trackHeight(){ return trackHeight; }
	public void setTrackWidth(float newWidth){ trackWidth = newWidth; }
	public void setTrackHeight(float newHeight){ trackHeight = newHeight; }
	

	@Override
	public void update(float timeStep) {
		if(this.x > centerX + trackWidth)
			this.x = centerX + trackWidth - this.width;
		else if(this.x < centerX - trackWidth)
			this.x = centerX - trackWidth + this.width;
		
		if(this.y > centerY + trackHeight - this.height)
			this.y = centerY + trackHeight - this.height;
		else if(this.y < centerY - trackHeight + this.height)
			this.y = centerY - trackHeight + this.height;
	}

	@Override
	protected void onRelease() {
	}

	@Override
	protected void onSlideRelease() {
	}

	@Override
	protected void onPress() {
	}

	@Override
	protected void onSelect() {

	}

	@Override
	protected void onUnselect() {
	}

	@Override
	protected void onDrag(float dx, float dy) {
		switch(orientation){
		case HORIZONTAL:
			this.x += dx;
			if(this.x > centerX + trackWidth)
				this.x = centerX + trackWidth;
			else if(this.x < centerX - trackWidth)
				this.x = centerX - trackWidth;
			break;
			
		case VERTICAL:
			this.y += dy;
			if(this.y > centerY + trackHeight)
				this.y = centerY + trackHeight;
			else if(this.y < centerY - trackHeight)
				this.y = centerY - trackHeight;
		}
	}
	
	public float getValue(){
		return minValue;
	}
}
