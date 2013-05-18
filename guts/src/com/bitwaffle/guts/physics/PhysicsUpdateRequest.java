package com.bitwaffle.guts.physics;

/**
 * Box2D doesn't play nice with threads, so any updates have to be done
 * in the same thread as the physics thread. Physics keeps a queue of these,
 * and they can be added from any thread and are guaranteed to be executed in the same
 * thread as the physics engine.
 * 
 * There is no guarantee that requests will be handled in the order they are added to the queue.
 * 
 * @author TranquilMarmot
 */
public interface PhysicsUpdateRequest {
	/**
	 * Does whatever you want. Can interact with the physics world.
	 * Will only get run one time, then the request will be removed.
	 */
	public void doRequest();
}
