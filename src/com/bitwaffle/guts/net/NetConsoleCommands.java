package com.bitwaffle.guts.net;

import java.net.InetSocketAddress;
import java.util.StringTokenizer;

import com.bitwaffle.guts.gui.console.Command;
import com.esotericsoftware.kryonet.Connection;

/**
 * Commands for controlling local servers/clients
 * 
 * @author TranquilMarmot
 */
public class NetConsoleCommands{
	/**
	 * Commands to control local server
	 */
	public static class ServerCommand implements Command{
		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String command = toker.nextToken();
				
				if(command.equals("start") || command.equals("begin"))
					startCommand(toker);
				else if(command.equals("stop") || command.equals("end"))
					endCommand(toker);
				else if(command.equals("status") || command.equals("stat"))
					statusCommand(toker);
				else if(command.equals("broadcast") || command.equals("wall"))
					broadcastCommand(toker);
				else
					System.out.println("Unrecognized server command " + command + " (use '/? server' for available commands)");
			}
		}

		@Override
		public void help() {
			System.out.println("Usage: /server COMMAND\n" + 
			                   "Available commands:\n" + 
			                   "start TCPport UDPport - Start server (leave ports blank for defaults)\n" +
			                   "stop       -  End server\n" +
			                   "status     -  Print current server status\n" +
			                   "broadcast  -  Send a message to all connected clients");
		}
		
		/**
		 * Starts the server
		 */
		private void startCommand(StringTokenizer toker){
			if(Net.server != null){
				System.out.println("Server already running.");
				return;
			}
			
			int tcpPort, udpPort;
			
			if(toker.countTokens() == 1){
				tcpPort = Integer.parseInt(toker.nextToken());
				udpPort = Net.DEFAULT_UDP_PORT;
				System.out.println("No UDP port specified, starting server with TCP port " + tcpPort + " and default UDP port " + udpPort);
			} else if(toker.countTokens() >= 2){
				tcpPort = Integer.parseInt(toker.nextToken());
				udpPort = Integer.parseInt(toker.nextToken());
				System.out.println("Starting server with TCP port " + tcpPort + " and UDP port " + udpPort);
			} else {
				tcpPort = Net.DEFAULT_TCP_PORT;
				udpPort = Net.DEFAULT_UDP_PORT;
				System.out.println("No ports specified, starting server with default TCP port " + tcpPort + " and UDP port " + udpPort);
			}
			
			Net.server = new NetServer(tcpPort, udpPort);
			Net.server.start();
		}
		
		/**
		 * Ends the server if it's running
		 */
		private void endCommand(StringTokenizer toker){
			if(Net.server == null){
				System.out.println("No server running");
				return;
			}
			
			Net.server.close();
			Net.server = null;
		}
		
		private void broadcastCommand(StringTokenizer toker){
			if(Net.server == null){
				System.out.println("No server running");
				return;
			}
			
			String which = toker.nextToken();
			String toSend = "";
			boolean udp = false; // if this is false, it defaults to tcp, make it true to default to udp
			
			if(which.equalsIgnoreCase("udp"))
				udp = true;
			else if(which.equalsIgnoreCase("tcp"))
				udp = false;
			else 
				toSend += which  + " ";
			
			while(toker.hasMoreTokens())
				toSend += toker.nextToken() + " ";
			
			if(udp)
				Net.server.broadcastUDP(toSend);
			else
				Net.server.broadcastTCP(toSend);
		}
		
		/**
		 * Prints out the status of the server (hostnames of anything connected)
		 */
		private void statusCommand(StringTokenizer toker){
			if(Net.server == null){
				System.out.println("Sevrer not running (do '/server start' to start server)");
				return;
			}
			
			System.out.println("Server running, listing connections... (this can take be slow)");
			
			// put this one in a thread since it can take a second to receive a reply
			new Thread(){
				@Override
				public void run(){
					Connection[] conns = Net.server.getConnections();
					
					// Prints out "Server has no connections", "Server has 1 connection", or "Server has X connections"
					System.out.println("Server has " +
					                  ((conns.length == 0) ? "no" : conns.length) +
					                  " connection" + ((conns.length > 1 || conns.length == 0) ? "s" : ""));
					
					// print out every connection
					if(conns.length > 0){
						System.out.println("ID |    hostname    :TCPport");
						System.out.println("------------");
						
						for(Connection connection : conns){
							InetSocketAddress tcpAddr = connection.getRemoteAddressTCP();
							System.out.println(String.format("%02d | %15s:%d", connection.getID(), tcpAddr.getHostName(), tcpAddr.getPort()));
						}
					}
				}
			}.start();
		}
	}
	
	
	
	/**
	 * Commands to control local client
	 */
	public static class ClientCommand implements Command{
		@Override
		public void issue(StringTokenizer toker) {
			if(!toker.hasMoreTokens())
				help();
			else{
				String command = toker.nextToken();
				
				if(command.equals("start") || command.equals("connect"))
					startCommand(toker);
				else if (command.equals("end") || command.equals("disconnect") || command.equals("stop"))
					endCommand(toker);
				else if(command.equals("status") || command.equals("stat"))
					statusCommand(toker);
				else if(command.equals("send") || command.equals("say"))
					sendCommand(toker);
				else
					System.out.println("Unrecognized client command " + command + " (use '/? client' for available commands)");
			}
		}
	
		@Override
		public void help() {
			System.out.println("Usage: /client COMMAND\n" + 
	                   "Available commands:\n" + 
	                   "connect URL TCPport UDPport - Connect to a server (leave URL blank for localhost, ports blank for defaults)\n" +
	                   "disconnect  -  Disconnect from server\n" +
	                   "status      -  Print current clien status\n" +
	                   "send        -  Send a message to server");
		}
		
		/**
		 * Starts a client
		 */
		private void startCommand(StringTokenizer toker){
			String url;
			int tcpPort, udpPort;
			
			if(toker.countTokens() == 1){
				url = toker.nextToken();
				tcpPort = Net.DEFAULT_TCP_PORT;
				udpPort = Net.DEFAULT_UDP_PORT;
				System.out.println("No ports specified, client connecting to " + url + " with default TCP port " + tcpPort + " and default UDP port " + udpPort);
			} else if(toker.countTokens() == 2){
				url = toker.nextToken();
				tcpPort = Integer.parseInt(toker.nextToken());
				udpPort = Net.DEFAULT_UDP_PORT;
				System.out.println("No UDP port specified, client connecting to " + url + " with TCP port " + tcpPort + " and default UDP port " + udpPort);
			} else if(toker.countTokens() >= 3){
				url = toker.nextToken();
				tcpPort = Integer.parseInt(toker.nextToken());
				udpPort = Integer.parseInt(toker.nextToken());
				System.out.println("Client connecting to " + url + " with TCP port " + tcpPort + " and UDP port " + udpPort);
			} else {
				url = "localhost";
				tcpPort = Net.DEFAULT_TCP_PORT;
				udpPort = Net.DEFAULT_UDP_PORT;
				System.out.println("No URL or ports specified, client connecting to localhost and default TCP port " + tcpPort + " and UDP port " + udpPort);
			}
			
			Net.client = new NetClient(url, tcpPort, udpPort);
			Net.client.start();
		}
		
		/**
		 * Ends a client
		 */
		private void endCommand(StringTokenizer toker){
			if(Net.client == null){
				System.out.println("No client running");
				return;
			}
			
			Net.client.close();
			Net.client = null;
		}
		
		/**
		 * Sends a message to a connected server from a client
		 */
		private void sendCommand(StringTokenizer toker){
			if(Net.client == null || !Net.client.isConnected()){
				System.out.println("No client running");
				return;
			}
			
			String which = toker.nextToken();
			String toSend = "";
			boolean udp = false; // if this is false, it defaults to tcp, make it true to default to udp
			
			if(which.equalsIgnoreCase("udp"))
				udp = true;
			else if(which.equalsIgnoreCase("tcp"))
				udp = false;
			else 
				toSend += which  + " ";
			
			while(toker.hasMoreTokens())
				toSend += toker.nextToken() + " ";
			
			if(udp)
				Net.client.sendUDP(toSend);
			else
				Net.client.sendTCP(toSend);
		}
		
		/**
		 * Prints out the status of the client
		 */
		private void statusCommand(StringTokenizer toker){
			if(Net.client == null){
				System.out.println("Client not connected (do '/client connect URL' to connect)");
				return;
			} else {
				new Thread(){
					@Override
					public void run(){
						String hostname = "Not connected";
						if(Net.client.isConnected()){
							InetSocketAddress tcpAddr = Net.client.getRemoteAddressTCP();
							hostname = tcpAddr.getHostName();
						}
						
						int id = Net.client.getID();
						System.out.println("Client ID    : " + id);
						System.out.println("Connected to : " + hostname + " (" + (Net.client.isIdle() ? "not " : "") + "idle)");
					}
				}.start();
			}
		}
	}
}