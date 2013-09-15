
package com.clubkunz.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerInfo {
	public JSONObject register(String username, String fname, String lname, String id){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("username", username));
        send.add(new BasicNameValuePair("f_name", fname));
        send.add(new BasicNameValuePair("l_name", lname));
        send.add(new BasicNameValuePair("device_id", id));
        NetworkThread t = new NetworkThread(send,"register");
        t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject register(String id){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("device_id", id));
		NetworkThread t = new NetworkThread(send,"register");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_new(String id,float lon, float lat, boolean publ){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("device_id", id));
        send.add(new BasicNameValuePair("long", String.valueOf(lon)));
        send.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        send.add(new BasicNameValuePair("public", String.valueOf(publ)));
		NetworkThread t = new NetworkThread(send,"game/new");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_list(String id,float lon, float lat){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("device_id", id));
        send.add(new BasicNameValuePair("long", String.valueOf(lon)));
        send.add(new BasicNameValuePair("lat", String.valueOf(lat)));
		NetworkThread t = new NetworkThread(send,"game/list");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_join(String id,int game_id){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("device_id", id));
        send.add(new BasicNameValuePair("game_id", String.valueOf(game_id)));
		NetworkThread t = new NetworkThread(send,"game/join");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_refresh(String id,float lon,float lat,int rad){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("device_id", id));
        send.add(new BasicNameValuePair("long", String.valueOf(lon)));
        send.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        send.add(new BasicNameValuePair("rad", String.valueOf(rad)));
		NetworkThread t = new NetworkThread(send,"game/refresh");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_bomb(String id,float lon,float lat,float alt){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("device_id", id));
        send.add(new BasicNameValuePair("long", String.valueOf(lon)));
        send.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        send.add(new BasicNameValuePair("alt", String.valueOf(alt)));
		NetworkThread t = new NetworkThread(send,"game/bomb");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_explode(String id, int bomb_id){
		List<NameValuePair> send = new ArrayList<NameValuePair>();
        send.add(new BasicNameValuePair("device_id", id));
        send.add(new BasicNameValuePair("bomb_id", String.valueOf(bomb_id)));
		NetworkThread t = new NetworkThread(send,"bomb_id");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	private class NetworkThread extends Thread{
		List<NameValuePair> send;
		JSONObject result;
		String command;
		
		NetworkThread(List<NameValuePair> j, String c){
			send = j;
			command = c;
		}
		
		public void run() {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://hackcmu.twebspace.com/"+command+"/");
		    try {
		        httppost.setEntity(new UrlEncodedFormEntity(send));
		        HttpResponse response = httpclient.execute(httppost);
		        InputStreamReader in = new InputStreamReader(response.getEntity().getContent());
				String s = "";
				char[] buffer = new char[256];
				while(in.read(buffer) > 0){
					s += String.valueOf(buffer).trim();
				}
				in.close();
				System.out.println(s);
				try {
					result = new JSONObject(s);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    } catch (ClientProtocolException e) {
		    } catch (IOException e) {
		    }
		}
	}
}
