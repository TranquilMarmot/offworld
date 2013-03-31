package com.bitwaffle.guts.input.listeners.player;

import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.graphics.camera.Camera;
import com.bitwaffle.guts.input.listeners.button.ButtonPointer;
import com.bitwaffle.guts.util.MathHelper;
import com.bitwaffle.offworld.entities.player.Player;

public class PlayerPointer extends ButtonPointer {
	private Player player;

	public PlayerPointer(Player player) {
		super();
		this.player = player;
	}
	
	@Override
	public void down(int pointerID, float x, float y){
		this.pointerID = pointerID;
		this.prevX = this.x;
		this.prevY = this.y;
		this.x = x;
		this.y = y;
		this.isDown = true;
		
		if(!checkForButtonPresses() && Render2D.camera != null && Render2D.camera.currentMode() != Camera.Modes.FREE){
			if(player != null && !player.isShooting())
				player.beginShooting(MathHelper.toWorldSpace(x, y, Render2D.camera));
		}
	}

	@Override
	public void up(float x, float y){
		super.up(x, y);
		
		if(player != null && player.isShooting())
			player.endShooting();
	}
	
	@Override
	public void move(float newX, float newY){
		super.move(newX,  newY);
		
		if(buttonDown == null){
			if(!player.isShooting())
				player.beginShooting(MathHelper.toWorldSpace(x, y, Render2D.camera));
			else
				player.setTarget(MathHelper.toWorldSpace(x, y, Render2D.camera));
		}
	}
}
