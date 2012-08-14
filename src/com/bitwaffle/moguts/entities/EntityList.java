package com.bitwaffle.moguts.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class EntityList<T extends Entity> {
	private ArrayList<T> entities;
	
	private Stack<T> toAdd, toRemove;
	
	public EntityList(){
		entities = new ArrayList<T>();
		toAdd = new Stack<T>();
		toRemove = new Stack<T>();
	}
	
	public void add(T add){
		toAdd.push(add);
	}
	
	public void remove(T rem){
		toRemove.push(rem);
	}
	
	public Iterator<T> getIterator(){
		return entities.iterator();
	}
	
	public void clear(){
		entities.clear();
		toAdd.clear();
		toRemove.clear();
	}
	
	public void update(float timeStep){
		while(!toRemove.isEmpty()){
			T ent = toRemove.pop();
			ent.cleanup();
			entities.remove(ent);
		}
		
		while(!toAdd.isEmpty())
			entities.add(toAdd.pop());
		
		Iterator<T> it = getIterator();
		
		while(it.hasNext()){
			T ent = it.next();
			
			if(ent != null){
				if(ent.removeFlag)
					toRemove.add(ent);
				else
					ent.update(timeStep);
			}
		}
	}
	
	public int size(){
		return entities.size();
	}
}
