package com.xmiles.android.backup;

import java.util.Calendar;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.Distance_calc;
import com.xmiles.android.support.GPSTracker;

import com.xmiles.android.R;

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
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class Scanning extends WakefulBroadcastReceiver{

	// The app's AlarmManager, which provides access to the system alarm services.
	private AlarmManager alarmMgr;
	// The pending intent that is triggered when the alarm fires.
	private PendingIntent alarmIntent;
	//TAG
	private static String TAG = "FACEBOOK";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
	
	// The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 10; // 10 seconds

    // GPSTracker class
	GPSTracker gps;
	
	//USER ROUTES Table - column names
	private static final Integer KEY_FAVORITE_ID  = 0;
	private static final Integer KEY_BUS_STOP_ID  = 1;
	private static final Integer KEY_B_LATITUDE   = 2;
	private static final Integer KEY_B_LONGITUDE  = 3;
	private static final Integer KEY_M_DISTANCE_K = 5;


    @Override
	public void onReceive(Context context, Intent intent) {

	 	Log.i(TAG, "Scanning onReceive");

		//------------
		//------------
		Uri uri = SqliteProvider.CONTENT_URI_USER_ROUTES_FLAG;
		Cursor data_temp_flag = context.getContentResolver().query(uri, null, null, null, null);
		//------------
		Log.w(TAG, "CONTENT_URI_USER_ROUTES_FLAG (count): " + data_temp_flag.getCount());		
		//------------		
		if (data_temp_flag.getCount() > 0) {
			
			Scanning_Handler(context);
			
		}

	 }
	 public void setAlarm(Context context) {

	 	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(context, Scanning.class);

	    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

	    Calendar calendar = Calendar.getInstance();

	    alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 15 * MIN_TIME_BW_UPDATES, alarmIntent);
	 }
	 /**
	  * Cancels the alarm.
	  * @param context
	  */
	 public void cancelAlarm(Context context) {

	 	Log.d(TAG, "Scanning cancelAlarm");

	    Intent intent = new Intent(context, Scanning.class);
	    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	    alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	    alarmMgr.cancel(alarmIntent);

	 }

    public void Scanning_Handler(final Context ctx){

        //get Latitude/Longitude
        //gps = new GPSTracker(ctx,1);
        gps = new GPSTracker(ctx);
        gps.getLocation(1);

		GeoPoint curGeoPoint = new GeoPoint(
                (int) (gps.getLatitude()  * 1E6),
                (int) (gps.getLongitude() * 1E6));

	    float Lat  = (float) (curGeoPoint.getLatitudeE6() / 1E6);
	    float Long = (float) (curGeoPoint.getLongitudeE6() / 1E6);
	    
	    Log.w(TAG, "Scanning Lat: " + Lat);
	    Log.w(TAG, "Scanning Long: " + Long);	    

		Distance_calc dist_calc = new Distance_calc();
		//------------
		Uri uri = SqliteProvider.CONTENT_URI_USER_ROUTES_FLAG;
		Cursor data_flag = ctx.getContentResolver().query(uri, null, null, null, null);
		//------------
		while (data_flag.moveToNext()) {

			double distance = dist_calc.calculo(data_flag.getDouble(KEY_B_LATITUDE), Lat, data_flag.getDouble(KEY_B_LONGITUDE), Long);


			//if (distance < Double.parseDouble(data_flag.getString(KEY_M_DISTANCE_K))){
			// TESTE... NECESSARIO TROCAR PARA A CONDICAO ACIMA
			if (distance < 0.5){				
				
				Log.w(TAG, "Distance " + distance + " is LOWER THAN dist_THR is " + data_flag.getString(KEY_M_DISTANCE_K));
				
				if(!gps.canGetGPSLocation()){
				
					
					NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

				    int icon = R.drawable.xmiles_logo_rev06;
				    CharSequence tickerText = ctx.getString(R.string.title_notification_01);
				    long time = System.currentTimeMillis();

				    Notification notification = new Notification(icon, tickerText, time);
				    notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

				    
				    CharSequence contentTitle = ctx.getString(R.string.title_notification_01);
				    CharSequence contentText = ctx.getString(R.string.content_notification_01);
				    
			        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
			                new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
				    
				    
				    notification.setLatestEventInfo(ctx, contentTitle, contentText, contentIntent);
				    mNotificationManager.notify(NOTIFICATION_ID,notification);
					
				} else {
					// Stop Scanning receiver
					cancelAlarm(ctx);
					
					// Start ScoreAlgorithm service
					//ctx.startService(new Intent(ctx, ScoreAlgorithm.class));					
					Intent serviceIntent = new Intent(ctx,ScoreAlgorithm.class);
					serviceIntent.putExtra("FAVORITE_ID", data_flag.getString(KEY_FAVORITE_ID));
					serviceIntent.putExtra("BUS_STOP_ID", data_flag.getString(KEY_BUS_STOP_ID));					
					
					ctx.startService(serviceIntent);
					
				}
				
			} else {
				Log.v(TAG, "Distance " + distance + " is HIGHER THAN dist_THR is " + data_flag.getString(KEY_M_DISTANCE_K));
				Log.v(TAG, "LATITUDE: "  + data_flag.getString(KEY_B_LATITUDE));
				Log.v(TAG, "Longitude: "  + data_flag.getString(KEY_B_LONGITUDE));
			}
			
		}
		
		//cancelAlarm(ctx);


	}

}
