package com.xmiles.android.scheduler;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.webservice.UserFunctions;

import android.os.AsyncTask;
import android.util.Log;



public class Invite_Friends_AsyncTask extends AsyncTask<Void,Void,Void> {
	 
	  
	  //-------------
	  private static final String TAG = "FACEBOOK";
	  protected static JSONArray jsonArray;
	  //------------- 
	  
	  private StringBuilder friends_id;
	  private String request_id;
	  private String user_id;
	 
    public Invite_Friends_AsyncTask(String user_id, StringBuilder friends_id, String request_id) {
    	
	   this.friends_id = friends_id;
	   this.request_id = request_id;
	   this.user_id = user_id;
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

		UserFunctions userFunc = new UserFunctions();

		JSONObject json = userFunc.inviteFriends(user_id,
					   							request_id,
					   							friends_id,
					   							"WAIT");
			   
			   
		        try {

		        	if (json.getString("success") != null) {

					    String res = json.getString("success");
					    if(Integer.parseInt(res) == 1){
					    	Log.v(TAG, "Invite_Friends: success");
					    	
					    }

		        	}		

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    	//return json;	     
  
	   return null;  
	  }
	 }
