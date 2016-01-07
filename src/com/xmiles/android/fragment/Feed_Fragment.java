package com.xmiles.android.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.listviewfeed.FeedListAdapter;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Feed_Fragment extends Fragment {

	private static final String TAG = "FACEBOOK";
	
	private static final Integer KEY_ID         = 1;
	private static final Integer KEY_NAME       = 2;
	private static final Integer KEY_IMAGE      = 3;
	private static final Integer KEY_STATUS     = 4;
	private static final Integer KEY_PICURL     = 5;
	private static final Integer KEY_TIME_STAMP = 6;
	private static final Integer KEY_URL        = 7;
	//---------------------
	//---------------------
	ProgressDialog progressBar;
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private JSONObject json;
	private Cursor data_newsfeed;
	//-----------------------	
	
	public Feed_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);

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
		
		Feed_Query fq = new Feed_Query();
		
		((ViewGroup) rootView).addView(custom);
		
		
		return rootView;
    }
		
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Feed_fgmt");
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
		    		    	//UserFunctions userFunc = new UserFunctions();
		    		    	//------------------		    		    	
		                    //json = userFunc.getNewsfeed();
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

				data_newsfeed.moveToPosition(i);
				
				FeedItem item = new FeedItem();
				item.setId(data_newsfeed.getInt(KEY_ID));
				item.setName(data_newsfeed.getString(KEY_NAME));

				// Image might be null sometimes
				String image = data_newsfeed.isNull(KEY_IMAGE) ? null : data_newsfeed
						.getString(KEY_IMAGE);  
				item.setImge(image);

				item.setStatus(data_newsfeed.getString(KEY_STATUS));
				item.setProfilePic(data_newsfeed.getString(KEY_PICURL));				
				item.setTimeStamp(data_newsfeed.getString(KEY_TIME_STAMP));				

				// url might be null sometimes
				String feedUrl = data_newsfeed.isNull(KEY_URL) ? null : data_newsfeed
						.getString(KEY_URL);
				item.setUrl(feedUrl);

				feedItems.add(item);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		}


	    
}
