package com.xmiles.android.fragment;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.xmiles.android.MainActivity;

import com.xmiles.android.R;

import com.xmiles.android.facebook_api_support.GetFacebookProfile;
import com.xmiles.android.facebook_api_support.Utility;



import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.GetDeviceName;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.UserFunctions;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Splash_Fragment extends Fragment {

	//TAG
	private static final String TAG = "FACEBOOK";

	JSONObject facebook_profile;

	
	
    // GPSTracker class
	GPSTracker gps;
	
	float Lat;
	float Long;

	private Handler mHandler;

	public Splash_Fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.splash, container,
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
			Log.e(TAG, "Pre execute");

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			/*
			 * Will make http call here This call will download required data
			 * before launching the app
			 * example:
			 */
			Log.i(TAG, "Splash_Fragment - doInBackground");
			//------

			// facebook profile
			facebook_profile = new GetFacebookProfile().GetResult(Utility.mFacebook.getAccessToken());
			//Log.i(TAG, "facebook_profile: " + facebook_profile);

			//------------------
            Support support = new Support();

			/** Setting up values to insert into UserProfile table */
			ContentValues contentValues = new ContentValues();

			try {
				contentValues.put(DatabaseHelper.KEY_ID, facebook_profile.getString("id"));
				contentValues.put(DatabaseHelper.KEY_NAME, facebook_profile.getString("name"));
				contentValues.put(DatabaseHelper.KEY_PICTURE, facebook_profile.optJSONObject("picture").optJSONObject("data").getString("url"));
				contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());
				//-------------
            	//REWARDS
            	xMiles_getRewards();				
            	
				//-------------				
				JSONObject json_login = xMiles_Login(facebook_profile.getString("name"),
							 			facebook_profile.getString("id"),
							 			facebook_profile.getString("gender"),
							 			facebook_profile.optJSONObject("picture").optJSONObject("data").getString("url"),
							 			new GetDeviceName().getDeviceName());
				
				contentValues.put(DatabaseHelper.KEY_SCORE, new JSONObject(json_login.getString("user")).getString("score"));
				contentValues.put(DatabaseHelper.KEY_RANK, new JSONObject(json_login.getString("user")).getString("rnk"));
				//-------------				
				/*
				 * If Login success = 1 then GET Blabla and later
				 */
                if(Integer.parseInt(json_login.getString("success")) == 1){
                	
                	//NEWSFEED
                	xMiles_getNewsfeed(facebook_profile.getString("id"));				

                	//RANKING
                	xMiles_getRanking();				


                }



			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_PROFILE_create, contentValues);


			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Log.i(TAG, "Splash_Fragment - onPostExecute");
			// After completing http call
			// will close this activity and lauch main activity
			// close this activity


            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

			getActivity().finish();

		  }


	}


    public JSONObject xMiles_Login(String name,String id, String gender, String picURL, String device) {
        UserFunctions userFunction = new UserFunctions();
        //---------------------------------------------
        //---------------------------------------------
        JSONObject json = userFunction.loginUser(id, name, picURL, device);
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

                }
            }
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return null;

    }

    public void xMiles_userRoutes (String user_id, Integer total_routes){

		//Your code goes here
    	//------------
		UserFunctions userFunc = new UserFunctions();

		for (int favorite_id = 1; favorite_id <= total_routes; favorite_id++) {
			//-----------
	    	ContentValues[] valueList;
	    	JSONArray jsonArray;
	    	//-----------

			JSONObject json = userFunc.userRoutes(user_id,Integer.toString(favorite_id));

			try {

	        	if (json.getString("success") != null) {

				    String res = json.getString("success");
				    if(Integer.parseInt(res) == 1){

				    	jsonArray = new JSONArray(json.getString("user"));
				    	valueList = new ContentValues[jsonArray.length()];
				    	//------------


						for (int position = 0; position < jsonArray.length(); position++) {

							JSONObject jsonObject = null;

							try {
								ContentValues values = new ContentValues();
								jsonObject = jsonArray.getJSONObject(position);

								values.put(DatabaseHelper.KEY_ID, user_id);
								values.put(DatabaseHelper.KEY_FAVORITE_ID, Integer.toString(favorite_id));
								values.put(DatabaseHelper.KEY_BUSLINE, jsonObject.getString("busline"));
								values.put(DatabaseHelper.KEY_BUS_STOP, jsonObject.getString("bus_stop"));
								values.put(DatabaseHelper.KEY_BUS_STOP_ID, jsonObject.getString("bus_stop_id"));
								values.put(DatabaseHelper.KEY_B_LATITUDE, jsonObject.getString("latitude"));
								values.put(DatabaseHelper.KEY_B_LONGITUDE, jsonObject.getString("longitude"));
								values.put(DatabaseHelper.KEY_M_DISTANCE_K, jsonObject.getString("max_distance_km"));
								values.put(DatabaseHelper.KEY_FLAG, jsonObject.getString("from_or_to_flag"));
								values.put(DatabaseHelper.KEY_CREATED_AT, jsonObject.getString("created_at"));

								valueList[position] = values;
								//-----------

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						if (favorite_id == 1) {
							getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_ROUTES_create, valueList);

						} else {
							getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_ROUTES_insert, valueList);

						}



				    }

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


    }

    public JSONObject xMiles_favoritesRoutes(String username, String user_id) {
		//Your code goes here
    	//------------
    	ContentValues[] valueList;
    	JSONArray jsonArray;
    	//-----------
		UserFunctions userFunc = new UserFunctions();
		JSONObject json = userFunc.favoritesRoutes(user_id);

        try {

        	if (json.getString("success") != null) {

			    String res = json.getString("success");
			    if(Integer.parseInt(res) == 1){

			    	jsonArray = new JSONArray(json.getString("user"));
			    	valueList = new ContentValues[jsonArray.length()];

		        	//Log.e(TAG, "jsonArray.length(): " + jsonArray.length());

					for (int position = 0; position < jsonArray.length(); position++) {

						JSONObject jsonObject = null;

						try {
							ContentValues values = new ContentValues();
							jsonObject = jsonArray.getJSONObject(position);

							values.put(DatabaseHelper.KEY_ID, user_id);
							values.put(DatabaseHelper.KEY_NAME, username);
							values.put(DatabaseHelper.KEY_BUSLINE, jsonObject.getString("busline"));
							values.put(DatabaseHelper.KEY_CITY, jsonObject.getString("city"));
							values.put(DatabaseHelper.KEY_UF, jsonObject.getString("uf"));
							values.put(DatabaseHelper.KEY_FROM, jsonObject.getString("_from"));
							values.put(DatabaseHelper.KEY_FROM_BUS_STOP_ID, jsonObject.getString("_from_bus_stop_id"));
							values.put(DatabaseHelper.KEY_TO, jsonObject.getString("_to"));
							values.put(DatabaseHelper.KEY_TO_BUS_STOP_ID, jsonObject.getString("_to_bus_stop_id"));
							values.put(DatabaseHelper.KEY_BD_UPDATED, "NO");
							values.put(DatabaseHelper.KEY_CREATED_AT, jsonObject.getString("created_at"));

							valueList[position] = values;
							//-----------

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_USER_FAVORITES_create, valueList);

			    }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return json;
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
    
    public void xMiles_getRewards() {
		//Your code goes here
    	//------------
    	ContentValues[] valueList;
    	JSONArray jsonArray;
    	//-----------
		UserFunctions userFunc = new UserFunctions();
		JSONObject json = userFunc.getRewards();

        try {

        	if (json.getString("success") != null) {

			    String res = json.getString("success");
			    if(Integer.parseInt(res) == 1){

			    	jsonArray = new JSONArray(json.getString("rewards"));
			    	valueList = new ContentValues[jsonArray.length()];

					for (int position = 0; position < jsonArray.length(); position++) {

						JSONObject jsonObject = null;

						try {
							ContentValues values = new ContentValues();
							jsonObject = jsonArray.getJSONObject(position);

							values.put(DatabaseHelper.KEY_REWARD, jsonObject.getString("reward_name"));
							values.put(DatabaseHelper.KEY_REWARD_TYPE, jsonObject.getString("reward_type"));
							values.put(DatabaseHelper.KEY_SCORE, jsonObject.getString("score"));
							values.put(DatabaseHelper.KEY_QUANTITY, jsonObject.getString("quantity"));
							values.put(DatabaseHelper.KEY_PICURL, jsonObject.getString("picurl"));

							valueList[position] = values;


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_REWARDS_create, valueList);

			    }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	//return json;
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


}
