package com.bitwaffle.guts.ai.path;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Renderer;

/**
 * Renders a Pathfinder
 * 
 * @author TranquilMarmot
 */
public class PathRenderer {
	
	/** Colors to render stuff as */
	private static float[]
			openSetColor = new float[]{1.0f, 0.0f, 0.0f, 0.6f},
			closedSetColor = new float[]{0.75f, 0.5f, 0.0f, 0.6f},
			currentNodeColor = new float[]{ 0.0f, 1.0f, 0.0f, 0.7f },
			pathColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f };
	
	/** How big to render nodes as */
	private static float nodeRenderWidth = 0.25f, nodeRenderHeight = 0.25f;
	
	/** Render the debug data from the given path finder  */
	public static void renderDebug(Renderer renderer, PathFinder finder){
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Game.resources.textures.bindTexture("blank");
		
		// render open set
		renderer.r2D.setColor(openSetColor[0], openSetColor[1], openSetColor[2], openSetColor[3]);
		renderNodeIterator(renderer, finder.getOpenset().iterator());
		
		// render closed set
		renderer.r2D.setColor(closedSetColor[0], closedSetColor[1], closedSetColor[2], closedSetColor[3]);
		renderNodeIterator(renderer, finder.getClosedSet().iterator());
		
		// render current node
		renderer.r2D.setColor(currentNodeColor[0], currentNodeColor[1], currentNodeColor[2], currentNodeColor[3]);
		Node current = finder.getCurrentNode();
		if(current != null)
			renderNode(renderer, current);
		
		// render path
		renderer.r2D.setColor(pathColor[0], pathColor[1], pathColor[2], pathColor[3]);
		LinkedList<Node> path = finder.getPath();
		if(path != null)
			renderNodeIterator(renderer, path.iterator());
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
	/** Renders each node in an iterator*/
	private static void renderNodeIterator(Renderer renderer, Iterator<Node> it){
		while(it.hasNext())
			renderNode(renderer, it.next());
	}
	
	/** Translates the renderer to the given node and renders it */
	private static void renderNode(Renderer renderer, Node n){
		Vector2 point = n.loc();
		renderer.modelview.idt();
		renderer.r2D.translateModelViewToCamera();
		renderer.modelview.translate(point.x, point.y, 0.0f);
		renderer.r2D.sendMatrixToShader();
		renderer.r2D.quad.render(nodeRenderWidth, nodeRenderHeight);
	}
}
