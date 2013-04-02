package com.bitwaffle.guts.input.listeners.controller.player;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Handles all controller
 * 
 * @author TranquilMarmot
 */
public abstract class PlayerControllerListener implements ControllerListener {
	/** Player this controller is controlling */
	private Player player;
	
	/**
	 * Create a new controller handler
	 * @param player Player controller is controlling
	 */
	public PlayerControllerListener(Player player){
		this.player = player;
	}

	@Override
	public void connected(Controller controller){ System.out.println(controller.getName() + " connected"); }
	@Override
	public void disconnected(Controller controller){ System.out.println(controller.getName() + " disconnected"); }
	
	@Override
	public boolean buttonDown (Controller controller, int buttonCode){
		if(isJump(buttonCode)){
			this.player.jump();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean buttonUp (Controller controller, int buttonCode){
		return false;
	}
	
	/**
	 * @param buttonCode Button being pressed
	 * @return Whether or not given button is jump button
	 */
	protected abstract boolean isJump(int buttonCode);
	
	/**
	 * @param axisCode Axis being moved
	 * @return Whether or not the given axis is the movement axis (X axis of left thumbstick)
	 */
	protected abstract boolean isMovementAxis(int axisCode);
	
	/**
	 * @param axisCode Axis being moved
	 * @return Whether or not the given axis is the X aiming axis (X axis of right thumbstick)
	 */
	protected abstract boolean isXAimAxis(int axisCode);
	
	/**
	 * @param value Axis value
	 * @return Whether or not the player's X aim should be changed
	 */
	protected abstract boolean isOutsideXAimDeadzone(float value);
	
	/**
	 * @param value Axis value
	 * @return Whether or not the player's Y aim should be changed
	 */
	protected abstract boolean isOustdieYAimDeadzone(float value);
	
	/**
	 * @param axisCode Axis being moved
	 * @return Whether or not the given axis is the Y aiming axis (Y axis of right thumbstick)
	 */
	protected abstract boolean isYAimAxis(int axisCode);
	
	/**
	 * @param axisCode Axis being moved
	 * @param value Value of axis
	 * @return Whether or not the axis is the left trigger axis
	 */
	protected abstract boolean isLTriggerAxis(int axisCode, float value);
	
	/**
	 * @param axisCode Axis being moved
	 * @param value Value of axis
	 * @return Whether or not the axis is the right trigger axis
	 */
	protected abstract boolean isRTriggerAxis(int axisCode, float value);
	
	/** @return How much the thumbstuck has to move to move the player */
	protected abstract boolean isOutsideMovementDeadzone(float value);
	
	/** @return Threshold for activating jetpack with trigger */
	protected abstract boolean isOutsideJetpackTriggerDeadzone(float value);
	
	/** @return Threshold for activating shooting */
	protected abstract boolean isOutsideShootingTriggerDeadzone(float value);
	
	@Override
	public boolean axisMoved (Controller controller, int axisCode, float value){
		// check if it's the movement axis
		if(isMovementAxis(axisCode)){
			if(isOutsideMovementDeadzone(value)){
				if(value > 0)
					this.player.moveRight();
				else if(value < 0)
					this.player.moveLeft();
			} else {
				player.stopMovingLeft();
				player.stopMovingRight();
			}
			return true;
			
		// check if aiming on X axis
		} else if(isXAimAxis(axisCode) && isOutsideXAimDeadzone(value)){
			float x = player.getLocation().x + value * 5.0f;
			float y = player.getCurrentTarget().y;
			player.setTarget(new Vector2(x, y));
			return true;
			
		// check if aiming on Y axis
		} else if(isYAimAxis(axisCode) && isOustdieYAimDeadzone(value)){
			float x = player.getCurrentTarget().x;
			float y = player.getLocation().y - value * 5.0f;
			player.setTarget(new Vector2(x, y));
			return true;
		
		
		// left trigger movement (jetpack)
		} else if(isLTriggerAxis(axisCode, value)){
			if(isOutsideJetpackTriggerDeadzone(value))
				player.enableJetpack();
			else
				player.disableJetpack();
			
			return true;
			
		// right trigger movement (fire)
		} else if(isRTriggerAxis(axisCode, value)){
			if(isOutsideShootingTriggerDeadzone(value))
				player.beginShooting();
			else
				player.endShooting();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean povMoved (Controller controller, int povCode, PovDirection value){
		// stop moving if the d-pad goes to the center
		if(value == PovDirection.center){
			player.stopMovingLeft();
			player.stopMovingRight();
			return true;
			
		// move right
		} else if(value == PovDirection.east || value ==  PovDirection.northEast || value == PovDirection.southEast){
			player.moveRight();
			return true;
			
		// move left
		} else if(value == PovDirection.west || value ==  PovDirection.northWest || value == PovDirection.southWest){
			player.moveLeft();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean xSliderMoved (Controller controller, int sliderCode, boolean value){
		return false;
	}
	
	@Override
	public boolean ySliderMoved (Controller controller, int sliderCode, boolean value){
		return false;
	}
	
	@Override
	public boolean accelerometerMoved (Controller controller, int accelerometerCode, Vector3 value){
		return false;
	}

}
