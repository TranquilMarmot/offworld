package com.bitwaffle.guts.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.bitwaffle.guts.android.Game;
import com.bitwaffle.guts.graphics.render.shapes.Polygon;
import com.bitwaffle.guts.resources.textures.SubImage;
import com.bitwaffle.guts.resources.textures.animation.Animation;
import com.bitwaffle.guts.resources.textures.animation.Frame;
import com.bitwaffle.guts.util.BufferUtils;
import com.bitwaffle.guts.util.PolygonLoader;

/**
 * Loads a resource file and initializes resources
 * 
 * @author TranquilMarmot
 */
public class ResourceLoader {
	private static final String LOGTAG = "ResourceLoader";
	/**
	 * Loads all the resources in a given JSON file 
	 * @param resourceFile File to load resources from
	 */
	public static void loadResourceFile(String resourceFile){
    	try {
			InputStream stream = Game.resources.openAsset(resourceFile);

			JSONObject jobj = getJSONObjectFromStream(stream);
			parseResources(jobj);
			
			stream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param in Stream of JSON file
	 * @return JSONObject created from file
	 * @throws JSONException If there's an error parsing the stream
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
	
	/**
	 * Initializes all the resources in the given JSON object
	 * @param jobj Object to intialize resources from
	 */
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
				parseTextureArray(textures);
			
			// parse any sprite sheets
			JSONArray sheets = resources.optJSONArray("sheets");
			if(sheets != null)
				parseSpriteSheetArray(sheets);
			
			// parse animations
			JSONArray animations = resources.optJSONArray("animations");
			if(animations != null)
				parseAnimationArray(animations);
			
			// parse sounds
			JSONArray sounds = resources.optJSONArray("sounds");
			if(sounds != null)
				parseSoundArray(sounds);
			
			JSONArray polygons = resources.optJSONArray("polygons");
			if(polygons != null)
				parsePolygonArray(polygons);
			
		} catch (JSONException e) {
			Log.e(LOGTAG, "Got invalid resources file! Doesn't begin with \"resources\" object!");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param arr Array of resource lists to parse
	 */
	private static void parseResourceListArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject resObj = arr.getJSONObject(i);
				// TODO keep names of resource lists?
				//String name = resObj.getString("name");
				String path = resObj.getString("path");
				loadResourceFile(path);
			}
		} catch(JSONException e){
			Log.e(LOGTAG, "Error parsing texture array in resource file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get an OpenGL filter from a JSON object
	 * The filter is optional and defaults to GL_NEAREST
	 * @param jobj Object to get filter from
	 * @param filterType Which filter to get- minFilter or magFilter
	 * @return GL int representing filter
	 */
	private static int getFilter(JSONObject jobj, String filterType){
		String filterString = jobj.optString(filterType);
		int filter = GLES20.GL_NEAREST;
		if(filterString.equalsIgnoreCase("linear"))
			filter = GLES20.GL_LINEAR;
		
		return filter;
	}
	
	/**
	 * @param arr Array of textures to load
	 */
	private static void parseTextureArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject texObj = arr.getJSONObject(i);
				
				String texName = texObj.getString("name");
				String texPath = texObj.getString("path");
				
				int minFilter = getFilter(texObj, "minFilter");
				int magFilter = getFilter(texObj, "magFilter");
				
				InputStream in = Game.resources.openAsset(texPath);
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				in.close();
				int handle = initTexture(bitmap, minFilter, magFilter);
				bitmap.recycle();
				Game.resources.textures.addTexture(texName, handle);
			}
		} catch(JSONException e){
			Log.e(LOGTAG, "Error parsing texture array in resource file!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes a texture with the given parameters and returns its handle
	 * @param bitmap Bitmap loaded with BitmapFactory.decodeStream();
	 * @param minFilter Min filter to use when loading
	 * @param magFilter Mag filter to use when loading
	 */
	public static int initTexture(Bitmap bitmap, int minFilter, int magFilter){
		int[] handles = new int[1];
		
		GLES20.glGenTextures(1, handles, 0);		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[0]);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR)
			Log.e(LOGTAG, "Error loading texture! (GL error number " + error + " string: "+ GLES20.glGetString(error) + ")");
		
		int handle = handles[0];
		
		return handle;
	}
	
	/**
	 * Gets the texture coordinates for a sub-image
	 * @param xOffset Xoffset into source image in pixels
	 * @param yOffset Y offset into source image in pixels
	 * @param subImageWidth Width of sub-image
	 * @param subImageHeight Height of sub-image
	 * @param sourceWidth Width of source image
	 * @param sourceHeight Height of source image
	 * @return FloatBuffer containing texture coordinates to render sub-image with a Quad
	 */
	private static FloatBuffer getSubImageTexCoords(int xOffset, int yOffset, int subImageWidth, int subImageHeight, int sourceWidth, int sourceHeight){
		float texX = (float)xOffset / (float)sourceWidth;
		float texY = (float)yOffset / (float)sourceHeight;
		float texWidth = (float)subImageWidth / (float)sourceWidth;
		float texHeight = (float)subImageHeight / (float)sourceHeight;
		
		// create texture coordinate array and fill a buffer (texture coordinates are used when rendering quad)
		float[] texCoords = {
				texX, texY,
				texX + texWidth, texY,
				texX + texWidth, texY + texHeight,
				
				texX + texWidth, texY + texHeight,
				texX, texY + texHeight,
				texX, texY
		};
		FloatBuffer buff = BufferUtils.getFloatBuffer(texCoords.length);
		buff.put(texCoords);
		buff.rewind();
		
		return buff;
	}
	
	/**
	 * @param arr Array of sprite sheet objects to parse
	 */
	private static void parseSpriteSheetArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject sheetObj = arr.getJSONObject(i);
				
				String imagePath = sheetObj.getString("path");
				int minFilter = getFilter(sheetObj, "minFilter");
				int magFilter = getFilter(sheetObj, "magFilter");
				
				// load entire sheet
				InputStream in = Game.resources.openAsset(imagePath);
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				in.close();
				int sheetHandle = initTexture(bitmap, minFilter, magFilter);
				
				JSONArray spritesArr = sheetObj.getJSONArray("sprites");
				for(int j = 0; j < spritesArr.length(); j++){
					JSONObject spriteObj = spritesArr.getJSONObject(j);
					
					String spriteName = spriteObj.getString("name");
					int spriteX = spriteObj.getInt("x");
					int spriteY = spriteObj.getInt("y");
					int spriteW = spriteObj.getInt("w");
					int spriteH = spriteObj.getInt("h");
					
					FloatBuffer spriteBuff = getSubImageTexCoords(
							spriteX, 
							spriteY,
							spriteW,
							spriteH,
							bitmap.getWidth(),
							bitmap.getHeight()
					);
					Game.resources.textures.addSubImage(spriteName, new SubImage(sheetHandle, spriteBuff));
				}
			}
		} catch(JSONException e){
			Log.e(LOGTAG, "Error parsing spritesheet array in resource file!");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOGTAG, "Error loading texture for sprite sheet!");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param arr Array of animations to parse
	 */
	private static void parseAnimationArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject animObj = arr.getJSONObject(i);
				
				String animName = animObj.getString("name");
				String imgPath = animObj.getString("path");
				int minFilter = getFilter(animObj, "minFilter");
				int magFilter = getFilter(animObj, "magFilter");
				
				// load entire animation sheet
				InputStream in = Game.resources.openAsset(imgPath);
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				in.close();
				int sheetHandle = initTexture(bitmap, minFilter, magFilter);
				
				JSONArray frameArr = animObj.getJSONArray("frames");
				Frame[] frames = new Frame[frameArr.length()];
				for(int j = 0; j < frameArr.length(); j++){
					JSONObject frameObj = frameArr.getJSONObject(j);
					
					float frameLength = (float)frameObj.getDouble("length");
					int frameX = frameObj.getInt("x");
					int frameY = frameObj.getInt("y");
					int frameW = frameObj.getInt("w");
					int frameH = frameObj.getInt("h");
					
					FloatBuffer frameBuff = getSubImageTexCoords(
							frameX,
							frameY,
							frameW,
							frameH,
							bitmap.getWidth(),
							bitmap.getHeight()
					);
					frames[j] = new Frame(frameLength, frameBuff);
				}
				
				Game.resources.textures.addAnimation(animName, new Animation(sheetHandle, frames));
			}
		} catch(JSONException e){
			Log.e(LOGTAG, "Error parsing animation array in resource file!");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOGTAG, "Error opening image file for animation!");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param arr Array of sounds to initialize
	 */
	private static void parseSoundArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject soundObj = arr.getJSONObject(i);
				
				String soundName = soundObj.getString("name");
				String soundPath = soundObj.getString("path");
				
				Game.resources.sounds.loadSound(soundName, soundPath, 1);
			}
		} catch(JSONException e){
			Log.e(LOGTAG, "Error parsing sound array in resource file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param arr Array of polygons to initialize
	 */
	private static void parsePolygonArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject polyObj = arr.getJSONObject(i);
				
				String polyName = polyObj.getString("name");
				String renderPath = polyObj.getString("render");
				String geomPath = polyObj.getString("geom");
				String polyTex = polyObj.getString("texture");
				float xScale, yScale;
				double polyScale = polyObj.optDouble("scale");
				if(polyScale == Double.NaN){
					xScale = (float)polyObj.getDouble("xScale");
					yScale = (float)polyObj.getDouble("yScale");
				} else {
					xScale = (float)polyScale;
					yScale = (float)polyScale;
				}
				
				Polygon poly = PolygonLoader.loadPolygon(renderPath, geomPath, xScale, yScale, polyTex);
				Game.resources.polygons.addPolygon(polyName, poly);
			}
		} catch(JSONException e){
			Log.e(LOGTAG, "Error parsing geometry array in resource file!");
			e.printStackTrace();
		}
	}
}