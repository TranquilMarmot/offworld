package com.bitwaffle.guts.entity.ai.states;

import com.bitwaffle.guts.entity.dynamic.DynamicEntity;

public abstract class AIState {
	protected DynamicEntity controlling;
	
	public AIState(DynamicEntity ent){
		this.controlling = ent;
	}
	
	public void setControlling(DynamicEntity ent){
		this.controlling = ent;
	}
	
	public abstract void update(float timeStep);
	
	/** Called when this state becomes the current state */
	public abstract void onLoseCurrentState();
	
	/** Called when this state loses the current state */
	public abstract void onGainCurrentState();
}
