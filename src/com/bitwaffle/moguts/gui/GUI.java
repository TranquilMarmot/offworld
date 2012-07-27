package com.bitwaffle.moguts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.button.Button;
import com.bitwaffle.moguts.gui.button.RectangleButton;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	// FIXME use an EntityList for this? Maybe rename EntityList :P
	// TODO buttons are seperate at the moment because they get checked on every touch event, probably also need a non-pressable UI list
	/** List of buttons */
	private ArrayList<Button> buttons;
	/** Used to avoid ConcurrentModificationExceptions */
	private Stack<Button> buttonsToAdd, buttonsToRemove;
	
	public GUI(){
		buttons = new ArrayList<Button>();
		buttonsToAdd = new Stack<Button>();
		buttonsToRemove = new Stack<Button>();
		
		tempInit();
	}
	
	public Iterator<Button> getButtonIterator(){
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
		
		Iterator<Button> it = getButtonIterator();
		
		while(it.hasNext())
			it.next().update();
	}
	
	/* FIXME TEMP CODE maybe serialize GUI elements? */
	private void tempInit(){
		RectangleButton leftLeftButt = new RectangleButton(40.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goLeft();
				
				this.y = Game.windowHeight - 40.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("leftarrow");
				super.draw(renderer);
			}
		};
		this.addButton(leftLeftButt);
		
		/* -------------- */
		
		RectangleButton rightLeftButt = new RectangleButton(Game.windowWidth - 150.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goLeft();
				
				this.y = Game.windowHeight - 40.0f;
				this.x = Game.windowWidth - 150.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("leftarrow");
				super.draw(renderer);
			}
		};
		this.addButton(rightLeftButt);
		
		/* ----------- */
		
		RectangleButton leftRightButt = new RectangleButton(150.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goRight();
				
				this.y = Game.windowHeight - 40.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("rightarrow");
				super.draw(renderer);
			}
		};
		this.addButton(leftRightButt);
		
		
		/* -------------------- */
		
		RectangleButton rightRightButt = new RectangleButton(Game.windowWidth - 40.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goRight();
				
				this.x = Game.windowWidth - 40.0f;
				this.y = Game.windowHeight - 40.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("rightarrow");
				super.draw(renderer);
			}
		};
		this.addButton(rightRightButt);
		
		/* ----------------- */
		
		
		RectangleButton rightJumpButt = new RectangleButton(Game.windowWidth - 40.0f, Game.windowHeight - 150.0f, 40.0f, 40.0f){
			protected void onRelease(){ }
			protected void onSlideRelease(){};
			
			@Override
			protected void onPress(){
				Game.player.jump();
			};
			
			@Override
			public void update(){
				this.y = Game.windowHeight - 150.0f;
				this.x = Game.windowWidth - 40.0f;
			}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("uparrow");
				super.draw(renderer);
			}
		};
		this.addButton(rightJumpButt);
		
		/* -------------------- */
		
		RectangleButton leftJumpButt = new RectangleButton(40.0f, Game.windowHeight - 150.0f, 40.0f, 40.0f){
			protected void onRelease(){}
			protected void onSlideRelease(){};
			
			@Override
			protected void onPress(){
				Game.player.jump();
			};
			
			@Override
			public void update(){
				this.y = Game.windowHeight - 150.0f;
			}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("uparrow");
				super.draw(renderer);
			}
		};
		this.addButton(leftJumpButt);
		
		/* -------------------- */
		
		
		RectangleButton camButt = new RectangleButton(20.0f, 20.0f, 20.0f, 20.0f){
			@Override
			protected void onRelease(){
				Game.vibration.vibrate(25);
				Camera.Modes mode = Render2D.camera.currentMode();
				if(mode == Camera.Modes.FOLLOW)
					Render2D.camera.setMode(Camera.Modes.FREE);
				else
					Render2D.camera.setMode(Camera.Modes.FOLLOW);
			}
			
			protected void onSlideRelease(){};
			protected void onPress(){};
			public void update(){};
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("camera");
				super.draw(renderer);
			}
		};
		this.addButton(camButt);
		
		/* -------------------- */
		
		
		RectangleButton boxButt = new RectangleButton(Game.windowWidth - 40.0f, 40.0f, 40.0f, 40.0f){
			@Override
			protected void onRelease(){
				Game.vibration.vibrate(25);
				Game.physics.makeRandomBox();
				Game.resources.sounds.play("test");
			}
			
			protected void onSlideRelease(){};
			protected void onPress(){};
			
			@Override
			public void update(){
				this.x = Game.windowWidth - 40.0f;
				this.active[0] = 0.75f;
				this.active[1] = 0.75f;
				this.active[2] = 0.75f;
			}
		};
		this.addButton(boxButt);
	}
}
