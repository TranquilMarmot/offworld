package com.bitwaffle.moguts.graphics.animation;

import android.opengl.GLES20;

public class Animation {
	private Frame[] frames;
	private int numFrames;
	
	private Frame currentFrame;
	private int currentFrameIndex;
	private float timeOnFrame;
	
	public Animation(int numFrames){
		this.numFrames = numFrames;
		frames = new Frame[numFrames];
		timeOnFrame = 0.0f;
		currentFrame = frames[0];
	}
	
	public Animation(Animation other){
		this.frames = other.frames;
		this.numFrames = other.numFrames;
		
	}
	
	public void putFrame(Frame frame){
		frames[frame.getIndex()] = frame;
		if(currentFrame == null)
			currentFrame = frame;
	}
	
	public void updateAnimation(float timeStep){
		timeOnFrame += timeStep;
		
		if(timeOnFrame >= currentFrame.getTime()){
			currentFrameIndex++;
			// FIXME use >?
			if(currentFrameIndex == numFrames)
				currentFrameIndex = 0;
			
			currentFrame = frames[currentFrameIndex];
			timeOnFrame = 0.0f;
		}
	}
	
	public void bindCurrentFrame(){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentFrame.getHandle());
	}
}
