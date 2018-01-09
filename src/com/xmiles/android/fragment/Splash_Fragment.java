package com.xmiles.android.fragment;




import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.android.maps.GeoPoint;
import com.xmiles.android.MainActivity;

import com.xmiles.android.R;

import com.xmiles.android.facebook_api_support.GetFacebookFriends;
import com.xmiles.android.facebook_api_support.GetFacebookProfile;
import com.xmiles.android.facebook_api_support.Utility;



import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.GetDeviceName;
import com.xmiles.android.support.GetUberPackageInfo;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.UserFunctions;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



public class Splash_Fragment extends Fragment {

	//TAG
	private static final String TAG = "FACEBOOK";

	JSONObject facebook_profile;
	
	JSONObject facebook_friends;

	private static final Integer KEY_USER_ID 	= 0;
	private static final Integer KEY_NAME 		= 1;
	private static final Integer KEY_PICTURE    = 2;
	private static final Integer KEY_CREATED_AT = 5;
	
	private static final Integer KEY_LAST_UPDATE = 3;
	
	
	private static String user_id;
	private static String user_name;
	private static String picurl;
	private static String gender;
	private static String time_stamp;
	private static boolean flag_picurl;
	
	private static StringBuilder fb_friends_id;
	private static boolean flag_fb_friends = false;

	public Splash_Fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.splash_fragment, container,
				false);
		

		new PrefetchData().execute();


		return rootView;

	}


	/*
	 * Async Task to make http call
	 */
	private class PrefetchData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// before making http calls
			//Log.e(TAG, "Pre execute");

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			/*
			 * Will make http call here This call will download required data
			 * before launching the app
			 * example:
			 */
			//Log.i(TAG, "Splash_Fragment - doInBackground");
			//------
			Support support = new Support();
			
			Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
			Cursor data_profile = getActivity().getApplicationContext().getContentResolver().query(uri_1, null, null, null, null);
			data_profile.moveToFirst();
			//-----
			long delta_days;
			//-----			
			try {
				
				long diff = System.currentTimeMillis() -
						Long.parseLong(support.getDateTime_long(data_profile.getString(KEY_CREATED_AT))); 
				
				delta_days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

				
			} catch (Exception e) {  	
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				delta_days = 10;
		    }


			//Log.v(TAG, "delta_days: " + delta_days);


			//if (data_profile.getCount() > 0 && delta_days < 0){
			if (data_profile.getCount() > 0 && delta_days < 5){	
			

				//Log.d(TAG, "facebook profile from SQLite");
				
				user_id   = data_profile.getString(KEY_USER_ID);
				user_name = data_profile.getString(KEY_NAME);				
				picurl 	  = data_profile.getString(KEY_PICTURE);
				gender	  = "masc";
				time_stamp = data_profile.getString(KEY_CREATED_AT);
				flag_picurl = true;
				
			} else {

				//Log.d(TAG, "facebook profile Facebook SDK");
				
				// facebook profile
				facebook_profile = new GetFacebookProfile().GetResult(Utility.mFacebook.getAccessToken());
				//Log.i(TAG, "facebook_profile: " + facebook_profile);
				
				// facebook friends
				facebook_friends = new GetFacebookFriends().GetResult(Utility.mFacebook.getAccessToken());
				//Log.e(TAG, "facebook_friends: " + facebook_friends);
				
				try {
				
					user_id   = facebook_profile.getString("id");
					user_name = facebook_profile.getString("name");							    
					picurl	  = facebook_profile.optJSONObject("picture").optJSONObject("data").getString("url");					
					gender	  = facebook_profile.getString("gender");
					time_stamp = support.getDateTime();
					flag_picurl = false;
					
					////Log.w(TAG, "picurl: " + picurl);					
					
					//--------------------------------------
					//NECESSÁRIO TESTAR COM PERFIL DO KUKI QUE NÃO POSSUI AMIGOS DO FB NO XMILES
					//--------------------------------------
					
					JSONArray friends = facebook_friends.getJSONArray("data");
					fb_friends_id = new StringBuilder();
					
					for (int i = 0; i< friends.length(); i++) {
						
						fb_friends_id.append(friends.getJSONObject(i).getString("id"));
						flag_fb_friends = true;
						
						if (i < friends.length() - 1) {
							fb_friends_id.append(";");
						}
						
					}
					//Log.e(TAG, "fb_friends_id: " + fb_friends_id);
					
				
				} catch (JSONException e) {
					//} catch (Exception e) {  	
						// TODO Auto-generated catch block
						e.printStackTrace();
				}

					
				
			}
			//------------------


			/** Setting up values to insert into UserProfile table */
			ContentValues contentValues = new ContentValues();

			try {
				contentValues.put(DatabaseHelper.KEY_ID, user_id);
				contentValues.put(DatabaseHelper.KEY_NAME, user_name);
				contentValues.put(DatabaseHelper.KEY_CREATED_AT, time_stamp);
				//-------------
				//
				//String lala = new GetUberPackageInfo(getActivity().getApplicationContext()).getUberPackageInfo();
				//-------------

				JSONObject json_login = xMiles_Login(user_name,
										user_id,
							 			gender,
							 			picurl,
							 			new GetDeviceName().getDeviceName(), //);
							 			Integer.toString(android.os.Build.VERSION.SDK_INT),
							 			support.getAppversionName(getActivity().getApplicationContext()),
							 			support.getAppversionCode(getActivity().getApplicationContext()),
							 			"login",
							 			new GetUberPackageInfo(getActivity().getApplicationContext()).getUberPackageInfo());
				
				

				try{
					contentValues.put(DatabaseHelper.KEY_SCORE, new JSONObject(json_login.getString("user")).getString("score"));
					contentValues.put(DatabaseHelper.KEY_RANK, new JSONObject(json_login.getString("user")).getString("rnk"));
					
				}catch (JSONException e) {
					//} catch (Exception e) {  	
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if (flag_picurl) {
					contentValues.put(DatabaseHelper.KEY_PICTURE, new JSONObject(json_login.getString("user")).getString("picurl"));
				} else {
					contentValues.put(DatabaseHelper.KEY_PICTURE, picurl);
				}
				
				//-------------				
				/*
				 * If Login success = 1 then GET Blabla and later
				 */
                if(Integer.parseInt(json_login.getString("success")) == 1){
                	
                	//NEWSFEED
                	xMiles_getNewsfeed(user_id);
                	
        			Uri uri_2 = SqliteProvider.CONTENT_URI_HISTORY;
        			Cursor data_history = getActivity().getApplicationContext().getContentResolver().query(uri_2, null, null, null, null);
        			data_history.moveToFirst();
        			//-----
        			//-----
        			long delta_hours;
        			//-----			
        			try {
        				
        				long diff = System.currentTimeMillis() -
        						Long.parseLong(support.getDateTime_long(data_history.getString(KEY_LAST_UPDATE))); 
        				
        				delta_hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);


        				
        			} catch (Exception e) {  	
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        				
        				delta_hours = 20;
        		    }
        			
        			//Log.e(TAG, "delta_hours: " +  delta_hours);
        			
        			if (delta_hours > 14){

	                	//RANKING
	                	xMiles_getRanking();	
	                	
	                	//HISTORY
	                	xMiles_getHistory(user_id);
        			}
                	//Upload Friends
                	if (flag_fb_friends) {
                		xMiles_upFriends(user_id);
                	}


                }
			} catch (JSONException e) {
			//} catch (Exception e) {  	
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_PROFILE_create, contentValues);


			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			//Log.i(TAG, "Splash_Fragment - onPostExecute");
			// After completing http call
			// will close this activity and lauch main activity
			// close this activity


            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

			getActivity().finish();

		  }


	}

	//public JSONObject xMiles_Login(String name,String id, String gender, String picURL, String device,	
	//		String android_api, String AppversionName, String AppversionCode, String access_type) {	

	public JSONObject xMiles_Login(String name,String id, String gender, String picURL, String device,	
			String android_api, String AppversionName, String AppversionCode, String access_type, String uber_package) {	
	
	
		UserFunctions userFunction = new UserFunctions();        
		//JSONObject json = userFunction.loginUser(id, name, picURL, device, android_api, AppversionName, AppversionCode, access_type);
        //JSONObject json = userFunction.loginUser(id, name, device, android_api, AppversionName, AppversionCode, access_type);
		JSONObject json = userFunction.loginUser(id, name, device, android_api, AppversionName, AppversionCode, access_type, uber_package);    		
        // check for login response
        try {
            if (json.getString("success") != null) {

                String res = json.getString("success");
                if(Integer.parseInt(res) != 1){
                	userFunction.registerUser(name, id, gender, picURL);
                	
                	
			         //NEWSFEED
			         xMiles_getNewsfeed(id);				
			
			         //RANKING
			         xMiles_getRanking();				
			         
	                 //HISTORY
	                 xMiles_getHistory(id);
			         
			         
	                 //Upload Friends
	                 if (flag_fb_friends) {
	                	 xMiles_upFriends(id);
	                 }


                }
            }
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return null;

    }

    public void xMiles_upFriends(String user_id){
    	
    	UserFunctions userFunc = new UserFunctions();
    	userFunc.uploadFriends(user_id, fb_friends_id);
    	
    }
    
    public void xMiles_getNewsfeed(String user_id){
    	
		//Your code goes here
    	//------------
    	ContentValues[] valueList;
    	JSONArray jsonArray;
    	//-----------
		UserFunctions userFunc = new UserFunctions();
		//JSONObject json = userFunc.getNewsfeed();
		JSONObject json = userFunc.getNewsfeed(user_id);
    	
        try {

        	if (json.getString("success") != null) {

			    String res = json.getString("success");
			    if(Integer.parseInt(res) == 1){
			    	jsonArray = new JSONArray(json.getString("feed"));
			    	valueList = new ContentValues[jsonArray.length()];

					for (int position = 0; position < jsonArray.length(); position++) {

						JSONObject jsonObject = null;

						try {
							ContentValues values = new ContentValues();
							jsonObject = jsonArray.getJSONObject(position);

							values.put(DatabaseHelper.KEY_ID, jsonObject.getString("id"));
							values.put(DatabaseHelper.KEY_NAME, jsonObject.getString("name"));
							
							// Image might be null sometimes
							String image = jsonObject.isNull("image") ? null : jsonObject
									.getString("image");
							
							values.put(DatabaseHelper.KEY_IMAGE, image);
							
							values.put(DatabaseHelper.KEY_STATUS, jsonObject.getString("status"));
							values.put(DatabaseHelper.KEY_PICURL, jsonObject.getString("profilepic"));
							values.put(DatabaseHelper.KEY_TIME_STAMP, jsonObject.getString("time_stamp"));

							// custom_time_stamp might be null sometimes							
							String custom_time_stamp = jsonObject.isNull("custom_time_stamp") ? null : jsonObject
									.getString("custom_time_stamp");							
							values.put(DatabaseHelper.KEY_CUSTOM_TIME_STAMP, custom_time_stamp);
							
							
							// url might be null sometimes
							String feedUrl = jsonObject.isNull("url") ? null : jsonObject
									.getString("url");							
							
							values.put(DatabaseHelper.KEY_URL, feedUrl);
							
							// hashtag might be null sometimes
							String hashtag = jsonObject.isNull("hashtag") ? null : jsonObject
									.getString("hashtag");							
							
							values.put(DatabaseHelper.KEY_HASHTAG, hashtag);							
							
							// like, comments stats
							values.put(DatabaseHelper.KEY_LIKE_STATS, jsonObject.getString("like_stats"));
							values.put(DatabaseHelper.KEY_COMMENT_STATS, jsonObject.getString("comment_stats"));

							//you like this
							values.put(DatabaseHelper.KEY_YOU_LIKE_THIS, jsonObject.getString("you_like_this"));
							
							//sender
							values.put(DatabaseHelper.KEY_SENDER, jsonObject.getString("sender"));
							
							//feed_type
							values.put(DatabaseHelper.KEY_FEED_TYPE, jsonObject.getString("feed_type"));
							
							valueList[position] = values;


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_NEWSFEED_create, valueList);

			    }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public void xMiles_getRanking() {
		//Your code goes here
    	//------------
    	ContentValues[] valueList;
    	JSONArray jsonArray;
    	//-----------
		UserFunctions userFunc = new UserFunctions();
		JSONObject json = userFunc.getRanking();

        try {

        	if (json.getString("success") != null) {

			    String res = json.getString("success");
			    if(Integer.parseInt(res) == 1){

			    	jsonArray = new JSONArray(json.getString("ranking"));
			    	valueList = new ContentValues[jsonArray.length()];

					for (int position = 0; position < jsonArray.length(); position++) {

						JSONObject jsonObject = null;

						try {
							ContentValues values = new ContentValues();
							jsonObject = jsonArray.getJSONObject(position);

							values.put(DatabaseHelper.KEY_ID, jsonObject.getString("user_id"));
							values.put(DatabaseHelper.KEY_NAME, jsonObject.getString("name"));
							values.put(DatabaseHelper.KEY_SCORE, jsonObject.getString("score"));
							values.put(DatabaseHelper.KEY_PICURL, jsonObject.getString("picurl"));
							values.put(DatabaseHelper.KEY_CREATED_AT, jsonObject.getString("last_update_at"));
							
							valueList[position] = values;


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_RANKING_create, valueList);

			    }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	//return json;
    }

    public void xMiles_getHistory(String user_id) {
		//Your code goes here
    	//------------
    	ContentValues[] valueList;
    	JSONArray jsonArray;
    	//-----------
		UserFunctions userFunc = new UserFunctions();
		String hashtag = "#histórico_pontuação";
		JSONObject json = userFunc.getNewsfeed_by_hashtag(user_id,hashtag);

        try {

        	if (json.getString("success") != null) {

			    String res = json.getString("success");
			    if(Integer.parseInt(res) == 1){

			    	jsonArray = new JSONArray(json.getString("feed"));
			    	valueList = new ContentValues[jsonArray.length()];
			    	
			    	//Log.i(TAG, "getHistory: " + jsonArray.getJSONObject(0).getString("status"));
			    	
					for (int position = 0; position < jsonArray.length(); position++) {

						JSONObject jsonObject = null;

						try {
							ContentValues values = new ContentValues();
							jsonObject = jsonArray.getJSONObject(position);

							values.put(DatabaseHelper.KEY_ID, jsonObject.getString("id"));
							values.put(DatabaseHelper.KEY_STATUS, jsonObject.getString("status"));
							values.put(DatabaseHelper.KEY_TIME_STAMP, jsonObject.getString("time_stamp"));
							
							Support support = new Support();							
							
							values.put(DatabaseHelper.KEY_LAST_UPDATE, support.getDateTime());
							
							valueList[position] = values;


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_HISTORY_create, valueList);
					

			    }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	//return json;
    }
    
}
