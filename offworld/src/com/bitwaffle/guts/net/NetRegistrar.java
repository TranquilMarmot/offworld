package com.bitwaffle.guts.net;

import com.bitwaffle.guts.net.messages.PlayerInfoReply;
import com.bitwaffle.guts.net.messages.PlayerInfoRequest;
import com.bitwaffle.guts.net.messages.SomeReply;
import com.bitwaffle.guts.net.messages.SomeRequest;
import com.esotericsoftware.kryo.Kryo;

public class NetRegistrar {
	
	
	
	public static void registerClasses(Kryo kryo){
		kryo.register(SomeRequest.class);
		kryo.register(SomeReply.class);
		kryo.register(PlayerInfoRequest.class);
		kryo.register(PlayerInfoReply.class);
	}
}
