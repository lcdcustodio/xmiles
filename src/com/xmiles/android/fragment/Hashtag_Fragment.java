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

import com.xmiles.android.listviewfeed.HashtagListAdapter;
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
import android.content.ContentValues;
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
//public class Hashtag_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
public class Hashtag_Fragment extends Fragment {	
	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_U_ID       = 0;
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
	private static final Integer KEY_YOU_LIKE_THIS = 11;
	//---------------------
	private static final Integer KEY_HASHTAG = 14;
	//---------------------
	Button buscode_button;
	
	ProgressDialog progressBar;
	private ListView listView;
	private HashtagListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private JSONObject json;
	//private Cursor data_newsfeed;
	private String hashtag;
	//-----------------------	
	private View rootView;

	
	public Hashtag_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		Log.i(TAG, "onCreateView Hashtag_fgmt");
		
		rootView = inflater.inflate(R.layout.fgmt_background, container, false);

		View custom = inflater.inflate(R.layout.hashtag_fgmt, null); 
		//View header = inflater.inflate(R.layout.ranking_header_item, null); 

		listView 	= (ListView) custom.findViewById(R.id.list_items);
		//---------------
		hashtag = getArguments().getString("hashtag");
		//---------------
		//TextView text_header = (TextView) header.findViewById(R.id.rnk_header_item);
		//text_header.setText(hashtag);
		//---------------
		feedItems = new ArrayList<FeedItem>();
		listAdapter = new HashtagListAdapter(getActivity(), feedItems);
		listView.setAdapter(listAdapter);
		//---------------
        progressBar = new ProgressDialog(getActivity());
		//*
        progressBar.setCancelable(true);
		progressBar.setMessage(getActivity().getString(R.string.please_wait));
		progressBar.show();
		//*/
		//--------------
		//--------------
		
		Feed_Query fq = new Feed_Query();
		///*
  
		//---------------		
		//((ViewGroup) rootView).addView(header);
		((ViewGroup) rootView).addView(custom);
		
		
		return rootView;
    }
		
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Hashtag_fgmt");
	        

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
				            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
				            Cursor data_profile = getActivity().getContentResolver().query(uri, null, null, null, null);
				            data_profile.moveToFirst();
				            
				            UserFunctions userFunc = new UserFunctions();
				            
				            json = userFunc.getNewsfeed_by_hashtag(data_profile.getString(KEY_U_ID),hashtag);

				    		        	
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
	                    		    	try {
											parseJsonFeed(new JSONArray(json.getString("feed")));
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

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
		private void parseJsonFeed(JSONArray newsfeed) {
			
			ContentValues[] valueList;
	    	valueList = new ContentValues[newsfeed.length()];

			for (int i = 0; i < newsfeed.length(); i++) {


				try {
					
					ContentValues values = new ContentValues();
					
					JSONObject newsfeedObj = (JSONObject) newsfeed.get(i);
					
					FeedItem item = new FeedItem();

					item.setId(newsfeedObj.getInt("id"));
					item.setName(newsfeedObj.getString("name"));

					values.put(DatabaseHelper.KEY_ID, newsfeedObj.getString("id"));
					values.put(DatabaseHelper.KEY_NAME, newsfeedObj.getString("name"));

					
					// Image might be null sometimes
					String image = newsfeedObj.isNull("image") ? null : newsfeedObj
							.getString("image");
					
					item.setImge(image);
					values.put(DatabaseHelper.KEY_IMAGE, image);
					
					item.setStatus(newsfeedObj.getString("status"));
					item.setProfilePic(newsfeedObj.getString("profilepic"));

					values.put(DatabaseHelper.KEY_STATUS, newsfeedObj.getString("status"));
					values.put(DatabaseHelper.KEY_PICURL, newsfeedObj.getString("profilepic"));

					// like, comments stats
					item.setLike_stats(newsfeedObj.getString("like_stats"));
					item.setComment_stats(newsfeedObj.getString("comment_stats"));
			
					values.put(DatabaseHelper.KEY_LIKE_STATS, newsfeedObj.getString("like_stats"));
					values.put(DatabaseHelper.KEY_COMMENT_STATS, newsfeedObj.getString("comment_stats"));
					
					//you_like_this
					item.setYou_like_this(newsfeedObj.getString("you_like_this"));

					values.put(DatabaseHelper.KEY_YOU_LIKE_THIS, newsfeedObj.getString("you_like_this"));

				
					if (newsfeedObj.isNull("custom_time_stamp")) {
						item.setTimeStamp(newsfeedObj.getString("time_stamp"));
						values.put(DatabaseHelper.KEY_CUSTOM_TIME_STAMP, newsfeedObj.getString("time_stamp"));

					} else {
						item.setTimeStamp(newsfeedObj.getString("custom_time_stamp"));
						values.put(DatabaseHelper.KEY_CUSTOM_TIME_STAMP, newsfeedObj.getString("custom_time_stamp"));
					}
					
					// url might be null sometimes
					String feedUrl = newsfeedObj.isNull("url") ? null : newsfeedObj
							.getString("url");
					
					item.setUrl(feedUrl);
					values.put(DatabaseHelper.KEY_URL, feedUrl);

					// hashtag might be null sometimes
					String hashtag = newsfeedObj.isNull("hashtag") ? null : newsfeedObj
							.getString("hashtag");
					
					item.setHashtag_1(hashtag);				
					values.put(DatabaseHelper.KEY_HASHTAG, hashtag);		
					
					//sender
					values.put(DatabaseHelper.KEY_SENDER, newsfeedObj.getString("sender"));
					
					//feed_type
					values.put(DatabaseHelper.KEY_FEED_TYPE, newsfeedObj.getString("feed_type"));


					valueList[i] = values;
					
					feedItems.add(item);

					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}

			getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_NEWSFEED_BY_HASHTAG_create, valueList);
			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		}
	    
}
