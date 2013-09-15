
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
		NetworkThread t = new NetworkThread(send);
		t.start();
		try {t.join();} catch (InterruptedException e) {}
		return t.result;
	}
	
	private class NetworkThread extends Thread{
		JSONObject send;
		
		JSONObject result;
		
		NetworkThread(JSONObject j){
			send = j;
		}
		public void run(){
			URL url = null;
			try{
				url = new URL("http://hackcmu.twebspace.com");
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
