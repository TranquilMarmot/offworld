package com.bitwaffle.offworld.gui.elements.map;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.guts.gui.elements.slider.Slider;
import com.bitwaffle.offworld.gui.elements.toolbox.Toolbox;

public class Map extends RectangleButton {
	/** Toolbox this map belongs to */
	public Toolbox toolbox;
	
	/** Slider that controls map's zoom */
	public Slider zoomSlider;

	/** */
	public int borderWidth = 150, borderHeight = 100;
	
	/** Zoom level that map is rendered at */
	public float mapZoom = 0.0125f;
	/** Minimum and maximum zoom levels for zoom slider */
	private float minMapZoom = 0.005f, maxMapZoom = 0.025f;

	public Map(Toolbox toolbox) {
		super(new MapRenderer(),0.0f, 0.0f, 10.0f, 10.0f);
		this.toolbox = toolbox;
		zoomSlider = new Slider(Slider.SlideOrientation.VERTICAL, 100.0f, 750.0f, 10.0f, 100.0f, 10.0f, 10.0f);
	}

	@Override
	public void update(float timeStep) {
		MapRenderer mapRend = (MapRenderer) this.renderer;
		
		// position zoom slider on the left of the map
		zoomSlider.setTrackHeight(mapRend.mapRenderHeight / 2.0f);
		zoomSlider.setCenterX(mapRend.viewXOffset - zoomSlider.trackWidth());
		zoomSlider.setCenterY(Game.windowHeight - mapRend.viewYOffset - zoomSlider.trackHeight());
		
		this.x = mapRend.viewXOffset + (mapRend.mapRenderWidth / 2.0f);
		this.y = (Game.windowHeight - mapRend.viewYOffset) - (mapRend.mapRenderHeight / 2.0f);
		this.width = (mapRend.mapRenderWidth / 2.0f);
		this.height = mapRend.mapRenderHeight / 2.0f;
				
		
		// set map zoom to slider's position
		mapZoom = zoomSlider.getValue(maxMapZoom, minMapZoom);
	}

	@Override
	public void cleanup() {}

	@Override
	protected void onRelease() {
	}

	@Override
	protected void onSlideRelease() {
	}

	@Override
	protected void onPress() {	
		System.out.println("iom the map");
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
}
