package com.bitwaffle.guts.input.listeners.player;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.gui.button.Button;
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
	private Camera camera;
	
	/**
	 * Create a new listener to control the player with
	 * a touch screen or mouse/keyboard
	 * @param player Player this listener is controlling
	 * @param camera Camera following player
	 */
	public PlayerInputListener(Player player, Camera camera){
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
				if(!player.jetpackEnabled())
					player.enableJetpack();
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
			if(player.jetpackEnabled())
				player.disableJetpack();
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
		
		if(Render2D.camera.currentMode() == Camera.Modes.FOLLOW)
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
		if(camera.currentMode() == Camera.Modes.FOLLOW)
			pointers.get(pointerID).move(pointerX, pointerY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(camera.currentMode() == Camera.Modes.FOLLOW)
			player.setTarget(MathHelper.toWorldSpace(screenX, screenY, Render2D.camera));
		
		// check if the mouse hovers over any buttons and select them
		if(Game.gui != null){
			// check if the mouse went off of the selected button
			if(Game.gui.selectedButton != null && !Game.gui.selectedButton.contains(screenX, screenY)){
				Game.gui.selectedButton.unselect();
				Game.gui.selectedButton = null;
			}
			
			// check every button to see if it's selected
			Iterator<Button> it = Game.gui.getButtonIterator();
			while (it.hasNext()) {
				Button b = it.next();
				
				if (b != Game.gui.selectedButton && b.isActive() && b.isVisible() && b.contains(screenX, screenY)) {
					Game.gui.selectedButton = b;
					b.select();
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
