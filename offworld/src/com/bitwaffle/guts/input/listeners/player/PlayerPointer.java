package com.bitwaffle.guts.input.listeners.player;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.util.MathHelper;
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
		if(Render2D.camera != null && Render2D.camera.currentMode() == Camera.Modes.FOLLOW && 
			player != null && !player.isShooting()
			&& !Game.gui.hasSelectedButton())
				player.beginShooting(MathHelper.toWorldSpace(x, y, Render2D.camera));
	}

	public void up(float x, float y){
		if(player != null && player.isShooting())
			player.endShooting();
	}
	
	public void move(float x, float y){
		if(!player.isShooting())
			player.beginShooting(MathHelper.toWorldSpace(x, y, Render2D.camera));
		else
			player.setTarget(MathHelper.toWorldSpace(x, y, Render2D.camera));
	}
}
