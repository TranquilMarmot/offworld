package com.bitwaffle.offworld.renderers;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.PolygonEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;

public class PolygonEntityRenderer implements EntityRenderer {
	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		PolygonEntity poly = (PolygonEntity)ent;
		renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, 1.0f);
		Game.resources.polygons.renderPolygon(renderer, poly.getPolygonName());
	}
}
