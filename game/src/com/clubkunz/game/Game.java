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
	GeofenceManager fenceman = new GeofenceManager();
	Player me = new Player();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fenceman.mainActivity=this;
		me.tracker=new LocationTracker();

		setContentView(R.layout.activity_game);
		
		me.tracker.init((LocationManager) getSystemService(Context.LOCATION_SERVICE));

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		//final View contentView = findViewById(R.id.fullscreen_content);
		Button b = (Button)controlsView.findViewById(R.id.set_bomb);
		Button radar = (Button)controlsView.findViewById(R.id.radar);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("A","A");
				if(me.tracker.hasLocation) fenceman.addFence(me.tracker.currentLocation, 10);
			}
		});
		radar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ServerInfo.ping(123, 12, 12, 12, 0);
			}
		});
		
	}
}
