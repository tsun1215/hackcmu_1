package com.clubkunz.game;

public class Bomb {
	public Coordinate location;
	public double radius;
	
	public Bomb(Coordinate l, double r){
		location=l;
		radius=r;
	}
	
	public boolean isInRange(Coordinate c){
		return location.distance(c) <= radius;
	}
}
