package com.bitwaffle.offworld.gui;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.states.BlankState;
import com.bitwaffle.guts.gui.states.GUIState;
import com.bitwaffle.offworld.gui.options.OptionsState;
import com.bitwaffle.offworld.gui.pause.PauseState;
import com.bitwaffle.offworld.gui.splash.SplashScreenState;
import com.bitwaffle.offworld.gui.titlescreen.TitleScreenState;

public enum GUIStates {
	NONE(new BlankState(Game.gui)),              // nothing
	PAUSE(new PauseState(Game.gui)),             // displayed when the game is paused
	TITLESCREEN(new TitleScreenState(Game.gui)), // the title screen
	OPTIONS(new OptionsState(Game.gui)),         // the options screen
	SPLASH(new SplashScreenState(Game.gui));     // splash screen (only displayed for a bit at beginning of game)
	
	// Each value in this enum basically acts as a wrapper to access a GUIState
	public GUIState state;
	GUIStates(GUIState state){ this.state = state; }
	protected void update(float timeStep){ state.update(timeStep); }
	protected void loseCurrentState(){ state.loseCurrentState(); }
	protected void gainCurrentState(){ state.gainCurrentState(); }
}
