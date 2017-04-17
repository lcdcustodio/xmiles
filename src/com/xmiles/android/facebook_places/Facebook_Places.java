package com.xmiles.android.facebook_places;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GetDistance;
import com.xmiles.android.support.Support;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


//--------------------------------------------------------
 public class Facebook_Places extends AsyncTask<Void,Void,JSONArray> {
	 
	  
	  //-------------
	  private static final String TAG = "FACEBOOK";
	  protected static JSONArray jsonArray;
	  //------------- 
	  
	  private Context context;
	  //private String context;
	  private String fb_token;
	  private double latitude;
	  private double longitude;
	  private int distance;
	  
	  //Facebook MAX #PLACES	  
	  int MAX_N_PLACES = 4;
	  
	  private ContentValues[] valueList;	
	  private JSONObject placeDetail;
	  private JSONObject picURL_Places;
	  private JSONObject jsonObject_location;
	  private JSONArray jsonArray_category;

	  
      public Facebook_Places(Context c,String fb_token, double Lat, double Long, int dist) {	  
	   this.context  = c;
       this.fb_token = fb_token;
	   this.latitude = Lat;
	   this.longitude = Long;
	   this.distance = dist;
	  }
	 
	  @Override
	  protected void onPostExecute(JSONArray findPlaces) {
	   super.onPostExecute(findPlaces);

	   	//----------------------------	   
	    Support support = new Support();
	   
	   	try {
		 	
	   		if (findPlaces.length() > 0) {
		 		
		 		int n_places;
		 		//--------------------
		 		/*
		        Uri uri_3 = SqliteProvider.CONTENT_URI_USER_PLACES;
				Cursor fb_places = context.getContentResolver().query(uri_3, null, null, null, null);
				fb_places.moveToFirst();
		 		
				//Log.e(TAG,"fb_places.getCount(): " + fb_places.getCount());
				*/
				//---------------------------
            	DatabaseHelper mDatabaseHelper;
            	mDatabaseHelper = new DatabaseHelper(context);
            	mDatabaseHelper.resetUserPlaces();
				//---------------------------
		        /*
				Cursor fb_places2 = context.getContentResolver().query(uri_3, null, null, null, null);
				fb_places2.moveToFirst();            	
            	
            	//Log.e(TAG,"fb_places.getCount(): " + fb_places2.getCount());
            	*/
		 		//--------------------		 		
			 	
            	if (MAX_N_PLACES > findPlaces.length()){
			 		n_places = findPlaces.length();
			 	} else {
			     	n_places = MAX_N_PLACES;
			 	}
			 	valueList = new ContentValues[n_places];
			 	
			 	for (int i = 0; i < n_places; i++) {
			             
			 		ContentValues values = new ContentValues();
			 		
			             	try {
	
			             		placeDetail = findPlaces.getJSONObject(i);
									
				            	// PicURL_Places
				    			try {
									picURL_Places = new Facebook_picURL_Places(fb_token,placeDetail.getString("id")).execute().get();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    			
				    			jsonObject_location = findPlaces.getJSONObject(i).getJSONObject("location");
				    			jsonArray_category = findPlaces.getJSONObject(i).getJSONArray("category_list");
	
				  	    	    GetDistance dist_calc = new GetDistance();
				  	    	    String get_distance = String.format("%.2f",dist_calc.calculo(jsonObject_location.getDouble("latitude"), latitude, jsonObject_location.getDouble("longitude"), longitude));
	
				  	    	    /** Setting up values to insert into UserPlaces table */
			                    values.put(DatabaseHelper.KEY_PLACE_ID, placeDetail.getString("id"));
			                    values.put(DatabaseHelper.KEY_NEARBY, placeDetail.getString("name"));
			                    values.put(DatabaseHelper.KEY_PICURL, picURL_Places.getJSONObject("picture").getJSONObject("data").getString("url"));
			                    values.put(DatabaseHelper.KEY_CITY, jsonObject_location.getString("city"));
			                    //new
			                    values.put(DatabaseHelper.KEY_UF, jsonObject_location.getString("state"));
			                    values.put(DatabaseHelper.KEY_CATEGORY, jsonArray_category.getJSONObject(0).getString("name"));
			                    values.put(DatabaseHelper.KEY_DISTANCE, get_distance);
			                    values.put(DatabaseHelper.KEY_U_LATITUDE, latitude);
			                    values.put(DatabaseHelper.KEY_U_LONGITUDE, longitude);
			                    values.put(DatabaseHelper.KEY_P_LATITUDE, jsonObject_location.getDouble("latitude"));
			                    values.put(DatabaseHelper.KEY_P_LONGITUDE, jsonObject_location.getDouble("longitude"));
			                    values.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());
			                    
			                    valueList[i] = values;
	
	
				    			
							} catch (JSONException e) {
									// TODO Auto-generated catch block
								e.printStackTrace();
							}            	      			
			             		    			
			 	}
		 	
		     	context.getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_PLACES, valueList);
		 	
		 	}
		 	
		 } catch (NullPointerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//Log.e(TAG, "findPlaces = null");

		 }
	   
	  
	  }
	 
	  @Override
	  protected void onPreExecute() {
	   super.onPreExecute();
	  }
	 
	  @Override
	  protected JSONArray doInBackground(Void... arg0) {  
	   PlacesService service = new PlacesService(fb_token);

 	   JSONArray findPlaces = service.findPlaces(latitude, longitude,distance); 	   	   
    
	   
	   return findPlaces;
	  }
	 }




