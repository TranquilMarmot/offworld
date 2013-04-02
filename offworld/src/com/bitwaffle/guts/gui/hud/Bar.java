package com.bitwaffle.guts.gui.hud;

import com.bitwaffle.guts.graphics.Render2D;
import com.bitwaffle.guts.gui.button.TiledButton;

public class Bar extends TiledButton {
	
	private float percent;
	
	public Bar(float x, float y, int columns, int rows, float columnWidth,
			float rowHeight) {
		super(x, y, columns, rows, columnWidth, rowHeight);
	}

	@Override
	protected void onRelease() {}

	@Override
	protected void onSlideRelease() {}

	@Override
	protected void onPress() {}

	@Override
	protected void onSelect() {}

	@Override
	protected void onUnselect() {}

	@Override
	protected void onDrag(float dx, float dy) {}

	@Override
	public void update(float timeStep) {}
	
	public void setPercent(float percent){
		this.percent = percent;
	}
	
	public float currentPresent(){
		return percent;
	}
	
	@Override
	public void render(Render2D renderer, boolean flipHorizontal, boolean flipVertical){
		super.render(renderer, flipHorizontal, flipVertical);
		
		float width = columns * columnWidth * (percent / 100.0f);
		float height = rows * rowHeight;
		
		renderer.quad.render(width, height);
	}
}
