package com.bitwaffle.offworld.entities.player.input;

import com.badlogic.gdx.controllers.Controller;

/**
 * Describes how a player is being controlled
 * 
 * @author TranquilMarmot
 */
public class ControlInfo {
	public static enum SplitScreenSections{
		/** Player has the entire screen */
		FULL,
		/** Player has top half of the screen */
		TOP_HALF,
		/** Player has bottom half of the screen */
		BOTTOM_HALF,
		/** Player has top-left quarter of the screen */
		TOP_LEFT_QUARTER,
		/** Player has top-right quarter of the screen */
		TOP_RIGHT_QUARTER,
		/** Player has bottom-left quarter of screen */
		BOTTOM_LEFT_QUARTER,
		/** Player has bottom-right quarter of screen */
		BOTTOM_RIGHT_QUARTER;
	}
	
	/** Which section of the screen the player is using */
	public SplitScreenSections screenSection;
	
	/** 
	 * Whether or not this player is being controlled by the mouse, so the mouse's location can get grabbed every frame
	 * If it's not grabbed every frame, the target only gets updated when the mouse moves which doesn't compensate for camera movement.
	 */
	public boolean controlledByMouse = true;
	
	/** 
	 * Which controller is controlling this player,
	 * null is not being controlled by a controller (i.e. by mouse and keyboard)
	 */
	public Controller controller;
}
