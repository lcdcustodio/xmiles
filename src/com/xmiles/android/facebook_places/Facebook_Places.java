package com.xmiles.android.facebook_places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


//--------------------------------------------------------
 public class Facebook_Places extends AsyncTask<Void,Void,JSONArray> {
	 
	  
	  //-------------
	  private static final String TAG = "Facebook_Places";
	  protected static JSONArray jsonArray;
	  //------------- 
	  
	  //private Context context;
	  private String context;
	  private float latitude;
	  private float longitude;
	  private int distance;
	 
      public Facebook_Places(String string, float Lat, float Long, int dist) {	  
	   this.context = string;
	   this.latitude = Lat;
	   this.longitude = Long;
	   this.distance = dist;
	  }
	 
	  @Override
	  protected void onPostExecute(JSONArray result) {
	   super.onPostExecute(result);

	   //----------------------------	   
	   jsonArray = result;
	   //Log.i(TAG,"jsonArray: " + jsonArray);
	  
	  }
	 
	  @Override
	  protected void onPreExecute() {
	   super.onPreExecute();
	  }
	 
	  @Override
	  protected JSONArray doInBackground(Void... arg0) {  
	   PlacesService service = new PlacesService(context);

 	   JSONArray findPlaces = service.findPlaces(latitude, longitude,distance); 	   	   
    
	   
	   return findPlaces;
	  }
	 }




