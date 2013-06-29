package com.bitwaffle.offworld.entities.player.input;

import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Controls a given player with the keyboard and mouse.
 * There should probably only be one of these in the main input multiplexer at a time.
 * 
 * @author TranquilMarmot
 */
public class PlayerInputListener implements InputProcessor {
	/** Array of pointers, gets expanded if necessary (if number of pointers down is > length) */
	private LinkedList<PlayerPointer> pointers;
	
	/** Player that this handler is handling */
	private Player player;
	
	/**
	 * Create a new listener to control the player with
	 * a touch screen or mouse/keyboard
	 * @param player Player this listener is controlling
	 * @param camera Camera following player
	 */
	public PlayerInputListener(Player player){
		this.player = player;
		pointers = new LinkedList<PlayerPointer>();
	}

	@Override
	public boolean keyDown(int keycode) {
		// if console is on, player isn't being controlled
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
		
		//if(Game.renderer.camera.currentMode() == CameraModes.follow)
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
		//if(camera.currentMode() == CameraModes.follow)
			pointers.get(pointerID).move(pointerX, pointerY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		//if(camera.currentMode() == CameraModes.follow)
			player.setTarget(MathHelper.toWorldSpace(screenX, screenY, Game.renderer.camera));
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
