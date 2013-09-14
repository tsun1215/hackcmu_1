package com.clubkunz.game;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
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



public class GeofenceManager extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, OnAddGeofencesResultListener {
	private LocationClient mLocationClient;
	private boolean mInProgress;
	private List<Geofence> geofences;

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST :
			switch (resultCode) {
			case Activity.RESULT_OK :
				break;
			}
		}
	}
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d("Geofence Detection", "Google Play services is available.");
			return true;
		} else {
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);

				errorFragment.show(getSupportFragmentManager(), "Geofence Detection");
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
		Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public void addGeofences() {
		if (!servicesConnected()) return;
		mLocationClient = new LocationClient(this, this, this);
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
		} else {
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
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			int errorCode = connectionResult.getErrorCode();
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Geofence Detection");
			}
		}
	}
}