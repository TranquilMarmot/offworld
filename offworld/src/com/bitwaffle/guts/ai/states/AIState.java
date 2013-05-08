package com.bitwaffle.guts.ai.states;

import com.bitwaffle.guts.entity.dynamic.DynamicEntity;

public abstract class AIState {
	DynamicEntity controlling;
	
	public AIState(){
	}
	
	public void setControlling(DynamicEntity ent){
		this.controlling = ent;
	}
	
	public abstract void update(float timeStep);
}
