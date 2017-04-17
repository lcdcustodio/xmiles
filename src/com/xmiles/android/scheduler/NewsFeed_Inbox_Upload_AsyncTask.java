package com.xmiles.android.scheduler;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.ConnectionDetector;
import com.xmiles.android.webservice.UserFunctions;

public class NewsFeed_Inbox_Upload_AsyncTask extends AsyncTask<Void, Void, JSONObject> {
	
	//TAG
	private static final String TAG = "FACEBOOK";
	
	private static final Integer KEY_ID_PROFILE  = 0;
	
	private static final Integer KEY_ID 		 = 1;
	private static final Integer KEY_NAME		 = 2;
	private static final Integer KEY_IMAGE	 	 = 3;
	private static final Integer KEY_STATUS 	 = 4;
	private static final Integer KEY_PICURL		 = 5;
	private static final Integer KEY_TIME_STAMP	 = 6;
	private static final Integer KEY_URL		 = 7;
	private static final Integer KEY_LIKE_STATS  = 9;
	private static final Integer KEY_COMMENT_STATS = 10;
	private static final Integer KEY_FEED_TYPE   = 11;
	private static final Integer KEY_FLAG_ACTION = 12;
	private static final Integer KEY_HASHTAG = 13;
	
	private static String destination;
	private static String sender;
	
	// Connection detector
	ConnectionDetector cd;

	private Context context;
	
	public NewsFeed_Inbox_Upload_AsyncTask(Context ctx){
		this.context  = ctx;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// before making http calls
		//Log.e(TAG, "NewsFeed_Inbox_Upload_AsyncTask onPreExecute");

	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
		/*
		 * Will make http call here This call will download required data
		 * before launching the app
		 * example:
		 */
		//Log.i(TAG, "NewsFeed_Inbox_Upload_AsyncTask - doInBackground");		
		
		//----------------------------------------
		//--- melhor chamar aqui o servico NewsFeed_Inbox_Upload 
		//---somente em caso de falta de internet.
		//---e não o serviço acionar esse AsyncTask
		//----------------------------------------
		
		cd = new ConnectionDetector(context.getApplicationContext());

		// Check if Internet present
		if (cd.isConnectingToInternet()) {
		
			JSONObject json_result = Uploading(context);
			return json_result;
		} else{
			
			NewsFeed_Inbox_Upload no_internet = new NewsFeed_Inbox_Upload();
			no_internet.setAlarm(context);

		}
		
		return null;



	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);

		//Log.i(TAG, "NewsFeed_Inbox_Upload_AsyncTask - onPostExecute");
		// After completing http call
		// will close this activity and lauch main activity
		// close this activity
		
        try {

            if (result.getString("success") != null) {
                
                String res = result.getString("success");
                if(Integer.parseInt(res) == 1){
                	//------------------------
                	/*
                	 * Reset Newsfeed_Upload Table @ SQLite
                	 */
                	DatabaseHelper mDatabaseHelper;
                	mDatabaseHelper = new DatabaseHelper(context);
                	mDatabaseHelper.resetNewsfeed_Upload();                	

                	//------------------------
                	//Log.w(TAG, "result success");
                	
                }else {
                	//Log.e(TAG, "result failed");
                }
            } else{
            	//Log.e(TAG, "result is null");
            }
    		        	
	    } catch (Exception e) {
	            e.printStackTrace();
	    }
	


	  }

    public JSONObject Uploading(Context c){
    	
        try {
        	//-------------
            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
            Cursor data_profile = c.getContentResolver().query(uri_1, null, null, null, null);
            data_profile.moveToFirst();
        	//-------------			        	
            Uri uri_2 = SqliteProvider.CONTENT_URI_NEWSFEED_UPLOAD;
        	Cursor data_NewsFeed = c.getContentResolver().query(uri_2, null, null, null, null);
        	data_NewsFeed.moveToFirst();			        	
        	//-------------			        	
        	UserFunctions userFunc = new UserFunctions();
        	//------------------


        	if (data_NewsFeed.getString(KEY_FEED_TYPE).equals("User gets into the bus")){
        		destination = "ALL";
        		sender 		= data_profile.getString(KEY_ID_PROFILE);
        	} else{
        		destination = data_profile.getString(KEY_ID_PROFILE);
        		sender		= "x1";
        	}

        	
        	//Your code goes here	        			
        	JSONObject json = userFunc.newsFeed_inbox(data_NewsFeed.getString(KEY_ID), 
        								   data_NewsFeed.getString(KEY_NAME), 
        								   //null,
        								   data_NewsFeed.getString(KEY_IMAGE),
        								   data_NewsFeed.getString(KEY_STATUS),
        								   data_NewsFeed.getString(KEY_PICURL), 
        								   null, 					 //data_NewsFeed.getString(KEY_URL)	
        								   sender, //data_profile.getString(KEY_ID_PROFILE), 
        								   destination, //"ALL", 
        								   data_NewsFeed.getString(KEY_FEED_TYPE), //"User gets into the bus", //data_NewsFeed.getString(KEY_FEED_TYPE), 
        								   "0",						 //data_NewsFeed.getString(KEY_LIKE_STATS), 
        								   "0",						 //data_NewsFeed.getString(KEY_COMMENT_STATS), 
        								   "ADD", 					 //data_NewsFeed.getString(KEY_FLAG_ACTION),
        								   data_NewsFeed.getString(KEY_HASHTAG), 
        								   data_NewsFeed.getString(KEY_TIME_STAMP));
        	
    	
        	
        	return json;
	    } catch (Exception e) {
	            e.printStackTrace();
	    }    	
    	
		return null;
 
    }

}

