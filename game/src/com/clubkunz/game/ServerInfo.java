
package com.clubkunz.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerInfo {
	public JSONObject register(String username, String fname, String lname, String id){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("username",username);
			send.put("f_name",fname);
			send.put("l_name",lname);
			send.put("device_id",id);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"register");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject register(String id){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("device_id",id);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"register");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_new(String id,float lon, float lat, boolean publ){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("device_id",id);
			send.put("long",lon);
			send.put("lat",lat);
			send.put("public", publ);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"game/new");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_list(String id,float lon, float lat){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("device_id",id);
			send.put("long",lon);
			send.put("lat",lat);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"game/list");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_join(String id,int game_id){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("device_id",id);
			send.put("game_id", game_id);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"game/join");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_refresh(String id,float lon,float lat,int rad){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("device_id",id);
			send.put("long",lon);
			send.put("lat",lat);
			send.put("rad", rad);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"game/refresh");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_bomb(String id,float lon,float lat,float alt){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("device_id",id);
			send.put("long",lon);
			send.put("lat", lat);
			send.put("alt", alt);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"game/bomb");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	public JSONObject game_explode(String id, int bomb_id){
		JSONObject send = null;
		try{
			send = new JSONObject();
			send.put("device_id",id);
			send.put("bomb_id", bomb_id);
		}catch(JSONException e){
			e.printStackTrace();
		}
		NetworkThread t = new NetworkThread(send,"bomb_id");
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	private class NetworkThread extends Thread{
		JSONObject send;
		JSONObject result;
		String command;
		
		NetworkThread(JSONObject j, String c){
			send = j;
			command = c;
		}
		public void run(){
			URL url = null;
			try{
				url = new URL("http://hackcmu.twebspace.com/"+command);
			}catch(MalformedURLException e){
				e.printStackTrace();
			}
			HttpURLConnection connection=null;
			try{
//				send the request
				connection=(HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestProperty("Accept","application/JSON");
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(send.toString());
				out.flush();
				out.close();
//				receive the request
				InputStreamReader in = new InputStreamReader(connection.getInputStream());
				char[] buffer = new char[10000];
				in.read(buffer);
				String s = String.valueOf(buffer).trim();
				in.close();
				result = new JSONObject(s);
			}catch(IOException e){
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
