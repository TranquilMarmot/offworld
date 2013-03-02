package com.bitwaffle.guts.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	private LwjglCanvas canvas;
	
	public GameFrame(ApplicationListener listener, LwjglApplicationConfiguration config){
		super(config.title);
		construct(listener, config);
	}
	
	private void construct(ApplicationListener listener, LwjglApplicationConfiguration config){
		canvas = new LwjglCanvas(new DesktopGame(), config){
			protected void stopped () {
				DesktopGame.frame.dispose();
			}
			
			protected void setTitle (String title) {
				DesktopGame.frame.setTitle(title);
			}
			
			protected void setDisplayMode (int width, int height) {
				DesktopGame.frame.getContentPane().setPreferredSize(new Dimension(width, height));
				DesktopGame.frame.getContentPane().invalidate();
				DesktopGame.frame.pack();
				DesktopGame.frame.setLocationRelativeTo(null);
				updateSize(width, height);
			}
			
			protected void resize (int width, int height) {
				DesktopGame.frame.updateSize(width, height);
			}
			
			protected void start () {
				DesktopGame.frame.start();
			}
			
			protected void exception (Throwable t) {
				DesktopGame.frame.exception(t);
			}
		};
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run () {
				Runtime.getRuntime().halt(0); // Because fuck you, deadlock causing Swing shutdown hooks.
			}
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setPreferredSize(new Dimension(config.width, config.height));
		
		initialize();
		pack();
		Point location = getLocation();
		if (location.x == 0 && location.y == 0) setLocationRelativeTo(null);
		canvas.getCanvas().setSize(getSize());

		// Finish with invokeLater so any LwjglFrame super constructor has a chance to initialize.
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				addCanvas();
				setVisible(true);
				canvas.getCanvas().requestFocus();
			}
		});
	}
	
	protected void exception (Throwable ex) {
		ex.printStackTrace();
		canvas.stop();
	}
	
	/** Called before the JFrame is made displayable. */
	protected void initialize () {
	}
	
	/** Adds the canvas to the content pane. This triggers addNotify and starts the canvas' game loop. */
	protected void addCanvas () {
		//getContentPane().add(canvas.getCanvas());
		Panel wat = new Panel();
		wat.setBackground(Color.PINK);
		wat.setSize(100,  100);
		wat.add(new JTextArea("buuts"));
		wat.add(canvas.getCanvas());
		getContentPane().add(wat);
	}
	
	/** Called when the canvas size changes. */
	public void updateSize (int width, int height) {
	}
	
	/** Called after {@link ApplicationListener} create and resize, but before the game loop iteration. */
	protected void start () {
	}

}
