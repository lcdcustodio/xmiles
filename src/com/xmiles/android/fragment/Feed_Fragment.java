package com.xmiles.android.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.xmiles.android.Gmaps;
import com.xmiles.android.NewRoutes;
import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.listviewfeed.FeedListAdapter;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.Score_Algorithm;
import com.xmiles.android.support.Support;
import com.xmiles.android.support.imageloader.RankingLazyAdapter;
import com.xmiles.android.webservice.UserFunctions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


//public class Feed_Fragment extends Fragment {
public class Feed_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static final String TAG = "FACEBOOK";
	
	private static final Integer KEY_ID         = 1;
	private static final Integer KEY_NAME       = 2;
	private static final Integer KEY_IMAGE      = 3;
	private static final Integer KEY_STATUS     = 4;
	private static final Integer KEY_PICURL     = 5;
	private static final Integer KEY_TIME_STAMP = 6;
	private static final Integer KEY_URL        = 7;
	private static final Integer KEY_CUSTOM_TIME_STAMP = 8;
	//---------------------
	AutoCompleteTextView buscode_search;
	Button buscode_button;
	//---------------------
	ProgressDialog progressBar;
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private JSONObject json;
	private Cursor data_newsfeed;
	//-----------------------	
	private View rootView;
	private View searchBus;
	//-----------------------
    // GPSTracker class
	GPSTracker gps;
	//----------------------
	private Handler mHandler;

	
	public Feed_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		getActivity().registerReceiver(FeedFragmentReceiver, new IntentFilter("feedfragmentupdater"));
		
		rootView = inflater.inflate(R.layout.fgmt_background, container, false);

		//searchBus = inflater.inflate(R.layout.search_buscode_autocomplete_textview, container, false);
		searchBus = inflater.inflate(R.layout.search_buscode_button, container, false);
		
		View custom = inflater.inflate(R.layout.feed_fgmt, null); 

		listView 	= (ListView) custom.findViewById(R.id.list);
		//---------------
		feedItems = new ArrayList<FeedItem>();
		listAdapter = new FeedListAdapter(getActivity(), feedItems);
		listView.setAdapter(listAdapter);
		//---------------
        progressBar = new ProgressDialog(getActivity());
		//*
        progressBar.setCancelable(true);
		progressBar.setMessage(getActivity().getString(R.string.please_wait));
		progressBar.show();
		//*/
		//--------------
        //Check Service Location
		gps = new GPSTracker(getActivity());
		gps.getLocation(0);

        if(!gps.canGetGPSLocation()){	
			gps.showSettingsAlert();
		} 
		//--------------
		
		Feed_Query fq = new Feed_Query();
		///*
		buscode_button = (Button) searchBus.findViewById(R.id.search);
	    buscode_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
                //Hide searchBus Button
                //((ViewGroup) rootView).removeView(searchBus);

				
                Intent intent = new Intent(getActivity(), Gmaps.class);                        
                
                startActivity(intent);

			}	
		});
		//*/
  
		//---------------		
		((ViewGroup) rootView).addView(searchBus);		
		((ViewGroup) rootView).addView(custom);
		
		
		return rootView;
    }
		
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Feed_fgmt");
	        
	        //-------------
	        getActivity().unregisterReceiver(FeedFragmentReceiver);
	        //-------------

	    }
	 //*
	 public class Feed_Query {
		 
		 public Feed_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {


		    		    	//Your code goes here
		    		    	//------------			        	
				            Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
				            data_newsfeed = getActivity().getContentResolver().query(uri, null, null, null, null);

				    		        	
					    } catch (Exception e) {
					            e.printStackTrace();
					    }
					}
			});

			thread.start();
			//-----------
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//-----------
			//progressBar.dismiss();
			runThread();			 

		 }
		 
	        private void runThread() {

	            new Thread() {
	                public void run() {

	                        try {
	                        	getActivity().runOnUiThread(new Runnable() {

	                                @Override
	                                public void run() {
	                                	
	                    		    	//Your code goes here            	
	                    		    	parseJsonFeed(data_newsfeed);

	                                }
	                            });

	                            Thread.sleep(50);
	                            progressBar.dismiss();
	                            
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                }
	            }.start();
	        }

	 }
	 //*/
		/**
		 * Parsing json reponse and passing the data to feed view list adapter
		 * */
		private void parseJsonFeed(Cursor newsfeed) {
			
			Log.w(TAG, "newsfeed.getCount(): " + newsfeed.getCount());
			
			for (int i = 0; i < newsfeed.getCount(); i++) {
				

				//data_newsfeed.moveToPosition(i);
				newsfeed.moveToPosition(i);
				
				FeedItem item = new FeedItem();
				
				//item.setId(data_newsfeed.getInt(KEY_ID));
				//item.setName(data_newsfeed.getString(KEY_NAME));
				item.setId(newsfeed.getInt(KEY_ID));
				item.setName(newsfeed.getString(KEY_NAME));

				// Image might be null sometimes
				//String image = data_newsfeed.isNull(KEY_IMAGE) ? null : data_newsfeed
				//		.getString(KEY_IMAGE);
				String image = newsfeed.isNull(KEY_IMAGE) ? null : newsfeed
						.getString(KEY_IMAGE);
				
				item.setImge(image);

				//item.setStatus(data_newsfeed.getString(KEY_STATUS));
				//item.setProfilePic(data_newsfeed.getString(KEY_PICURL));				
				//item.setTimeStamp(data_newsfeed.getString(KEY_TIME_STAMP));				
				item.setStatus(newsfeed.getString(KEY_STATUS));
				item.setProfilePic(newsfeed.getString(KEY_PICURL));
				
				Log.i(TAG, "newsfeed.isNull(KEY_CUSTOM_TIME_STAMP): " + newsfeed.isNull(KEY_CUSTOM_TIME_STAMP));
				Log.e(TAG, "newsfeed.isNull(KEY_IMAGE): " + newsfeed.isNull(KEY_IMAGE));
				
				if (newsfeed.isNull(KEY_CUSTOM_TIME_STAMP)) {
					item.setTimeStamp(newsfeed.getString(KEY_TIME_STAMP));
				} else {
					item.setTimeStamp(newsfeed.getString(KEY_CUSTOM_TIME_STAMP));
				}
				
				//item.setTimeStamp(newsfeed.getString(KEY_TIME_STAMP));				
				
				// url might be null sometimes
				//String feedUrl = data_newsfeed.isNull(KEY_URL) ? null : data_newsfeed
				//		.getString(KEY_URL);
				String feedUrl = newsfeed.isNull(KEY_URL) ? null : newsfeed
						.getString(KEY_URL);
				
				item.setUrl(feedUrl);

				feedItems.add(item);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		}

		private final BroadcastReceiver FeedFragmentReceiver = new BroadcastReceiver() {


			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				getLoaderManager().restartLoader(0, null, Feed_Fragment.this);
			}};


		@Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			
			Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
			return new CursorLoader(getActivity(), uri, null, null, null, null);
			//return null;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
			// TODO Auto-generated method stub
			
			Log.d(TAG, "Feed_Fgmnt onLoadFinished");			
			
			listView.setAdapter(null);
			//---------------
			feedItems = new ArrayList<FeedItem>();
			listAdapter = new FeedListAdapter(getActivity(), feedItems);
			listView.setAdapter(listAdapter);
			//---------------
			
			parseJsonFeed(arg1);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			// TODO Auto-generated method stub
			
		}


	    
}
