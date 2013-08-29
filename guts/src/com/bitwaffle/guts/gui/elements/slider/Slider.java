package com.bitwaffle.guts.gui.elements.slider;

import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;

/**
 * A button that can be slid between any two points.
 * 
 * The "thumb" (the thing that slides) is a RectangleButton that the "Slider" object actually refers to.
 * That is, slider.x, slider.y, slider.width, slider.height are all members of the RectangleButton class
 * and represent the current location/size of the thumb.
 * 
 * The "track" is represented by centerX, centerY, trackWidth, trackHeight.
 * 
 * @author TranquilMarmot
 */
public class Slider extends RectangleButton {
	
	/** Center location of the track */
	private float centerX, centerY;
	
	/** How large the track is rendered*/
	private float trackWidth, trackHeight;
	
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
	
	/** @return Normalized position of thumb in track (between 0.0f and 1.0f) */
	public float getValue(){
		switch(orientation){
		case HORIZONTAL:
			return ((this.x - centerX) + (trackWidth - this.width)) / ((trackWidth - this.width) * 2.0f);
		case VERTICAL:
			return ((this.y - centerY) + (trackHeight - this.height)) / ((trackHeight - this.height) * 2.0f);
		}
		return 0.0f;
	}
	
	/**
	 * Returns the position of thumb as if it were between 'min' and 'max'
	 * @param min Min position to clamp to
	 * @param max Max position to clamp to
	 * @return Position of thumb between min and max
	 */
	public float getValue(float min, float max){
		return (this.getValue() * (max - min)) + min;
	}
}
