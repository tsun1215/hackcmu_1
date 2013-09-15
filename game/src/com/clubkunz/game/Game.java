package com.clubkunz.game;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Game extends FragmentActivity {
	//GeofenceManager fenceman = new GeofenceManager();
	Player me = new Player();
	ServerInfo server = new ServerInfo();
	ArrayList<Bomb> bombs=new ArrayList<Bomb>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//fenceman.mainActivity=this;
		me.tracker=new LocationTracker(this);

		setContentView(R.layout.activity_game);
		
		me.tracker.init((LocationManager) getSystemService(Context.LOCATION_SERVICE));

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		//final View contentView = findViewById(R.id.fullscreen_content);
		Button b = (Button)controlsView.findViewById(R.id.set_bomb);
		Button radar = (Button)controlsView.findViewById(R.id.radar);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(me.tracker.hasLocation){
					//fenceman.addFence(me.tracker.currentLocation, 10);
				}
			}
		});
		radar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setContentView(R.layout.radar);
			}
		});
		
	}
	
	public void update(){
		JSONObject o = server.game_refresh(me.id, (float)me.tracker.currentLocation.longitude, (float)me.tracker.currentLocation.latitude, 100);
		synchronized(bombs){
			bombs=new ArrayList<Bomb>();
			try {
				JSONArray bombarr = o.getJSONArray("bombs");
				for(int i = 0; i < bombarr.length(); i++){
					JSONObject j = bombarr.getJSONObject(i);
						bombs.add(new Bomb(new Coordinate(j.getDouble("latitude"), j.getDouble("longitude"), j.getDouble("altitude")), j.getDouble("radius")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			for(Bomb b : bombs){
				if(b.isInRange(me.tracker.currentLocation)) explode(b);
			}
		}
	}
	
	private void explode(Bomb b){
		
	}
}
