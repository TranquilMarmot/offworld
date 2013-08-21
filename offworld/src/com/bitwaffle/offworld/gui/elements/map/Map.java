package com.bitwaffle.offworld.gui.elements.map;

import java.util.Iterator;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.Entity;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.guts.physics.Room;

public class Map extends GUIObject {

	public Map(float x, float y) {
		super(x, y);
	}

	@Override
	public void update(float timeStep) {
	}

	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical) {
		Room room = Game.physics.currentRoom();
		Iterator<Entity> geomIt = room.getStaticGeometryIterator();
		
		renderer.modelview.scale(0.1f, 0.1f, 1.0f);
		while(geomIt.hasNext()){
			Entity ent = geomIt.next();
			ent.renderer.render(renderer, ent, true);
		}
		
	}

	@Override
	public void cleanup() {
		
	}
	
}
