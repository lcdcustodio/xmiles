package com.xmiles.android.scheduler;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.xmiles.android.R;
import com.xmiles.android.backup.DataRioHttpGetAsyncTask;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GetDistance;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.Score_Algorithm;
import com.xmiles.android.support.Support;
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
    private static final long MIN_TIME_BW_UPDATES = 1000 * 3 * 60; // 3 min

	private static final Integer KEY_ID = 0;
	//private static final Integer KEY_URL = 2;
	private static final Integer KEY_FLAG = 3;
	//private static final Integer KEY_BUSCODE = 2;
	private static final Integer KEY_BUSCODE_URL = 1;
	private static final Integer KEY_HASHCODE = 4;
	private static final Integer KEY_BUS_TYPE = 2;


	private static final Integer KEY_B_LATITUDE  = 4;
	private static final Integer KEY_B_LONGITUDE = 5;
	private static final Integer KEY_SCORE		 = 8;
	private static final Integer KEY_U_DIFF_TIME = 7;
	private static final Integer KEY_U_DIFF_DISTANCE = 6;

	

	//private static final Integer MAX_POINTS = 4;
	//# times criteria (dist > MAX_POINTS || time > MAX_TIME_OFFSET) is not fulfil
	private static final Integer MAX_POINTS = 1;  
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
			
			//GBD_Handler(ctx, bus_gps_url.getString(KEY_BUSCODE_URL), bus_gps_url.getInt(KEY_FLAG),bus_gps_url.getString(KEY_BUS_TYPE),bus_gps_url.getString(KEY_HASHCODE));
			GBD_Handler(ctx, bus_gps_url.getString(KEY_BUSCODE_URL), bus_gps_url.getInt(KEY_FLAG),bus_gps_url.getString(KEY_HASHCODE));
			

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
	
    public void GBD_Handler(final Context ctx, String buscode, int flag, String hashcode){
    	
    	
    	JSONArray jArray;

		try {
			
			
			Score_Algorithm sca = new Score_Algorithm(ctx);
			JSONObject json = sca.getApiBusPosition(buscode, buscode);
			
			jArray = new JSONArray(json.getString("api_buscode"));
			
			/** Setting up values to insert into UserProfile table */

			ContentValues contentValues = new ContentValues();
			contentValues.put(DatabaseHelper.KEY_CREATED_AT, jArray.getJSONObject(0).getString("time_stamp"));
			contentValues.put(DatabaseHelper.KEY_BUSCODE, jArray.getJSONObject(0).getString("buscode"));
			contentValues.put(DatabaseHelper.KEY_B_LATITUDE, jArray.getJSONObject(0).getString("latitude"));
			contentValues.put(DatabaseHelper.KEY_B_LONGITUDE, jArray.getJSONObject(0).getString("longitude"));

			contentValues.put(DatabaseHelper.KEY_BUSLINE, jArray.getJSONObject(0).getString("busline"));
			
			contentValues.put(DatabaseHelper.KEY_BUS_TYPE, jArray.getJSONObject(0).getString("bus_type"));
			contentValues.put(DatabaseHelper.KEY_HASHCODE, jArray.getJSONObject(0).getString("hashcode"));

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

	    double Lat    = (double) (curGeoPoint.getLatitudeE6() / 1E6);
	    double Long   = (double) (curGeoPoint.getLongitudeE6() / 1E6);
	  	
	    //--------------	    
		//------------------
        Support support = new Support();

		/** Setting up values to insert into UserProfile table */
		ContentValues contentValues = new ContentValues();

		contentValues.put(DatabaseHelper.KEY_U_LATITUDE, Lat);
		contentValues.put(DatabaseHelper.KEY_U_LONGITUDE, Long);
		//contentValues.put(DatabaseHelper.KEY_SPEED, Speed);
		contentValues.put(DatabaseHelper.KEY_HASHCODE, hashcode);
		contentValues.put(DatabaseHelper.KEY_LOCATION_PROVIDER, gps.getProvider());
		contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());

		//-----------------------------------------
		//--Evaluting Measurements (User vs. BUS)--
		//-----------------------------------------
		
  	    GetDistance dist_calc = new GetDistance();

  	    Double get_distance = null;
  	    String get_diff_time = null;

		try {
			get_distance = dist_calc.calculo(Double.parseDouble(jArray.getJSONObject(0).getString("latitude")), Lat, Double.parseDouble(jArray.getJSONObject(0).getString("longitude")), Long);
			
			get_diff_time = support.DiffTime(support.getDateTime().split(" ")[1], 
					jArray.getJSONObject(0).getString("time_stamp").split(" ")[1]);

			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  	    contentValues.put(DatabaseHelper.KEY_DIFF_DISTANCE, get_distance);
  	    contentValues.put(DatabaseHelper.KEY_DIFF_TIME, Math.abs(Double.parseDouble(get_diff_time)));
		contentValues.put(DatabaseHelper.KEY_ACCURACY, gps.getAccuracy());
		
		
		
		if (get_distance >= MAX_DIST || Math.abs(Double.parseDouble(get_diff_time)) >= MAX_TIME_OFFSET) {

	   		ContentValues cV = new ContentValues();
	   		cV.put(DatabaseHelper.KEY_BUSCODE, buscode);
	   		cV.put(DatabaseHelper.KEY_HASHCODE, hashcode);
	   		try {
					cV.put(DatabaseHelper.KEY_BUS_TYPE, jArray.getJSONObject(0).getString("bus_type"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	   		cV.put(DatabaseHelper.KEY_FLAG, flag + 1);
	   		
	   		Log.w(TAG,"flag: " + flag);
	   		//----------------------------								
	   		ctx.getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_URL_insert, cV);
	    
			if (flag + 1 > MAX_POINTS ) {
				Toast.makeText(ctx, "Você não está mais conectado!", Toast.LENGTH_LONG).show();
				
				contentValues.put(DatabaseHelper.KEY_STATUS, "STOP");
				ctx.getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_LOCATION_insert, contentValues);
				
	    		// cancel Getting GpsBusData
	        	cancelAlarm(ctx);
	
	        	GpsBusData_Upload gbd = new GpsBusData_Upload();
	        	gbd.setAlarm(ctx);	    		
	    	} else {
		 
			  //need to add inside else condition in order to avoid repetition
			  contentValues.put(DatabaseHelper.KEY_STATUS, "MEASURING");
			  ctx.getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_LOCATION_insert, contentValues);
		
	    	}
		
		} else {
		  
		  contentValues.put(DatabaseHelper.KEY_STATUS, "MEASURING");
		  ctx.getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_LOCATION_insert, contentValues);
	
			
		}
  	    //-----------------------------------------
  	    //-----------------------------------------
		//Uri uri = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
		//Cursor bus_gps_data = ctx.getContentResolver().query(uri, null, null, null, null);
		//bus_gps_data.moveToLast();
		
        //Uri uri_3 = SqliteProvider.CONTENT_URI_USER_LOCATION;
   	    //Cursor data_UserLocation = ctx.getContentResolver().query(uri_3, null, null, null, null);			        	
   	    //data_UserLocation.moveToLast();
   	    
	
	    //Log.e(TAG, "bus_gps_data.getInt(KEY_ID): " + bus_gps_data.getInt(KEY_ID));

    }
    
}
