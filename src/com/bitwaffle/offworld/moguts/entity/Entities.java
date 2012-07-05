package com.bitwaffle.offworld.moguts.entity;

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
	
	public void update(){
		while(!toRemove.isEmpty())
			entities.remove(toRemove.pop());
		
		while(!toAdd.isEmpty()){
			Entity top = toAdd.pop();
			entities.add(top);
		}
	}
	
	public Iterator<Entity> getIterator(){
		return entities.iterator();
	}
}
