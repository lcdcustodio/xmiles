package com.xmiles.android.scheduler;

import com.google.android.maps.GeoPoint;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ScoreAlgorithm extends Service implements LocationListener {

	//TAG
	private static String TAG = "FACEBOOK";
	
	LocationManager lm; //LocationManager
    double latitude; // latitude
    double longitude; // longitude
    double speed;     //speed
    
    String favorite_id;
    String bus_stop_id;    
	
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 2 * 1; // 2 seconds

	
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//--------------
		Log.w(TAG, "ScoreAlgorithm onStart");
		//--------------		
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    
		lm.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MIN_TIME_BW_UPDATES,
				MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

	    // Example: if(time >= [60000 milisec]minTime OR dist >= [500 m] minDistance) 
	    // 			then check for location updates
	    
		//lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 500, this);
	    //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
		Bundle extras = intent.getExtras();
		favorite_id = (String) extras.get("FAVORITE_ID");
		bus_stop_id = (String) extras.get("BUS_STOP_ID");		
		
		Log.d(TAG, "favorite_id: " + favorite_id);
		Log.i(TAG, "bus_stop_id:" + bus_stop_id);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		Log.d(TAG, "ScoreAlgorithm onLocationChanged");
		
	    GeoPoint curGeoPoint = new GeoPoint(
                (int) (location.getLatitude() * 1E6),
                (int) (location.getLongitude() * 1E6));

	    latitude = (float) (curGeoPoint.getLatitudeE6() / 1E6);
        longitude = (float) (curGeoPoint.getLongitudeE6() / 1E6);
        
        speed = (location.getSpeed()*3600)/1000;
        
		Toast.makeText(getApplicationContext(), "Current speed: " + speed,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d(TAG, "ScoreAlgorithm onProviderEnabled");
		
		lm.requestLocationUpdates(
				LocationManager.GPS_PROVIDER,
				MIN_TIME_BW_UPDATES,
				MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
