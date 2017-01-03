package com.xmiles.android.support;


import com.xmiles.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;


//public class GPSTracker implements LocationListener {
//public class GPSTracker extends Service implements LocationListener {
public class GPSTracker extends Service implements LocationListener, android.location.GpsStatus.Listener {
	
	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetGPSLocation = false;
	boolean canGetNW_Location = false;

	Location location; // location
    double latitude;   // latitude
    double longitude;  // longitude
    double speed;      // speed    
    double accuracy;   // accuracy
    
    public static final int NOTIFICATION_ID = 1; // An ID used to post the notification.

    String location_provider;	

	// Declaring a Location Manager
	protected LocationManager locationManager;

	private static String TAG = "FACEBOOK";

	public GPSTracker(Context context) {
		this.mContext = context;
		
	}

	public void getLocation(int option) {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			switch(option){
			 case 0:{
					if (isGPSEnabled && isNetworkEnabled) {
						// network and GPS provider are enabled
						this.canGetGPSLocation = true;
						this.canGetNW_Location = true;

					} else if (isGPSEnabled && !isNetworkEnabled) {
						this.canGetGPSLocation = true;

					} else if (!isGPSEnabled && isNetworkEnabled) {
						this.canGetNW_Location = true;

					}

				 break;
			 }			 
			 case 2: {
					if (!isGPSEnabled && !isNetworkEnabled) {
						// no network provider is enabled
					} else {
						if (isNetworkEnabled) {
							locationManager.requestLocationUpdates(
									LocationManager.NETWORK_PROVIDER,
									0, 0, this);
							Log.d(TAG, "Network Provider");
							if (locationManager != null) {
								location = locationManager
										.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								if (location != null) {
									latitude  = location.getLatitude();
									longitude = location.getLongitude();
									speed     = location.getSpeed();
									accuracy  = location.getAccuracy();
									//----------
									location_provider = "NETWORK_PROVIDER";
									//----------									
								}
							}
						}
						// if GPS Enabled get lat/long using GPS Services
						if (isGPSEnabled) {
							//if (location == null) {
								locationManager.requestLocationUpdates(
										LocationManager.GPS_PROVIDER,
										0, 0, this);
								Log.d(TAG, "GPS Enabled");
								if (locationManager != null) {
									location = locationManager
											.getLastKnownLocation(LocationManager.GPS_PROVIDER);
									if (location != null) {
										latitude  = location.getLatitude();
										longitude = location.getLongitude();
										speed     = location.getSpeed();
										accuracy  = location.getAccuracy();
										//----------
										location_provider = "GPS_PROVIDER";
										//----------										
									} else {
										location = locationManager
												.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
										if (location != null) {
											latitude  = location.getLatitude();
											longitude = location.getLongitude();
											speed     = location.getSpeed();
											accuracy  = location.getAccuracy();
											//----------
											location_provider = "NETWORK_PROVIDER";
											//----------										
										}										
									}
								}
							//}
						}
					}

			 break;				 
			 }
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetGPSLocation() {
		return this.canGetGPSLocation;
	}

	public boolean canGetNW_Location() {
		return this.canGetNW_Location;
	}
	//------------------------------
	public double getLatitude(){
		return this.latitude;
	}

	public double getLongitude(){
		return this.longitude;
	}

	public double getSpeed(){
		return this.speed;
	}
	
	public double getAccuracy(){
		return this.accuracy;
	}


	
	public String getProvider(){
		return this.location_provider;
	}

	//------------------------------
	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPSTracker(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}		
	}	
	//------------------------------
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle(mContext.getString(R.string.location_service));
        

        alertDialog.setMessage(mContext.getString(R.string.content_notification_01));

        // On pressing Settings button
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	mContext.startActivity(intent);
            	//--------------------
                ((Activity) mContext).finish();
            	//--------------------                
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            //------------------
            ((Activity) mContext).finish();
            //------------------

            }
        });

        // Showing Alert Message
        alertDialog.show();
	}
	
	public void Notification_MSG(){
		NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

	    int icon = R.drawable.xmiles_logo_rev06;
	    CharSequence tickerText = mContext.getString(R.string.location_service);
	    long time = System.currentTimeMillis();

	    Notification notification = new Notification(icon, tickerText, time);
	    notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

	    
	    CharSequence contentTitle = mContext.getString(R.string.location_service);
	    CharSequence contentText = mContext.getString(R.string.content_notification_01);
	    
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
	    
	    
	    notification.setLatestEventInfo(mContext, contentTitle, contentText, contentIntent);
	    mNotificationManager.notify(NOTIFICATION_ID,notification);

	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
        switch(event) 
        {
            case GpsStatus.GPS_EVENT_STARTED:            	
                //Toast.makeText(mContext, "GPS_SEARCHING", Toast.LENGTH_SHORT).show();
            	Log.w(TAG,"GPS_SEARCHING");
                System.out.println("TAG - GPS searching: ");                        
                break;
            case GpsStatus.GPS_EVENT_STOPPED:    
                System.out.println("TAG - GPS Stopped");
                Log.w(TAG,"GPS Stopped");
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:

                /*
                 * GPS_EVENT_FIRST_FIX Event is called when GPS is locked            
                 */
                    //Toast.makeText(mContext, "GPS_LOCKED", Toast.LENGTH_SHORT).show();
                    Log.w(TAG,"GPS_LOCKED");
                    //Location gpslocation = locationManager
                    //        .getLastKnownLocation(LocationManager.GPS_PROVIDER);


                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
 //                 System.out.println("TAG - GPS_EVENT_SATELLITE_STATUS");
                break;                  
       }
	}

}
