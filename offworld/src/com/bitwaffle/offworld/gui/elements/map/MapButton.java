package com.bitwaffle.offworld.gui.elements.map;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButtonRenderer;
import com.bitwaffle.offworld.gui.elements.toolbox.Toolbox;

public class MapButton extends RectangleButton {
	
	/** Actual map GUI object */
	private Map map;
	
	/** Whether or not the map is being displayed*/
	private boolean mapInGui;

	public MapButton(Toolbox toolbox, float x, float y, float width, float height) {
		super(
				new RectangleButtonRenderer(
						"mapbutton", 
						new Color(0.5f, 0.5f, 0.5f, 0.75f), 
						new Color(0.5f, 0.5f, 0.5f, 1.0f),
						new Color(0.75f, 0.75f, 0.75f, 1.0f),
						new Color(0.5f, 0.5f, 0.5f, 0.8f)
				), 
				x, y, 
				width, height
		);
		map = new Map(toolbox);
		mapInGui = false;
	}
	
	/** @return Whether or not the map is being displayed */
	public boolean mapInGui(){ return mapInGui; }

	@Override
	protected void onRelease() {
		// add map to GUI
		if(!mapInGui){
			Game.gui.addButton(map);
			Game.gui.addButton(map.zoomSlider);

		// remove map from GUI
		} else {
			Game.gui.removeButton(map);
			Game.gui.removeButton(map.zoomSlider);
		}
		
		mapInGui = !mapInGui;
	}
	

	@Override
	public void update(float timeStep) {}

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
}
