package com.xmiles.android.facebook_places;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


//--------------------------------------------------------
 //public class Facebook_picURL_Places extends AsyncTask<Void,Void,JSONArray> {
public class Facebook_picURL_Places extends AsyncTask<Void,Void,JSONObject> {	 
	 /**
	  * https://graph.facebook.com/235361419852755?
	  * fields=picture.height(64).width(64)
	  * &access_token=CAACEdEose0cBAFnnXaUIQywaQ9wu5sPUrwZC9HYPpI9ZCncTE9128XRFASXZA7noZBMntushkHSOV6mWW6zR9zA5bZBBeBmqItV5zN1a0VUZCWDAkNQaZAZCyHn3CxidXr6ZCMGhKxEHYuwOzBOiWfeZAKoV5h99l7jTxBDnthuUqqD2JgZBT4HD0vrg5ZCWiKoGxF3WrWrbaghrPQZDZD
	  * 
	  */
	  
	  //-------------
	  private static final String TAG = "Facebook_Places";
	  protected static JSONArray jsonArray;
	  protected static JSONObject jsonObject;
	  //------------- 
	  
	  //private Context context;
	  private String context;
	  private String id;	  
	 
      public Facebook_picURL_Places(String string, String id) {	  
	   this.context = string;
	   this.id = id;
	  }
	 
	  @Override
	  //protected void onPostExecute(JSONArray result) {
	  protected void onPostExecute(JSONObject result) { 	  
	   super.onPostExecute(result);

	   //----------------------------	   
	   jsonObject = result;
	  
	  }
	 
	  @Override
	  protected void onPreExecute() {
	   super.onPreExecute();
	  }
	 
	  @Override
	  //protected JSONArray doInBackground(Void... arg0) {  
	  protected JSONObject doInBackground(Void... arg0) {	  
		  PicURL_Places_Service service = new PicURL_Places_Service(context);
		  //JSONArray findPlaces = service.findPlaces(id);
		  JSONObject findPicUrlPlaces = service.findPlaces(id);
    
	   
	   return findPicUrlPlaces;
	  }
	 }




