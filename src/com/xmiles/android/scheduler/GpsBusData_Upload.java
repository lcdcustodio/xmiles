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

public class GpsBusData_Upload extends WakefulBroadcastReceiver {

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
		
		private static final Integer KEY_ID 		 = 0;
		private static final Integer KEY_CREATED_AT  = 1;
		private static final Integer KEY_BUSCODE 	 = 2;
		private static final Integer KEY_BUSLINE 	 = 3;
		private static final Integer KEY_B_LATITUDE  = 4;
		private static final Integer KEY_B_LONGITUDE = 5;
		private static final Integer KEY_SPEED 		 = 6;
		private static final Integer KEY_DIRECTION 	 = 7;
		private static final Integer KEY_SCORE		 = 8;
		//-----------------------------
		private static final Integer KEY_U_ROW_ID 			 = 0;
		private static final Integer KEY_U_LATITUDE 		 = 1;
		private static final Integer KEY_U_LONGITUDE 		 = 2;
		private static final Integer KEY_U_SPEED 			 = 3;
		private static final Integer KEY_U_LOCATION_PROVIDER = 4;
		private static final Integer KEY_U_CREATED_AT 		 = 5;
		private static final Integer KEY_U_DIFF_DISTANCE 	 = 6;
		private static final Integer KEY_U_DIFF_TIME 		 = 7;
		private static final Integer KEY_U_LOCATION_STATUS   = 8;
		private static final Integer KEY_U_ACCURACY 		 = 9;
		//-----------------------------		
		
		
	   public void onReceive(Context context, Intent intent) {   

	    	
	    	Log.i(TAG, "GpsBusData_Update onReceive");

			    
			GpsBusData_Update_Handler(context);
	    	
	    }

	    public void setAlarm(Context context) {

	    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, GpsBusData_Upload.class);

	        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	        Calendar calendar = Calendar.getInstance();
	  
	        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), MIN_TIME_BW_UPDATES, alarmIntent);
	    }
	    /**
	     * Cancels the alarm.
	     * @param context
	     */

	    public void cancelAlarm(Context context) {
	        	Log.d(TAG, "GpsBusData_Update cancelAlarm");
	        	
	        	Intent intent = new Intent(context, GpsBusData_Upload.class);
	        	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        	alarmMgr.cancel(alarmIntent);	
	       
	    }
	    
	    public void GpsBusData_Update_Handler(final Context c){
	    	
			 Thread thread = new Thread(new Runnable(){
				 @Override
				 public void run() {

	        
			        try {
			        	//-------------
			            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
			            Cursor data_profile = c.getContentResolver().query(uri_1, null, null, null, null);
			            data_profile.moveToFirst();
			        	//-------------			        	
			            Uri uri_2 = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
			        	Cursor data_GpsBusData = c.getContentResolver().query(uri_2, null, null, null, null);
			        	//-------------			        	
			            Uri uri_3 = SqliteProvider.CONTENT_URI_USER_LOCATION;
			        	Cursor data_UserLocation = c.getContentResolver().query(uri_3, null, null, null, null);			        	
			        	//------------			        	
			        	UserFunctions userFunc = new UserFunctions();
			        	//------------------
			            StringBuilder gps_bus_id		 	= new StringBuilder();
			            StringBuilder user_id				= new StringBuilder(); 
			            StringBuilder latitude				= new StringBuilder();
			            StringBuilder longitude				= new StringBuilder();
			            StringBuilder speed					= new StringBuilder();
			            StringBuilder buscode			    = new StringBuilder();
			            StringBuilder busline		    	= new StringBuilder();
			            StringBuilder direction		    	= new StringBuilder();
			            StringBuilder created_at			= new StringBuilder();
			    		//---------------------			        	
			            StringBuilder u_locat_id			= new StringBuilder();
			            StringBuilder u_latitude			= new StringBuilder();
			            StringBuilder u_longitude			= new StringBuilder();
			            StringBuilder u_speed				= new StringBuilder();			            
			            StringBuilder u_locat_provider		= new StringBuilder();
			            StringBuilder u_created_at			= new StringBuilder();
			            StringBuilder u_diff_dist			= new StringBuilder();
			            StringBuilder u_diff_time			= new StringBuilder();
			            StringBuilder u_locat_status		= new StringBuilder();
			            StringBuilder u_accuracy			= new StringBuilder();
			    		//---------------------
			            StringBuilder score					= new StringBuilder();

			            
			    		while (data_GpsBusData.moveToNext()) {

			    			
			    			//Log.d(TAG, "data_GpsBusData.getString(KEY_B_LATITUDE): "+ data_GpsBusData.getString(KEY_B_LATITUDE));
				        	//Your code goes here
			    			
			    			
				    		gps_bus_id.append(data_GpsBusData.getString(KEY_ID));
				    		user_id.append(data_profile.getString(KEY_ID));
				    		latitude.append(data_GpsBusData.getString(KEY_B_LATITUDE));
				    		longitude.append(data_GpsBusData.getString(KEY_B_LONGITUDE));
				    		speed.append(data_GpsBusData.getString(KEY_SPEED));
				    		buscode.append(data_GpsBusData.getString(KEY_BUSCODE));
				    		busline.append(data_GpsBusData.getString(KEY_BUSLINE));
				    		direction.append(data_GpsBusData.getString(KEY_DIRECTION));
				    		created_at.append(data_GpsBusData.getString(KEY_CREATED_AT));
				    		//---------------------
	
				    		
				    		if (!data_GpsBusData.isLast()){
				    			gps_bus_id.append(";");
					    		user_id.append(";");
					    		latitude.append(";");
					    		longitude.append(";");
					    		speed.append(";");
					    		buscode.append(";");
					    		busline.append(";");
					    		direction.append(";");
					    		created_at.append(";");
					    		score.append("0.0;");
				    		} else if (data_GpsBusData.isLast()){
				    			score.append(data_GpsBusData.getString(KEY_SCORE));
				    	    	Log.w(TAG, "data_GpsBusData.getString(KEY_SCORE): " + data_GpsBusData.getString(KEY_SCORE));
				    		}
			    			

			    		}
			    		
			    		
			    		while (data_UserLocation.moveToNext()) {
			    			
			    			//------------------------------------
			    			u_locat_id.append(data_UserLocation.getString(KEY_U_ROW_ID)); 
			    			u_latitude.append(data_UserLocation.getString(KEY_U_LATITUDE));
			    			u_longitude.append(data_UserLocation.getString(KEY_U_LONGITUDE));
			    			u_speed.append(data_UserLocation.getString(KEY_U_SPEED));
			    			u_locat_provider.append(data_UserLocation.getString(KEY_U_LOCATION_PROVIDER));
			    			u_created_at.append(data_UserLocation.getString(KEY_U_CREATED_AT));
			    			u_diff_dist.append(data_UserLocation.getString(KEY_U_DIFF_DISTANCE));
			    			u_diff_time.append(data_UserLocation.getString(KEY_U_DIFF_TIME));
			    			u_locat_status.append(data_UserLocation.getString(KEY_U_LOCATION_STATUS));
			    			u_accuracy.append(data_UserLocation.getString(KEY_U_ACCURACY));
			    			//-------------------------------------
			    			if (!data_UserLocation.isLast()){

				    			u_locat_id.append(";");
				    			u_latitude.append(";");
				    			u_longitude.append(";");
				    			u_speed.append(";");
				    			u_locat_provider.append(";");
				    			u_created_at.append(";");
				    			u_diff_dist.append(";");
				    			u_diff_time.append(";");
				    			u_locat_status.append(";");
				    			u_accuracy.append(";");
				    			
			    			}
			    			
			    			
			    		}
			        	
			        	//Your code goes here	        			
			        	json = userFunc.busGps(gps_bus_id,
			        								 user_id,
			        								 latitude,
			        								 longitude,
			        								 speed,
			        								 buscode,
			        								 busline,
			        								 direction,
			        								 created_at,
			        								 //----------
			        								 u_locat_id,
			        								 u_latitude,
			        								 u_longitude,
			        								 u_speed,
			        								 u_locat_provider,
			        								 u_created_at,
			        								 u_diff_dist,
			        								 u_diff_time,
			        								 u_locat_status,
			        								 u_accuracy,
			        								 //----------
			        								 score
			        								 //----------
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
			                	 * Reset GpsBusData Table @ SQLite
			                	 */
			                	DatabaseHelper mDatabaseHelper;
			                	mDatabaseHelper = new DatabaseHelper(c);
			                	mDatabaseHelper.resetBusGpsData();
			                	//------------------------
			                	mDatabaseHelper.resetUserLocation();
			                	mDatabaseHelper.resetBusGpsUrl();
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
