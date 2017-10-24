package com.xmiles.android.scheduler;



import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.Support;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class PowerConnected extends BroadcastReceiver {

	//TAG
	private static final String TAG = "FACEBOOK";
	
	private static final Integer KEY_BUSCODE  = 1;
	private static final Integer KEY_BUS_TYPE = 2;
	
	private static final Integer KEY_ID_PROFILE  = 0;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;

	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		//Log.v(TAG, "Boot Receiver");
		
		if (intent.getAction().equals("android.intent.action.POWER_CONNECTED")){
			
			try {
				//DO something
				
	            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
	            Cursor data_profile = context.getContentResolver().query(uri_1, null, null, null, null);
	            data_profile.moveToFirst();
	        	//-------------			        	


	    		//------------		
				if (data_profile.getCount() > 0 ) {
					
					
					String bus_type = bus_gps_url.getString(KEY_BUS_TYPE);

				    if (bus_type.equals("BUS")){
				    	bus_type = "ônibus";
					}

					String status = "O seu telefone foi desligado ou ficou sem bateria durante <bold>a conexão com o  " + bus_type + " " + bus_gps_url.getString(KEY_BUSCODE) + " <bold>." +
									" Logo os pontos acumulados dessa rota foram descartados.";

					
					DatabaseHelper mDatabaseHelper;
					mDatabaseHelper = new DatabaseHelper(context);
					//------------------------
					mDatabaseHelper.resetBusGpsData();			
					mDatabaseHelper.resetUserLocation();
					mDatabaseHelper.resetBusGpsUrl();
					//------------------------
					Support support = new Support();
					
					ContentValues contentValues = new ContentValues();

					contentValues.put(DatabaseHelper.KEY_ID, "-1");
					contentValues.put(DatabaseHelper.KEY_NAME, data_profile.getString(KEY_NAME));

					contentValues.put(DatabaseHelper.KEY_STATUS, status);

					contentValues.put(DatabaseHelper.KEY_PICURL, data_profile.getString(KEY_PICURL));
					contentValues.put(DatabaseHelper.KEY_TIME_STAMP, support.getDateTime());

					contentValues.put(DatabaseHelper.KEY_IMAGE, "");
					
					//feed_type
					contentValues.put(DatabaseHelper.KEY_FEED_TYPE, "Users score issue - power");
					
					context.getContentResolver().insert(SqliteProvider.CONTENT_URI_NEWSFEED_UPLOAD_insert, contentValues);
					//--------
					NewsFeed_Inbox_Upload nfi = new NewsFeed_Inbox_Upload();
					nfi.setAlarm(context);
					//--------
					
				}

			}catch(Exception e){
				// here you can catch all the exceptions
				e.printStackTrace();
			}

        }
	}

}
