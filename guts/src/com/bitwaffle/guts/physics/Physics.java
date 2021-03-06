package com.bitwaffle.guts.physics;

import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.physics.Entities.EntityHashMap;
import com.bitwaffle.guts.physics.callbacks.FirstHitQueryCallback;

/**
 * Handles all physics workings
 * 
 * @author TranquilMarmot
 */
public abstract class Physics {
	/** World that everything is in */
	private World world;
	
	/** The current room */
	private Room currentRoom;
	
	/** Entities in the world */
	private Entities entities;
	
	/** Gravity for the world */
	private Vector2 gravity = new Vector2(0.0f, -9.8f);
	
	/** Whether or not to sleep entities when they don't move for a bit */
	boolean doSleep = true;
	
	/** How many iterations to do for calculations */
	final int velocityIterations = 6, positionIterations = 2;
	
	/**
	 * Sometimes, DynamicEntities can get added in the middle of a physics tick.
	 * When the entity is added to the physics world, this can cause the game to crash
	 * without a stack trace (since the crash is in the C++ native code)
	 * So, each DynamicEntity has an init() method that adds it to the physics world.
	 * Any time a DynamicEntity is added (via the addEntity() method in this class)
	 * it gets added to this stack, then at the beginning of every update() the stack
	 * gets emptied and whatever was on it has its init() method called.
	 */
	private Stack<DynamicEntity> toInitialize;
	
	/**
	 * Since box2D doesn't play well with threads (see comment on toInitialize stack),
	 * entities have to be updated from the thread that physics is running in.
	 * Requests can be added from any thread, and every tick this queue is emptied
	 * and runs all requests.
	 * There is no guarantee that the requests will be handled in the order they are added.
	 */
	private ConcurrentLinkedQueue<PhysicsUpdateRequest> updateRequests;
	
	public Physics(){
		entities = new Entities();
		toInitialize = new Stack<DynamicEntity>();
		updateRequests = new ConcurrentLinkedQueue<PhysicsUpdateRequest>();
		
		// initialize the world
		initWorld();
	}
	
	/**
	 * Initializes a new Physics world
	 * WARNING: Everything in the world will be destroyed! Use with caution!
	 */
	private void initWorld(){
		if(world != null)
			world.dispose();
		
		world = new World(gravity, doSleep);
		world.setContactListener(getContactListener());
		world.setContactFilter(getContactFilter());
	}
	
	protected abstract ContactListener getContactListener();
	protected abstract ContactFilter getContactFilter();
	
	/** Steps the physics simulation and updates every entity's location */
	public void update(float timeStep){
		// initialize any entities that need intialization
		while(!toInitialize.isEmpty()){
			DynamicEntity ent = toInitialize.pop();
			ent.init(world);
			if(Game.net.server != null)
				Game.net.server.entityAddedNotification(ent);
		}
		
		while(!updateRequests.isEmpty())
			updateRequests.poll().doRequest();

		// this makes everything move around
		world.step(timeStep, velocityIterations, positionIterations);
		
		// this actually calls each entity's update() method
		entities.update(timeStep);
		
		// this updates any entities added/removed to the current room
		if(currentRoom != null)
			currentRoom.update(timeStep);
	}
	
	/** Set the current room in the Physics world */
	public void setCurrentRoom(Room newRoom){
		if(currentRoom != null)
			currentRoom.removeFromWorld(this);
		
		if(newRoom != null)
			newRoom.addToWorld(this);
		
		currentRoom = newRoom;
	}
	
	/** @return The current room */
	public Room currentRoom(){ return currentRoom; }
	
	/**
	 * Add an Entity to the Entities list
	 * @param addToCurrentRoom Whether or not to add the entity to the current room as well (if added, it gets removed on room change)
	 */
	public void addEntity(Entity ent, boolean addToCurrentRoom){
		this.addEntity(ent, ent.hashCode(), addToCurrentRoom);
	}
	
	/**
	 * Add an Entity to the Entities list with a specific hash
	 * @param addToCurrentRoom Whether or not to add the entity to the current room as well (if added, it gets removed on room change)
	 */
	public void addEntity(Entity ent, int hash, boolean addToCurrentRoom){
		entities.addEntity(ent, hash);
		
		// add to current room as well, if told to
		if(addToCurrentRoom && currentRoom != null)
			currentRoom.addEntity(ent);
		
		// DynamicEntity needs to be initialized when it's added
		if(ent instanceof DynamicEntity)
			toInitialize.push((DynamicEntity)ent);
	}
	
	
	/**
	 * Remove an entity from the world
	 * @param removeFromCurrentRoom Whether or not to remove the entity from the current room as well (generally, yes)
	 */
	public void removeEntity(Entity ent, boolean removeFromCurrentRoom){
		this.removeEntity(ent, ent.hashCode(), removeFromCurrentRoom);
	}
	
	/**
	 * Remove an entity from the world with a specific hash
	 * @param removeFromCurrentRoom Whether or not to remove the entity from the current room as well (generally, yes)
	 */
	public void removeEntity(Entity ent, int hash, boolean removeFromCurrentRoom){
		entities.removeEntity(ent, hash);
		
		// remove from current room as well
		if(removeFromCurrentRoom && currentRoom != null)
			currentRoom.removeEntity(ent);
		
		if(Game.net.server != null)
			Game.net.server.entityRemovedNotification(ent);
	}
	
	/** Get an entity from a given layer with a given hash */
	public Entity get2DEntity(int layer, int hash){
		return entities.layers[layer].get(hash);
	}
	
	/**
	 * Completely clears the physics world, getting rid of EVERYTHING.
	 * Use with caution! Can easily cause ConcurrentModificationExceptions
	 * if something is using an iterator from this physics class.
	 */
	public void clearWorld(){
		clearEntities();
		initWorld();	
	}
	
	/**
	 * Calls every entity's cleanup() method and then
	 * clears all the lists inside of entities.
	 */
	protected void clearEntities(){
		// handle any pending requests before deleting everything FIXME is this necessary?
		while(!updateRequests.isEmpty())
			updateRequests.poll().doRequest();
		
		// get rid of the current room
		if(currentRoom != null){
			currentRoom.removeFromWorld(this);
			currentRoom = null;
		}
		

		for(EntityHashMap layer : entities.layers){
			for(Entity ent : layer.values())
				this.removeEntity(ent, false);
		}
		
		// update so entities actually get removed/have cleanup() called
		this.update(1.0f / 60.0f);
		
		// actually clear the arraylists
		entities.clear();
	}
	
	/** @param request Request to add */
	public void addUpdateRequest(PhysicsUpdateRequest request){
		updateRequests.add(request);
	}
	
	/** @return Current number of dynamic entities */
	public int numEntities(){  return entities.numEntities(); }
	
	/** @return Whether or not there are any entities in the world */
	public boolean entitiesExist() { return numEntities() > 0; }
	
	/** Get a layer's hash map */
	public EntityHashMap getLayer(int layer){ return entities.layers[layer]; }
	
	/** Total number of layers */
	public int numLayers(){ return Entities.NUM_LAYERS; }
	
	/**
	 * Query the world's AABB for the first entity found in the given bounding box
	 * @param origin Center of query box
	 * @param queryWidth Width of query box
	 * @param queryHeight Height of query box
	 * @return First dynamic entity found in query box, null if none found
	 */
	public DynamicEntity checkForEntityAt(Vector2 origin, float queryWidth, float queryHeight){
		FirstHitQueryCallback callback = new FirstHitQueryCallback();
		world.QueryAABB(callback, origin.x - queryWidth, origin.y - queryHeight,
		                          origin.x + queryWidth, origin.y + queryHeight);
		return callback.getHit();
	}
	
	/**
	 * Perform a raycast
	 * @param callback Callback to use
	 * @param point1 First point
	 * @param point2 Second point
	 */
	public void rayCast(RayCastCallback callback, Vector2 point1, Vector2 point2){
		world.rayCast(callback, point1, point2);
	}
	
	public void queryAABB(QueryCallback callback, float lowerX, float lowerY, float upperX, float upperY){
		world.QueryAABB(callback, lowerX, lowerY, upperX, upperY);
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
