package com.bitwaffle.guts.gui.console;

import java.util.Iterator;
import java.util.StringTokenizer;

import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.entities.Entity;
import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.net.NetConsoleCommands;
import com.bitwaffle.guts.util.PhysicsHelper;


/**
 * All the possible console commands. These are called by the console by their name,
 * so name a command however you want it to be referenced to in-game.
 * Each command constructor takes in a command class that implements the inner Command interface.
 * Many commands can use the same command class.
 * Commands can also take other commands in their constructors, which allows multiple commands to call the
 * same Command class without needing to instantiate it.
 * Each constructor also takes in a boolean value to hide the command when all commands are list.
 * If given true, the command will be listed, false it won't. If no boolean is supplied, the command is printed out.
 * @author TranquilMarmot
 *
 */
public enum ConsoleCommands {
	help(new HelpCommand(), true),
	h(help, true),
	man(help, true),
	
	list(new ListCommand()),
	ls(list, true),
	
	numentities(new NumberCommand()),
	numents(numentities, true),
	
	beer(new BeerCommand(), true),
	
	camera(new CameraCommand(), true),
	
	quit(new QuitCommand()),
	q(quit, true),
	exit(quit, true),
	
	mute(new MuteCommand()),
	
	volume(new VolumeCommand()),
	vol(volume, true),
	
	log(new SetLoggingCommand()),
	logging(log, true),
	
	sysout(new SetSysOutCommand()),
	systemout(sysout, true),
	
	server(new NetConsoleCommands.ServerCommand()),
	client(new NetConsoleCommands.ClientCommand()),
	
	init(new InitCommand());

	/** Function to call for this ConsoleCommands */
	private Command function;
	/** Whether or not to list this command when executing help command */
	private boolean hidden;
	
	/**
	 * Create a console command with a new function
	 * @param function Function to use for this command
	 * @param hidden Whether or not to list this function
	 */
	private ConsoleCommands(Command function, boolean hidden){
		this.hidden = hidden;
		this.function = function;
	}
	
	/**
	 * Create a console command with a new function
	 * @param function Function to use for this command
	 */
	private ConsoleCommands(Command function){
		this(function, false);
	}
	
	/**
	 * Create a console command that uses a function that another command already uses
	 * @param command Command to get function from
	 * @param hidden Whether or not to list this command
	 */
	private ConsoleCommands(ConsoleCommands command, boolean hidden){
		this.function = command.function;
		this.hidden = hidden;
		
	}
	
	/**
	 * Create a console command that uses a function that another command already uses
	 * @param command Command to get function from
	 */
	private ConsoleCommands(ConsoleCommands command){
		this(command, false);
	}
	
	/**
	 * Issues a command
	 * @param toker StringTokenizer at the first arg for the command (calling toker.nextToken() will return the command's args[1]- the command itself is at args[0])
	 */
	public void issue(StringTokenizer toker){
		function.issue(toker);
	}
	
	/**
	 * Prints out help for the command
	 */
	public void help(){
		function.help();
	}
	
	/**
	 * @return Whether or not to list this function
	 */
	public boolean hidden(){
		return hidden;
	}
}

/**
 * 
 */
class HelpCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		if(toker.hasMoreElements()){
			String commStr = toker.nextToken();
			try{
				ConsoleCommands command = ConsoleCommands.valueOf(commStr);
				System.out.println("HELP for " + command + ":");
				command.help();
			} catch(IllegalArgumentException e){
				System.out.println("Command not found! (" + commStr + ")");
			}
		} else{
			System.out.println("AVAILABLE COMMANDS:");
			System.out.println("(use /? COMMAND for more details)");
			for(ConsoleCommands command : ConsoleCommands.values()){
				if(!command.hidden())
					System.out.println(command.name());
			}
		}
	}
	
	@Override
	public void help(){
		System.out.println("Usage: /help COMMAND (or) /? COMMAND (or) /h COMMAND\n" +
		                   "Leave COMMAND blank to get a list of commands");
	}
}

/**
 * Clear the console
 */
class ClearCommand implements Command{
	@Override
	public void issue(StringTokenizer toker) {
		GUI.console.clear();
	}
	
	@Override
	public void help(){
		System.out.println("Usage: /clear (or) /clr\n" +
		                   "Clears the console");
	}
}

/**
 * List dynamic entitites, static entities, or lights
 */
class ListCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		Integer layerFilter = null;
		if(toker.hasMoreTokens()){
			String opt = toker.nextToken();
			if(opt.equals("-l")){
				layerFilter = Integer.parseInt(toker.nextToken());
			} else{
				System.out.println("Unkown /list option " + opt + "(see help for possible options)");
			}
		}
			
		if(layerFilter != null)
			System.out.println("Listing entities on layer " + layerFilter + "...");
		else
			System.out.println("Listing all entities...");
		
		for(Iterator<Entity> it : Game.physics.getAllIterators()){
			while(it.hasNext()){
				Entity ent = it.next();
				Vector2 loc = ent.getLocation();
				int layer = ent.getLayer();
				if(layerFilter != null){
					if(layer == layerFilter)
						System.out.println(ent.getClass().getCanonicalName() + " | x: " + loc.x + " y: " + loc.y + " angle: " + ent.getAngle() + " layer: " + ent.getLayer());
				} else{
					System.out.println(ent.getClass().getCanonicalName() + " | x: " + loc.x + " y: " + loc.y + " angle: " + ent.getAngle() + " layer: " + ent.getLayer());
				}
			}
		}
	}
	
	@Override
	public void help(){
		System.out.println("Usage: /list [-l LAYERFILTER] (or) /ls [-l LAYERFILTER]\n" +
		                   "Lists all entities, followed by their location and angle\n" +
		                   "Use -l with an int for LAYERFILTER to limit list to a spcific layer.");
	}
	
}

/**
 * Print out how many entities there are
 */
class NumberCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		System.out.println(Game.physics.numEntities());
	}
	
	@Override
	public void help(){
		System.out.println("Usage: /numentities (or) /numents\n" +
		                   "Lists the number of entities");
	}
}

/**
 * 99 bottles of beer on the wall, 99 bottles of beer!
 * @author nate
 *
 */
class BeerCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		 int i = 99;
		 do {
			 System.out.println(i + " bottles of beer on the wall, " + i
					 + " bottles of beer");
			 System.out.println("Take one down, pass it around, "
					 + (i - 1) + " bottles of beer on the wall");
			 i--;
		 } while(i > 0);
	}
	
	@Override
	public void help(){
		System.out.println("Computers are much faster at counting than you are!");
	}
}

/**
 * Change values for the camera-
 * zoom - how zoomed in/out the camera is
 * yoffset - camera offset on y axis
 * xoffset - camera offset on x axis
 * follow - tell the camera to follow an entity, either the player or the name of an entity type from Entities.dynamicEntities
 */
class CameraCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		//String cameraCommand = toker.nextToken().toLowerCase();
		help();
	}
	
	@Override
	public void help(){
		System.out.println("Sorry! Camera commands not implemented yet.");
	}
}

/**
 * Quit the game
 */
class QuitCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		System.exit(0); // FIXME see if this works lol
	}
	
	@Override
	public void help(){
		System.out.println("Usage: /quit (or) /exit (or) /q\n" +
		                   "Quit the game... instantly");
	}
}

/**
 * Mutes/unmutes audio
 */
class MuteCommand implements Command{
	@Override
	public void issue(StringTokenizer toker) {
		//Game.resources.sounds.mute();
		
		//System.out.println("Audio is now " + (Game.resources.sounds.isMuted() ? "muted" : ("not muted (Volume: " + Game.resources.sounds.currentVolume()) + ")"));
	}

	@Override
	public void help() {
		System.out.println("Mutes and un-mutes the audio");
	}
}

/**
 * Sets volume to a new level (0 = none, 0.5 = 50%, 1 = 100%, 2 = 200% etc.)
 * Prints out current volume if not given a new volume
 */
class VolumeCommand implements Command{
	@Override
	public void issue(StringTokenizer toker) {
		// if there's more elements, set the volume
		if(toker.hasMoreElements()){
			try{
				//float newVolume = Float.valueOf(toker.nextToken());
				//float oldVolume = Game.resources.sounds.currentVolume();
				
				//Game.resources.sounds.setVolume(newVolume);
				
				//System.out.println("Volume set from " + oldVolume + " to " + newVolume);
			} catch(IllegalArgumentException e){
				// check for invalid input
				System.out.println("Invalid number " + e.getLocalizedMessage().toLowerCase());
			}
		} else{
			// there's no argument, print out the current volume
			//System.out.println("Current volume: " + Game.resources.sounds.currentVolume());
		}
	}

	@Override
	public void help() {
		System.out.println("Usage: /volume NEWVOL (or) /vol NEWVOL\n" + 
					       "Sets volume to a new level (0 = none, 0.5 = 50%, 1 = 100%, 2 = 200% etc.)\n" +
						   "Leave NEWVOL blank to print out current volume");
	}
}

class SetLoggingCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		if(toker.hasMoreElements()){
			/*Boolean newStatus = XMLHelper.parseBoolean(toker.nextToken());
			
			if(newStatus == null)
				System.out.println("Invalid boolean input (try 'true', 'false', 'y', 'n', 'yup', 'naw')");
			else{
				if(newStatus)
					Game.console.outputStream.enableLog();
				else
					Game.console.outputStream.disableLog();
				
				System.out.println("Logging is now " + (Game.console.outputStream.isWritingToLog() ? "enabled" : "disabled"));
			}	*/
		} else {
			System.out.println("Logging is currently " + (GUI.console.outputStream.isWritingToLog() ? "enabled" : "disabled"));
		}
	}
	
	@Override
	public void help(){
		System.out.println("Usage: /log NEWSTATUS\n" +
		                   "Where NEWSTATUS is 'enabled', 'disabled' or any boolean value\n" +
		                   "Enables/disables writing to a log.\n" +
		                   "Leave NEWSTATUS blank to see current status.");
	}
}

class SetSysOutCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		if(toker.hasMoreElements()){
			/*Boolean newStatus = XMLHelper.parseBoolean(toker.nextToken());
			
			if(newStatus == null)
				System.out.println("Invalid boolean input (try 'true', 'false', 'y', 'n', 'yup', 'naw', 'enable', 'disable')");
			else{
				if(newStatus)
					Game.console.outputStream.enableSystemOut();
				else
					Game.console.outputStream.enableSystemOut();
				
				System.out.println("System.out is now " + (Game.console.outputStream.isWritingToSysOut() ? "enabled" : "disabled"));
			}	*/
		} else {
			System.out.println("System.out is currently " + (GUI.console.outputStream.isWritingToSysOut() ? "enabled" : "disabled"));
		}
	}
	
	@Override
	public void help(){
		System.out.println("Usage: /sysout NEWSTATUS\n" +
		                   "Where NEWSTATUS is 'enabled', 'disabled' or any boolean value\n" +
		                   "Enables/disables printing to default System.out, as well as this console.\n" +
		                   "Leave NEWSTATUS blank to see current status.");
	}
}

class InitCommand implements Command{
	@Override
	public void issue(StringTokenizer toker){
		PhysicsHelper.temp(Game.physics);
	}
	
	@Override
	public void help(){
		
	}
}
