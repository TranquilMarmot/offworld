package com.bitwaffle.offworld.renderers;

import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.entities.dynamic.PolygonEntity;
import com.bitwaffle.guts.graphics.render.EntityRenderer;
import com.bitwaffle.guts.graphics.render.Render2D;

public class PolygonEntityRenderer implements EntityRenderer {
	@Override
	public void render(Render2D renderer, Entity ent, boolean renderDebug) {
		PolygonEntity poly = (PolygonEntity)ent;
		poly.renderPolygon(renderer);
	}

}
