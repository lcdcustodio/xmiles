package com.xmiles.android.scheduler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.webservice.UserFunctions;

import android.os.AsyncTask;
import android.util.Log;



public class Invite_Friends extends AsyncTask<Void,Void,Void> {
	 
	  
	  //-------------
	  private static final String TAG = "Facebook";
	  protected static JSONArray jsonArray;
	  //------------- 
	  
	  private StringBuilder friend_id;
	  private String request_id;
	 
    public Invite_Friends(StringBuilder friend_id, String request_id) {	  
	   this.friend_id = friend_id;
	   this.request_id = request_id;
	  }
	 
	  @Override
	  protected void onPostExecute(Void result) {
	   super.onPostExecute(result);

	   //----------------------------	   
	   //jsonArray = result;
	   //Log.i(TAG,"jsonArray: " + jsonArray);
	   Log.i(TAG, "Invite_Friends - onPostExecute");
	  
	  }
	 
	  @Override
	  protected void onPreExecute() {
	   super.onPreExecute();
	  }
	 
	  @Override
	  protected Void doInBackground(Void... arg0) {  
	   //PlacesService service = new PlacesService(context);

	   //JSONArray findPlaces = service.findPlaces(latitude, longitude,distance);
	
	   UserFunctions userFunc = new UserFunctions();
	   JSONObject json = userFunc.getRewards();

		        try {

		        	if (json.getString("success") != null) {

					    String res = json.getString("success");
					    if(Integer.parseInt(res) == 1){
					    	
					    	
					    }

		        	}		

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    	//return json;	     
  
	   
	   //return findPlaces;
	   return null;  
	  }
	 }
