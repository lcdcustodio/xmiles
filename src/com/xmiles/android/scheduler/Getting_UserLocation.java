package com.xmiles.android.scheduler;

import java.util.Calendar;

import com.google.android.maps.GeoPoint;
import com.xmiles.android.R;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.Support;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class Getting_UserLocation extends WakefulBroadcastReceiver{

						
	private AlarmManager alarmMgr; 			// The app's AlarmManager, which provides access to the system alarm services.
	private PendingIntent alarmIntent; 		// The pending intent that is triggered when the alarm fires.
	private static String TAG = "FACEBOOK"; //TAG    
    
	
	// The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 30; // 10 seconds (360 points/hour)

	private static final Integer KEY_ID = 0;
	//private static final Integer MAX_POINTS = 720;  // 720 points / 360 = 2 HOURS
	private static final Integer MAX_POINTS = 420; 
    // GPSTracker class
	GPSTracker gps;

	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub
	 	Log.i(TAG, "Getting_Location onReceive");
	 		 			
		try {
			//DO something
			Uri uri = SqliteProvider.CONTENT_URI_USER_ROUTES_FLAG;
			Cursor data_temp_flag = ctx.getContentResolver().query(uri, null, null, null, null);
			//------------
			Log.w(TAG, "CONTENT_URI_USER_ROUTES_FLAG (count): " + data_temp_flag.getCount());		
			//------------		
			if (data_temp_flag.getCount() > 0) {
				
				GL_Handler(ctx);
				
			}

		}catch(Exception e){
			// here you can catch all the exceptions
			e.printStackTrace();
		}

	 	
	}
	
	public void setAlarm(Context context) {

		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, Getting_UserLocation.class);

		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		Calendar calendar = Calendar.getInstance();

		alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), MIN_TIME_BW_UPDATES, alarmIntent);
	}
	/**
	* Cancels the alarm.
	* @param context
	*/
	public void cancelAlarm(Context context) {

		Log.d(TAG, "Getting_Location cancelAlarm");

		//---------------
        // locationManager.removeUpdates
        gps = new GPSTracker(context);
        gps.stopUsingGPSTracker();
		//---------------
		Intent intent = new Intent(context, Getting_UserLocation.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent);
	}

    public void GL_Handler(final Context ctx){
    	
        //get Latitude/Longitude
        gps = new GPSTracker(ctx);
        //----------------------------
        // check isGPSEnabled
        gps.getLocation(0);
        if(!gps.canGetGPSLocation()) {
        	
        	gps.Notification_MSG();
        }
        //-----------------------------
        gps.getLocation(2);

        
		GeoPoint curGeoPoint = new GeoPoint(
                (int) (gps.getLatitude()  * 1E6),
                (int) (gps.getLongitude() * 1E6));

	    float Lat    = (float) (curGeoPoint.getLatitudeE6() / 1E6);
	    float Long   = (float) (curGeoPoint.getLongitudeE6() / 1E6);
	    double Speed = (gps.getSpeed()*3600)/1000;		
	    //--------------	    
	    Log.w(TAG, "Getting Loc. Latitude: " + Lat);
	    Log.w(TAG, "Getting Loc. Longitude: " + Long);
	    //--------------
	    Log.i(TAG, "getProvider(): " + gps.getProvider());
	    Log.e(TAG, "getAccuracy(): " + gps.getAccuracy());
		//------------------
        Support support = new Support();

		/** Setting up values to insert into UserProfile table */
		ContentValues contentValues = new ContentValues();

		contentValues.put(DatabaseHelper.KEY_U_LATITUDE, Lat);
		contentValues.put(DatabaseHelper.KEY_U_LONGITUDE, Long);
		contentValues.put(DatabaseHelper.KEY_SPEED, Speed);
		contentValues.put(DatabaseHelper.KEY_LOCATION_PROVIDER, gps.getProvider());
		contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());

		ctx.getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_LOCATION_insert, contentValues);
	    
		
        Uri uri = SqliteProvider.CONTENT_URI_USER_LOCATION;
    	Cursor data_userLocation = ctx.getContentResolver().query(uri, null, null, null, null);
    	data_userLocation.moveToLast();
    	//--------------
    	Log.i(TAG, "data_userLocation.getInt(KEY_ID): " + data_userLocation.getInt(KEY_ID));
    	//--------------
    	if (data_userLocation.getInt(KEY_ID) > MAX_POINTS ) {	

    		// cancel Getting Location
        	cancelAlarm(ctx);	    
        	
        	UserLocation_Upload ulu = new UserLocation_Upload();
        	ulu.setAlarm(ctx);
    		
    	}


    }
}
