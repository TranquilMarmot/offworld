package com.bitwaffle.offworld;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.input.Input;
import com.bitwaffle.guts.net.Net;
import com.bitwaffle.guts.physics.Physics;
import com.bitwaffle.guts.resources.loader.ResourceLoader;
import com.bitwaffle.offworld.entities.player.Player;
import com.bitwaffle.offworld.gui.GUIStates;
import com.bitwaffle.offworld.input.OffworldInput;
import com.bitwaffle.offworld.net.OffworldNet;
import com.bitwaffle.offworld.physics.OffworldPhysics;
import com.bitwaffle.offworld.rooms.Room1;

/**
 * Welcome to Offworld.
 * 
 * @author TranquilMarmot
 */
public abstract class OffworldGame extends Game {
	/** Current version of the game */
	public static final String VERSION = "0.0.6.12 (pre-alpha)";
	
	/** Whether or not to show the splash screen */
	public static boolean showSplash = false;
	
	/** List of players */
	public static Player[] players = new Player[4];
	
	@Override
	public void create(){
		super.create();
		
		// load vital resources from base resource file
		ResourceLoader.loadResourceFile("base.res");
		
		if(showSplash)
			gui.setCurrentState(GUIStates.SPLASH.state);
		else {
			Game.gui.setCurrentState(GUIStates.TITLESCREEN.state);
			//tempInit(Game.physics);
			//Game.gui.setCurrentState(GUIStates.NONE.state);
		}
	}
	
	@Override
	protected void initGDX(){
		super.initGDX();
		Gdx.graphics.setTitle("Offworld " + VERSION);
	}
	
	@Override
	protected Physics initPhysics(){ return new OffworldPhysics(); }
	
	@Override
	protected Net initNet(){ return new OffworldNet(); }
	
	@Override
	protected Input initInput(){ return new OffworldInput(); }
	
	@Override
	protected Renderer initRenderer(){
		Renderer r = super.initRenderer();
		// grab camera and set camera modes to use it
		return r;
	}
	
	/**
	 * Temporary initialization
	 * @param physics Physics world to initialize
	 */
	public static void tempInit(Physics physics){
		Room1 r1 = new Room1();
		physics.setCurrentRoom(r1);
	}

}
