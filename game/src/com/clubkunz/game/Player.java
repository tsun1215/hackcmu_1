package com.clubkunz.game;

import android.provider.Settings;

public class Player {
	String id = Settings.Secure.ANDROID_ID;
	int score = 0;
	int bombs = 0;
	LocationTracker tracker;
	
	public boolean setBomb(){
		if(bombs <= 0) return false;
		else{
			
			return true;
		}
	}
	
}
