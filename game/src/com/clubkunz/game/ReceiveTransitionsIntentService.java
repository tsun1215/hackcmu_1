package com.clubkunz.game;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.LocationClient;

public class ReceiveTransitionsIntentService extends IntentService {
	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
	}
	/**
	 * Handles incoming intents
	 *@param intent The Intent sent by Location Services. This
	 * Intent is provided
	 * to Location Services (inside a PendingIntent) when you call
	 * addGeofences()
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		// First check for errors
		if (LocationClient.hasError(intent)) {
			// Get the error code with a static method
			int errorCode = LocationClient.getErrorCode(intent);
			// Log the error
			Log.e("ReceiveTransitionsIntentService", "Location Services error: " + Integer.toString(errorCode));
		} else {
			// Get the type of transition (entry or exit)
			int transitionType = LocationClient.getGeofenceTransition(intent);

		}
	}
}