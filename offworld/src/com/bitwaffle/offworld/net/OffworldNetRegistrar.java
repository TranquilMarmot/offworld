package com.bitwaffle.offworld.net;

import com.bitwaffle.offworld.net.messages.PlayerCreateMessage;
import com.bitwaffle.offworld.net.messages.PlayerUpdateMessage;
import com.bitwaffle.offworld.net.messages.entity.BreakableRockCreateMessage;
import com.esotericsoftware.kryo.Kryo;

/**
 * Registers all the objects that are used for networking specifically for Offworld.
 * Classes must be registered in the same order on the server AND
 * the client in order for things to work.
 * 
 * @author TranquilMarmot
 */
public class OffworldNetRegistrar {
	
	public static void registerClasses(Kryo kryo){
		kryo.register(PlayerUpdateMessage.class);
		kryo.register(PlayerCreateMessage.class);
		kryo.register(BreakableRockCreateMessage.class);
	}
}
