package com.bitwaffle.guts.swarm;

import com.swarmconnect.SwarmActiveUser;
import com.swarmconnect.delegates.SwarmLoginListener;

/**
 * Handles what happens when the users logs in/out of swarm
 * 
 * @author TranquilMarmot
 */
public class LoginListener implements SwarmLoginListener {
	/** 
	 * This method is called when the login process has started
	 * (when a login dialog is displayed to the user).
	 * */
	public void loginStarted() {
		
	}
	
	/**
	 * This method is called if the user cancels the login process.
	 */
	public void loginCanceled() {
		
	}

	/**
	 * This method is called when the user has successfully logged in.
	 */
	public void userLoggedIn(SwarmActiveUser user) {
		
	}

	/**
	 * This method is called when the user logs out.
	 */
	public void userLoggedOut() {
		
	}
}
