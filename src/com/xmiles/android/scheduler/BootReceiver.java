package com.xmiles.android.scheduler;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class BootReceiver extends BroadcastReceiver {

	//TAG
	private static final String TAG = "FACEBOOK";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.v(TAG, "Boot Receiver");
		
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
			
			try {
				//DO something
				Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
				Cursor user_profile = context.getContentResolver().query(uri, null, null, null, null);
				//------------
				Log.i(TAG, "CONTENT_URI_USER_PROFILE (count): " + user_profile.getCount());		
				//------------		
				if (user_profile.getCount() > 0) {
					
					// start Scanning service
					//Scanning sc = new Scanning();			
					//sc.setAlarm(context);
					
					// start Getting_Location service
					//Getting_UserLocation gl = new Getting_UserLocation();
					//gl.setAlarm(context);
				}

			}catch(Exception e){
				// here you can catch all the exceptions
				e.printStackTrace();
			}

        }
	}

}
