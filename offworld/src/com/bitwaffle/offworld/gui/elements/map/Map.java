package com.bitwaffle.offworld.gui.elements.map;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButton;
import com.bitwaffle.guts.gui.elements.slider.Slider;
import com.bitwaffle.offworld.gui.elements.toolbox.Toolbox;

/**
 * A map that shows a larger portion of the map than what the player can see.
 * Can be zoomed, panned, etc.
 * 
 * @author TranquilMarmot
 */
public class Map extends RectangleButton {
	/** Toolbox this map belongs to */
	public Toolbox toolbox;
	
	/** Slider that controls map's zoom */
	public Slider zoomSlider;

	/** How large of a transparent border is around the map */
	public int borderWidth = 125, borderHeight = 75;
	
	/** Offset from center of screen that map is drawn at */
	public int xOffset = -32, yOffset = -37;
	
	/** Zoom level that map is rendered at */
	public float mapZoom = 0.0125f;
	/** Minimum and maximum zoom levels for zoom slider */
	public float minMapZoom = 0.005f, maxMapZoom = 0.025f;
	
	/** How scrolled the map is from the player */
	public float scrollX = 0.0f, scrollY = 0.0f;

	public Map(Toolbox toolbox) {
		super(new MapRenderer(),0.0f, 0.0f, 10.0f, 10.0f);
		this.toolbox = toolbox;
		zoomSlider = new Slider(
				Slider.SlideOrientation.VERTICAL,
				// center of track (moves with map)
				100.0f, 750.0f,
				// width and height of track
				15.0f, 100.0f,
				// width and height of thumb
				15.0f, 15.0f);
	}

	@Override
	public void update(float timeStep) {
		MapRenderer mapRend = (MapRenderer) this.renderer;
		
		// position zoom slider on the left of the map
		zoomSlider.setTrackHeight(mapRend.mapRenderHeight() / 2.0f);
		zoomSlider.setCenterX(mapRend.viewXOffset() - zoomSlider.trackWidth());
		zoomSlider.setCenterY(Game.windowHeight - mapRend.viewYOffset() - zoomSlider.trackHeight());
		
		// set button locations/dimensions so map can be clicked
		this.x = mapRend.viewXOffset() + (mapRend.mapRenderWidth() / 2.0f);
		this.y = (Game.windowHeight - mapRend.viewYOffset()) - (mapRend.mapRenderHeight() / 2.0f);
		this.width = (mapRend.mapRenderWidth() / 2.0f);
		this.height = mapRend.mapRenderHeight() / 2.0f;
				
		
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
	}

	@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}

	@Override
	protected void onDrag(float dx, float dy) {
		scrollX += (dx / (mapZoom * 1000.0f));
		scrollY -= (dy / (mapZoom * 1000.0f));
	}
}
