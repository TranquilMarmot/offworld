package com.bitwaffle.offworld.entities.pickups.diamond;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.entity.dynamic.DynamicEntity;
import com.bitwaffle.guts.graphics.render.render3d.EntityRenderer3D;
import com.bitwaffle.guts.graphics.shapes.model.ModelRenderer;
import com.bitwaffle.offworld.entities.player.Inventory;
import com.bitwaffle.offworld.interfaces.Money;

/**
 * Now YOU have a friend in the diamond business.
 * 
 * @author TranquilMarmot
 */
public class Diamond extends DynamicEntity implements Money {
	private static final int MONEY_VAL = 10;
	
	public Diamond(int layer, Vector2 location, float rotation){
		super(
				//new ModelPolygonRenderer(Game.resources.polygons.getModelPolygon("diamond")),
				new ModelRenderer(Game.resources.models.getModel("diamond")),
				layer,
				getBodyDef(location, rotation),
				Game.resources.entityInfo.getEntityFixtureDef("diamond")
			);
	}
	
	/** Gets body def and sets location/rotation */
	private static BodyDef getBodyDef(Vector2 location, float rotation){
		BodyDef def = Game.resources.entityInfo.getEntityBodyDef("diamond");
		def.position.set(location);
		def.angle = rotation;
		return def;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		Vector2 linVec = this.body.getLinearVelocity();
		
		// spin the diamond on the Y axis
		((EntityRenderer3D)renderer).view.rotate(0.0f, 1.0f, 0.0f , (timeStep * (linVec.x + linVec.y)) * 10.0f);
	}
	
	@Override // from Item
	public void onAddToInventory(Inventory inv){
		Game.physics.removeEntity(this, true);
	}
	
	@Override // from Money
	public int moneyValue(){ return MONEY_VAL; }
}
