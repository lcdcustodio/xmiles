package com.xmiles.android.facebook_places;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PlacesService {
	 
	 private String Access_Token;
	 private final String TAG = "FACEBOOK";
	 
	 public PlacesService(String a_token) {
		  this.Access_Token = a_token;
		 }
		 
		 public void set_a_token(String a_token) {
		  this.Access_Token = a_token;
		 }
	 
	 
	 
	 public JSONArray findPlaces(double latitude, double longitude, int distance){

	  String urlString = makeUrl(latitude, longitude, distance);
	 
	  try {
	   //---------------------------	  
	   String json = getJSON(urlString);
	 
	   System.out.println(json);
	   JSONObject object = new JSONObject(json);	   
	   JSONArray array = object.getJSONArray("data");

	   //Log.v(TAG, "array.length(): " + array.length());

	   return array;
	  } catch (JSONException ex) {
	   Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
	     null, ex);
	  }
	  return null;
	 }
	 
	 private String makeUrl(double latitude, double longitude, int distance) {	 
	  StringBuilder urlString = new StringBuilder(
	    "https://graph.facebook.com/search?type=place");

	   urlString.append("&center=");
	   urlString.append(Double.toString(latitude));
	   urlString.append(",");
	   urlString.append(Double.toString(longitude));
	   urlString.append("&distance=" +	Integer.toString(distance));	   
	   urlString.append("&access_token=" +	Access_Token);

	   //Log.i(TAG, "urlString " + urlString);

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
	//</place></place></place>