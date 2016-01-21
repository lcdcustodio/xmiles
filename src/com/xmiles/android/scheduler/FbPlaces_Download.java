package com.xmiles.android.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.facebook_places.Facebook_Places;
import com.xmiles.android.facebook_places.Facebook_picURL_Places;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GetDistance;
import com.xmiles.android.support.Support;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */


public class FbPlaces_Download extends WakefulBroadcastReceiver implements LocationListener{	

	// The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    
    private static String TAG = "FACEBOOK";
    
    // flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    boolean canGetLocation = false;
 
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 10; // 10 minutes
 
    // Declaring a Location Manager
    protected LocationManager lm;
 
    private Handler mHandler;
    
    private ContentValues[] valueList;
    
    //Facebook place distance require
    int MIN_DISTANCE = 1000;
    int MAX_DISTANCE = 5000;
    
    //Facebook place distance require
    //int MAX_N_PLACES = 30;
    int MAX_N_PLACES = 3;

    
    @Override
    public void onReceive(Context context, Intent intent) {   
        // BEGIN_INCLUDE(alarm_onreceive)
    	
    	Log.i(TAG, "FbPlaces onReceive");
    	//*

    	getLocation(context);

    	if(location != null){
		    
		    FbPlaces_Handler(context);
    	}
	    //*/
    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {

    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, FbPlaces_Download.class);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
   
        // Set the alarm to fire at approximately 8:30 a.m., according to the device's
        // clock, and to repeat once a day.
        //-----
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 15 * MIN_TIME_BW_UPDATES, alarmIntent);

    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.

        
    	Log.d(TAG, "FbPlaces cancelAlarm");
    	
    	Intent intent = new Intent(context, FbPlaces_Download.class);
    	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	alarmMgr.cancel(alarmIntent);	
       
    }

    
    public void FbPlaces_Handler(final Context c){
    	
		GeoPoint curGeoPoint = new GeoPoint(
                (int) (latitude * 1E6),
                (int) (longitude * 1E6));

	    
	    final float Lat = (float) (curGeoPoint.getLatitudeE6() / 1E6);
	    final float Long = (float) (curGeoPoint.getLongitudeE6() / 1E6);

	    Log.d(TAG, "Latitude: " + Lat);
	    Log.e(TAG, "Longitude: " + Long);
	    Log.i(TAG, "Provider: " + location.getProvider());
	    //------------
	    mHandler = new Handler();
	    //-------------
	    
        mHandler.post(new Runnable() {
            @Override
            public void run() {
  	  
                JSONArray findPlaces = null;
                JSONObject placeDetail;
                JSONObject jsonObject_location;
                JSONArray jsonArray_category;
                  		            
                JSONObject picURL_Places = null;
                  
                //Converting float to Double
                Double lat_double = new Double(Lat);
                Double long_double  = new Double(Long);
                
                Support support = new Support();
                
              	// call FACEBOOK PLACES		   
            	try {
					
            		findPlaces = new Facebook_Places(Utility.mFacebook.getAccessToken(),Lat,Long,MIN_DISTANCE).execute().get();
					
            		try {
            			
            			if (findPlaces.length() > 0) {
            				
            				int n_places;
            				
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
	        		    			picURL_Places = new Facebook_picURL_Places(Utility.mFacebook.getAccessToken(),placeDetail.getString("id")).execute().get();
	        		    			
	        		    			jsonObject_location = findPlaces.getJSONObject(i).getJSONObject("location");
	        		    			jsonArray_category = findPlaces.getJSONObject(i).getJSONArray("category_list");

	        		  	    	    GetDistance dist_calc = new GetDistance();
	        		  	    	    String get_distance = String.format("%.2f",dist_calc.calculo(jsonObject_location.getDouble("latitude"), Lat, jsonObject_location.getDouble("longitude"), Long));

	        		  	    	    /** Setting up values to insert into UserPlaces table */
	        	                    values.put(DatabaseHelper.KEY_PLACE_ID, placeDetail.getString("id"));
	        	                    values.put(DatabaseHelper.KEY_NEARBY, placeDetail.getString("name"));
	        	                    values.put(DatabaseHelper.KEY_PICURL, picURL_Places.getJSONObject("picture").getJSONObject("data").getString("url"));
	        	                    values.put(DatabaseHelper.KEY_CITY, jsonObject_location.getString("city"));
	        	                    //new
	        	                    values.put(DatabaseHelper.KEY_UF, jsonObject_location.getString("state"));
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
            	      		
    		            	c.getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_PLACES, valueList);
    		            	//stop FbPlaces service
    		            	cancelAlarm(c);
            	      		
            			}
            			
            		} catch (NullPointerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Log.e(TAG, "findPlaces = null");
						
		            	//stop FbPlaces service
		            	cancelAlarm(c);

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
    
    
    
    //*
    //public Location getLocation(Context ctx) {
    private Location getLocation(Context ctx) {
    
    	
        try {

    
            lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            //lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	    //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    	    
            // getting GPS status
            isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                	/*
                    lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    */
                    lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0, 0, this);
                    
                    Log.d(TAG, "Network");
                    if (lm != null) {
                        location = lm
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        lm.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(TAG, "GPS Enabled");
                        if (lm != null) {
                            location = lm
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return location;
    }
    //*/

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
