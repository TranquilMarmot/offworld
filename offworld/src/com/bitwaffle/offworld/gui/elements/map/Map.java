package com.bitwaffle.offworld.gui.elements.map;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.graphics2d.shapes.TiledBox;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.guts.gui.elements.slider.Slider;
import com.bitwaffle.offworld.gui.elements.toolbox.Toolbox;

public class Map extends GUIObject {
	private static final String
	BUTTON_CORNER = "buttoncorner",
	BUTTON_SEGMENT = "buttonsegment",
	BUTTON_SIDE = "buttonside",
	BUTTON_MIDDLE = "blank";
	
	public TiledBox box;
	
	public Toolbox toolbox;
	
	public Slider zoomSlider;

	public int width = 150, height = 100;
	
	public float mapZoom = 0.0125f;
	private float minMapZoom = 0.005f, maxMapZoom = 0.025f;

	public Map(Toolbox toolbox) {
		super(new MapRenderer(),0.0f, 0.0f);
		this.toolbox = toolbox;
		zoomSlider = new Slider(Slider.SlideOrientation.VERTICAL, 100.0f, 750.0f, 10.0f, 100.0f, 10.0f, 10.0f);
		
		box = new TiledBox(10, 10, 16.0f, 16.0f,
				BUTTON_CORNER, BUTTON_SEGMENT, BUTTON_SIDE, BUTTON_MIDDLE);
	}

	@Override
	public void update(float timeStep) {
		MapRenderer mapRend = (MapRenderer) this.renderer;
		
		int numRows = 15, numCols = 15;
		box.setRows(numRows);
		box.setColumns(numCols);
		box.setColumnWidth(mapRend.mapRenderWidth / (numRows * 2));
		box.setRowHeight(mapRend.mapRenderHeight / (numCols * 2));
		
		// position zoom slider on the left of the map
		zoomSlider.setTrackHeight(mapRend.mapRenderHeight / 2.0f);
		zoomSlider.setCenterX(mapRend.viewXOffset - zoomSlider.trackWidth());
		zoomSlider.setCenterY(Game.windowHeight - mapRend.viewYOffset - zoomSlider.trackHeight());
		
		// set map zoom to slider's position
		mapZoom = zoomSlider.getValue(maxMapZoom, minMapZoom);
	}

	@Override
	public void cleanup() {}
}
