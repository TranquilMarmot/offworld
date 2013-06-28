package com.bitwaffle.offworld.graphics;

import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.offworld.OffworldGame;

public class OffworldRenderer extends Renderer {
	
	@Override
	public void renderScene(){
		if(OffworldGame.players[0] != null && OffworldGame.players[0].getCamera() != null)
			this.camera = OffworldGame.players[0].getCamera();
		// TODO do glViewPort for each player and render the scene with that player's camera
		super.renderScene();
	}
}
