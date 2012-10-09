package com.bitwaffle.guts.serialization.serializers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Serializes a BodyDef
 * 
 * @author TranquilMarmot
 */
public class BodyDefSerializer extends Serializer<BodyDef>{

	@Override
	public BodyDef read(Kryo kryo, Input input, Class<BodyDef> type) {
		BodyDef def = new BodyDef();
		
		def.active = input.readBoolean();
		def.allowSleep = input.readBoolean();
		
		def.angle = input.readFloat();
		def.angularDamping = input.readFloat();
		
		def.awake = input.readBoolean();
		def.bullet = input.readBoolean();
		def.fixedRotation = input.readBoolean();
		
		def.gravityScale = input.readFloat();
		def.linearDamping = input.readFloat();
		
		def.linearVelocity.set(kryo.readObject(input, Vector2.class));
		def.position.set(kryo.readObject(input, Vector2.class));
		
		def.type = BodyDef.BodyType.values()[input.readInt()];
		
		return def;
	}

	@Override
	public void write(Kryo kryo, Output output, BodyDef def) {
		output.writeBoolean(def.active);
		output.writeBoolean(def.allowSleep);
		
		output.writeFloat(def.angle);
		output.writeFloat(def.angularDamping);
		
		output.writeBoolean(def.awake);
		output.writeBoolean(def.bullet);
		output.writeBoolean(def.fixedRotation);
		
		output.writeFloat(def.gravityScale);
		output.writeFloat(def.linearDamping);
		
		kryo.writeObject(output, def.linearVelocity);
		kryo.writeObject(output, def.position);
		
		output.writeInt(def.type.ordinal());
	}

}
