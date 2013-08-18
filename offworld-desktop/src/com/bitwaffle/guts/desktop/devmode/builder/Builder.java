package com.bitwaffle.guts.desktop.devmode.builder;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.desktop.devmode.DevModeDisplay;
import com.bitwaffle.guts.graphics.Renderer;

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
		infoPanel.add(createToolPanel());
		
		
		return infoPanel;
	}
	
	private Panel createToolPanel(){
		Panel toolPanel = new Panel(),  leftTools = new Panel();
		
		leftTools.setLayout(new BoxLayout(leftTools, BoxLayout.Y_AXIS));
		Button debugButton = new Button("Debug");
		debugButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Renderer.renderDebug = !Renderer.renderDebug;
			}
		});
		leftTools.add(debugButton);
		
		toolPanel.add(leftTools);
		return toolPanel;
	}
}
