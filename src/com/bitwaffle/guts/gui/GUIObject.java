package com.bitwaffle.guts.gui;

import com.bitwaffle.guts.graphics.render.Render2D;

/**
 * Everything that the GUI uses should extend this class.
 * @author TranquilMarmot
 */
public abstract class GUIObject {
    /** whether or not the object is visible */
    public boolean isVisible;
    
    /** middle of the object */
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
    
    /**
     * Updates this object
     */
    public abstract void update();
    
    public void render(Render2D renderer){
    	this.draw(renderer, false, false);
    }
    
    /**
     * Draws the object
     */
    public abstract void draw(Render2D renderer, boolean flipHorizontal, boolean flipVertical);
    
    /**
     * Get rid of any resources the object may have allocated
     */
    public abstract void cleanup();
}
