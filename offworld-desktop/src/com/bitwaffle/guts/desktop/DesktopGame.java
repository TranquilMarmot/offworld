package com.bitwaffle.guts.desktop;

import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.desktop.devmode.DevModeDisplay;
import com.bitwaffle.guts.desktop.devmode.builder.Builder;
import com.bitwaffle.guts.desktop.gui.DesktopGUI;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.offworld.OffworldGame;

/**
 * Runs the game on the desktop
 * 
 * @author TranquilMarmot
 */
public class DesktopGame extends OffworldGame {
	private static int defaultWindowWidth = 1024, defaultWindowHeight = 768;
	
	private static Builder builder;
	
	public static void main(String[] args){
		Game.windowWidth = defaultWindowWidth;
		Game.windowHeight = defaultWindowHeight;
		//Keyboard.enableRepeatEvents(true);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.width = defaultWindowWidth;
		config.height = defaultWindowHeight;
		config.useGL20 = true;
		config.addIcon("icons/16x16.png", FileType.Local);
		config.addIcon("icons/32x32.png", FileType.Local);
		config.addIcon("icons/64x64.png", FileType.Local);
		
		//new LwjglApplication(new DesktopGame(), config, DisplayHelper.gameCanvas);
		
		if(args.length == 1 && args[0].equals("devmode")){
			DevModeDisplay.createFrame();
			builder = new Builder();
			new LwjglApplication(new DesktopGame(), config, DevModeDisplay.gameCanvas);
		}else
			new LwjglApplication(new DesktopGame(), config);
	}
	
	@Override
	protected GUI initGUI(){
		return new DesktopGUI();
	}
	
	@Override
	protected void update(){
		super.update();

		// TODO
    	//if(KeyBindings.SYS_SCREENSHOT.pressedOnce())
		//	Screenshot.takeScreenshot(windowWidth, windowHeight);
    	
    	// FIXME get out of fullscreen too!!
    	//if(KeyBindings.SYS_FULLSCREEN.pressedOnce()){
    	//	Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
    	//}
    	
		if(builder != null)
			builder.update();
	}
	/** Called right before dying to close all resources */
	@Override
	public void dispose(){
		super.dispose();
		System.out.println(goodbye());
		Mouse.setGrabbed(false);
	}
    
	/**
	 * This is a secret method that does secret things
	 * @return None of your business
	 */
	private String goodbye(){
		String[] shutdown = { "Goodbye, world!...", "Goodbye, cruel world!...", "See ya!...", "Later!...", "Buh-bye!...", "Thank you, come again!...",
				"Until Next Time!...", "¡Adios, Amigo!...", "Game Over, Man! Game Over!!!...", "And So, I Bid You Adieu!...", "So Long, And Thanks For All The Fish!...",
				"¡Ciao!...", "Y'all Come Back Now, Ya Hear?...", "Catch You Later!...", "Mahalo And Aloha!...", "Sayonara!...", "Thanks For Playing!...",
				"Auf Wiedersehen!...", "Yo Holmes, Smell Ya Later!... (Looked Up At My Kingdom, I Was Finally There, To Sit On My Throne As The Prince Of Bel-air)",
				"Shop Smart, Shop S-Mart!...", "Good Night, And Good Luck!...", "Remember, I'm Pulling For You. We're All In This Together!...", "Keep Your Stick On The Ice!...",
				"Omnia Extares!...", "C'est la vie!...", "See you on the flip side!...", "Toodle-oo!...", "Ta ta (For Now)!...", "¡Hasta La Vista, Baby!...",
				"Komapsumnida!...", "...!olleH", "Live Long And Prosper!...", "Cheerio!...", "Shalom!...", "Peace Out!...", "Arrivederci!..."};
		
		return shutdown[random.nextInt(shutdown.length)];
	}
}
