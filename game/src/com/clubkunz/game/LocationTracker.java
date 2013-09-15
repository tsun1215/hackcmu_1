package com.clubkunz.game;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTracker {
	LocationManager manager;
	int updateFreq = 2000;
	boolean hasLocation = false;
	Coordinate currentLocation;
	
	Game g;
	
	public LocationTracker(Game g){
		this.g=g;
	}
	
	public void init(LocationManager m){
		manager = m;
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		manager.requestLocationUpdates("gps", updateFreq, 0, new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			@Override
			public void onProviderEnabled(String provider) {}
			
			@Override
			public void onProviderDisabled(String provider) {
				hasLocation = false;
			}
			
			@Override
			public void onLocationChanged(Location location) {
				currentLocation = new Coordinate(location.getLongitude(), location.getLatitude(), location.getAltitude());
				if(location.getAccuracy() <= 20){
					hasLocation = true;
					g.update();
				}
			}
		});
	}
}
