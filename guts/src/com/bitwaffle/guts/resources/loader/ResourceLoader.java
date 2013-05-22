package com.bitwaffle.guts.resources.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.bitwaffle.guts.Game;

/**
 * Loads a resource file and initializes resources
 * 
 * @author TranquilMarmot
 */
public class ResourceLoader {
	// TODO support relative paths! Absolute makes it too easy to mess up.
	private static final String LOGTAG = "ResourceLoader";
	
	/** Loads all the resources in a given JSON file  */
	public static void loadResourceFile(String resourceFile){
		Gdx.app.debug(LOGTAG, "Loading " + resourceFile + "...");
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
			
			// resource lists
			JSONArray toLoad = resources.optJSONArray("resources");
			if(toLoad != null)
				parseResourceListArray(toLoad);
			
			// texture lists
			JSONArray textures = resources.optJSONArray("textures");
			if(textures != null)
				ImageResourceLoader.parseTextureArray(textures);
			
			// sprite sheets
			JSONArray sheets = resources.optJSONArray("sheets");
			if(sheets != null)
				ImageResourceLoader.parseSpriteSheetArray(sheets);
			
			// animations
			JSONArray animations = resources.optJSONArray("animations");
			if(animations != null)
				ImageResourceLoader.parseAnimationArray(animations);
			
			// sounds
			JSONArray sounds = resources.optJSONArray("sounds");
			if(sounds != null)
				parseSoundArray(sounds);
			
			// polygons
			JSONArray polys = resources.optJSONArray("polygons");
			if(polys != null)
				PolygonResourceLoader.parsePolygonArray(polys);
			
			// entities
			JSONArray ents = resources.optJSONArray("entities");
			if(ents != null)
				EntityInfoLoader.parseEntityInfoArray(ents);
			
			// models
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
				// TODO keep names/paths of resource lists? Could be useful for un-loading (just go through json again?)
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
}