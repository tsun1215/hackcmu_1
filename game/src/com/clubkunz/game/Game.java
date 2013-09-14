package com.clubkunz.game;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Game extends Activity {
	LocationTracker location = new LocationTracker();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		
		location.init((LocationManager) getSystemService(Context.LOCATION_SERVICE));

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		//final View contentView = findViewById(R.id.fullscreen_content);
		Button b = (Button)controlsView.findViewById(R.id.set_bomb);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}
}
