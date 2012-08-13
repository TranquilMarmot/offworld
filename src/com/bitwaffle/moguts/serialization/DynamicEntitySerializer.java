package com.bitwaffle.moguts.serialization;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.moguts.entities.DynamicEntity;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class DynamicEntitySerializer extends Serializer<DynamicEntity>{
	@Override
	public DynamicEntity read(Kryo kryo, Input input, Class<DynamicEntity> type) {
		BodyDef bodyDef = kryo.readObject(input, BodyDef.class);
		
		int numFixtures = input.readInt();
		
		FixtureDef[] fixtures = new FixtureDef[numFixtures];
		for(int i = 0; i < numFixtures; i++){
			fixtures[i] = kryo.readObject(input, FixtureDef.class);
		}
		
		DynamicEntity ent = new DynamicEntity(bodyDef, fixtures);
		
		return null;
	}

	@Override
	public void write(Kryo kryo, Output output, DynamicEntity ent) {
		kryo.writeObject(output, getBodyDef(ent.body));
		
		for(Fixture f : ent.body.getFixtureList())
			kryo.writeObject(output, getFixtureDef(f));
	}
	
	private FixtureDef getFixtureDef(Fixture fixt){
		FixtureDef def = new FixtureDef();
		
		def.density = fixt.getDensity();
		def.friction = fixt.getFriction();
		def.isSensor = fixt.isSensor();
		def.restitution = fixt.getRestitution();
		def.shape = fixt.getShape();
		
		Filter filter = fixt.getFilterData();
		def.filter.categoryBits = filter.categoryBits;
		def.filter.groupIndex = filter.groupIndex;
		def.filter.maskBits = filter.maskBits;
		
		return def;
	}
	
	private BodyDef getBodyDef(Body bod){
		BodyDef def = new BodyDef();
		
		def.active = bod.isActive();
		def.allowSleep = bod.isSleepingAllowed();
		def.angle = bod.getAngle();
		def.angularDamping = bod.getAngularDamping();
		def.angularVelocity = bod.getAngularVelocity();
		def.awake = bod.isAwake();
		def.bullet = bod.isBullet();
		def.fixedRotation = bod.isFixedRotation();
		def.gravityScale = bod.getGravityScale();
		def.linearDamping = bod.getLinearDamping();
		def.linearVelocity.set(bod.getLinearVelocity());
		def.position.set(bod.getPosition());
		def.type = bod.getType();
		
		return def;
	}
}
