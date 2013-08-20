package com.bitwaffle.guts.gui.elements;

import com.bitwaffle.guts.graphics.Renderer;

/**
 * Everything that the GUI uses should extend this class.
 * @author TranquilMarmot
 */
public abstract class GUIObject {
    /** Whether or not the object is visible */
    private boolean isVisible;
    
    /** Middle of the object */
    public float x, y;
    
    /**
     * GUIObject constructor
     * @param x Initial X position
     * @param y Initial Y position
     */
    public GUIObject(float x, float y){
        this.x = x;
        this.y = y;
        isVisible = true;
    }
    
    /** Updates this object */
    public abstract void update(float timeStep);
    
    /** Draws the object */
    public abstract void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical);
    
    /** Get rid of any resources the object may have allocated */
    public abstract void cleanup();
    
    /** Hides this GUI object (skips rendering) */
	public void hide(){ isVisible = false; }
	
	/** Shows this GUI object */
	public void show(){ isVisible = true; }
	
	/** @return Whether or not this object is being rendered */
	public boolean isVisible(){ return isVisible; }
}
