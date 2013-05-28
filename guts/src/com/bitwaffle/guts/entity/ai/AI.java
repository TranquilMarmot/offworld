package com.bitwaffle.guts.entity.ai;

import com.bitwaffle.guts.entity.ai.states.AIState;
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
		// notify the current state that it's losing the current state
		if(currentState != null)
			currentState.onLoseCurrentState();
		
		if(state != null){
			// set state to control this AI's entity
			state.setControlling(controlling);
			
			// notify state that it's becoming the current state
			state.onGainCurrentState();
		}
		
		this.currentState = state;
	}

	public AIState currentState() { return currentState; }
}
