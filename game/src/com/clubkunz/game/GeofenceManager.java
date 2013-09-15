package com.clubkunz.game;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;



public class GeofenceManager implements ConnectionCallbacks, OnConnectionFailedListener, OnAddGeofencesResultListener {
	private LocationClient mLocationClient;
	private boolean mInProgress;
	private List<Geofence> geofences;
	public FragmentActivity mainActivity;
	
	public void addFence(Location c, float radius){
		geofences=new ArrayList<Geofence>();
		geofences.add(new Geofence.Builder().setCircularRegion(c.getLatitude(), c.getLongitude(), radius).setExpirationDuration(5*60*1000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT).setRequestId("test").build());
		addGeofences();
	}

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static class ErrorDialogFragment extends DialogFragment {
		private Dialog mDialog;
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mainActivity);
		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d("Geofence Detection", "Google Play services is available.");
			return true;
		} else {
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, mainActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);

				errorFragment.show(mainActivity.getSupportFragmentManager(), "Geofence Detection");
			}
			return false;
		}
	}


	/*
	 * Create a PendingIntent that triggers an IntentService in your
	 * app when a geofence transition occurs.
	 */
	private PendingIntent getTransitionPendingIntent() {
		// Create an explicit Intent
		Intent intent = new Intent(mainActivity, ReceiveTransitionsIntentService.class);
		return PendingIntent.getService(mainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public void addGeofences() {
		if (!servicesConnected()) return;
		mLocationClient = new LocationClient(mainActivity, this, this);
		if (!mInProgress) {
			mInProgress = true;
			mLocationClient.connect();
		} else {
			Log.w("A", "geofence adding in progress already");
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		PendingIntent pendingIntent = getTransitionPendingIntent();
		mLocationClient.addGeofences(geofences, pendingIntent, this);
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		if (LocationStatusCodes.SUCCESS == statusCode) {
			Log.i("Geofence add","Geofence add success");
		} else {
			Log.e("Geofence", ""+statusCode+" "+LocationStatusCodes.ERROR+" "+LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
			Log.e("Geofence add","Geofence add failed");
		}
		// Turn off the in progress flag and disconnect the client
		mInProgress = false;
		mLocationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		mInProgress = false;
		mLocationClient = null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		mInProgress = false;
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(mainActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			int errorCode = connectionResult.getErrorCode();
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, mainActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(mainActivity.getSupportFragmentManager(), "Geofence Detection");
			}
		}
	}
}