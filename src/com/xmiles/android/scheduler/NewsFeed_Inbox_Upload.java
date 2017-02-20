package com.xmiles.android.scheduler;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.ConnectionDetector;
import com.xmiles.android.webservice.UserFunctions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class NewsFeed_Inbox_Upload extends WakefulBroadcastReceiver {

		// The app's AlarmManager, which provides access to the system alarm services.
		private AlarmManager alarmMgr;
		// The pending intent that is triggered when the alarm fires.
		private PendingIntent alarmIntent;    
		
		private static String TAG = "FACEBOOK";

		private static String destination;
		private static String sender;
	    
		// The minimum time between updates in milliseconds
	    //private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 3600; // 3600 seconds
	    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 360; // 360 seconds
	
		protected static JSONArray jsonArray;
		protected static JSONObject json;
	
	    //--------------------
		private static final Integer KEY_ID_PROFILE  = 0;
		
		private static final Integer KEY_ID 		 = 1;
		private static final Integer KEY_NAME		 = 2;
		private static final Integer KEY_IMAGE	 	 = 3;
		private static final Integer KEY_STATUS 	 = 4;
		private static final Integer KEY_PICURL		 = 5;
		private static final Integer KEY_TIME_STAMP	 = 6;
		private static final Integer KEY_URL		 = 7;
		private static final Integer KEY_LIKE_STATS  = 9;
		private static final Integer KEY_COMMENT_STATS = 10;
		private static final Integer KEY_FEED_TYPE   = 11;
		private static final Integer KEY_FLAG_ACTION = 12;
		private static final Integer KEY_HASHTAG = 13;
		
		// Connection detector
		ConnectionDetector cd;

		
	   public void onReceive(Context context, Intent intent) {   

	    	
	    	Log.i(TAG, "NewsFeed_Inbox_Update onReceive");

			cd = new ConnectionDetector(context.getApplicationContext());
			
			// Check if Internet present
			if (cd.isConnectingToInternet()) {
			
				NewsFeed_Inbox_Handler(context);
			} 
	    	
	    }

	    public void setAlarm(Context context) {

	    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, NewsFeed_Inbox_Upload.class);

	        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	        Calendar calendar = Calendar.getInstance();
	  
	        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), MIN_TIME_BW_UPDATES, alarmIntent);
	    }
	    /**
	     * Cancels the alarm.
	     * @param context
	     */

	    public void cancelAlarm(Context context) {
	        	Log.d(TAG, "Newsfeed_Upload cancelAlarm");
	        	
	        	Intent intent = new Intent(context, NewsFeed_Inbox_Upload.class);
	        	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        	alarmMgr.cancel(alarmIntent);	
	       
	    }
	    
	    public void NewsFeed_Inbox_Handler(final Context c){
	    	
			 Thread thread = new Thread(new Runnable(){
				 @Override
				 public void run() {

	        
			        try {
			        	//-------------
			            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
			            Cursor data_profile = c.getContentResolver().query(uri_1, null, null, null, null);
			            data_profile.moveToFirst();
			        	//-------------			        	
			            Uri uri_2 = SqliteProvider.CONTENT_URI_NEWSFEED_UPLOAD;
			        	Cursor data_NewsFeed = c.getContentResolver().query(uri_2, null, null, null, null);
			        	data_NewsFeed.moveToFirst();			        	
			        	//-------------			        	
			        	UserFunctions userFunc = new UserFunctions();
			        	//------------------
						//contentValues.put(DatabaseHelper.KEY_FLAG_ACTION, "ADD");
						//contentValues.put(DatabaseHelper.KEY_FEED_TYPE, "User gets into the bus");
						//contentValues.put(DatabaseHelper.KEY_LIKE_STATS, "0");
						//contentValues.put(DatabaseHelper.KEY_COMMENT_STATS, "0");
			        	//------------------

			        	if (data_NewsFeed.getString(KEY_FEED_TYPE).equals("User gets into the bus")){
			        		destination = "ALL";
			        		sender 		= data_profile.getString(KEY_ID_PROFILE);
			        	} else{
			        		destination = data_profile.getString(KEY_ID_PROFILE);
			        		sender		= "x1";
			        	}

			        	
			        	//Your code goes here	        			
			        	json = userFunc.newsFeed_inbox(data_NewsFeed.getString(KEY_ID), 
			        								   data_NewsFeed.getString(KEY_NAME), 
			        								   //null,
			        								   data_NewsFeed.getString(KEY_IMAGE),
			        								   data_NewsFeed.getString(KEY_STATUS),
			        								   data_NewsFeed.getString(KEY_PICURL), 
			        								   null, 					 //data_NewsFeed.getString(KEY_URL)	
			        								   sender, //data_profile.getString(KEY_ID_PROFILE), 
			        								   destination, //"ALL", 
			        								   data_NewsFeed.getString(KEY_FEED_TYPE), //"User gets into the bus", //data_NewsFeed.getString(KEY_FEED_TYPE), 
			        								   "0",						 //data_NewsFeed.getString(KEY_LIKE_STATS), 
			        								   "0",						 //data_NewsFeed.getString(KEY_COMMENT_STATS), 
			        								   "ADD", 					 //data_NewsFeed.getString(KEY_FLAG_ACTION),
			        								   data_NewsFeed.getString(KEY_HASHTAG), 
			        								   data_NewsFeed.getString(KEY_TIME_STAMP));
			        	
			    		        	
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
			                	 * Reset Newsfeed_Upload Table @ SQLite
			                	 */
			                	DatabaseHelper mDatabaseHelper;
			                	mDatabaseHelper = new DatabaseHelper(c);
			                	mDatabaseHelper.resetNewsfeed_Upload();
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
