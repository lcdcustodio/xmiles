package com.xmiles.android.scheduler;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.webservice.UserFunctions;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class GpsBusData_Upload_AsyncTask extends AsyncTask<Void, Void, JSONObject> {
	
	//TAG
	private static final String TAG = "FACEBOOK";
	
	protected static JSONArray jsonArray;
	//protected static JSONObject json;

    //--------------------
	
	private static final Integer KEY_ID 		 = 0;
	private static final Integer KEY_CREATED_AT  = 1;
	private static final Integer KEY_BUSCODE 	 = 2;
	private static final Integer KEY_BUSLINE 	 = 3;
	private static final Integer KEY_B_LATITUDE  = 4;
	private static final Integer KEY_B_LONGITUDE = 5;
	private static final Integer KEY_BUS_TYPE	 = 6;
	private static final Integer KEY_B_HASHCODE	 = 7;
	//-----------------------------
	private static final Integer KEY_U_ROW_ID 			 = 0;
	private static final Integer KEY_U_LATITUDE 		 = 1;
	private static final Integer KEY_U_LONGITUDE 		 = 2;
	private static final Integer KEY_U_HASHCODE 		 = 3;
	private static final Integer KEY_U_LOCATION_PROVIDER = 4;
	private static final Integer KEY_U_CREATED_AT 		 = 5;
	private static final Integer KEY_U_DIFF_DISTANCE 	 = 6;
	private static final Integer KEY_U_DIFF_TIME 		 = 7;
	private static final Integer KEY_U_STATUS   		 = 8;
	private static final Integer KEY_U_ACCURACY 		 = 9;
	//------------------------------
	private Context context;
	
	public GpsBusData_Upload_AsyncTask(Context ctx){
		this.context  = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// before making http calls
		//Log.e(TAG, "GpsBusData_Upload_AsyncTask onPreExecute");

	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
		/*
		 * Will make http call here This call will download required data
		 * before launching the app
		 * example:
		 */
		//Log.i(TAG, "GpsBusData_Upload_AsyncTask - doInBackground");
		
		
        try {
        	//-------------
            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
            Cursor data_profile = context.getContentResolver().query(uri_1, null, null, null, null);
            data_profile.moveToFirst();
        	//-------------			        	
            Uri uri_2 = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
        	Cursor data_GpsBusData = context.getContentResolver().query(uri_2, null, null, null, null);
        	//-------------			        	
            Uri uri_3 = SqliteProvider.CONTENT_URI_USER_LOCATION;
        	Cursor data_UserLocation = context.getContentResolver().query(uri_3, null, null, null, null);			        	
        	//------------			        	
        	UserFunctions userFunc = new UserFunctions();
        	//------------------
            StringBuilder gps_bus_id		 	= new StringBuilder();
            StringBuilder user_id				= new StringBuilder(); 
            StringBuilder latitude				= new StringBuilder();
            StringBuilder longitude				= new StringBuilder();
            //StringBuilder speed					= new StringBuilder();
            StringBuilder bustype				= new StringBuilder();
            StringBuilder buscode			    = new StringBuilder();
            StringBuilder busline		    	= new StringBuilder();
            //StringBuilder direction		    	= new StringBuilder();
            StringBuilder bus_hashcode		    = new StringBuilder();
            StringBuilder created_at			= new StringBuilder();
    		//---------------------			        	
            StringBuilder u_locat_id			= new StringBuilder();
            StringBuilder u_latitude			= new StringBuilder();
            StringBuilder u_longitude			= new StringBuilder();
            //StringBuilder u_speed				= new StringBuilder();
            StringBuilder u_hashcode		    = new StringBuilder();
            StringBuilder u_locat_provider		= new StringBuilder();
            StringBuilder u_created_at			= new StringBuilder();
            StringBuilder u_diff_dist			= new StringBuilder();
            StringBuilder u_diff_time			= new StringBuilder();
            //StringBuilder u_locat_status		= new StringBuilder();
            StringBuilder u_status				= new StringBuilder();
            StringBuilder u_accuracy			= new StringBuilder();
    		//---------------------
            //StringBuilder score					= new StringBuilder();

            
    		while (data_GpsBusData.moveToNext()) {

    			
    			////Log.d(TAG, "data_GpsBusData.getString(KEY_B_LATITUDE): "+ data_GpsBusData.getString(KEY_B_LATITUDE));
	        	//Your code goes here
    			
    			
	    		gps_bus_id.append(data_GpsBusData.getString(KEY_ID));
	    		user_id.append(data_profile.getString(KEY_ID));
	    		latitude.append(data_GpsBusData.getString(KEY_B_LATITUDE));
	    		longitude.append(data_GpsBusData.getString(KEY_B_LONGITUDE));
	    		//speed.append(data_GpsBusData.getString(KEY_SPEED));
	    		bustype.append(data_GpsBusData.getString(KEY_BUS_TYPE));
	    		buscode.append(data_GpsBusData.getString(KEY_BUSCODE));
	    		busline.append(data_GpsBusData.getString(KEY_BUSLINE));
	    		//direction.append(data_GpsBusData.getString(KEY_DIRECTION));
	    		bus_hashcode.append(data_GpsBusData.getString(KEY_B_HASHCODE));
	    		created_at.append(data_GpsBusData.getString(KEY_CREATED_AT));
	    		//---------------------

	    		
	    		if (!data_GpsBusData.isLast()){
	    			gps_bus_id.append(";");
		    		user_id.append(";");
		    		latitude.append(";");
		    		longitude.append(";");
		    		//speed.append(";");
		    		bustype.append(";");
		    		buscode.append(";");
		    		busline.append(";");
		    		//direction.append(";");
		    		bus_hashcode.append(";");
		    		created_at.append(";");
		    		//score.append("0.0;");
	    		}

    		}
    		
    		
    		while (data_UserLocation.moveToNext()) {
    			
    			//------------------------------------
    			u_locat_id.append(data_UserLocation.getString(KEY_U_ROW_ID)); 
    			u_latitude.append(data_UserLocation.getString(KEY_U_LATITUDE));
    			u_longitude.append(data_UserLocation.getString(KEY_U_LONGITUDE));
    			//u_speed.append(data_UserLocation.getString(KEY_U_SPEED));
    			u_hashcode.append(data_UserLocation.getString(KEY_U_HASHCODE));
    			u_locat_provider.append(data_UserLocation.getString(KEY_U_LOCATION_PROVIDER));
    			u_created_at.append(data_UserLocation.getString(KEY_U_CREATED_AT));
    			u_diff_dist.append(data_UserLocation.getString(KEY_U_DIFF_DISTANCE));
    			u_diff_time.append(data_UserLocation.getString(KEY_U_DIFF_TIME));
    			//u_locat_status.append(data_UserLocation.getString(KEY_U_LOCATION_STATUS));
    			u_status.append(data_UserLocation.getString(KEY_U_STATUS));
    			u_accuracy.append(data_UserLocation.getString(KEY_U_ACCURACY));
    			//-------------------------------------
    			if (!data_UserLocation.isLast()){

	    			u_locat_id.append(";");
	    			u_latitude.append(";");
	    			u_longitude.append(";");
	    			//u_speed.append(";");
	    			u_hashcode.append(";");
	    			u_locat_provider.append(";");
	    			u_created_at.append(";");
	    			u_diff_dist.append(";");
	    			u_diff_time.append(";");
	    			u_status.append(";");
	    			u_accuracy.append(";");
	    			
    			}
    			
    			
    		}
        	
    		
    		//Log.e(TAG,"gps_bus_id: " + gps_bus_id);
    		//Log.e(TAG,"user_id: " + user_id);
    		//Log.e(TAG,"latitude: " + latitude);
    		//Log.e(TAG,"longitude: " + longitude);
    		//Log.e(TAG,"bustype: " + bustype);			        								 
    		//Log.e(TAG,"buscode: " + buscode);
    		//Log.e(TAG,"busline: " + busline);
    		//Log.e(TAG,"bus_hashcode: " + bus_hashcode);
    		//Log.e(TAG,"created_at: " + created_at);
    		//Log.e(TAG,"u_locat_id: " + u_locat_id);
    		//Log.e(TAG,"u_latitude: " + u_latitude);
    		//Log.e(TAG,"u_longitude: " + u_longitude);
    		//Log.e(TAG,"u_hashcode: " + u_hashcode);
    		//Log.e(TAG,"u_locat_provider: " + u_locat_provider);
    		//Log.e(TAG,"u_created_at: " + u_created_at);
    		//Log.e(TAG,"u_diff_dist: " + u_diff_dist);
    		//Log.e(TAG,"u_diff_time: " + u_diff_time);
    		//Log.e(TAG,"u_status: " + u_status);
    		//Log.e(TAG,"u_accuracy: " + u_accuracy);
    		
    		
        	//Your code goes here	        			
        	JSONObject json = userFunc.busGps(gps_bus_id,
        								 user_id,
        								 latitude,
        								 longitude,
        								 //speed,
        								 bustype,			        								 
        								 buscode,
        								 busline,
        								 //direction,
        								 bus_hashcode,
        								 created_at,
        								 //----------
        								 u_locat_id,
        								 u_latitude,
        								 u_longitude,
        								 //u_speed,
        								 u_hashcode,
        								 u_locat_provider,
        								 u_created_at,
        								 u_diff_dist,
        								 u_diff_time,
        								 //u_locat_status,
        								 u_status,
        								 u_accuracy//,
        								 //----------
        								 //score
        								 //----------
        								 );
        	
        	return json;	        	
	    } catch (Exception e) {
	            e.printStackTrace();
	    }

		return null;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);

		//Log.i(TAG, "GpsBusData_Upload_AsyncTask - onPostExecute");
		// After completing http call
		// will close this activity and lauch main activity
		// close this activity
		
        try {

            if (result.getString("success") != null) {
                
                String res = result.getString("success");
                if(Integer.parseInt(res) == 1){
                	//------------------------
                	/*
                	 * Reset GpsBusData Table @ SQLite
                	 */
                	DatabaseHelper mDatabaseHelper;
                	mDatabaseHelper = new DatabaseHelper(context);
                	mDatabaseHelper.resetBusGpsData();
                	//------------------------
                	mDatabaseHelper.resetUserLocation();
                	mDatabaseHelper.resetBusGpsUrl();
                	//------------------------
                	//Log.w(TAG, "result success");
                	
                }else {
                	//Log.e(TAG, "result failed");
                }
            }
    		        	
	    } catch (Exception e) {
	            e.printStackTrace();
	    }
	


	  }



}
