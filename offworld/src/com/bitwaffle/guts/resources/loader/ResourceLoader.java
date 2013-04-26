package com.bitwaffle.guts.resources.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.physics.CollisionFilters;
import com.bitwaffle.guts.resources.entityinfo.EntityInfo;

/**
 * Loads a resource file and initializes resources
 * 
 * @author TranquilMarmot
 */
public class ResourceLoader {
	private static final String LOGTAG = "ResourceLoader";
	
	/** Loads all the resources in a given JSON file  */
	public static void loadResourceFile(String resourceFile){
		System.out.println("loading " + resourceFile);
    	try {
			InputStream stream = Game.resources.openAsset(resourceFile);

			JSONObject jobj = getJSONObjectFromStream(stream);
			parseResources(jobj);
			
			stream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			Gdx.app.error(LOGTAG, "Error loading " + resourceFile);
			e.printStackTrace();
		}
	}
	
	/**
	 * @param in Stream of JSON file
	 * @return JSONObject created from file
	 */
	private static JSONObject getJSONObjectFromStream(InputStream in) throws JSONException {
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in), 8);
			String source = "";
			String line;
			while ((line = reader.readLine()) != null) {
				source += line + "\n";
			}
			reader.close();
			return new JSONObject(source);
		} catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/** Initializes all the resources in the given JSON object */
	private static void parseResources(JSONObject jobj){
		try {
			JSONObject resources = jobj.getJSONObject("resources");
			
			// parse any resource lists
			JSONArray toLoad = resources.optJSONArray("resources");
			if(toLoad != null)
				parseResourceListArray(toLoad);
			
			// parse any texture lists
			JSONArray textures = resources.optJSONArray("textures");
			if(textures != null)
				TextureResourceLoader.parseTextureArray(textures);
			
			// parse any sprite sheets
			JSONArray sheets = resources.optJSONArray("sheets");
			if(sheets != null)
				TextureResourceLoader.parseSpriteSheetArray(sheets);
			
			// parse animations
			JSONArray animations = resources.optJSONArray("animations");
			if(animations != null)
				TextureResourceLoader.parseAnimationArray(animations);
			
			// parse sounds
			JSONArray sounds = resources.optJSONArray("sounds");
			if(sounds != null)
				parseSoundArray(sounds);
			
			// parse polygons
			JSONArray polys = resources.optJSONArray("polygons");
			if(polys != null)
				PolygonResourceLoader.parsePolygonArray(polys);
			
			// parse entities
			JSONArray ents = resources.optJSONArray("entities");
			if(ents != null)
				parseEntityInfoArray(ents);
			
			// parse models
			JSONArray models = resources.optJSONArray("models");
			if(models != null)
				PolygonResourceLoader.parseModelArray(models);
			
		} catch (JSONException e) {
			Gdx.app.error(LOGTAG, "Got invalid resources file! Doesn't begin with \"resources\" object!");
			e.printStackTrace();
		}
	}
	
	/** @param arr Array of resource lists to parse */
	private static void parseResourceListArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject resObj = arr.getJSONObject(i);
				// TODO keep names of resource lists? Could be useful for un-loading (just go through json again?)
				//String name = resObj.getString("name");
				String path = resObj.getString("path");
				loadResourceFile(path);
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing texture array in resource file!");
			e.printStackTrace();
		}
	}
	
	/** @param arr Array of sounds to initialize */
	private static void parseSoundArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject soundObj = arr.getJSONObject(i);
				
				String soundName = soundObj.getString("name");
				String soundPath = soundObj.getString("path");
				
				Game.resources.sounds.loadSound(soundName, soundPath);
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing sound array in resource file!");
			e.printStackTrace();
		}
	}
	
	
	/** @param arr Array to load info from */
	private static void parseEntityInfoArray(JSONArray arr){
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