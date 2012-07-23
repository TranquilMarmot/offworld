package com.bitwaffle.moguts.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.button.Button;
import com.bitwaffle.moguts.gui.button.RectangleButton;
import com.bitwaffle.moguts.resources.Textures;

/**
 * Handles all GUI elements
 * 
 * @author TranquilMarmot
 */
public class GUI {
	/** List of buttons */
	private ArrayList<Button> buttons;
	/** Used to avoid ConcurrentModificationExceptions */
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
	
	/* FIXME TEMP CODE maybe serialize GUI elements? */
	private void temp(){
		RectangleButton leftButt = new RectangleButton(40.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			@Override
			public void release(){
				super.release();
				Vector2 linVec = Game.player.body.getLinearVelocity();
				linVec.x += 10.0f;
				Game.player.body.setLinearVelocity(linVec);
			}
			
			@Override
			public void update(){
				if(this.isDown()){
					Vector2 linVec = Game.player.body.getLinearVelocity();
					linVec.x -= 3.0f;
					Game.player.body.setLinearVelocity(linVec);
				}
				
				this.y = Game.windowHeight - 40.0f;
			}
			
			@Override
			public void press(){
				super.press();
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Textures.leftArrowTex);
				super.draw(renderer);
			}
		};
		this.addButton(leftButt);
		
		/* -------------- */
		
		RectangleButton rightButt = new RectangleButton(150.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			@Override
			public void release(){
				super.release();
				Vector2 linVec = Game.player.body.getLinearVelocity();
				linVec.x -= 10.0f;
				Game.player.body.setLinearVelocity(linVec);
			}
			
			@Override
			public void update(){
				if(this.isDown()){
					Vector2 linVec = Game.player.body.getLinearVelocity();
					linVec.x += 3.0f;
					Game.player.body.setLinearVelocity(linVec);
				}
				
				this.y = Game.windowHeight - 40.0f;
			}
			
			@Override
			public void press(){
				super.press();
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Textures.rightArrowTex);
				super.draw(renderer);
			}
		};
		this.addButton(rightButt);
		
		
		/* -------------------- */
		
		
		RectangleButton jumpButt = new RectangleButton(Game.windowWidth - 40.0f, Game.windowHeight - 40.0f, 40.0f, 40.0f){
			@Override
			public void release(){
				super.release();
				Game.vibration.vibrate(25);
			
				// TODO make it so you can only jump when hitting the ground
				Vector2 linVec = Game.player.body.getLinearVelocity();
				if(linVec.y <= 5.0f && linVec.y >= -5.0f){
					linVec.y += 50.0f;
					Game.player.body.setLinearVelocity(linVec);
				}
			}
			
			@Override
			public void update(){
				this.y = Game.windowHeight - 40.0f;
				this.x = Game.windowWidth - 40.0f;
			}
			
			@Override
			public void draw(Render2D renderer){
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Textures.upArrowTex);
				super.draw(renderer);
			}
		};
		this.addButton(jumpButt);
		
		/* -------------------- */
		
		
		RectangleButton camButt = new RectangleButton(20.0f, 20.0f, 20.0f, 20.0f){
			@Override
			public void release(){
				Game.vibration.vibrate(25);
				super.release();
				Camera.Modes mode = Render2D.camera.currentMode();
				if(mode == Camera.Modes.FOLLOW)
					Render2D.camera.setMode(Camera.Modes.FREE);
				else
					Render2D.camera.setMode(Camera.Modes.FOLLOW);
			}
			
			@Override
			public void draw(Render2D renderer){
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Textures.cameraTex);
				super.draw(renderer);
			}
		};
		this.addButton(camButt);
		
		/* -------------------- */
		
		
		RectangleButton boxButt = new RectangleButton(Game.windowWidth - 20.0f, 20.0f, 20.0f, 20.0f){
			@Override
			public void release(){
				Game.vibration.vibrate(25);
				super.release();
				// FIXME still breaks game (maybe do a stack-queue thing?)
				Game.physics.makeRandomBox();
			}
			
			@Override
			public void update(){
				this.x = Game.windowWidth - 20.0f;
			}
		};
		this.addButton(boxButt);
	}
}
