package com.xmiles.android.fragment;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.MainActivity;

import com.xmiles.android.R;

import com.xmiles.android.facebook_api_support.GetFacebookProfile;
import com.xmiles.android.facebook_api_support.Utility;

import com.xmiles.android.scheduler.FbPlaces_Download;
import com.xmiles.android.scheduler.Getting_UserLocation;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.UserFunctions;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Splash_Fragment extends Fragment {

	//TAG
	private static final String TAG = "FACEBOOK";

	JSONObject facebook_profile;

	FbPlaces_Download FbPlaces;

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

			// start FbPlaces service
			FbPlaces = new FbPlaces_Download();
			FbPlaces.setAlarm(getActivity());
			//------------------
			/* 
			 *  TRY to cancel Getting_Location receiver 
			 *  if is running in order to ensure that receiver is working
			 */
			Getting_UserLocation gl = new Getting_UserLocation();
		    try {
		    	
		    	gl.cancelAlarm(getActivity());
		    	
		    	gl.setAlarm(getActivity());
		    	
		    } catch (Exception e) {
		        Log.e(TAG, "Getting_Location service was not canceled. " + e.toString());
		    }
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
				JSONObject json_login = xMiles_Login(facebook_profile.getString("name"),
							 			facebook_profile.getString("id"),
							 			facebook_profile.getString("gender"),
							 			facebook_profile.optJSONObject("picture").optJSONObject("data").getString("url"));

				/*
				 * If Login success = 1 then Check TABLE_USER_FAVORITES and later
				 * [new] TABLE_USER_ROUTES
				 */
                if(Integer.parseInt(json_login.getString("success")) == 1){

                	//Log.i(TAG, "xMiles_Login: " + json_login);
    				JSONObject json_favoritesRoutes = xMiles_favoritesRoutes(facebook_profile.getString("name"),
    																		 facebook_profile.getString("id"));
    				//Log.i(TAG, "xMiles_favoritesRoutes: " + json_favoritesRoutes);

			        try {

			        	if (json_favoritesRoutes.getString("success") != null) {

						    String res = json_favoritesRoutes.getString("success");
						    if(Integer.parseInt(res) == 1){

						    	JSONArray jsonArray = new JSONArray(json_favoritesRoutes.getString("user"));
						    	//Log.w(TAG, "json_favoritesRoutes.lenght()" + jsonArray.length());

						    	xMiles_userRoutes(facebook_profile.getString("id"),
						    					  jsonArray.length());
						    }

			        	}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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


    public JSONObject xMiles_Login(String name,String id, String gender, String picURL) {
        UserFunctions userFunction = new UserFunctions();
        //---------------------------------------------
        //---------------------------------------------
        JSONObject json = userFunction.loginUser(id);
        // check for login response
        try {
            if (json.getString("success") != null) {

                String res = json.getString("success");
                if(Integer.parseInt(res) != 1){
                	userFunction.registerUser(name, id, gender, picURL);

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
}
