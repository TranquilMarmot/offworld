package com.bitwaffle.offworld.gui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.offworld.OffworldGame;

/**
 * Just draws simple info to the screen
 * 
 * @author TranquilMarmot
 */
public class DebugText extends GUIObject {
	public DebugText() { super(new DebugTextRenderer(), 0.0f, 0.0f); }

	private static class DebugTextRenderer implements ObjectRenderer2D{
		@Override
		public void render(Renderer renderer, Object ent) {
			// draw some debug info
			float[] debugTextColor = new float[]{ 0.3f, 0.3f, 0.3f, 1.0f };
			float tscale = 0.35f;
			
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ZERO);
			
			String vers = "Version " + OffworldGame.VERSION;
			renderer.r2D.font.drawString(
					vers, renderer,
					Game.windowWidth - renderer.r2D.font.stringWidth(vers, tscale),
					renderer.r2D.font.stringHeight(vers, tscale) * 2,
					tscale, debugTextColor
			);
			
			String fps = Game.currentFPS + " FPS";
			renderer.r2D.font.drawString(
					fps, renderer,
					Game.windowWidth - renderer.r2D.font.stringWidth(fps, tscale),
					renderer.r2D.font.stringHeight(fps, tscale) * 4,
					tscale, debugTextColor
			);
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
	}

	@Override
	public void cleanup() {}
	@Override
	public void update(float timeStep) {}
}
