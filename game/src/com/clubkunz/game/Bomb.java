package com.clubkunz.game;

import android.location.Location;

public class Bomb {
	public Location location;
	public double radius;
	public int id;
	public String ownerId;
	
	public Bomb(int id, String ownerId, Location l, double r){
		location=l;
		radius=r;
		this.id=id;
		this.ownerId=ownerId;
	}
	
	public boolean isInRange(Location c){
		return location.distanceTo(c) <= radius;
	}
}
