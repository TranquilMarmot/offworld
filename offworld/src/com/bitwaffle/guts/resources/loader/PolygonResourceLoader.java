package com.bitwaffle.guts.resources.loader;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.shapes.model.Model;
import com.bitwaffle.guts.graphics.shapes.model.ModelPolygon;
import com.bitwaffle.guts.graphics.shapes.model.ObjParser;
import com.bitwaffle.guts.graphics.shapes.polygon.Polygon;
import com.bitwaffle.guts.graphics.shapes.polygon.PolygonLoader;

/**
 * Handles loading polygons and models from resource files
 * 
 * @author TranquilMarmot
 */
public class PolygonResourceLoader {
	private static final String LOGTAG = "TextureLoader";

	/** @param arr Array of polygons to initialize */
	public static void parsePolygonArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject polyObj = arr.getJSONObject(i);
				
				String polyName = polyObj.getString("name");
				
				float xScale, yScale;
				double polyScale = polyObj.optDouble("scale");
				if(polyScale == Double.NaN){
					xScale = (float)polyObj.getDouble("xScale");
					yScale = (float)polyObj.getDouble("yScale");
				} else {
					xScale = (float)polyScale;
					yScale = (float)polyScale;
				}
				
				String typeStr = polyObj.optString("type");
				Polygon.Types shapeType = null;
				if(typeStr.equalsIgnoreCase("POLYGON"))
					shapeType = Polygon.Types.POLYGON;
				else if(typeStr.equalsIgnoreCase("LOOP"))
					shapeType = Polygon.Types.LOOP;
				else if(typeStr.equalsIgnoreCase("CHAIN"))
					shapeType = Polygon.Types.CHAIN;
				
				String geomPath = polyObj.optString("geom");
				String debugPath = polyObj.optString("debug");
				
				ArrayList<String> renderObjLocs = new ArrayList<String>();
				ArrayList<String> textureNames = new ArrayList<String>();
				
				JSONArray renderArr = polyObj.optJSONArray("render");
				String model = polyObj.optString("model");
				
				// polygon with a model attached to it (model gets loaded separately)
				if(!(model.equals(""))){
					ModelPolygon poly = PolygonLoader.loadModelPolygon(xScale, yScale, model, geomPath, shapeType, debugPath);
					Game.resources.polygons.addPolygon(polyName, poly);
					return;
					
				// array of rendering objects
				} else if(renderArr != null){
					for(int j = 0; j < renderArr.length(); j++){
						JSONObject renderObj = renderArr.getJSONObject(j);
						String renderPath = renderObj.getString("render");
						renderObjLocs.add(renderPath);
						
						String texName = renderObj.getString("texture");
						textureNames.add(texName);
						
					}
					
				// single render object
				} else {
					String singleRenderPath = polyObj.optString("render");
					if(!(singleRenderPath.equals(""))){
						renderObjLocs.add(singleRenderPath);
						String polyTex = polyObj.getString("texture");
						textureNames.add(polyTex);
					}
				}
				
				Polygon poly = PolygonLoader.loadPolygon(xScale, yScale, renderObjLocs, textureNames, geomPath, shapeType, debugPath);
				Game.resources.polygons.addPolygon(polyName, poly);
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing geometry array in resource file!");
			e.printStackTrace();
		}
	}
	
	/** @param arr Array of models to initialize */
	public static void parseModelArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject modelObj = arr.getJSONObject(i);
				
				String modelName = modelObj.getString("name");
				String texture = modelObj.getString("texture");
				String directory = modelObj.getString("dir");
				
				float xScale = (float)modelObj.optDouble("xScale");
				if(xScale == Float.NaN)
					xScale = 1.0f;
				
				float yScale = (float)modelObj.optDouble("yScale");
				if(yScale == Float.NaN)
					yScale = 1.0f;
				
				float zScale = (float)modelObj.optDouble("zScale");
				if(zScale == Float.NaN)
					zScale = 1.0f;
				
				
				Model m = ObjParser.loadObjFile(directory, new Vector3(xScale, yScale, zScale), texture);
				
				Game.resources.models.addModel(modelName, m);
				
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing entity info array in resource file!");
			e.printStackTrace();
		}
	}
}
