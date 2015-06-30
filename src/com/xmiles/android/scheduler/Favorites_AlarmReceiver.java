package com.xmiles.android.scheduler;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.webservice.UserFunctions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class Favorites_AlarmReceiver extends WakefulBroadcastReceiver {

		// The app's AlarmManager, which provides access to the system alarm services.
		private AlarmManager alarmMgr;
		// The pending intent that is triggered when the alarm fires.
		private PendingIntent alarmIntent;    
		
		private static String TAG = "FACEBOOK";
	    

		// The minimum time between updates in milliseconds
	    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 sec
	
		protected static JSONArray jsonArray;
		protected static JSONObject json;
		private static final Integer KEY_ID = 0;
		private static final Integer KEY_NAME = 1;
	
	    private ContentValues[] valueList;
	
	   public void onReceive(Context context, Intent intent) {   

	    	
	    	Log.i(TAG, "Favorites_AlarmReceiver onReceive");

			    
			Favorites_Handler(context);
	    	
	    }

	    public void setAlarm(Context context) {

	    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, Favorites_AlarmReceiver.class);

	        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	        Calendar calendar = Calendar.getInstance();
	  
	        //-----
	        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 15 * MIN_TIME_BW_UPDATES, alarmIntent);
	    }
	    /**
	     * Cancels the alarm.
	     * @param context
	     */

	    public void cancelAlarm(Context context) {
	        // If the alarm has been set, cancel it.
	        //if (alarmMgr!= null) {
	        	Log.d(TAG, "Favorites_AlarmReceiver cancelAlarm");
	           // alarmMgr.cancel(alarmIntent);
	        //}
	        	
	        	Intent intent = new Intent(context, Favorites_AlarmReceiver.class);
	        	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        	alarmMgr.cancel(alarmIntent);	
	       
	    }
	    
	    public void Favorites_Sqlite(JSONArray favorites, String User_Id, String User_Name, Context c){
	    	
	    	valueList = new ContentValues[favorites.length()];
	    	
			for (int position = 0; position < favorites.length(); position++) {	
				
				JSONObject jsonObject = null;
				
				try {
					ContentValues values = new ContentValues();
					jsonObject = favorites.getJSONObject(position);
					
					values.put(DatabaseHelper.KEY_ID, User_Id);
					values.put(DatabaseHelper.KEY_NAME, User_Name);
					values.put(DatabaseHelper.KEY_BUSLINE, jsonObject.getString("busline"));
					values.put(DatabaseHelper.KEY_CITY, jsonObject.getString("city"));
					values.put(DatabaseHelper.KEY_UF, jsonObject.getString("uf"));
					values.put(DatabaseHelper.KEY_FROM, jsonObject.getString("_from"));					
					values.put(DatabaseHelper.KEY_FROM_BUS_STOP_ID, jsonObject.getString("_from_bus_stop_id"));
					values.put(DatabaseHelper.KEY_TO, jsonObject.getString("_to"));
					values.put(DatabaseHelper.KEY_TO_BUS_STOP_ID, jsonObject.getString("_to_bus_stop_id"));
					values.put(DatabaseHelper.KEY_BD_UPDATED, "NO");
					values.put(DatabaseHelper.KEY_CREATED_AT, jsonObject.getString("created_at"));

					valueList[position] = values;					
					//-----------
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			c.getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_FAVORITES, valueList);
			cancelAlarm(c);

	    }

	    
	    public void Favorites_Handler(final Context c){
	    	

		    //mHandler = new Handler();
		    //-------------
			 Thread thread = new Thread(new Runnable(){
				 @Override
				 public void run() {

	        
			        try {

			            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
			        	Cursor data = c.getContentResolver().query(uri, null, null, null, null);
			        	
			        	if (data != null && data.getCount() > 0){
			        		data.moveToFirst();

			        		//Your code goes here
			        		UserFunctions userFunc = new UserFunctions();
			        		json = userFunc.favoritesRoutes(data.getString(KEY_ID));

			        		jsonArray = new JSONArray(json.getString("user"));
			        		//Log.i(TAG,"testing 1: " + jsonArray.get(1));
			        		
			        		Favorites_Sqlite(jsonArray,data.getString(KEY_ID),data.getString(KEY_NAME), c);

			        	}
			    		        	
				    } catch (Exception e) {
				            e.printStackTrace();
				    }
				
 
	 
	            }
	        });
			 
			thread.start();
			
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
}
