package com.clubkunz.game;


public class Coordinate {
	public double latitude;
	public double longitude;
	public double altitude;

	public Coordinate(double lat, double lon, double alt){
		latitude = lat;
		longitude = lon;
		altitude = alt;
	}

	public double distance(Coordinate target){
		int R = 6371; // Radius of the earth in km
		double dLat = deg2rad(target.latitude-latitude);  // deg2rad below
		double dLon = deg2rad(target.longitude-longitude); 
		double a = 
				Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(target.latitude)) * 
				Math.sin(dLon/2) * Math.sin(dLon/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c; // Distance in km
		return d*1000;
	}
	private double deg2rad(double deg) {
		return deg * (Math.PI/180);
	}
}
