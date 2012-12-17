package com.bitwaffle.guts.physics;

import java.util.Iterator;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.bitwaffle.guts.entities.Entities;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.render.Render2D;
import com.bitwaffle.guts.physics.callbacks.FirstHitQueryCallback;

/**
 * Handles all physics workings
 * 
 * @author TranquilMarmot
 */
public class Physics {
	/** World that everything is in */
	private World world;
	
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
		GdxNativesLoader.load();
		
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
	 * Steps the physics simulation and updates every entity's location
	 */
	public void update(float timeStep){
		// initialize any entities that need intialization
		while(!toInitialize.isEmpty())
			toInitialize.pop().init(world);

		world.step(timeStep, velocityIterations, positionIterations);
		entities.update(timeStep);
	}
	
	/**
	 * Completely clears the physics world, getting rid of EVERYTHING
	 * Use with caution! Can easily cause ConcurrentModificationExceptions
	 * if something is using an iterator from this physics class.
	 */
	public void clearWorld(){
		entities.clear();
		initWorld();	
	}
	
	/**
	 * Add a DynamicEntity to the Physics world
	 * @param ent Entity to add
	 */
	public void addDynamicEntity(DynamicEntity ent){
		entities.addEntity(ent);
		toInitialize.push(ent);
	}
	
	/**
	 * @param ent Entity to remove from world
	 */
	public void removeEntity(Entity ent){
		entities.removeEntity(ent);
	}
	
	/**
	 * Add a passive Entity to the Entities list
	 * @param ent Entity to add
	 */
	public void addEntity(Entity ent){
		entities.addEntity(ent);
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
	 * @return Whether or not there are any entities in the world
	 */
	public boolean entitiesExist() {
		return entities.numEntities() > 0;
	}
}
