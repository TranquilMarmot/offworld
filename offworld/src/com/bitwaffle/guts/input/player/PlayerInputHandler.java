package com.bitwaffle.guts.input.player;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Controls a given player with the keyboard and mouse.
 * There should probably only be one of these in the main input multiplexer at a time.
 * 
 * @author TranquilMarmot
 */
public class PlayerInputHandler implements InputProcessor {
	/** 
	 * Array of pointers, gets expanded if necessary (if number of pointers down is > length)
	 * Given a MotionEvent e,  calling e.getPointerId(e.getActionIndex()) will give you the index
	 * in this array of the action's Pointer
	 */
	private ArrayList<PlayerPointer> pointers;
	
	/** How many pointers are currently down */
	protected int pointerCount;
	
	/** Player that this handler is handling */
	private Player player;
	
	public PlayerInputHandler(Player player){
		this.player = player;
		pointers = new ArrayList<PlayerPointer>(3);
		pointerCount = 0;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		
		case Input.Keys.A:
			player.moveLeft();
			return true;
			
		case Input.Keys.D:
			player.moveRight();
			return true;
			
		case Input.Keys.SPACE:
		case Input.Keys.W:
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
			player.stopMovingLeft();
			return true;
			
		case Input.Keys.D:
			player.stopMovingRight();
			return true;
			
		case Input.Keys.SPACE:
		case Input.Keys.W:
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
		pointerCount++;
		while(pointers.size() < pointerCount)
			pointers.add(new PlayerPointer(player));
		
		pointers.get(pointerID).down(pointerID, pointerX, pointerY);
		return false;
	}

	@Override
	public boolean touchUp(int pointerX, int pointerY, int pointerID, int button) {
		pointerCount--;
		pointers.get(pointerID).up(pointerX, pointerY);
		return false;
	}

	@Override
	public boolean touchDragged(int pointerX, int pointerY, int pointerID) {
		pointers.get(pointerID).move(pointerX, pointerY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		player.setTarget(MathHelper.toWorldSpace(screenX, screenY, Render2D.camera));
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
