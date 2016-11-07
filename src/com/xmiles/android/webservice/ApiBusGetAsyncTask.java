package com.xmiles.android.webservice;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ApiBusGetAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    

	protected static String jsonArray;
	
	@Override
    protected JSONObject doInBackground(String... buscode) {
		
		Log.w("FACEBOOK", "buscode[0]: " + buscode[0]);
		Log.w("FACEBOOK", "buscode[1]: " + buscode[1]);
          
        return GET(buscode[0],buscode[1]);
    }
	// onPostExecute displays the results of the AsyncTask.	
    @Override
    protected void onPostExecute(JSONObject result) {    	

    	Log.w("FACEBOOK", "API BUS result: " + result);
    	
    
   }

    
    public static JSONObject GET(String buscode_digits, String buscode){
		
		JSONObject result = null;
		
		
		try {
			
			UserFunctions userFunc = new UserFunctions();
			result = userFunc.getApiBuscode(buscode_digits, buscode);	
		
		} catch (Exception e) {
			Log.d("FACEBOOK", e.getLocalizedMessage());
		}
		
		return result;
	} 
    
}
