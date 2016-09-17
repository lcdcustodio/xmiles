package com.xmiles.android.scheduler;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xmiles.android.backup.UserRoutes_Download;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.webservice.UserFunctions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class Favorites_Upload extends WakefulBroadcastReceiver {

		// The app's AlarmManager, which provides access to the system alarm services.
		private AlarmManager alarmMgr;
		// The pending intent that is triggered when the alarm fires.
		private PendingIntent alarmIntent;    
		
		private static String TAG = "FACEBOOK";
	    
		// The minimum time between updates in milliseconds
	    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 sec
	
		protected static JSONArray jsonArray;
		protected static JSONObject json;
	
	    //--------------------
		private static final Integer KEY_ID = 0;
		private static final Integer KEY_NAME = 1;
		private static final Integer KEY_BUSLINE = 2;
		private static final Integer KEY_CITY = 3;
		private static final Integer KEY_UF = 4;
		private static final Integer KEY_FROM = 5;
		private static final Integer KEY_FROM_BUS_STOP_ID = 7;
		private static final Integer KEY_TO = 6;
		private static final Integer KEY_TO_BUS_STOP_ID = 8;
		private static final Integer KEY_FAVORITE_ID = 9;
		
	   public void onReceive(Context context, Intent intent) {   

	    	
	    	Log.i(TAG, "Favorites_Update onReceive");

			    
			Favorites_Update_Handler(context);
	    	
	    }

	    public void setAlarm(Context context) {

	    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, Favorites_Upload.class);

	        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	        Calendar calendar = Calendar.getInstance();
	  
	        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 15 * MIN_TIME_BW_UPDATES, alarmIntent);
	    }
	    /**
	     * Cancels the alarm.
	     * @param context
	     */

	    public void cancelAlarm(Context context) {
	        	Log.d(TAG, "Favorites_Update cancelAlarm");
	        	
	        	Intent intent = new Intent(context, Favorites_Upload.class);
	        	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        	alarmMgr.cancel(alarmIntent);	
	       
	    }
	    
	    public void Favorites_Update_Handler(final Context c){
	    	
			 Thread thread = new Thread(new Runnable(){
				 @Override
				 public void run() {

	        
			        try {
			            Uri uri = SqliteProvider.CONTENT_URI_USER_FAVORITES;
			        	Cursor data_userFavorites = c.getContentResolver().query(uri, null, null, null, null);
			        	data_userFavorites.moveToLast();
			        	//------------
			        	//Your code goes here
			        	UserFunctions userFunc = new UserFunctions();		
			        	json = userFunc.insert_favoritesRoutes(data_userFavorites.getString(KEY_ID),
							       data_userFavorites.getString(KEY_NAME),
							       data_userFavorites.getString(KEY_BUSLINE),
							       data_userFavorites.getString(KEY_FROM),
							       data_userFavorites.getString(KEY_TO),
							       data_userFavorites.getString(KEY_CITY),
							       data_userFavorites.getString(KEY_UF),				       
							       data_userFavorites.getString(KEY_TO_BUS_STOP_ID),
							       data_userFavorites.getString(KEY_FROM_BUS_STOP_ID),
							       data_userFavorites.getString(KEY_FAVORITE_ID));
			        	
			    		        	
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
			        	//Log.d(TAG, "json @ Favorites_Update:" + json.toString());
			        	//Log.d(TAG, "json.getString(success):" + json.getString("success"));
			        	//cancelAlarm(c);

			            if (json.getString("success") != null) {
			                
			                String res = json.getString("success");
			                if(Integer.parseInt(res) == 1){

			                	cancelAlarm(c);
			                	//-------
			                	UserRoutes_Download URoutes_Dw = new UserRoutes_Download();
			                	URoutes_Dw.setAlarm(c);
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
