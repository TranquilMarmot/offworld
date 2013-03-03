package com.bitwaffle.guts.desktop.devmode;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import com.bitwaffle.guts.desktop.DesktopGame;

/**
 * This class handles creating and displaying a resizeable window to render to
 * 
 * @author TranquilMarmot
 */
public class DevModeDisplay {
	/** the minimum size that the window can be */
	private static final int MIN_FRAME_WIDTH = 1024, MIN_FRAME_HEIGHT = 768;
	
	/** the window's title */
	private static String windowTitle = "Offworld " + DesktopGame.VERSION + " DEV MODE";

	/** The frame everything is in so that the window is resizeable */
	public static JFrame frame;
	
	/** Canvas that game is being drawn in */
	public static Canvas gameCanvas;
	
	/**
	 * Creates a window to render to
	 */
	public static void createFrame(){
		LwjglNativesLoader.load();
		
		// create the frame that holds everything
		frame = new JFrame(windowTitle);
		frame.setLayout(new BorderLayout());
		frame.setBackground(java.awt.Color.black);
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ICON_PATH));
		gameCanvas = new Canvas();
		
		// add listeners to the canvas
		// handles closing the window
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {				
				frame.dispose();
				Gdx.app.exit();
			}
		});

		// add the canvas to the frame
		frame.add(gameCanvas, BorderLayout.CENTER);

		// pretty self explanatory, boilerplate awt frame creation
		frame.setPreferredSize(new Dimension(DesktopGame.windowWidth, DesktopGame.windowHeight));
		frame.setMinimumSize(new Dimension(MIN_FRAME_WIDTH,
				MIN_FRAME_HEIGHT));
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DesktopGame.aspect = (float)DesktopGame.windowWidth / (float)DesktopGame.windowHeight;
		
		// from LibGDX's LwjglFrame class
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run () {
				Runtime.getRuntime().halt(0); // Because fuck you, deadlock causing Swing shutdown hooks.
			}
		});
		
		gameCanvas.requestFocus();
	}
}