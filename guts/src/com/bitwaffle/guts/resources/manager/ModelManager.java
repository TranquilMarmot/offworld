package com.bitwaffle.guts.resources.manager;

import java.util.HashMap;

import com.bitwaffle.guts.graphics.graphics3d.model.Model;

public class ModelManager {
	private HashMap<String, Model> models;
	
	public ModelManager(){
		models = new HashMap<String, Model>();
	}
	
	public void addModel(String name, Model model){
		models.put(name, model);
	}
	
	public Model getModel(String name){
		return models.get(name);
	}
}
