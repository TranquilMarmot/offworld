package com.bitwaffle.guts.gui.state.titlescreen;

import com.bitwaffle.guts.entities.passive.GLSLSandbox;
import com.bitwaffle.guts.gui.state.GUIState;

public class TitleScreen extends GUIState {
	
	private GLSLSandbox sandbox;
	
	public TitleScreen(){
		super();
		
		sandbox = new GLSLSandbox();
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
	}
	
	
}
