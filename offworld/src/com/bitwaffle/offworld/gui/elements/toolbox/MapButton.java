package com.bitwaffle.offworld.gui.elements.toolbox;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.TransparentRectangleButton;
import com.bitwaffle.offworld.gui.elements.map.Map;

public class MapButton extends TransparentRectangleButton {
	
	/** Actual map GUI object */
	private Map map;
	
	private boolean mapInGui;

	public MapButton(Toolbox toolbox, float x, float y, float width, float height) {
		super(x, y, width, height, "mapbutton", new Color(0.5f, 0.5f, 0.5f, 0.75f), new Color(0.5f, 0.5f, 0.5f, 1.0f));
		map = new Map(150.0f, 150.0f, toolbox);
		mapInGui = false;
	}

	@Override
	protected void onRelease() {
		if(!mapInGui)
			Game.gui.addObject(map);
		else
			Game.gui.removeObject(map);
		
		mapInGui = !mapInGui;
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
