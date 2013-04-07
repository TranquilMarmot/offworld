package com.bitwaffle.guts.net;

import java.util.StringTokenizer;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.gui.console.Command;

public class NetConsoleCommands {
	public static class ServerCommand implements Command{

		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String command = toker.nextToken();
				
				switch(command){
				case "start":
				case "begin":
					Game.net.startServer();
					break;
				case "end":
					Game.net.server.endServer();
					break;
				}
			}
		}

		@Override
		public void help() {
			
		}
		
	}
	
	public static class ClientCommand implements Command {

		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String command = toker.nextToken();
				
				switch(command){
				case "start":
				case "connect":
				case "begin":
					Game.net.startClient(toker.nextToken());
					break;
				case "send":
					Game.net.send(toker.nextToken());
					break;
				}
			}
		}

		@Override
		public void help() {
			
		}
		
	}
}
