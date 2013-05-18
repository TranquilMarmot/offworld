package com.bitwaffle.guts.input.gui.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.GUI;

public abstract class GUIControllerListener implements ControllerListener {
	
	protected abstract boolean isConfirmButton(int buttonCode);
	
	protected abstract boolean isSelectXAxis(int axisCode);
	
	protected abstract boolean isSelectYAxis(int axisCode);
	
	protected abstract boolean isOutsideSelectDeadzone(float value);
	
	protected abstract boolean isPauseButton(int buttonCode);
	
	/** GUI being controlled by this listener */
	private GUI gui;
	
	public GUIControllerListener(GUI gui){
		this.gui = gui;
	}

	@Override
	public void connected(Controller controller) {}

	@Override
	public void disconnected(Controller controller) {}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if(isConfirmButton(buttonCode)){
			gui.selectedButtonDown();
			return true;
		} else if(isPauseButton(buttonCode)){
			Game.togglePause();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if(isConfirmButton(buttonCode)){
			gui.selectedButtonUp();
			return true;
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if(isSelectXAxis(axisCode) && isOutsideSelectDeadzone(value)){
			if(value > 0)
				gui.moveRight();
			else if(value < 0)
				gui.moveLeft();
		} else if(isSelectYAxis(axisCode) && isOutsideSelectDeadzone(value)){
			if(value < 0)
				gui.moveUp();
			else if(value > 0)
				gui.moveDown();
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if(value == PovDirection.north)
			gui.moveUp();
		else if(value == PovDirection.south)
			gui.moveDown();
		else if(value == PovDirection.west)
			gui.moveLeft();
		else if(value == PovDirection.east)
			gui.moveRight();
		
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

}
