package com.bitwaffle.offworld.entities.player.input;

import com.badlogic.gdx.controllers.Controller;

/**
 * Describes how a player is being controlled
 * 
 * @author TranquilMarmot
 */
public class ControlInfo {
	/** Describes which portion of the screen a player is taking up */
	public static enum SplitScreenSection{
		/** Entire screen */
		FULL,
		/** Top half of the screen */
		TOP_HALF,
		/** Bottom half of the screen */
		BOTTOM_HALF,
		/** Top-left quarter of the screen */
		TOP_LEFT_QUARTER,
		/** Top-right quarter of the screen */
		TOP_RIGHT_QUARTER,
		/** Bottom-left quarter of screen */
		BOTTOM_LEFT_QUARTER,
		/** Bottom-right quarter of screen */
		BOTTOM_RIGHT_QUARTER;
	}
	
	/** Which section of the screen the player is using */
	public SplitScreenSection screenSection = SplitScreenSection.FULL;
	
	/** 
	 * Whether or not this player is being controlled by the mouse, so the mouse's location can get grabbed every frame
	 * If it's not grabbed every frame, the target only gets updated when the mouse moves which doesn't compensate for camera movement.
	 */
	public boolean controlledByMouse = true;
	
	/** 
	 * Which controller is controlling this player,
	 * If null, player is not being controlled by a controller (i.e. by mouse and keyboard)
	 */
	public Controller controller;
}
