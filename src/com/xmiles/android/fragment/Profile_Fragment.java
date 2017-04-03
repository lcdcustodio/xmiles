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
import com.xmiles.android.listviewfeed.HashtagListAdapter;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.imageloader.ProfileLazyAdapter;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Profile_Fragment extends Fragment {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;	
	private static final Integer KEY_U_ID   = 0;
	private static final String hashtag = "#histórico_pontuação";
	//---------------------
	protected static final Integer TYPE1 = 1;
	
	//---------------------
	ListView mListFavorites;
	Button Add_route;
	TextView Route;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	Cursor data_Ranking;
	
	//-----------------------
	ProfileLazyAdapter listAdapter;
	ListView listView;	
	private List<FeedItem> feedItems;
	
	//-----------------------	
	
	public Profile_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);

		View custom = inflater.inflate(R.layout.ranking_fgmt, null); 


		listView 		   = (ListView) custom.findViewById(R.id.list);
		//---------------
		feedItems = new ArrayList<FeedItem>();
		listAdapter = new ProfileLazyAdapter(getActivity(), feedItems);
		listView.setAdapter(listAdapter);
		
		//---------------
        progressBar = new ProgressDialog(getActivity());

        progressBar.setCancelable(true);
		progressBar.setMessage(getActivity().getString(R.string.please_wait));
		progressBar.show();


		Ranking_Query rq = new Ranking_Query();
		

		((ViewGroup) rootView).addView(custom);
		
		return rootView;
    }
		
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Profile_fgmt");
	    }
	 
	 public class Ranking_Query {
		 
		 public Ranking_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {


				            Uri uri = SqliteProvider.CONTENT_URI_RANKING;
				            data_Ranking = getActivity().getContentResolver().query(uri, null, null, null, null);
				            
				            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
				            Cursor data_profile = getActivity().getContentResolver().query(uri_1, null, null, null, null);
				            data_profile.moveToFirst();				            

				            UserFunctions userFunc = new UserFunctions();
				            				            
				            json = userFunc.getNewsfeed_by_hashtag(data_profile.getString(KEY_U_ID),hashtag);
				            //json = userFunc.getNewsfeed_by_hashtag(user_id,hashtag);
				            

				    		        	
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
	                            //Thread.sleep(400);
	                            Thread.sleep(50);
	                            progressBar.dismiss();
	                            
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                }
	            }.start();
	        }

	 }
	 

		private void parseJsonFeed(JSONArray newsfeed) {
			
			//ContentValues[] valueList;
	    	//valueList = new ContentValues[newsfeed.length()];

			for (int i = 0; i < newsfeed.length(); i++) {


				try {
					
										
					JSONObject newsfeedObj = (JSONObject) newsfeed.get(i);
					
					FeedItem item = new FeedItem();

					item.setId(newsfeedObj.getInt("id"));
					item.setName(newsfeedObj.getString("name"));

					
					// Image might be null sometimes
					String image = newsfeedObj.isNull("image") ? null : newsfeedObj
							.getString("image");
					
					item.setImge(image);
					
					item.setStatus(newsfeedObj.getString("status"));
					item.setProfilePic(newsfeedObj.getString("profilepic"));


					// like, comments stats
					item.setLike_stats(newsfeedObj.getString("like_stats"));
					item.setComment_stats(newsfeedObj.getString("comment_stats"));
		
					
					//you_like_this
					item.setYou_like_this(newsfeedObj.getString("you_like_this"));


				
					if (newsfeedObj.isNull("custom_time_stamp")) {
						item.setTimeStamp(newsfeedObj.getString("time_stamp"));

					} else {
						item.setTimeStamp(newsfeedObj.getString("custom_time_stamp"));
					}
					
					// url might be null sometimes
					String feedUrl = newsfeedObj.isNull("url") ? null : newsfeedObj
							.getString("url");
					
					item.setUrl(feedUrl);

					// hashtag might be null sometimes
					String hashtag = newsfeedObj.isNull("hashtag") ? null : newsfeedObj
							.getString("hashtag");
					
					item.setHashtag_1(hashtag);									
					
					feedItems.add(item);

					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		}
	    
}
