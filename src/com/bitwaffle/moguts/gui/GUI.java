package com.bitwaffle.moguts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.gui.button.Button;
import com.bitwaffle.moguts.gui.button.RectangleButton;

public class GUI {
	private ArrayList<Button> buttons;
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	public GUI(){
		buttons = new ArrayList<Button>();
		buttonsToAdd = new Stack<Button>();
		buttonsToRemove = new Stack<Button>();
		
		temp();
	}
	
	public Iterator<Button> getIterator(){
		return buttons.iterator();
	}
	
	public void addButton(Button obj){
		buttonsToAdd.add(obj);
	}
	
	public void removeButton(Button obj){
		buttonsToRemove.add(obj);
	}
	
	public void update(){
		while(!buttonsToRemove.isEmpty()){
			Button obj = buttonsToRemove.pop();
			obj.cleanup();
			buttons.remove(obj);
		}
		
		while(!buttonsToAdd.isEmpty())
			buttons.add(buttonsToAdd.pop());
		
		Iterator<Button> it = getIterator();
		
		while(it.hasNext())
			it.next().update();
	}
	
	/* FIXME TEMP CODE */
	private void temp(){
		RectangleButton leftButt = new RectangleButton(40.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			@Override
			public void actionPerformed(){
				//Game.physics.makeRandomBox();
				//Game.player.body.applyForceToCenter(1.0f, 0.0f);
				Game.player.body.setLinearVelocity(0.0f, 0.0f);
			}
			
			@Override
			public void update(){
				if(this.isDown)
					//Game.player.body.applyForceToCenter(-100.0f, 0.0f);
					Game.player.body.setLinearVelocity(-10.0f, 0.0f);
				
				
				this.y = Game.windowHeight - 40.0f;
			}
		};
		this.addButton(leftButt);
		
		/* -------------- */
		
		RectangleButton rightButt = new RectangleButton(150.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			@Override
			public void actionPerformed(){
				//Game.physics.makeRandomBox();
				//Game.player.body.applyForceToCenter(1.0f, 0.0f);
				Game.player.body.setLinearVelocity(0.0f, 0.0f);
			}
			
			@Override
			public void update(){
				if(this.isDown){
					//Game.player.body.applyForceToCenter(100.0f, 0.0f);
					Game.player.body.setLinearVelocity(10.0f, 0.0f);
				}
				
				this.y = Game.windowHeight - 40.0f;
			}
		};
		this.addButton(rightButt);
		
		
		
		
	}
}
