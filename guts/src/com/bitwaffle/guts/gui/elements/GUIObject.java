package com.bitwaffle.guts.gui.elements;

import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;

/**
 * Everything that the GUI uses should extend this class.
 * @author TranquilMarmot
 */
public abstract class GUIObject {
    /** Whether or not the object is visible */
    private boolean isVisible;
    
    /** Middle of the object */
    public float x, y;
    
    /** Rotation of object */
    public float angle;
    
    /** Renders this object */
    public ObjectRenderer2D renderer;
    
    /**
     * @param x Initial X position
     * @param y Initial Y position
     */
    public GUIObject(ObjectRenderer2D renderer, float x, float y){
        this(renderer, x, y, 0.0f);
    }
    
    /**
     * @param x Initial X position
     * @param y Initial Y position
     * @param angle Rotation of object
     */
    public GUIObject(ObjectRenderer2D renderer, float x, float y, float angle){
    	this.renderer = renderer;
    	this.x = x;
    	this.y = y;
    	this.angle = angle;
    	isVisible = true;
    }
    
    /** Updates this object */
    public abstract void update(float timeStep);
    
    /** Get rid of any resources the object may have allocated */
    public abstract void cleanup();
    
    /** Hides this GUI object (skips rendering) */
	public void hide(){ isVisible = false; }
	
	/** Shows this GUI object */
	public void show(){ isVisible = true; }
	
	/** @return Whether or not this object is being rendered */
	public boolean isVisible(){ return isVisible; }
}
