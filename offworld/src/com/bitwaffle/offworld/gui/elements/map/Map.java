package com.bitwaffle.offworld.gui.elements.map;

import com.bitwaffle.guts.graphics.graphics2d.shapes.TiledBox;
import com.bitwaffle.guts.gui.elements.GUIObject;
import com.bitwaffle.offworld.gui.elements.toolbox.Toolbox;

public class Map extends GUIObject {
	private static final String
	BUTTON_CORNER = "buttoncorner",
	BUTTON_SEGMENT = "buttonsegment",
	BUTTON_SIDE = "buttonside",
	BUTTON_MIDDLE = "blank";
	
	public TiledBox box;
	
	public Toolbox toolbox;

	public int width = 100, height = 100;
	public float mapZoom = 0.0125f;

	public Map(Toolbox toolbox) {
		super(new MapRenderer(),0.0f, 0.0f);
		this.toolbox = toolbox;
		
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
	}

	@Override
	public void cleanup() {
		
	}
}
