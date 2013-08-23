package com.bitwaffle.offworld.gui.elements.toolbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.elements.button.TransparentRectangleButton;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * An in-game menu for accessing maps, inventory, etc.
 * 
 * @author TranquilMarmot
 */
public class Toolbox extends TransparentRectangleButton {
	private static String LOGTAG = "Toolbox";
	
	private static float
		/** How far from the top-left cornder of the player's screen the toolbox button is */
		xOffset = 1.5f, yOffset = 1.5f,
		/** How far apart each button is when the toolbox is expanded */
		buttonSpacing = 3.0f;
	
	/** The player that this toolbox belongs to */
	private Player player;
	
	/** The buttons inside the toolbox */
	private ArrayList<Button> buttons;
	
	/** Whether or not the toolbox's buttons are being shown */
	private boolean expanded;
	
	/** Used for saving location of button before it expands */
	private float oldX;
	
	/** @param player Player this toolbox belongs to */
	public Toolbox(Player player, float x, float y, float width, float height){
		super(x, y, width, height, "toolboxbutton", new Color(0.5f, 0.5f, 0.5f, 0.75f), new Color(0.5f, 0.5f, 0.5f, 1.0f));
		this.player = player;
		buttons = new ArrayList<Button>();
		expanded = false;
		oldX = x;
		
		MapButton mapButt = new MapButton(this, 32.0f, 32.0f, 32.0f, 32.0f);
		mapButt.deactivate();
		mapButt.hide();
		this.addButton(mapButt);
		Game.gui.addButton(mapButt);
		
		InventoryButton invButt = new InventoryButton(32.0f, 32.0f, 32.0f, 32.0f);
		invButt.deactivate();
		invButt.hide();
		this.addButton(invButt);
		Game.gui.addButton(invButt);
		
		SettingsButton setButt = new SettingsButton(32.0f, 32.0f, 32.0f, 32.0f);
		setButt.deactivate();
		setButt.hide();
		this.addButton(setButt);
		Game.gui.addButton(setButt);
	}
	
	public void addButton(Button button){ buttons.add(button); }
	public void removeButton(Button button){ buttons.remove(button); }
	
	public Player getPlayer(){ return this.player; }
	
	@Override
	public void update(float timeStep) {
		switch(player.controlInfo.screenSection){
		// top-left corner of screen
		case FULL:
		case TOP_HALF:
		case TOP_LEFT_QUARTER:
			this.x = this.width + xOffset;
			this.y = this.height + yOffset;
			break;
		
		// halfway down the screen on the left
		case BOTTOM_HALF:
		case BOTTOM_LEFT_QUARTER:
			this.x = this.width + xOffset;
			this.y = (Game.windowHeight / 2.0f) + this.height + yOffset;
			break;
			
		// halfway across the screen on the top
		case TOP_RIGHT_QUARTER:
			this.x = (Game.windowWidth / 2.0f) + this.width + xOffset;
			this.y = this.height + yOffset;
			break;
			
		// halfway down the screen and halfway accross
		case BOTTOM_RIGHT_QUARTER:
			this.x = (Game.windowWidth / 2.0f) + this.width + xOffset;
			this.y = (Game.windowHeight / 2.0f) + this.height + yOffset;
			break;
			
		default:
			Gdx.app.error(LOGTAG, "Unkown splitscreen section in toolbox");
			break;
		}
		
		// move out to be next to all buttons if expanded
		if(expanded){
			// save X for when toolbox shrinks again
			oldX = this.x;
			
			// move all buttons to the right spot
			float prevX = this.x;
			for(Button b : buttons){
				b.x = prevX;
				prevX += (b.getWidth() * 2.0f) + buttonSpacing;
				b.y = this.y;
				
				// move toolbox over
				this.x += (b.getWidth() * 2.0f) + buttonSpacing;
			}
		}
		
		// TODO make toolbox slide right to reveal buttons when pressed, maybe make it turn a bit
	}

	@Override
	protected void onRelease() {
		expanded = !expanded;
		
		// show and activate all buttons
		if(expanded){
			this.angle = 90.0f;
			for(Button b : buttons){
				b.show();
				b.activate();
			}
			
		// hide and deactivate all buttons and move toolbox button back to its start
		} else {
			this.angle = 0.0f;
			this.x = oldX;
			for(Button b : buttons){
				b.hide();
				b.deactivate();
			}
		}
	}

	@Override
	protected void onSlideRelease() {
		
	}

	@Override
	protected void onPress() {
		
	}

	@Override
	protected void onSelect() {
		
	}

	@Override
	protected void onUnselect() {
		
	}

	@Override
	protected void onDrag(float dx, float dy) {
		
	}
}
