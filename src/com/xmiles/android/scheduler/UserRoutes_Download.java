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
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class UserRoutes_Download extends WakefulBroadcastReceiver {

	// The app's AlarmManager, which provides access to the system alarm services.
	private AlarmManager alarmMgr;
	// The pending intent that is triggered when the alarm fires.
	private PendingIntent alarmIntent;

	private static String TAG = "FACEBOOK";

	// The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 sec

	String favorite_id;
	String user_id;
	private static final Integer KEY_FAVORITE_ID = 9;
	private static final Integer KEY_ID = 0;


	   public void onReceive(Context context, Intent intent) {


	    	Log.i(TAG, "UserRoutes_Download onReceive");


			UserRoutes_Download_Handler(context);

	    }
	    public void setAlarm(Context context) {

	    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, UserRoutes_Download.class);

	        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	        Calendar calendar = Calendar.getInstance();

	        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 15 * MIN_TIME_BW_UPDATES, alarmIntent);
	    }
	    /**
	     * Cancels the alarm.
	     * @param context
	     */

	    public void cancelAlarm(Context context) {

	    		Log.d(TAG, "UserRoutes_Download cancelAlarm");

	        	Intent intent = new Intent(context, UserRoutes_Download.class);
	        	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        	alarmMgr.cancel(alarmIntent);

	    }
	    public void UserRoutes_Download_Handler(final Context c){


			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {


					//Your code goes here
			    	//------------
		            Uri uri = SqliteProvider.CONTENT_URI_USER_FAVORITES;
		        	Cursor data_userFavorites = c.getContentResolver().query(uri, null, null, null, null);
		        	data_userFavorites.moveToLast();
		        	//------------
		        	favorite_id = data_userFavorites.getString(KEY_FAVORITE_ID);
		        	user_id		= data_userFavorites.getString(KEY_ID);
			    	//------------
			    	ContentValues[] valueList;
			    	JSONArray jsonArray;
			    	//-----------
			    	UserFunctions userFunc = new UserFunctions();
			    	JSONObject json = userFunc.userRoutes(user_id,favorite_id);
			    	//JSONObject json = userFunc.favoritesRoutes(user_id);
			    	//*
			        try {

			        	if (json.getString("success") != null) {

						    String res = json.getString("success");
						    if(Integer.parseInt(res) == 1){

						    	jsonArray = new JSONArray(json.getString("user"));
						    	valueList = new ContentValues[jsonArray.length()];


								for (int position = 0; position < jsonArray.length(); position++) {

									JSONObject jsonObject = null;

									try {
										ContentValues values = new ContentValues();
										jsonObject = jsonArray.getJSONObject(position);

										values.put(DatabaseHelper.KEY_ID, user_id);
										values.put(DatabaseHelper.KEY_FAVORITE_ID, favorite_id);
										values.put(DatabaseHelper.KEY_BUSLINE, jsonObject.getString("busline"));
										values.put(DatabaseHelper.KEY_BUS_STOP, jsonObject.getString("bus_stop"));
										values.put(DatabaseHelper.KEY_BUS_STOP_ID, jsonObject.getString("bus_stop_id"));
										values.put(DatabaseHelper.KEY_B_LATITUDE, jsonObject.getString("latitude"));
										values.put(DatabaseHelper.KEY_B_LONGITUDE, jsonObject.getString("longitude"));
										values.put(DatabaseHelper.KEY_M_DISTANCE_K, jsonObject.getString("max_distance_km"));
										values.put(DatabaseHelper.KEY_FLAG, jsonObject.getString("from_or_to_flag"));
										values.put(DatabaseHelper.KEY_CREATED_AT, jsonObject.getString("created_at"));

										valueList[position] = values;
										//-----------

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

								c.getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_ROUTES_insert, valueList);

								cancelAlarm(c);

						    }

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//*/
				 }
		        });

				thread.start();


	    }


}
