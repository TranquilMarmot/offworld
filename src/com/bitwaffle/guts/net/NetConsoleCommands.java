package com.bitwaffle.guts.net;

import java.util.StringTokenizer;

import com.bitwaffle.guts.gui.console.Command;

public class NetConsoleCommands{
	public static final int DEFAULT_TCP_PORT = 42024;
	public static final int DEFAULT_UDP_PORT = 42042;
	
	public static class ServerCommand implements Command{

		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String serverCommand = toker.nextToken();
				
				if(serverCommand.equals("start"))
					startCommand(toker);
				else if(serverCommand.equals("end"))
					endCommand(toker);
				else if(serverCommand.equals("broadcast"))
					broadcastCommand(toker);
			}
		}

		@Override
		public void help() {
			
		}
		
		private void startCommand(StringTokenizer toker){
			int tcpPort, udpPort;
			if(toker.countTokens() != 2){
				System.out.println("Not enough args given, starting with default TCP port " + DEFAULT_TCP_PORT + " and UDP port " + DEFAULT_UDP_PORT);
				tcpPort = DEFAULT_TCP_PORT;
				udpPort = DEFAULT_UDP_PORT;
			} else {
				tcpPort = Integer.parseInt(toker.nextToken());
				udpPort = Integer.parseInt(toker.nextToken());
			}
			NetHandler.server = new OffworldServer(tcpPort, udpPort);
			NetHandler.server.start();
		}
		
		private void endCommand(StringTokenizer toker){
			if(NetHandler.server == null){
				System.out.println("No server running");
				return;
			}
			
			NetHandler.server.server.close();
		}
		
		private void broadcastCommand(StringTokenizer toker){
			if(NetHandler.server == null){
				System.out.println("No server running");
				return;
			}
			
			String which = toker.nextToken();
			if(which.equalsIgnoreCase("tcp")){
				sendRestToTCP(toker);
			} else if(which.equalsIgnoreCase("udp")){
				sendRestToUDP(toker);
			} else {
				System.out.println("TCP or UDP not specified, sending to TCP...");
				NetHandler.server.server.sendToAllTCP(which);
				sendRestToTCP(toker);
				//NetHandler.server.server.sendToAllUDP(which);
				//sendRestToUDP(toker);
			}
		}
		
		private static void sendRestToTCP(StringTokenizer toker){
			while(toker.hasMoreTokens())
				NetHandler.server.server.sendToAllTCP(toker.nextToken());
		}
		
		private static void sendRestToUDP(StringTokenizer toker){
			while(toker.hasMoreTokens())
				NetHandler.server.server.sendToAllUDP(toker.nextToken());
		}
	}
	
	public static class ClientCommand implements Command{
		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String serverCommand = toker.nextToken();
				
				if(serverCommand.equals("start"))
					startCommand(toker);
				else if (serverCommand.equals("end"))
					endCommand(toker);
				else if(serverCommand.equals("send"))
					sendCommand(toker);
			}
		}
	
		@Override
		public void help() {
			
		}
		
		private void startCommand(StringTokenizer toker){
			String url;
			int tcpPort, udpPort;
			if(toker.countTokens() != 3){
				System.out.println("Not enough args given, starting with localhost and default TCP port " + DEFAULT_TCP_PORT + " and UDP port " + DEFAULT_UDP_PORT);
				url = "localhost";
				tcpPort = DEFAULT_TCP_PORT;
				udpPort = DEFAULT_UDP_PORT;
			} else {
				url = toker.nextToken();
				tcpPort = Integer.parseInt(toker.nextToken());
				udpPort = Integer.parseInt(toker.nextToken());
			}
			NetHandler.client = new OffworldClient(url, tcpPort, udpPort);
			NetHandler.client.start();
		}
		
		private void endCommand(StringTokenizer toker){
			if(NetHandler.client == null){
				System.out.println("No client running");
				return;
			}
			
			NetHandler.client.client.close();
		}
		
		private void sendCommand(StringTokenizer toker){
			if(NetHandler.client == null || !NetHandler.client.client.isConnected()){
				System.out.println("No client running");
				return;
			}
			
			String which = toker.nextToken();
			if(which.equalsIgnoreCase("tcp")){
				sendRestToTCP(toker);
			} else if(which.equalsIgnoreCase("udp")){
				sendRestToUDP(toker);
			} else {
				System.out.println("TCP or UDP not specified, sending to TCP...");
				NetHandler.client.client.sendTCP(which);
				sendRestToTCP(toker);
				//NetHandler.client.client.sendUDP(which);
				//sendRestToUDP(toker);
			}
		}
		
		private static void sendRestToTCP(StringTokenizer toker){
			while(toker.hasMoreTokens())
				NetHandler.client.client.sendTCP(toker.nextToken());
		}
		
		private static void sendRestToUDP(StringTokenizer toker){
			while(toker.hasMoreTokens())
				NetHandler.client.client.sendUDP(toker.nextToken());
		}
	}
}