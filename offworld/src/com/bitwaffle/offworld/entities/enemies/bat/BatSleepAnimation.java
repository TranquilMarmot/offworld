package com.bitwaffle.offworld.entities.enemies.bat;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.animation.Animation;

public class BatSleepAnimation extends Animation {
	
	private Bat bat;

	public BatSleepAnimation(Bat bat) {
		super(Game.resources.textures.getAnimation("bat-sleep"));
		this.bat = bat;
	}

}
