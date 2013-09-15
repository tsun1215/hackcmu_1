package com.clubkunz.game;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerInfo {
	public JSONObject ping(int id, double lon, double lat, double alt, int event){
		JSONObject send = null;
		
		URL url = null;
		try{
			url = new URL("128.237.132.169:8000");
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		try{
			send = new JSONObject();
			send.put("id",Integer.toString(id));
			send.put("lon",Double.toString(lon));
			send.put("lat",Double.toString(lat));
			send.put("alt", Double.toString(alt));
			send.put("event",Integer.toString(event));
		}catch(JSONException e){
			e.printStackTrace();
		}
		HttpURLConnection connection=null;
		try{
//			send the request
			connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept","application/JSON");
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(send.toString());
//			receive the request
						
		}catch(IOException e){
			e.printStackTrace();
		}
	return result;
	}
}
