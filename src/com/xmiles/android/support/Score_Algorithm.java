package com.xmiles.android.support;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xmiles.android.R;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.webservice.ApiBusGetAsyncTask;
import com.xmiles.android.webservice.HttpGetAsyncTask;
import com.xmiles.android.webservice.UserFunctions;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class Score_Algorithm {

	private final Context mContext;	
	private static final String TAG = "FACEBOOK";
	
	private static final Integer KEY_USER_ID 	= 0;
	private static final Integer KEY_NAME 		= 1;
	
	public Score_Algorithm(Context ctx) {
		this.mContext = ctx;		

		
	}


    public JSONObject getApiBusPosition(String buscode_digits, String buscode) {


    	JSONObject json;

    	try {
    		
    		String[] params= {buscode_digits, buscode};
    		
    		json = new ApiBusGetAsyncTask().execute(params).get();
	     } catch (Exception e) {
	         throw new RuntimeException(e);
	     }

    	return json;
    }    
 
    
    public void GpsNotFound(final String url_gnf, final String buscode_gnf) {
    	
        Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
        final Cursor dp = mContext.getContentResolver().query(uri_1, null, null, null, null);
        dp.moveToFirst();
        
        final Support support_gnf = new Support();
        //------------------------------    	
    	Thread thread = new Thread(new Runnable(){
    		@Override
    		public void run() {
    		    try {

    		    	//Your code goes here
    		    	//------------			        	
    		    	UserFunctions userFunc = new UserFunctions();
    		    	//------------------
    		    	userFunc.gps_Notfound(dp.getString(KEY_USER_ID), 
    		    						  dp.getString(KEY_NAME), 
    		    					      url_gnf, 
    		    					      buscode_gnf,
    		    					      support_gnf.getDateTime());

    		    
    				        	
    		    } catch (Exception e) {
    		            e.printStackTrace();
    		    }
    		}
    	});
        //------------------------------
    	thread.start();
        //------------------------------

    }
}
