package com.xmiles.android.backup;

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


public class Feed_Fragment_backup extends Fragment {

	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;	
	//---------------------
	protected static final Integer TYPE1 = 1;
	
	//---------------------
	ProgressDialog progressBar;
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private JSONObject json;
	//-----------------------	
	
	public Feed_Fragment_backup(){}
	
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
		    		    	UserFunctions userFunc = new UserFunctions();
		    		    	//------------------		    		    	
		                    json = userFunc.getNewsfeed();

				    		        	
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
	                    		    	parseJsonFeed(json);

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
		private void parseJsonFeed(JSONObject response) {
			try {
				//JSONArray feedArray = response.getJSONArray("feed");
				JSONArray feedArray = new JSONArray(response.getString("feed"));

				for (int i = 0; i < feedArray.length(); i++) {
					JSONObject feedObj = (JSONObject) feedArray.get(i);

					FeedItem item = new FeedItem();
					item.setId(feedObj.getInt("id"));
					item.setName(feedObj.getString("name"));

					// Image might be null sometimes
					String image = feedObj.isNull("image") ? null : feedObj
							.getString("image");
					item.setImge(image);
					item.setStatus(feedObj.getString("status"));
					//item.setProfilePic(feedObj.getString("profilePic"));
					item.setProfilePic(feedObj.getString("profilepic"));				
					//item.setTimeStamp(feedObj.getString("timeStamp"));
					item.setTimeStamp(feedObj.getString("time_stamp"));				

					// url might be null sometimes
					String feedUrl = feedObj.isNull("url") ? null : feedObj
							.getString("url");
					item.setUrl(feedUrl);

					feedItems.add(item);
				}

				// notify data changes to list adapater
				listAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}


	    
}
