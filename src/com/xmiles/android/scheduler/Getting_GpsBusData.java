package com.xmiles.android.scheduler;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.xmiles.android.R;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GetDistance;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.Score_Algorithm;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.DataRioHttpGetAsyncTask;
import com.xmiles.android.webservice.HttpGetAsyncTask;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

public class Getting_GpsBusData extends WakefulBroadcastReceiver{

						
	private AlarmManager alarmMgr; 			// The app's AlarmManager, which provides access to the system alarm services.
	private PendingIntent alarmIntent; 		// The pending intent that is triggered when the alarm fires.
	private static String TAG = "FACEBOOK"; //TAG    
    
	
	// The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 20; // 60 seconds

	private static final Integer KEY_ID = 0;
	private static final Integer KEY_URL = 2;
	//private static final Integer KEY_BUSCODE = 2;
	private static final Integer KEY_BUSCODE_URL = 1;
	
	
	private static final Integer index_STIME     = 0;
	private static final Integer index_BUSCODE   = 1;
	private static final Integer index_LATITUDE  = 3;
	private static final Integer index_LONGITUDE = 4;
	private static final Integer index_SPEED 	 = 5;
	private static final Integer index_DIRECTION = 6;
	private static final Integer index_BUSLINE	 = 2;

	private static final Integer KEY_B_LATITUDE  = 4;
	private static final Integer KEY_B_LONGITUDE = 5;
	private static final Integer KEY_SCORE		 = 8;
	private static final Integer KEY_U_DIFF_TIME = 7;
	private static final Integer KEY_U_DIFF_DISTANCE = 6;

	
	//private static final Integer MAX_POINTS = 720;  // 720 points / 360 = 2 HOURS
	//private static final Integer MAX_POINTS = 1420;
	//private static final Integer MAX_POINTS = 10;
	private static final Integer MAX_POINTS = 4;
	private static final Integer MAX_DIST 	  = 3; //3km
	private static final Integer MAX_TIME_OFFSET = 300; //300secs = 5min

	
    // GPSTracker class
	GPSTracker gps;

	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub
	 	Log.i(TAG, "GpsBusData onReceive");
	 		 			
		try {
			//DO something

			Uri uri = SqliteProvider.CONTENT_URI_BUS_GPS_URL;
			Cursor bus_gps_url = ctx.getContentResolver().query(uri, null, null, null, null);
			bus_gps_url.moveToLast();
			
			GBD_Handler(ctx, bus_gps_url.getString(KEY_URL), bus_gps_url.getString(KEY_BUSCODE_URL));
			

		}catch(Exception e){
			// here you can catch all the exceptions
			e.printStackTrace();
		}

	 	
	}
	
	public void setAlarm(Context context) {

		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, Getting_GpsBusData.class);

		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		Calendar calendar = Calendar.getInstance();

		alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), MIN_TIME_BW_UPDATES, alarmIntent);
	}
	/**
	* Cancels the alarm.
	* @param context
	*/
	public void cancelAlarm(Context context) {

		Log.d(TAG, "GpsBusData cancelAlarm");

		//---------------
        // locationManager.removeUpdates
        gps = new GPSTracker(context);
        gps.stopUsingGPSTracker();
		//---------------
		Intent intent = new Intent(context, Getting_GpsBusData.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(alarmIntent);
	}
	
    public void GBD_Handler(final Context ctx, String url, String buscode){
    	
    	String [] dataBusArray;

		try {
			
			Support support = new Support();
			Score_Algorithm sca = new Score_Algorithm(ctx);
			JSONObject json = sca.getBusPosition(url, buscode);
			
			dataBusArray = json.getString("DATA").substring(2, json.getString("DATA").length()-2).split(",");
			//--------------------
			Uri uri_2 = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
			Cursor bus_score = ctx.getContentResolver().query(uri_2, null, null, null, null);
			//--------------------
			GetDistance score = new GetDistance();
			//--------------------
			/** Setting up values to insert into UserProfile table */

			ContentValues contentValues = new ContentValues();
			contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.fixDateTime(dataBusArray[index_STIME].replace("\"","")));
			contentValues.put(DatabaseHelper.KEY_BUSCODE, dataBusArray[index_BUSCODE].replace("\"",""));
			contentValues.put(DatabaseHelper.KEY_B_LATITUDE, dataBusArray[index_LATITUDE]);
			contentValues.put(DatabaseHelper.KEY_B_LONGITUDE, dataBusArray[index_LONGITUDE]);
			
			if (bus_score.getCount()>0) {
				bus_score.moveToLast();
				Double get_score =  bus_score.getDouble(KEY_SCORE) +
									score.calculo(Double.parseDouble(dataBusArray[index_LATITUDE]), 
												 bus_score.getFloat(KEY_B_LATITUDE), 
												 Double.parseDouble(dataBusArray[index_LONGITUDE]), 
												 bus_score.getFloat(KEY_B_LONGITUDE));
				
				contentValues.put(DatabaseHelper.KEY_SCORE, get_score);
				//------------------
				Log.v(TAG, "bus_score.getDouble(KEY_SCORE): " + bus_score.getDouble(KEY_SCORE));
				Log.w(TAG, "get_score: " + get_score);
				//------------------				
			} else {
				contentValues.put(DatabaseHelper.KEY_SCORE, 0.0);
			}


			//-------------------
			contentValues.put(DatabaseHelper.KEY_SPEED, dataBusArray[index_SPEED]);
			contentValues.put(DatabaseHelper.KEY_DIRECTION, dataBusArray[index_DIRECTION]);
			contentValues.put(DatabaseHelper.KEY_BUSLINE, dataBusArray[index_BUSLINE].replace("\"",""));

			ctx.getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_DATA_insert, contentValues);

	     } catch (Exception e) {
	         throw new RuntimeException(e);
	     }
	    //----------------------------------------
		//***** Getting UserLocation *************
	    //----------------------------------------		
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
		//------------------
        Support support = new Support();

		/** Setting up values to insert into UserProfile table */
		ContentValues contentValues = new ContentValues();

		contentValues.put(DatabaseHelper.KEY_U_LATITUDE, Lat);
		contentValues.put(DatabaseHelper.KEY_U_LONGITUDE, Long);
		contentValues.put(DatabaseHelper.KEY_SPEED, Speed);
		contentValues.put(DatabaseHelper.KEY_LOCATION_PROVIDER, gps.getProvider());
		contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());

		//-----------------------------------------
		//--Evaluting Measurements (User vs. BUS)--
		//-----------------------------------------
		
  	    GetDistance dist_calc = new GetDistance();

  	    Double get_distance = dist_calc.calculo(Double.parseDouble(dataBusArray[index_LATITUDE]), Lat, Double.parseDouble(dataBusArray[index_LONGITUDE]), Long);
  	    DecimalFormat df = new DecimalFormat("##.##");
  	    
  	    String get_diff_time = support.DiffTime(support.getDateTime().split(" ")[1], 
  	    		support.fixDateTime(dataBusArray[index_STIME].replace("\"","")).split(" ")[1]);

  	    contentValues.put(DatabaseHelper.KEY_DIFF_DISTANCE, get_distance);
  	    contentValues.put(DatabaseHelper.KEY_DIFF_TIME, Double.parseDouble(get_diff_time));
		contentValues.put(DatabaseHelper.KEY_ACCURACY, gps.getAccuracy());
  	    
		
		ctx.getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_LOCATION_insert, contentValues);
  	    //-----------------------------------------
  	    //-----------------------------------------
		Uri uri = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
		Cursor bus_gps_data = ctx.getContentResolver().query(uri, null, null, null, null);
		bus_gps_data.moveToLast();
		
        Uri uri_3 = SqliteProvider.CONTENT_URI_USER_LOCATION;
   	    Cursor data_UserLocation = ctx.getContentResolver().query(uri_3, null, null, null, null);			        	
   	    data_UserLocation.moveToLast();
   	    
		//-----------------------------
    	Intent intent=new Intent("profilefragmentupdater");
    	ctx.sendBroadcast(intent);
		//-----------------------------
	    Log.e(TAG, "bus_gps_data.getInt(KEY_ID): " + bus_gps_data.getInt(KEY_ID));

		//if (data_UserLocation.getDouble(KEY_U_DIFF_DISTANCE) >= MAX_DIST ||
		//		 data_UserLocation.getDouble(KEY_U_DIFF_TIME) >= MAX_TIME_OFFSET) {

	    
    	if (bus_gps_data.getInt(KEY_ID) > MAX_POINTS ) {	
			Toast.makeText(ctx, "Você não está mais conectado!", Toast.LENGTH_LONG).show();
    		// cancel Getting GpsBusData
        	cancelAlarm(ctx);

        	GpsBusData_Upload gbd = new GpsBusData_Upload();
        	gbd.setAlarm(ctx);
    		
    	}
    	

    }
    
}
