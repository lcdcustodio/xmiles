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

public class Comments_Inbox_Upload extends WakefulBroadcastReceiver {

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
		
		
		private static final Integer KEY_ID 		 = 1;
		private static final Integer KEY_U_ID		 = 2;
		private static final Integer KEY_TIME_STAMP	 = 3;
		private static final Integer KEY_FLAG_ACTION = 4;
		private static final Integer KEY_SENDER 	 = 5;
		private static final Integer KEY_STATUS 	 = 6;
		private static final Integer KEY_FEED_TYPE 	 = 7;
		private static final Integer KEY_COMMENT 	 = 8;
		
		
	   public void onReceive(Context context, Intent intent) {   

	    	
	    	//Log.i(TAG, "Comments_Inbox_Upload onReceive");

			    
	    	Comments_Inbox_Handler(context);
	    	
	    }

	    public void setAlarm(Context context) {

	    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, Comments_Inbox_Upload.class);

	        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	        Calendar calendar = Calendar.getInstance();
	  
	        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), MIN_TIME_BW_UPDATES, alarmIntent);
	    }
	    /**
	     * Cancels the alarm.
	     * @param context
	     */

	    public void cancelAlarm(Context context) {
	        	//Log.d(TAG, "Comments_Inbox_Upload cancelAlarm");
	        	
	        	Intent intent = new Intent(context, Comments_Inbox_Upload.class);
	        	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        	alarmMgr.cancel(alarmIntent);	
	       
	    }
	    
	    public void Comments_Inbox_Handler(final Context c){
	    	
			 Thread thread = new Thread(new Runnable(){
				 @Override
				 public void run() {

	        
			        try {
			        	//-------------			        
			            Uri uri_1 = SqliteProvider.CONTENT_URI_COMMENTS_UPLOAD;
			            Cursor data_comments = c.getContentResolver().query(uri_1, null, null, null, null);
			            data_comments.moveToFirst();

			        	//-------------			        	
			        	UserFunctions userFunc = new UserFunctions();
			        	//------------------

			        	//------------------
			        	//Log.e(TAG,"data_comments.getString(KEY_COMMENT): " + data_comments.getString(KEY_COMMENT));
			        	
			        	//Your code goes here
			        	//*
			        	json = userFunc.comments_inbox(data_comments.getString(KEY_ID),
			        							       data_comments.getString(KEY_SENDER),
													   data_comments.getString(KEY_U_ID),
			        								   data_comments.getString(KEY_FLAG_ACTION),
			        								   data_comments.getString(KEY_TIME_STAMP),//);
			        								   data_comments.getString(KEY_STATUS),
			        								   data_comments.getString(KEY_FEED_TYPE),
			        								   data_comments.getString(KEY_COMMENT));
			    		//*/        	
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
			                	 * Reset Likes_Upload Table @ SQLite
			                	 */
			                	DatabaseHelper mDatabaseHelper;
			                	mDatabaseHelper = new DatabaseHelper(c);
			                	mDatabaseHelper.resetComments_upload();
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
