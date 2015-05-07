package com.xmiles.android;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.webservice.UserFunctions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class Routes_Fragment extends Fragment {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_ID = 0;
	//---------------------
	ListView mListFavorites;
	SimpleCursorAdapter mAdapter;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	
	
	public Routes_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		View custom = inflater.inflate(R.layout.favorites_fragment, null); 
		
		mListFavorites = (ListView) custom.findViewById(R.id.list_favorites);
		
		
		Favorites_Query fq = new Favorites_Query();
		
		((ViewGroup) rootView).addView(custom);
        return rootView;
    }
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	         
	        FragmentManager fragMgr = getFragmentManager();
	        Fragment currentFragment = (Fragment) fragMgr.findFragmentById(0);
	         

	        if (currentFragment != null){	        	
	        	FragmentTransaction fragTrans = fragMgr.beginTransaction();
	            fragTrans.remove(currentFragment);
	            fragTrans.commit();
	        }
	    }
	 
	 public class Favorites_Query {
		 
		 public Favorites_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {

				            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
				        	Cursor data = getActivity().getContentResolver().query(uri, null, null, null, null);
				        	
				        	if (data != null && data.getCount() > 0){
				        		data.moveToFirst();
				        		//Log.i(TAG,"testing SQLITE: " + data.getString(KEY_ID) + "," + data.getString(1));

				        		//Your code goes here
				        	UserFunctions userFunc = new UserFunctions();
					        json = userFunc.favoritesRoutes(data.getString(KEY_ID));
					        //jsonArray = json.getJSONArray("user");
					        //jsonArray = json.getJSONObject("user");
					        Log.i(TAG,"testing SQLITE: " + json.length());
					        Log.d(TAG,"testing SQLITE: " + json.getJSONObject("user").length());

				        	}
				    		        	
					        } catch (Exception e) {
					            e.printStackTrace();
					        }
					    }
			});

			thread.start();
			
			Thread thread2 = new Thread(new Runnable(){
						    @Override
						    public void run() {

						    }	
			});

			thread2.start();
			 
		 }
	 }
	 
	 
}
