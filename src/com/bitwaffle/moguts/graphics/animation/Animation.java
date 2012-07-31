package com.bitwaffle.moguts.graphics.animation;

import android.opengl.GLES20;

public class Animation {
	private Frame[] frames;
	
	private Frame currentFrame;
	private int currentFrameIndex;
	private float timeOnFrame;
	
	public Animation(Frame[] frames){
		this.frames = frames;
		timeOnFrame = 0.0f;
		currentFrame = frames[0];
	}
	
	public Animation(Animation other){
		this.frames = other.frames;
		timeOnFrame = 0.0f;
		currentFrame = frames[0];
	}
	
	public void updateAnimation(float timeStep){
		timeOnFrame += timeStep;
		
		if(timeOnFrame >= currentFrame.getLength()){
			currentFrameIndex++;
			if(currentFrameIndex == frames.length)
				currentFrameIndex = 0;
			
			currentFrame = frames[currentFrameIndex];
			timeOnFrame = 0.0f;
		}
	}
	
	public void bindCurrentFrame(){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentFrame.getHandle());
	}
}
