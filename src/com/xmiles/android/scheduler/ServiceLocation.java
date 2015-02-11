package com.xmiles.android.scheduler;


import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.android.maps.GeoPoint;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.Support;
import com.xmiles.android.support.Distance_calc;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.facebook_places.Facebook_Places;
import com.xmiles.android.facebook_places.Facebook_picURL_Places;


import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

/**
 * Essa classe é a responsável por rodar o serviço de Location/Facebook places
 * em background
 * 
 * @author leonardo.dorneles
 *
 */

//public class ServiceLocation extends Service implements LocationListener, LoaderManager.LoaderCallbacks<Cursor> {
public class ServiceLocation extends Service implements LocationListener {	

	private final String DEBUG_TAG = "Scheduling Demo";
    // Database Helper
	DatabaseHelper db;
	
	private LocationManager lm;
	//private double latitude;
	//private double longitude;
	//private double accuracy;
    private Handler mHandler;
    
    private ContentValues[] valueList;
    
    //Facebook place distance require
    int MIN_DISTANCE = 1000;
    int MAX_DISTANCE = 5000;
    
    //Facebook place distance require
    int MAX_N_PLACES = 30;
    
    
	@Override
	public void onLocationChanged(Location location) {
	    Log.d(DEBUG_TAG, "onLocationChanged");
	
	    /*
	    latitude = location.getLatitude();
	    longitude = location.getLongitude();
	
	    accuracy = location.getAccuracy();
	    */
	    
	    GeoPoint curGeoPoint = new GeoPoint(
	                (int) (location.getLatitude() * 1E6),
	                (int) (location.getLongitude() * 1E6));

	    final float Lat = (float) (curGeoPoint.getLatitudeE6() / 1E6);
	    final float Long = (float) (curGeoPoint.getLongitudeE6() / 1E6);
	    //-------
	    Log.d(DEBUG_TAG, "Latitude: " + Lat);
	    Log.e(DEBUG_TAG, "Longitude: " + Long);
	    //-------
	    Toast.makeText(getApplicationContext(), "Latitude: " + Lat + " || Longitude: " + Long 
	    	  		  , Toast.LENGTH_LONG).show();

	      //----------------
        mHandler.post(new Runnable() {
            @Override
            public void run() {
  	  
		    	    JSONArray findPlaces;
		            JSONObject placeDetail;
		            JSONObject jsonObject_location;
		            JSONArray jsonArray_category;
		            		            
		            JSONObject picURL_Places;
		            
	  	    	    //Converting float to Double
	  	    	    Double lat_double = new Double(Lat);
	  	    	    Double long_double  = new Double(Long);
	  	    	    
	  	    	    Support support = new Support();

		            try {
		            	       	
		            	// call FACEBOOK PLACES
		            	findPlaces = new Facebook_Places(Utility.mFacebook.getAccessToken(),Lat,Long,MIN_DISTANCE).execute().get();
		            	//Log.d(DEBUG_TAG, "findPlaces.length(): " + findPlaces.length());
		            	if (findPlaces.length()== 0) {
		            		findPlaces = new Facebook_Places(Utility.mFacebook.getAccessToken(),Lat,Long,MAX_DISTANCE).execute().get();		            		
		            	}
		            	
		            	if (findPlaces.length() > 0) {
		            		
		            		int n_places;
		            		
		            		if (MAX_N_PLACES > findPlaces.length()){
		            			n_places = findPlaces.length();
		            		} else {
			            		n_places = MAX_N_PLACES;
		            		}
		            		
	    	  	    	    //ContentValues[] up to 30 positions for places
		            		valueList = new ContentValues[n_places];
		            		
			            	for (int i = 0; i < n_places; i++) {
				            	try {
				                    ContentValues values = new ContentValues();
				            		
					            	placeDetail = findPlaces.getJSONObject(i);
					            	
					            	// PicURL_Places
					            	picURL_Places = new Facebook_picURL_Places(Utility.mFacebook.getAccessToken(),placeDetail.getString("id")).execute().get();
					            	//Log.i("Facebook_Places", "picURL_Places: " + picURL_Places);
					            	//Log.e("Facebook_Places", "picURL_Places.getJSONObject(picture): " + picURL_Places.getJSONObject("picture"));
					            	//Log.v("Facebook_Places", "picURL_Places.getJSONObject(picture).getJSONObject(data): " + picURL_Places.getJSONObject("picture").getJSONObject("data"));					            	
					            	Log.d("Facebook_Places", "picURL_Places.getJSONObject(picture).getJSONObject(data).getString(url): " + picURL_Places.getJSONObject("picture").getJSONObject("data").getString("url"));
					            	

			
					            	jsonObject_location = findPlaces.getJSONObject(i).getJSONObject("location");	    			
					    			
					    			jsonArray_category = findPlaces.getJSONObject(i).getJSONArray("category_list");
			
					  	    	    Distance_calc dist_calc = new Distance_calc();
					  	    	    String get_distance = String.format("%.2f",dist_calc.calculo(jsonObject_location.getDouble("latitude"), Lat, jsonObject_location.getDouble("longitude"), Long));
					  	    	    
					  	    	    /** Setting up values to insert into UserPlaces table */
				                    values.put(DatabaseHelper.KEY_PLACE_ID, placeDetail.getString("id"));
				                    values.put(DatabaseHelper.KEY_NEARBY, placeDetail.getString("name"));
				                    values.put(DatabaseHelper.KEY_PICURL, picURL_Places.getJSONObject("picture").getJSONObject("data").getString("url"));
				                    values.put(DatabaseHelper.KEY_CITY, jsonObject_location.getString("city"));
				                    values.put(DatabaseHelper.KEY_CATEGORY, jsonArray_category.getJSONObject(0).getString("name"));
				                    values.put(DatabaseHelper.KEY_DISTANCE, get_distance);
				                    values.put(DatabaseHelper.KEY_U_LATITUDE, lat_double);
				                    values.put(DatabaseHelper.KEY_U_LONGITUDE, long_double);
				                    values.put(DatabaseHelper.KEY_P_LATITUDE, jsonObject_location.getDouble("latitude"));
				                    values.put(DatabaseHelper.KEY_P_LONGITUDE, jsonObject_location.getDouble("longitude"));
				                    values.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());
				                    
				                    valueList[i] = values;
		
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									
									
								}
			            	}
			            	getApplicationContext().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_PLACES, valueList);
			            	
							/** Restarting the MainActivity's loader to refresh the listview */
			            	//getApplicationContext().getSupportLoaderManager().restartLoader(0, null, (LoaderCallbacks<Cursor>)getApplicationContext());
			            	//getApplicationContext().getLoaderManager().restartLoader(0, null, (LoaderCallbacks<Cursor>)getApplicationContext());
			            	Intent intent=new Intent("fragmentupdater");
			            	sendBroadcast(intent);

			            	
		            	}else {

		            		/** Setting up values to insert into UserProfile table */
		        			ContentValues contentValues = new ContentValues();

		        			contentValues.put(DatabaseHelper.KEY_U_LATITUDE, lat_double);
		        			contentValues.put(DatabaseHelper.KEY_U_LONGITUDE, long_double);
		        			contentValues.put(DatabaseHelper.KEY_P_LATITUDE, 0.0);
		        			contentValues.put(DatabaseHelper.KEY_P_LONGITUDE, 0.0);
		        			contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());
			    			
		        			/** Invokes content provider's insert operation */
			            	getApplicationContext().getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_PLACES, contentValues);
		            	}
		            	
		    			
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
            }
        });

	    
	    
	}
	
	@Override
	public void onProviderDisabled(String provider) {
	    Log.d(DEBUG_TAG, "onProviderDisabled");
	    /*
	    Toast.makeText(
	            getApplicationContext(),
	            "Attempted to ping your location, and GPS was disabled.",
	            Toast.LENGTH_LONG).show();
	    */        
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	    Log.d(DEBUG_TAG, "onProviderEnabled");
	    //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this);
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	    // if(time >= [60000 milisec]minTime OR dist >= [500 m] minDistance) 
	    // then check for location updates
	    //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 500, this);
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	    Log.d(DEBUG_TAG, "onStatusChanged");
	
	}
	
	public void onCreate() {
	    Log.i(DEBUG_TAG, "onCreate ServiceLocation");
	    
	    mHandler = new Handler();
    	//db = new DatabaseHelper(getApplicationContext());
	}
	
	public void onDestroy() {
	    Log.d(DEBUG_TAG, "onDestroy");
	}
	
	public IBinder onBind(Intent intent) {
	    Log.d(DEBUG_TAG, "onBind");
	
	    return null;
	}
	
	public void onStart(Intent intent, int startid) {
	    Log.d(DEBUG_TAG, "onStart");
	
	    lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	
	    //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10f, this);
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	    // if(time >= [60000 milisec]minTime OR dist >= [500 m] minDistance) 
	    // then check for location updates
	    //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 500, this);
	
	    Log.d(DEBUG_TAG, lm.toString());


	}
}