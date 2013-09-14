package com.clubkunz.game;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTracker {
	LocationManager manager;
	int updateFreq = 1000;
	boolean hasLocation = false;
	Coordinate currentLocation;
	
	public void init(LocationManager m){
		manager = m;
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		manager.requestLocationUpdates("gps", updateFreq, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				hasLocation = false;
			}
			
			@Override
			public void onLocationChanged(Location location) {
				currentLocation = new Coordinate(location.getLongitude(), location.getLatitude(), location.getAltitude());
				hasLocation = true;
			}
		});
	}
}
