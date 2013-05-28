package com.bitwaffle.guts.ai;

import com.bitwaffle.guts.ai.states.AIState;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;

/**
 * Contains an AI state that can control a DynamicEntity.
 * Should be updated with the owning entity's update method and timeStep.
 * 
 * @author TranquilMarmot
 */
public class AI {
	/** Entity that this AI is controlling */
	protected DynamicEntity controlling;
	
	/** Current state of AI */
	private AIState currentState;
	
	/** @param controlling Entity this AI belongs to */
	public AI(DynamicEntity controlling){
		this.controlling = controlling;
	}
	
	public void update(float timeStep){
		if(currentState != null)
			currentState.update(timeStep);
	}
	
	public void setState(AIState state){
		if(currentState != null){
			currentState.onLoseCurrentState();
		}
		if(state != null){
			state.setControlling(controlling);
			state.onGainCurrentState();
		}
		this.currentState = state;
	}

	public AIState currentState() { return currentState; }
}
