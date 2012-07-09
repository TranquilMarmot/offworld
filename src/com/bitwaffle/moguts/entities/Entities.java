package com.bitwaffle.moguts.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Entities {
	private ArrayList<Entity> entities;
	private Stack<Entity> toAdd, toRemove;
	
	public Entities(){
		entities = new ArrayList<Entity>();
		toAdd = new Stack<Entity>();
		toRemove = new Stack<Entity>();
	}
	
	public void addEntity(Entity ent){
		toAdd.add(ent);
	}
	
	public void removeEntity(Entity ent){
		toRemove.add(ent);
	}
	
	public Iterator<Entity> getIterator(){
		return entities.iterator();
	}
	
	public void update(float timeStep){
		while(!toRemove.isEmpty())
			entities.remove(toRemove.pop());
		
		while(!toAdd.isEmpty())
			entities.add(toAdd.pop());
		
		Iterator<Entity> it = getIterator();
		
		while(it.hasNext())
			it.next().update(timeStep);
	}
}
