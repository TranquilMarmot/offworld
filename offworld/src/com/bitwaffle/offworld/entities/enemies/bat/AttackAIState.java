package com.bitwaffle.offworld.entities.enemies.bat;

import com.bitwaffle.guts.ai.path.PathFinderSettings;
import com.bitwaffle.guts.ai.states.PathFollower;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.offworld.entities.player.Player;

public class AttackAIState extends PathFollower {
	/** Player bat is attacking */
	private Player player;
	
	public AttackAIState(DynamicEntity ent, PathFinderSettings settings, float nodeThreshold, float followSpeed) {
		super(ent, settings, nodeThreshold, followSpeed);
		
	}
	
	public void setPlayer(Player player){ this.player = player; }
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		((Bat)controlling).attackState.pathfinder.getCurrentSettings().start.set(controlling.getLocation());
		((Bat)controlling).attackState.pathfinder.getCurrentSettings().goal.set(player.getLocation());
	}
}
