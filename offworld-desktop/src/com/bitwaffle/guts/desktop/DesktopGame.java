package com.bitwaffle.guts.desktop;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.desktop.input.MouseManager;
import com.bitwaffle.guts.desktop.scala.builder.Builder;
import com.bitwaffle.guts.input.KeyBindings;

/**
 * @author TranquilMarmot
 */
public class DesktopGame extends Game {
	public static GameFrame frame;
	/**
	 * @param args None if LWJGL library path is in /libs/natives,
	 * location of natives otherwise (See comment at top of class)
	 */
	public static void main(String[] args){
		//Keyboard.enableRepeatEvents(true);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.width = 1024;
		config.height = 768;
		config.useGL20 = true;
		config.addIcon("icon.png", FileType.Local);
		
		//frame = new Frame();
		frame = new GameFrame(new DesktopGame(), config);
	}
	
	@Override
	protected void update(){
		super.update();
		
		MouseManager.update();

		// desktop has its own screenshot code
    	if(KeyBindings.SYS_SCREENSHOT.pressedOnce())
			Screenshot.takeScreenshot(windowWidth, windowHeight);
    	
    	// FIXME get out of fullscreen too!!
    	if(KeyBindings.SYS_FULLSCREEN.pressedOnce()){
    		//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
    		//buildMode();
    		Builder.create();
    	}
    	
    	Builder.update();
	}
	/**
	 * Called right before dying to close all resources
	 */
	@Override
	public void dispose(){
		super.dispose();
		System.out.println(goodbye());
		Mouse.setGrabbed(false);
	}
    

	@Override
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
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
