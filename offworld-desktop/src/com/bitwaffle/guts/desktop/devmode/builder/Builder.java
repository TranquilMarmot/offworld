package com.bitwaffle.guts.desktop.devmode.builder;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.Panel;

import javax.swing.BoxLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.desktop.devmode.DevModeDisplay;

public class Builder {
	private static int[] koonami = new int[]{
		Input.Keys.DPAD_UP, Input.Keys.DPAD_UP,
		Input.Keys.DPAD_DOWN,Input.Keys.DPAD_DOWN, 
		Input.Keys.DPAD_LEFT, Input.Keys.DPAD_RIGHT,
		Input.Keys.DPAD_LEFT, Input.Keys.DPAD_RIGHT,
		Input.Keys.B, Input.Keys.A, Input.Keys.ENTER,
	};
	private int currentKey = 0;
	private boolean currentKeyDown = false;
	
	/** Labels that hold info about the game */
	private Label numEntsLabel, fpsLabel;
	
	public Builder(){
		numEntsLabel = new Label();
		fpsLabel = new Label();
	}
	
	public void update(){
		if(currentKey >= 0){
			if(Gdx.input.isKeyPressed(koonami[currentKey])){
				currentKeyDown = true;
			} else if(currentKeyDown && !Gdx.input.isKeyPressed(koonami[currentKey])){
				currentKeyDown = false;
				currentKey++;
				if(currentKey == koonami.length){
					currentKey = -1;
					enterDevMode();
				}
			}
		}
		
		numEntsLabel.setText("Num Ents:    " + Game.physics.numEntities());
		fpsLabel.setText("Current FPS: " + Game.currentFPS);
	}
	
	private void enterDevMode(){
		DevModeDisplay.frame.add(createInfoPanel(), BorderLayout.WEST);
		DevModeDisplay.frame.validate();
	}
	
	private Panel createInfoPanel(){
		Panel infoPanel = new Panel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		infoPanel.add(numEntsLabel);
		infoPanel.add(fpsLabel);
		
		
		return infoPanel;
	}
}
