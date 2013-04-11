package com.bitwaffle.guts.net;

import com.bitwaffle.guts.entities.EntityRemoveRequest;
import com.bitwaffle.guts.net.messages.PlayerUpdateMessage;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.bitwaffle.guts.net.messages.entity.BreakableRockCreateRequest;
import com.bitwaffle.guts.net.messages.entity.DynamicEntityUpdateMessage;
import com.bitwaffle.guts.net.messages.entity.EntityUpdateMessage;
import com.esotericsoftware.kryo.Kryo;

/**
 * Registers all the objects that are used for networking.
 * Classes must be registered in the same order on the server AND
 * the client in order for things to work.
 * 
 * @author TranquilMarmot
 */
public class NetRegistrar {
	/**
	 * Registers classes for the given Kryo instance
	 * @param kryo Kryo being used to communicate between client/server
	 */
	public static void registerClasses(Kryo kryo){
		kryo.register(SomeRequest.class);
		kryo.register(SomeReply.class);
		
		kryo.register(PlayerUpdateMessage.class);
		kryo.register(BreakableRockCreateRequest.class);
		kryo.register(DynamicEntityUpdateMessage.class);
		kryo.register(EntityUpdateMessage.class);
		kryo.register(EntityRemoveRequest.class);
	}
}
