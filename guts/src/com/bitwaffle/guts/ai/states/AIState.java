package com.bitwaffle.guts.ai.states;

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
}
