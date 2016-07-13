package com.xmiles.android.facebook_api_support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GetFacebookProfile {
	
	//TAG
	private static final String TAG = "FACEBOOK";

	
	public GetFacebookProfile(){
		
	}
	
	public JSONObject GetResult(String a_token){
		
		String urlString = makeUrl(a_token);
		
		  try {
			   //---------------------------	  
			   String json = getJSON(urlString);
			 
			   System.out.println(json);
			   JSONObject object = new JSONObject(json);	   
			   
			   return object;

		  } catch (JSONException ex) {

		  }	
		
		
		return null;
	}
	
	public String makeUrl(String a_token){
		StringBuilder urlString = new StringBuilder(
			    "https://graph.facebook.com/me?access_token=");
		
		urlString.append(a_token);
		urlString.append("&format=json&fields=name%2C+picture%2Cbirthday%2Clocation%2Cgender%2Crelationship_status");
		
		Log.i(TAG, "urlString " + urlString);
		
		return urlString.toString();
	}
	
	 protected String getJSON(String url) {
		  return getUrlContents(url);
	 }
	
	 private String getUrlContents(String theUrl) {
		  StringBuilder content = new StringBuilder();
		  try {
		   URL url = new URL(theUrl);
		   URLConnection urlConnection = url.openConnection();
		   BufferedReader bufferedReader = new BufferedReader(
		     new InputStreamReader(urlConnection.getInputStream()), 8);
		   String line;
		   while ((line = bufferedReader.readLine()) != null) {
		    content.append(line + "\n");
		   }
		   bufferedReader.close();
		  }catch (Exception e) {
		   e.printStackTrace();
		  }
		  return content.toString();
		 }

}
