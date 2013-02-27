package com.bitwaffle.guts.physics;

import java.util.Iterator;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entities.Entities;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.physics.callbacks.FirstHitQueryCallback;

/**
 * Handles all physics workings
 * 
 * @author TranquilMarmot
 */
public class Physics {
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
	 * Initialized physics
	 */
	public Physics(){
		// load natvies before doing anything (this is important!)
		//GdxNativesLoader.load();
		
		entities = new Entities();
		toInitialize = new Stack<DynamicEntity>();
		
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
		ContactHandler handler = new ContactHandler();
		world.setContactListener((ContactListener)handler);
		world.setContactFilter((ContactFilter) handler);
	}
	
	/**
	 * Set the current room in the Physics world
	 * @param newRoom New room
	 */
	public void setCurrentRoom(Room newRoom){
		if(currentRoom != null)
			currentRoom.removeFromWorld(this);
		
		if(newRoom != null)
			newRoom.addToWorld(this);
		
		currentRoom = newRoom;
	}
	
	
	/**
	 * @return The current room
	 */
	public Room currentRoom(){
		return currentRoom;
	}
	
	/**
	 * Steps the physics simulation and updates every entity's location
	 */
	public void update(float timeStep){
		// initialize any entities that need intialization
		while(!toInitialize.isEmpty())
			toInitialize.pop().init(world);

		// this makes everything move around
		world.step(timeStep, velocityIterations, positionIterations);
		
		// this actually calls each entity's update() method
		entities.update(timeStep);
		
		// this updates any entities added/removed to the current room
		if(currentRoom != null)
			currentRoom.update(timeStep);
	}
	
	/**
	 * Completely clears the physics world, getting rid of EVERYTHING
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
	private void clearEntities(){
		// get rid of the current room
		if(currentRoom != null){
			currentRoom.removeFromWorld(this);
			currentRoom = null;
		}
		
		// remove every entity, so that cleanup() gets called
		for(Iterator<Entity> it : entities.getAllIterators()){
			while(it.hasNext())
				this.removeEntity(it.next(), false);
		}
		
		// update so entities actually get removed/have cleanup() called
		this.update(1.0f / 60.0f);
		
		// actually clear the arraylists
		entities.clear();
		
		// get rid of the player FIXME should this be here?
		Game.player = null;
	}
	
	/**
	 * Directly remove an entity from the world
	 * @param ent Entity to remove from world
	 * @param removeFromCurrentRoom Whether or not to remove the entity from the current room as well (generally, yes)
	 */
	public void removeEntity(Entity ent, boolean removeFromCurrentRoom){
		entities.removeEntity(ent);
		
		// remove from current room as well
		if(removeFromCurrentRoom && currentRoom != null)
			currentRoom.removeEntity(ent);
	}
	
	/**
	 * Add a passive Entity to the Entities list
	 * @param ent Entity to add
	 * @param addToCurrentRoom Whether or not to add the entity to the current room as well (if added, it gets removed on room change)
	 */
	public void addEntity(Entity ent, boolean addToCurrentRoom){
		entities.addEntity(ent);
		
		// add to current room as well, if told to
		if(addToCurrentRoom && currentRoom != null)
			currentRoom.addEntity(ent);
		
		// DynamicEntity needs to be initialized when it's added
		if(ent instanceof DynamicEntity)
			toInitialize.push((DynamicEntity)ent);
	}
	
	/**
	 * @return Current number of dynamic entities
	 */
	public int numEntities(){
		return entities.numEntities();
	}
	
	/**
	 * @return List of every iterator for every entity in the world
	 */
	public Iterator<Entity>[] getAllIterators(){
		return entities.getAllIterators();
	}
	
	/**
	 * Renders everything in the world
	 * @param renderer Renderer to use to render
	 */
	public void renderAll(Render2D renderer){
		entities.renderAll(renderer);
	}
	
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

	/**
	 * @return Whether or not there are any entities in the world
	 */
	public boolean entitiesExist() {
		return entities.numEntities() > 0;
	}
}
