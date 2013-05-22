package com.bitwaffle.guts.resources.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.CollisionFilters;
import com.bitwaffle.guts.resources.manager.EntityInfoManager.EntityInfo;

public class EntityInfoLoader {
	private static final String LOGTAG = "EntityInfoLoader";
	
	/** @param arr Array to load info from */
	public static void parseEntityInfoArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject entObj = arr.getJSONObject(i);
				
				String entName = entObj.getString("name");
				//int layer =  entObj.getInt("layer"); TODO should layer be specified in JSON?
				
				// get shape
				JSONObject fixtureObj = entObj.getJSONObject("fixture");
				FixtureDef fixDef = new FixtureDef();
				JSONObject shapeObj = fixtureObj.getJSONObject("shape");
				String type = shapeObj.getString("type");
				if(type.equals("polygon")){
					String polyName = shapeObj.getString("polyName");
					fixDef.shape = Game.resources.polygons.getShape(polyName);
				} else if(type.equals("rectangle")){
					// TODO width/height
				} else if(type.equals("circle")){
					// TODO radius
				} else{
					Gdx.app.error(LOGTAG, "ERROR! Got unkown shape type when parsing entity info (got " + type + ")");
				}
				
				// rest of fixture info
				fixDef.density = (float) fixtureObj.getDouble("density");
				fixDef.friction = (float) fixtureObj.getDouble("friction");
				fixDef.restitution = (float) fixtureObj.getDouble("restitution");
				fixDef.isSensor = fixtureObj.optBoolean("isSensor", false);
				fixDef.filter.groupIndex = (short) fixtureObj.getInt("groupIndex"); // FIXME does this work? (cast from int to short)
				fixDef.filter.categoryBits = CollisionFilters.getFilter(fixtureObj.getString("category"));
				fixDef.filter.maskBits = CollisionFilters.getFilter(fixtureObj.getString("mask"));
				
				// get body info
				JSONObject bodObj = entObj.getJSONObject("body");
				// info about body's behavior
				BodyDef bodyDef = new BodyDef();
				bodyDef.allowSleep = bodObj.optBoolean("allowSleep", true);
				bodyDef.active = bodObj.optBoolean("active", true);
				bodyDef.bullet = bodObj.optBoolean("bullet", false);
				bodyDef.fixedRotation = bodObj.optBoolean("fixedRotation", false);
				bodyDef.gravityScale = (float)bodObj.optDouble("gravityScale", 1.0);
				bodyDef.linearDamping = (float)bodObj.optDouble("linearDamping", 0.0);
				bodyDef.angularDamping = (float)bodObj.optDouble("angularDamping", 0.0);
				String typeString = bodObj.getString("type");
				if(typeString.equals("dynamic"))
					bodyDef.type = BodyDef.BodyType.DynamicBody;
				else if(typeString.equals("static"))
					bodyDef.type = BodyDef.BodyType.StaticBody;
				else if(typeString.equals("kinematic"))
					bodyDef.type = BodyDef.BodyType.KinematicBody;	
				
				// info about body's location/velocity
				bodyDef.angle = (float)bodObj.optDouble("angle", 0.0);
				bodyDef.angularVelocity = (float)bodObj.optDouble("angularVelocity", 0.0);
				JSONObject linVelObj = bodObj.optJSONObject("linearVelocity");
				if(linVelObj != null){
					bodyDef.linearVelocity.x = (float)linVelObj.getDouble("x");
					bodyDef.linearVelocity.y = (float)linVelObj.getDouble("y");
				}
				JSONObject posObj = bodObj.optJSONObject("position");
				if(posObj != null){
					bodyDef.position.x = (float)posObj.getDouble("x");
					bodyDef.position.y = (float)posObj.getDouble("y");
				}
				
				EntityInfo entInf = new EntityInfo(fixDef, bodyDef);
				Game.resources.entityInfo.addEntityInfo(entName, entInf);
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing entity info array in resource file!");
			e.printStackTrace();
		}
	}
}
