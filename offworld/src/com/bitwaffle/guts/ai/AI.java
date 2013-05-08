package com.bitwaffle.guts.ai;

import com.bitwaffle.guts.ai.states.AIState;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;

public class AI {
	/** Entity that this AI is controlling */
	private DynamicEntity controlling;
	
	/** Current state of AI */
	private AIState currentState;
	
	public AI(DynamicEntity controlling){
		this.controlling = controlling;
	}
	
	public void update(float timeStep){
		currentState.update(timeStep);
	}
	
	public void setState(AIState state){
		state.setControlling(controlling);
		this.currentState = state;
	}
}
