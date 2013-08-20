package com.bitwaffle.offworld.gui.elements.toolbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.gui.elements.button.RectangleButton;

public class InventoryButton extends RectangleButton {

	public InventoryButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Renderer renderer, boolean flipHorizontal,
			boolean flipVertical) {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR);
		
		float 
			r = 0.5f,
			g = 0.5f,
			b = 0.5f,
			a = this.isDown() ? 1.0f : 0.75f;
		renderer.r2D.setColor(r, g, b, a);
		
		Game.resources.textures.bindTexture("inventorybutton");
		renderer.r2D.quad.render(this.width, this.height, flipHorizontal, flipVertical);
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

	@Override
	protected void onRelease() {
	}

	@Override
	protected void onSlideRelease() {
	}

	@Override
	protected void onPress() {
	}

	@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}

	@Override
	protected void onDrag(float dx, float dy) {
	}

	@Override
	public void update(float timeStep) {
	}

}
