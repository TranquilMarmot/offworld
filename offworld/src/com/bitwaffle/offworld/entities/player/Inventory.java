package com.bitwaffle.offworld.entities.player;

import java.util.ArrayList;

import com.bitwaffle.offworld.interfaces.Item;
import com.bitwaffle.offworld.interfaces.Money;

public class Inventory {
	
	ArrayList<Item> items;
	
	private int money;
	
	public Inventory(){
		items = new ArrayList<Item>();
		money = 0;
	}
	
	public void addItem(Item item){
		if(item instanceof Money)
			money += ((Money)item).moneyValue();
		else
			items.add(item);
		
		item.onAddToInventory(this);
	}
	
	public int money(){ return money; }
}
