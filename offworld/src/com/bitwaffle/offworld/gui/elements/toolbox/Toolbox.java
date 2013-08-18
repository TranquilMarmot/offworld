package com.bitwaffle.offworld.gui.elements.toolbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.button.Button;
import com.bitwaffle.guts.gui.elements.button.RectangleButton;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * An in-game menu for accessing maps, inventory, etc.
 * 
 * @author TranquilMarmot
 */
public class Toolbox extends RectangleButton {
	/** The player that this toolbox belongs to */
	private Player player;
	
	/** The buttons inside the toolbox */
	private ArrayList<Button> buttons;
	
	/** Whether or not the toolbox's buttons are being shown */
	private boolean buttonsHidden;
	
	public Toolbox(Player player, float x, float y, float width, float height){
		super(x, y, width, height);
		this.player = player;
		buttons = new ArrayList<Button>();
		buttonsHidden = true;
	}
	
	public void addButton(Button button){ buttons.add(button); }
	public void removeButton(Button button){ buttons.remove(button); }
	public void setButton(int number, Button button){ buttons.set(number, button); }
	public Button getButton(int number){ return buttons.get(number); }

	@Override
	public void render(Renderer renderer, boolean flipHorizontal,
			boolean flipVertical) {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		float r = 0.5f;
		float g = 0.5f;
		float b = 0.5f;
		float a = 0.5f;
		if(this.isDown())
			a = 1.0f;
		else if(this.isSelected())
			a = 0.75f;
		renderer.r2D.setColor(r, g, b, a);
		
		Game.resources.textures.bindTexture("toolboxbutton");
		renderer.r2D.quad.render(this.width, this.height, flipHorizontal, flipVertical);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

	@Override
	protected void onRelease() {
		buttonsHidden = !buttonsHidden;
		
		if(!buttonsHidden){
			float oldX = this.x;
			for(Button b : buttons){
				this.x += b.getWidth();
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

	@Override
	public void update(float timeStep) {
		
	}
}
