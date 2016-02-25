package com.xmiles.android.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xmiles.android.listviewfeed.RelListAdapter;
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


public class Rel_Fragment_bckup_20160216 extends Fragment {
//public class Rel_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
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
	private static final Integer KEY_LIKE_STATS = 9;
	private static final Integer KEY_COMMENT_STATS = 10;	
	//---------------------
	AutoCompleteTextView buscode_search;
	Button buscode_button;
	//---------------------
	ProgressDialog progressBar;
	private ListView listView;
	private RelListAdapter listAdapter;
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
	//----------------------
	private int position;

	
	public Rel_Fragment_bckup_20160216(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
	
		rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		
		View custom = inflater.inflate(R.layout.likes_fgmt, null); 

		listView 	= (ListView) custom.findViewById(R.id.list_likes);		
		//--------------
	    //position = getArguments().getInt("position");
		//---------------
		feedItems = new ArrayList<FeedItem>();
		
		//listAdapter = new RelListAdapter(getActivity());
		
		//listAdapter = new RelListAdapter(getActivity(), position);
		listAdapter = new RelListAdapter(getActivity(), feedItems);
		//listAdapter = new RelListAdapter(getActivity(), feedItems, position);
		
		listView.setAdapter(listAdapter);
		//---------------
        progressBar = new ProgressDialog(getActivity());
		//*
        progressBar.setCancelable(true);
		progressBar.setMessage(getActivity().getString(R.string.please_wait));
		progressBar.show();
		//*/

		//---------------
		Log.i(TAG,"getArguments().getString(name)" + getArguments().getString("name"));
		/*
		Log.e(TAG,"getArguments().getString(image)" + getArguments().getString("image"));
		Log.e(TAG,"getArguments().getString(status)" + getArguments().getString("status"));
		Log.e(TAG,"getArguments().getString(profilepic)" + getArguments().getString("profilepic"));
		Log.e(TAG,"getArguments().getString(url)" + getArguments().getString("url"));
		Log.e(TAG,"getArguments().getString(like_stats)" + getArguments().getString("like_stats"));
		Log.e(TAG,"getArguments().getString(comment_stats)" + getArguments().getString("comment_stats"));
		Log.e(TAG,"getArguments().getString(time_stamp)" + getArguments().getString("time_stamp"));
		Log.e(TAG,"getArguments().getString(custom_time_stamp)" + getArguments().getString("custom_time_stamp"));
		*/
		//---------------
		Map<String, String> map_newsfeed = new HashMap<String, String>();
		map_newsfeed.put("name",getArguments().getString("name"));
		
		//map.clear();		
		//map.get("header_friend")

		
		Rel_Query rq = new Rel_Query();
		///*
  
		//---------------		
		((ViewGroup) rootView).addView(custom);
		
		
		return rootView;
    }
		
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Rel_fgmt");
	        

	    }
	 //*
	 public class Rel_Query {
		 
		 public Rel_Query(){
			 
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
			
			for (int i = 0; i < newsfeed.getCount(); i++) {

				//newsfeed.moveToPosition(position);
				newsfeed.moveToPosition(i);
					
				FeedItem item = new FeedItem();
					
				item.setId(newsfeed.getInt(KEY_ID));
				item.setName(newsfeed.getString(KEY_NAME));
	
				// Image might be null sometimes
				String image = newsfeed.isNull(KEY_IMAGE) ? null : newsfeed
							.getString(KEY_IMAGE);
					
				item.setImge(image);
	
				item.setStatus(newsfeed.getString(KEY_STATUS));
				item.setProfilePic(newsfeed.getString(KEY_PICURL));
	
				// like, comments stats
				item.setLike_stats(newsfeed.getString(KEY_LIKE_STATS));
				item.setComment_stats(newsfeed.getString(KEY_COMMENT_STATS));
					
				
				if (newsfeed.isNull(KEY_CUSTOM_TIME_STAMP)) {
					item.setTimeStamp(newsfeed.getString(KEY_TIME_STAMP));
				} else {
					item.setTimeStamp(newsfeed.getString(KEY_CUSTOM_TIME_STAMP));
				}
					
				// url might be null sometimes
				String feedUrl = newsfeed.isNull(KEY_URL) ? null : newsfeed
							.getString(KEY_URL);
					
				item.setUrl(feedUrl);
	
				feedItems.add(item);
			
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		}

		    
}
