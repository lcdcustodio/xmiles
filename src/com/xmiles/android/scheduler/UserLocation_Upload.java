package com.xmiles.android.scheduler;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.webservice.UserFunctions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class UserLocation_Upload extends WakefulBroadcastReceiver {

		// The app's AlarmManager, which provides access to the system alarm services.
		private AlarmManager alarmMgr;
		// The pending intent that is triggered when the alarm fires.
		private PendingIntent alarmIntent;    
		
		private static String TAG = "FACEBOOK";
	    
		// The minimum time between updates in milliseconds
	    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 3600; // 3600 seconds
	
		protected static JSONArray jsonArray;
		protected static JSONObject json;
	
	    //--------------------
		private static final Integer KEY_ID = 0;
		private static final Integer KEY_U_LATITUDE = 1;
		private static final Integer KEY_U_LONGITUDE = 2;
		private static final Integer KEY_SPEED = 3;
		private static final Integer KEY_LOCATION_PROVIDER = 4;
		private static final Integer KEY_CREATED_AT = 5;
		
		
	   public void onReceive(Context context, Intent intent) {   

	    	
	    	Log.i(TAG, "UserLocation_Update onReceive");

			    
			UserLocation_Update_Handler(context);
	    	
	    }

	    public void setAlarm(Context context) {

	    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, UserLocation_Upload.class);

	        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	        Calendar calendar = Calendar.getInstance();
	  
	        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), MIN_TIME_BW_UPDATES, alarmIntent);
	    }
	    /**
	     * Cancels the alarm.
	     * @param context
	     */

	    public void cancelAlarm(Context context) {
	        	Log.d(TAG, "UserLocation_Update cancelAlarm");
	        	
	        	Intent intent = new Intent(context, UserLocation_Upload.class);
	        	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        	alarmMgr.cancel(alarmIntent);	
	       
	    }
	    
	    public void UserLocation_Update_Handler(final Context c){
	    	
			 Thread thread = new Thread(new Runnable(){
				 @Override
				 public void run() {

	        
			        try {
			        	//-------------
			            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
			            Cursor data_profile = c.getContentResolver().query(uri_1, null, null, null, null);
			            data_profile.moveToFirst();
			        	//-------------
			        	
			            Uri uri_2 = SqliteProvider.CONTENT_URI_USER_LOCATION;
			        	Cursor data_userLocation = c.getContentResolver().query(uri_2, null, null, null, null);
			        	//------------			        	
			        	UserFunctions userFunc = new UserFunctions();
			        	//------------------
			            StringBuilder user_location_id	 	= new StringBuilder();
			            StringBuilder user_id				= new StringBuilder(); 
			            StringBuilder latitude				= new StringBuilder();
			            StringBuilder longitude				= new StringBuilder();
			            StringBuilder speed					= new StringBuilder();
			            StringBuilder location_provider		= new StringBuilder();
			            StringBuilder bus_stop_radius_flag	= new StringBuilder();
			            StringBuilder bus_stop_radius_count	= new StringBuilder();
			            StringBuilder favorite_id			= new StringBuilder();
			            StringBuilder bus_stop_id			= new StringBuilder();
			            StringBuilder created_at			= new StringBuilder();
			    		//---------------------			        	
			        	
			    		while (data_userLocation.moveToNext()) {
			    			
				        	//Your code goes here
			    			user_location_id.append(data_userLocation.getString(KEY_ID));
			    			user_id.append(data_profile.getString(KEY_ID));
			    			latitude.append(data_userLocation.getString(KEY_U_LATITUDE));
			    			longitude.append(data_userLocation.getString(KEY_U_LONGITUDE));
			    			speed.append(data_userLocation.getString(KEY_SPEED));
			    			location_provider.append(data_userLocation.getString(KEY_LOCATION_PROVIDER));
			    			bus_stop_radius_flag.append("lala");
			    			bus_stop_radius_count.append("lala");
			    			favorite_id.append("lala");
			    			bus_stop_id.append("0");
			    			created_at.append(data_userLocation.getString(KEY_CREATED_AT));
			    			
			    			if (!data_userLocation.isLast()){
				    			user_location_id.append(";");
				    			user_id.append(";");
				    			latitude.append(";");
				    			longitude.append(";");
				    			speed.append(";");
				    			location_provider.append(";");
				    			bus_stop_radius_flag.append(";");
				    			bus_stop_radius_count.append(";");
				    			favorite_id.append(";");
				    			bus_stop_id.append(";");
				    			created_at.append(";");			    				
			    			}

			    		}
			        	
			        	//Your code goes here	        			
			        	json = userFunc.userLocation(user_location_id,
			        								 user_id,
			        								 latitude,
			        								 longitude,
			        								 speed,
			        								 location_provider,
			        								 bus_stop_radius_flag,
			        								 bus_stop_radius_count,
			        								 favorite_id,
			        								 bus_stop_id,
			        								 created_at
			        								 );
			        	
			    		        	
				    } catch (Exception e) {
				            e.printStackTrace();
				    }
				
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
 
	 
	            }
	        });
			 
			thread.start();
			
			 Thread thread_2 = new Thread(new Runnable(){
				 @Override
				 public void run() {

					try {
							Thread.sleep(800);
					} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}   
	        
			        try {

			            if (json.getString("success") != null) {
			                
			                String res = json.getString("success");
			                if(Integer.parseInt(res) == 1){
			                	//------------------------
			                	/*
			                	 * Reset UserLocation Table @ SQLite
			                	 */
			                	DatabaseHelper mDatabaseHelper;
			                	mDatabaseHelper = new DatabaseHelper(c);
			                	mDatabaseHelper.resetUserLocation();
			                	//------------------------			                	
			                	cancelAlarm(c);
			                }
			            }
			    		        	
				    } catch (Exception e) {
				            e.printStackTrace();
				    }
				
 
	            }
	        });
			 
			thread_2.start();

	    }
}
