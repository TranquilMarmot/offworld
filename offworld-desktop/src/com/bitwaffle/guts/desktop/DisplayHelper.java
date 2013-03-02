package com.bitwaffle.guts.desktop;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicReference;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.bitwaffle.guts.graphics.Render2D;

/**
 * This class handles creating and displaying a resizeable window to render to
 * 
 * @author TranquilMarmot
 */
public class DisplayHelper {
	/** Path to icon for window */
	private static final String ICON_PATH = "assets/icon.png";
	
	/** How many samples to use for multisample anti-aliasing */
	public static final int MSAA_SAMPLES = 4;
	
	/** the minimum size that the window can be */
	private static final int MIN_WINDOW_WIDTH = 1024, MIN_WINDOW_HEIGHT = 768;
	
	/** the window's title */
	private static String windowTitle = "Offworld " + DesktopGame.VERSION;

	/** target fps (might not be reached on slower machines) */
	public static int targetFPS = 60;
	
	/** Whether or not vsync is being used */
	public static boolean vsync = true;

	/** This is just to handle the resizeable window */
	private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();

	/** The frame everything is in so that the window is resizeable */
	public static Frame frame;
	
	/** Used to preserve the size of the window when switching between fullscreen and windowed */
	private static int oldWindowHeight, oldWindowWidth;
	
	/** whether or not the game is running in fullscreen mode */
	public static boolean fullscreen = false;
	
	/**
	 * Creates a window to render to
	 */
	public static void createWindow(){
		System.out.println("wat");
		// create the frame that holds everything
		frame = new Frame(windowTitle);
		frame.setLayout(new BorderLayout());
		frame.setBackground(java.awt.Color.black);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ICON_PATH));
		final Canvas canvas = new Canvas();
		
		// add listeners to the canvas
		// this one handles resizes
		canvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				newCanvasSize.set(canvas.getSize());
			}
		});

		// the canvas is the only thing we want focus on
		frame.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				canvas.requestFocusInWindow();
			}
		});

		// handles closing the window
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//DesktopGame.endGame();
			}
		});

		// add the canvas to the frame
		frame.add(canvas, BorderLayout.CENTER);

		try {
			// set the display to use the canvas
			Display.setParent(canvas);
			Display.setVSyncEnabled(vsync);

			// pretty self explanatory, boilerplate awt frame creation
			frame.setPreferredSize(new Dimension(DesktopGame.windowWidth, DesktopGame.windowHeight));
			frame.setMinimumSize(new Dimension(MIN_WINDOW_WIDTH,
					MIN_WINDOW_HEIGHT));
			frame.pack();
			frame.setVisible(true);
			
			// for creating a display with multisampling
			PixelFormat pf = new PixelFormat().withSamples(MSAA_SAMPLES);
			
			try{
				Display.create(pf);
			} catch(LWJGLException e){
				//Logger.error("Couldn't initialize display with " + MSAA_SAMPLES + "x MSAA, initializing with no anti-aliasing instead");
				try{
					Display.create();
				} catch(LWJGLException e2){
					//Logger.error("Couldn't initialize display! (Most likely means graphics card or OpenGL isn't supported)");
					Sys.alert("Couldn't initialize display!", "ERROR: Couldn't initialize display\nMost likely means the game can't access the graphics card or OpenGL isn't supported.");
					System.exit(1);
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		DesktopGame.aspect = (float)DesktopGame.windowWidth / (float)DesktopGame.windowHeight;
	}
	
	/**
	 * Resizes the window if it's dimensions have been changed
	 */
	public static void resizeWindow(){
		Dimension d = new Dimension();

		/*
		 * newCanvasSize is updated by the listener on the canvas that
		 * everything is being displayed on. This gets the current
		 * newCanvasSize, and then sets it to null. This way, whenever the size
		 * hasn't changed newCanvasSize is null and nothing happens. If the size
		 * has changed, newCanvasSize will contain the new size and won't be
		 * null.
		 */
		d = newCanvasSize.getAndSet(null);

		if (d != null) {
			// change the height and width
			DesktopGame.windowWidth = (int) d.getWidth();
			DesktopGame.windowHeight = (int) d.getHeight();
			// reset the viewport
			GL11.glViewport(0, 0, DesktopGame.windowWidth, DesktopGame.windowHeight);
			DesktopGame.aspect = (float)DesktopGame.windowWidth / (float)DesktopGame.windowHeight;
			// force the camera to update it's view of the world
			if(Render2D.camera != null)
				Render2D.camera.setZoom(Render2D.camera.getZoom());
		}
		
		doFullscreenLogic();
	}
	
	/**
	 * Checks to see if the fullscreen key has been pressed and, if it has, acts accordingly
	 */
	private static void doFullscreenLogic(){
		//check for fullscreen key press
		//if (KeyBindings.SYS_FULLSCREEN.pressedOnce())
		//	fullscreen = !fullscreen;
		
		
		//take care of necessary funtcion calls
		try {
			// switch from windowed to fullscreen
			if (fullscreen && !Display.isFullscreen()) {
				// save the window width and height
				oldWindowWidth = DesktopGame.windowWidth;
				oldWindowHeight = DesktopGame.windowHeight;
				// fullscreen the display
				Display.setTitle(windowTitle + " (FULLSCREEN)");
				Display.setFullscreen(fullscreen);
				frame.setVisible(false);
				// set the width and height
				DisplayMode dm = Display.getDisplayMode();
				DesktopGame.windowWidth = dm.getWidth();
				DesktopGame.windowHeight = dm.getHeight();
				
			// switch from fullscreen to windowed
			} else if (!fullscreen && Display.isFullscreen()) {
				// get the window width and height from before fullscreen
				DesktopGame.windowWidth = oldWindowWidth;
				DesktopGame.windowHeight = oldWindowHeight;
				// un-fullscreen the display
				frame.setVisible(true);
				Display.setFullscreen(fullscreen);
				
				frame.requestFocus();
			}
			
			DesktopGame.aspect = (float)DesktopGame.windowWidth / (float)DesktopGame.windowHeight;
			
			// change the viewport
			GL11.glViewport(0, 0, DesktopGame.windowWidth, DesktopGame.windowHeight);
			
			if(Render2D.camera != null)
				Render2D.camera.setZoom(Render2D.camera.getZoom());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		// make sure the frame's title is right (this allows for a changing title!)
		if (!(frame.getTitle().equals(windowTitle)))
			frame.setTitle(windowTitle);
	}
	
	/*
    private static ByteBuffer loadIcon(String file){
        try {
        	InputStream is = new FileInputStream(file);
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            is.close();
            return bb;
        } catch (IOException e){
        	e.printStackTrace();
        }
        
        return null;
    }
    */
}
