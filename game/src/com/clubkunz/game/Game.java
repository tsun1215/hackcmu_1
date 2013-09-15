package com.clubkunz.game;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Game extends FragmentActivity {
	LocationTracker location = new LocationTracker();
	GeofenceManager fenceman = new GeofenceManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fenceman.mainActivity=this;

		setContentView(R.layout.activity_game);
		
		location.init((LocationManager) getSystemService(Context.LOCATION_SERVICE));

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		//final View contentView = findViewById(R.id.fullscreen_content);
		Button b = (Button)controlsView.findViewById(R.id.set_bomb);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("A","A");
				if(location.hasLocation) fenceman.addFence(location.currentLocation, 10);
			}
		});
	}
}
