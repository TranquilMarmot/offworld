package com.bitwaffle.moguts.gui;

/**
 * Everything that the GUI uses should extend this class.
 * @author TranquilMarmot
 */
public abstract class GUIObject {
    /** whether or not the object is visible */
    public boolean isVisible;
    
    /** top left of the object */
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
     * Updates this object. Should set the <code>mouseOver</code> variable depending on whether or not the mouse is over the object.
     */
    public abstract void update();
    
    /**
     * Draws the object.
     */
    public abstract void draw();
    
    /**
     * Get rid of any resources the object may have allocated
     */
    public abstract void cleanup();
}
