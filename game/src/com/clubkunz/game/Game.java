package com.clubkunz.game;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
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

		View controlsView = findViewById(R.id.fullscreen_content_controls);
		Button b = (Button)controlsView.findViewById(R.id.set_bomb);
		Button radar = (Button)controlsView.findViewById(R.id.radar);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(me.tracker.hasLocation){
					if(me.bombs > 0){
						JSONObject j = server.game_bomb(me.id, (float)me.tracker.currentLocation.getLongitude(), 
								(float)me.tracker.currentLocation.getLatitude(), (float)me.tracker.currentLocation.getAltitude());
						try {
							if(j.getBoolean("success")){
								me.bombs--;
								Location l = new Location("bomb");
								l.setLatitude(j.getDouble("latitude"));
								l.setLongitude(j.getDouble("longitude"));
								l.setAltitude(j.getDouble("altitude"));
								bombs.add(new Bomb(j.getInt("id"), j.getString("placed_by"), l, j.getDouble("radius")));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		radar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setContentView(R.layout.radar);
			}
		});
		doTestGame();
	}
	
	public void doTestGame(){
		System.out.println(server.register("da5id", "David", "Lindenbaum", me.id));
		System.out.println(server.game_new(me.id, -79.9426f, 40.44623f, true));
		System.out.println(server.game_list(me.id, -79.9426f, 40.44623f));
		Location l = new Location("A");
		l.setLatitude(40.45623);
		l.setLongitude(-79.9426);
		bombs.add(new Bomb(1, me.id, l, 10));
	}
	
	public void update(){
		JSONObject o = server.game_refresh(me.id, (float)me.tracker.currentLocation.getLongitude(), (float)me.tracker.currentLocation.getLatitude(), 100);
		if(o == null){
			
		}else{
		synchronized(bombs){
			bombs=new ArrayList<Bomb>();
			try {
				JSONArray bombarr = o.getJSONArray("bombs");
				for(int i = 0; i < bombarr.length(); i++){
					JSONObject j = bombarr.getJSONObject(i);
					Location l = new Location("bomb");
					l.setLatitude(j.getDouble("latitude"));
					l.setLongitude(j.getDouble("longitude"));
					l.setAltitude(j.getDouble("altitude"));
					bombs.add(new Bomb(j.getInt("id"), j.getString("placed_by"), l, j.getDouble("radius")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			for(Bomb b : bombs){
				if(b.isInRange(me.tracker.currentLocation)) explode(b);
			}
		}
		}
	}
	
	private void explode(Bomb b){
		if(b.ownerId == me.id) return;
		try {
			if(server.game_explode(me.id, b.id).getBoolean("success")){
				Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				v.vibrate(4000);
				bombs.remove(b);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
