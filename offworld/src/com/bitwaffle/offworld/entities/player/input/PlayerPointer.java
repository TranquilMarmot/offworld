package com.bitwaffle.offworld.entities.player.input;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.MathHelper;
import com.bitwaffle.guts.gui.button.Button;
import com.bitwaffle.offworld.camera.CameraModes;
import com.bitwaffle.offworld.entities.player.Player;

/**
 * Since we don't want the player to shoot if a UI element is pressed,
 * this extends ButtonPointer and only shoots if a button isn't pressed.
 * 
 * @author TranquilMarmot
 */
public class PlayerPointer {
	/** Player this pointer is controlling */
	private Player player;

	public PlayerPointer(Player player) {
		super();
		this.player = player;
	}
	
	public void down(int pointerID, float x, float y){
		if(Game.renderer.r2D.camera != null && Game.renderer.r2D.camera.currentMode() == CameraModes.follow && 
			player != null && !player.isShooting()
			&& !Game.gui.hasSelectedButton() && (Game.gui.buttonAt(x, y) == null))
				player.beginShooting(MathHelper.toWorldSpace(x, y, Game.renderer.r2D.camera));
	}

	public void up(float x, float y){
		Button buttonAt = Game.gui.buttonAt(x, y);
		if(player != null && player.isShooting() && (buttonAt == null || !buttonAt.isDown()))
			player.endShooting();
	}
	
	public void move(float x, float y){
		Button buttonAt = Game.gui.buttonAt(x, y);
		if(player.isShooting() && (buttonAt == null || !buttonAt.isDown()))
			player.setTarget(MathHelper.toWorldSpace(x, y, Game.renderer.r2D.camera));
		else if(buttonAt == null)
			player.beginShooting(MathHelper.toWorldSpace(x, y, Game.renderer.r2D.camera));
	}
}
