package com.clubkunz.game;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationTracker {
	LocationManager manager;
	int slowSpeed = 2000;
	int fastSpeed = 20;
	int serverUpdateSpeed = 3000;
	boolean hasLocation = false;
	Location currentLocation;
	LocationListener listener;
	long lastUpdateTime;
	
	Game g;
	
	public LocationTracker(Game g){
		this.g=g;
	}
	
	public void init(LocationManager m){
		manager = m;
		listener = new LocationListener() {
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
				currentLocation = location;
				if(location.getAccuracy() <= 20){
					hasLocation = true;
					if( System.currentTimeMillis() - lastUpdateTime > serverUpdateSpeed){
							g.update();
							lastUpdateTime = System.currentTimeMillis();
					}
				}
			}
		};
		setUpdate(slowSpeed);
	}
	
	public void setUpdate(int speed){
		if(listener!=null) manager.removeUpdates(listener);
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		manager.requestLocationUpdates("gps", speed, 0, listener);
	}
	
	
	
}
