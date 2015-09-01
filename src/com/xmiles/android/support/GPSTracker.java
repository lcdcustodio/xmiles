package com.xmiles.android.support;

import com.xmiles.android.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

//public class GPSTracker implements LocationListener {
public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetGPSLocation = false;
	boolean canGetNW_Location = false;

	Location location; // location
    double latitude; // latitude
    double longitude; // longitude


	// Declaring a Location Manager
	protected LocationManager locationManager;

	private static String TAG = "FACEBOOK";

	public GPSTracker(Context context, int type) {
		this.mContext = context;
		getLocation(type);
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
			 case 1: {
		            if (!isGPSEnabled && !isNetworkEnabled) {
		                // no network provider is enabled
		            } else {

		                // if GPS Enabled get lat/long using GPS Services
		                if (isGPSEnabled) {
		                    if (location == null) {
		                    	locationManager.requestLocationUpdates(
		                                LocationManager.GPS_PROVIDER,
		                                0, 0, this);

		                    	Log.d(TAG, "GPS Enabled");

		                        if (locationManager != null) {
		                            location = locationManager
		                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
		                            if (location != null) {
		                                latitude = location.getLatitude();
		                                longitude = location.getLongitude();
		                            }
		                        }
		                    }
		                }    
		                // 2nd get location from Network Provider
		                //if (isNetworkEnabled) {
		                else if (isNetworkEnabled) {
		                	locationManager.requestLocationUpdates(
		                            LocationManager.NETWORK_PROVIDER,
		                            0, 0, this);

		                    Log.d(TAG, "Network");

		                    if (locationManager != null) {
		                        location = locationManager
		                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		                        if (location != null) {
		                            latitude = location.getLatitude();
		                            longitude = location.getLongitude();
		                        }
		                    }
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
	//------------------------------
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("xMiles Alerta:");

        // Setting Dialog Message
        alertDialog.setMessage("Serviço de Localização não está habilitado. Necessário ativá-lo.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            //------------------
            //Intent intent = new Intent(mContext, MainActivity.class);
            //mContext.startActivity(intent);
            ((Activity) mContext).finish();
            //------------------

            }
        });

        // Showing Alert Message
        alertDialog.show();
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

}
