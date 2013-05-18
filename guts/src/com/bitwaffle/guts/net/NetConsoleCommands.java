package com.bitwaffle.guts.net;

import java.util.StringTokenizer;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.console.Command;

/**
 * Commands for fiddling with console stuff.
 * 
 * @author TranquilMarmot
 */
public class NetConsoleCommands {
	/**
	 * Server commands
	 */
	public static class ServerCommand implements Command{

		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String command = toker.nextToken();
				
				// start server
				if(command.equals("start") || command.equals("begin"))
					Game.net.startServer();
				// end server
				else if(command.equals("end"))
					Game.net.server.endServer();
			}
		}

		@Override
		public void help() {
			
		}
		
	}
	
	/**
	 * Client commands
	 */
	public static class ClientCommand implements Command {

		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String command = toker.nextToken();
				
				// start client
				if(command.equals("start") || command.equals("connect") || command.equals("begin"))
					Game.net.startClient(toker.nextToken());
			}
		}

		@Override
		public void help() {
			
		}
		
	}
}
