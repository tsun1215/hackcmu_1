package com.clubkunz.game;


public class Coordinate_deprecated {
	public double latitude;
	public double longitude;
	public double altitude;

	public Coordinate_deprecated(double lat, double lon, double alt){
		latitude = lat;
		longitude = lon;
		altitude = alt;
	}

	public double distance(Coordinate_deprecated target){
		int R = 6371; // Radius of the earth in km
		double dLat = Math.toRadians(target.latitude-latitude);  // deg2rad below
		double dLon = Math.toRadians(target.longitude-longitude); 
		double a = 
				Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(target.latitude)) * 
				Math.sin(dLon/2) * Math.sin(dLon/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c; // Distance in km
		return d*1000;
	}
	
	public double bearing(Coordinate_deprecated target){
		double y = Math.sin(target.longitude - longitude) * Math.cos(target.latitude);
		double x = Math.cos(latitude)*Math.sin(target.latitude) -
		        Math.sin(latitude)*Math.cos(target.latitude)*Math.cos(target.longitude - longitude);
		return Math.toDegrees(Math.atan2(y, x));
	}
}
