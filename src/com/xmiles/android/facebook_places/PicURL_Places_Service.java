package com.xmiles.android.facebook_places;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PicURL_Places_Service {
	 
	 private String Access_Token;
	 private final String TAG = "Facebook_Places";
	 
	 public PicURL_Places_Service(String a_token) {
		  this.Access_Token = a_token;
		 }
		 
		 public void set_a_token(String a_token) {
		  this.Access_Token = a_token;
		 }
	 
	 
	 
	 //public JSONArray findPlaces(String id){
	 public JSONObject findPlaces(String id){
	 

	  String urlString = makeUrl(id);
	 
	  try {
		   //---------------------------	  
		   String json = getJSON(urlString);	    
		   //---------------------------
		 
		   System.out.println(json);
		   JSONObject object = new JSONObject(json);	   
		   //JSONArray array = object.getJSONArray("data");
		   //JSONArray array = object.getJSONArray("picture");
		   //JSONArray array = object.getJSONArray("picture");
		   //------------------------------------------------
		   //-----------------------------------------------
		   //Log.v(TAG, "array.length(): " + array.length());
		   Log.v(TAG, "object.length(): " + object.length());
		   //---------------------------	 
		   //return array;
		   return object;
	  } catch (JSONException ex) {
		   Logger.getLogger(PicURL_Places_Service.class.getName()).log(Level.SEVERE,
		     null, ex);
	  }
	  return null;
	 }
	 
	 private String makeUrl(String id) {	 
	  StringBuilder urlString = new StringBuilder(
	    "https://graph.facebook.com/");

	   urlString.append(id);
	   urlString.append("?fields=picture.height(64).width(64)");
	   urlString.append("&access_token=" +	Access_Token);

	   Log.e(TAG, "urlString_PicURL_Places: " + urlString);

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