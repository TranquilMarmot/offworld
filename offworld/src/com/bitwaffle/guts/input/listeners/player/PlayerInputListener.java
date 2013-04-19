package com.bitwaffle.guts.input.listeners.player;

import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.render2d.camera.Camera2D;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Controls a given player with the keyboard and mouse.
 * There should probably only be one of these in the main input multiplexer at a time.
 * 
 * @author TranquilMarmot
 */
public class PlayerInputListener implements InputProcessor {
	/** 
	 * Array of pointers, gets expanded if necessary (if number of pointers down is > length)
	 * Given a MotionEvent e,  calling e.getPointerId(e.getActionIndex()) will give you the index
	 * in this array of the action's Pointer
	 */
	private LinkedList<PlayerPointer> pointers;
	
	/** Player that this handler is handling */
	private Player player;
	
	/** Camera following player */
	private Camera2D camera;
	
	/**
	 * Create a new listener to control the player with
	 * a touch screen or mouse/keyboard
	 * @param player Player this listener is controlling
	 * @param camera Camera following player
	 */
	public PlayerInputListener(Player player, Camera2D camera){
		this.player = player;
		this.camera = camera;
		pointers = new LinkedList<PlayerPointer>();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Game.gui.console.isOn())
			return false;
		
		switch(keycode){
		
		case Input.Keys.A:
		case Input.Keys.LEFT:
			player.moveLeft();
			return true;
			
		case Input.Keys.D:
		case Input.Keys.RIGHT:
			player.moveRight();
			return true;
			
		case Input.Keys.SPACE:
		case Input.Keys.W:
		case Input.Keys.UP:
			if(player.getJumpSensor().numContacts() <= 0){
				if(!player.jetpack.isEnabled())
					player.jetpack.enable();
			} else 
				player.jump();
			return true;
			
		default:
			return false;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		
		case Input.Keys.A:
		case Input.Keys.LEFT:
			player.stopMovingLeft();
			return true;
			
		case Input.Keys.D:
		case Input.Keys.RIGHT:
			player.stopMovingRight();
			return true;
			
		case Input.Keys.SPACE:
		case Input.Keys.W:
		case Input.Keys.UP:
			if(player.jetpack.isEnabled())
				player.jetpack.disable();
			return true;
			
		default:
			return false;
		}
	}

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int pointerX, int pointerY, int pointerID, int button) {
		while(pointers.size() < pointerID + 1)
			pointers.add(new PlayerPointer(player));
		
		if(Game.renderer.render2D.camera.currentMode() == Camera2D.Modes.FOLLOW)
			pointers.get(pointerID).down(pointerID, pointerX, pointerY);
		
		return false;
	}

	@Override
	public boolean touchUp(int pointerX, int pointerY, int pointerID, int button) {
		while(pointers.size() < pointerID + 1)
			pointers.add(new PlayerPointer(player));
		
		pointers.get(pointerID).up(pointerX, pointerY);
		return false;
	}

	@Override
	public boolean touchDragged(int pointerX, int pointerY, int pointerID) {
		if(camera.currentMode() == Camera2D.Modes.FOLLOW)
			pointers.get(pointerID).move(pointerX, pointerY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(camera.currentMode() == Camera2D.Modes.FOLLOW)
			player.setTarget(MathHelper.toWorldSpace(screenX, screenY, Game.renderer.render2D.camera));
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
