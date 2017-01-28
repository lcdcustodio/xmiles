package com.xmiles.android.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.xmiles.android.Gmaps;
import com.xmiles.android.MainActivity;

import com.xmiles.android.R;
import com.xmiles.android.Relationship;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.listviewfeed.CommentItem;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.listviewfeed.FeedListAdapter;
import com.xmiles.android.listviewfeed.SupportRelAdapterItem;
import com.xmiles.android.listviewfeed.LikeItem;
import com.xmiles.android.listviewfeed.RelListAdapter;
import com.xmiles.android.scheduler.Comments_Inbox_Upload;
import com.xmiles.android.scheduler.Likes_Inbox_Upload;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Rel_Fragment extends Fragment {
//public class Rel_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static final String TAG = "FACEBOOK";
	
	private static final Integer KEY_NAME_PROFILE = 1;
	private static final Integer KEY_PICURL_PROFILE = 2;
	//----------------------
	private static final Integer KEY_ID_PROFILE  = 0;
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
	private static final Integer KEY_SENDER = 12;
	private static final Integer KEY_FEED_TYPE = 13;
	//---------------------
	private static final Integer KEY_HASHTAG = 14;
	
	//---------------------
	AutoCompleteTextView add_cmts;
	
	//---------------------
	ProgressDialog progressBar;
	private ListView listView;
	private RelListAdapter listAdapter;
	private List<FeedItem> feedItems;
	//----------------
	private List<LikeItem> likeItems;	
	private List<CommentItem> commentItems;
	private List<SupportRelAdapterItem> supportreladapterItems;
	//----------------
	private JSONObject json;
	private JSONArray jsonArray;
	private Cursor data_newsfeed;
	//-----------------------	
	private View rootView;
	//-----------------------
    // GPSTracker class
	GPSTracker gps;
	//----------------------
	private int position;
	private String feed_id;
	private String adapter;

	
	public Rel_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		
		View custom_1 = inflater.inflate(R.layout.rel_fgmt, container, false);
		listView 	= (ListView) custom_1.findViewById(R.id.list_items);
		add_cmts = (AutoCompleteTextView) custom_1.findViewById(R.id.add_comment);
		
		position = getArguments().getInt("position");
		feed_id = getArguments().getString("feed_id");
		adapter = getArguments().getString("adapter");
		//---------------
		feedItems = new ArrayList<FeedItem>();
		likeItems = new ArrayList<LikeItem>();
		commentItems = new ArrayList<CommentItem>();
		supportreladapterItems = new ArrayList<SupportRelAdapterItem>();
		
		listAdapter = new RelListAdapter(getActivity(), feedItems, likeItems, commentItems, supportreladapterItems);
		

		//---------------
        progressBar = new ProgressDialog(getActivity());
		//*
        progressBar.setCancelable(true);
		progressBar.setMessage(getActivity().getString(R.string.please_wait));
		progressBar.show();
		//*/
		//--------------
      	//HANDLE EVENT - TYPE COMMENTS
		add_cmts.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchContent = add_cmts.getText().toString();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
            		            		
                	//Hide keyboard                	
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(add_cmts.getWindowToken(), 0);

                    //CleanUp text
                    add_cmts.setText("");
                    
            		listAdapter = new RelListAdapter(getActivity(), feedItems, likeItems, commentItems, supportreladapterItems);
            		
	        		CommentItem comment_item = new CommentItem();
	        		
    		    	//Your code goes here
    		    	//------------			        	
		            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
		            Cursor data_profile = getActivity().getContentResolver().query(uri, null, null, null, null);
		            
		            data_profile.moveToLast();

		            Support support = new Support();
		            		            
	        		comment_item.setName(data_profile.getString(KEY_NAME_PROFILE));        		
	        		comment_item.setProfilePic(data_profile.getString(KEY_PICURL_PROFILE));
	        		comment_item.setTimeStamp(support.getDateTime());
	        		comment_item.setComment(searchContent);
	        		
	        		commentItems.add(comment_item);
            		//-----------------
	        		Log.i(TAG, "supportreladapterItems.size(): " + supportreladapterItems.size());
	        		
	        		//header_comments
	        		boolean header_comments = false;
	        		
	        		for (int j = 0; j < supportreladapterItems.size(); j++) {
	        			if (supportreladapterItems.get(j).getType_action().equals("header_comments")){
	        				header_comments = true;
	        				break;
	        			}
	        		}
	        		
	        		if (!header_comments){
	        			
	        			SupportRelAdapterItem supportreladapter_hc_item = new SupportRelAdapterItem();
	        			supportreladapter_hc_item.setType_action("header_comments");
	        			supportreladapterItems.add(supportreladapter_hc_item);
	        			
	        		}
            		//-----------------	        		
            		SupportRelAdapterItem supportreladapter_comments_item = new SupportRelAdapterItem();	        		
	        		supportreladapter_comments_item.setType_action("comments");
	        		supportreladapterItems.add(supportreladapter_comments_item);
                            			
        			listAdapter.notifyDataSetChanged();
            		//-----------------	 
            		//-----------------
        			Log.i(TAG, "feed_id " + feed_id);
        			Log.d(TAG, "data_newsfeed.getString(KEY_ID): " + data_newsfeed.getString(KEY_ID));

    				Uri uri_2 = SqliteProvider.CONTENT_URI_NEWSFEED_update;
    				
    				ContentValues cv_1 = new ContentValues();
    				
    				int comments_stats = Integer.parseInt(data_newsfeed.getString(KEY_COMMENT_STATS));    				
    				cv_1.put(DatabaseHelper.KEY_COMMENT_STATS, String.valueOf(comments_stats + 1));
    				//-----
    				getActivity().getContentResolver().update(uri_2, 
    						cv_1,
    						DatabaseHelper.KEY_ID + " = " + feed_id, null);
            		//-----------------	 
            		//-----------------    								 
    				data_profile.moveToFirst();

    				ContentValues cv_2 = new ContentValues();
    				
    				//feed_id
    				cv_2.put(DatabaseHelper.KEY_ID, data_newsfeed.getString(KEY_ID));				
    				//user_id
    				cv_2.put(DatabaseHelper.KEY_U_ID, data_profile.getString(KEY_ID_PROFILE));
    				// flag_action
    				cv_2.put(DatabaseHelper.KEY_FLAG_ACTION, "ADD");
    				// time_stamp    				
    				cv_2.put(DatabaseHelper.KEY_TIME_STAMP, support.getDateTime());
    				//sender
    				cv_2.put(DatabaseHelper.KEY_SENDER, data_newsfeed.getString(KEY_SENDER));
    				//status
    				cv_2.put(DatabaseHelper.KEY_STATUS, data_newsfeed.getString(KEY_STATUS));
    				//feed_type
    				cv_2.put(DatabaseHelper.KEY_FEED_TYPE, data_newsfeed.getString(KEY_FEED_TYPE));
    				//comment
    				cv_2.put(DatabaseHelper.KEY_COMMENT, searchContent);
    				
					Uri uri_5 = SqliteProvider.CONTENT_URI_COMMENTS_UPLOAD_insert;
					getActivity().getContentResolver().insert(uri_5, cv_2);
					//---------------------
					Comments_Inbox_Upload ciu = new Comments_Inbox_Upload();
					ciu.setAlarm(getActivity());	
    				
    				
                }
                return false;
            }

        });    
		
		//--------------
		Feed_Query fq = new Feed_Query();
		///*
  
		//---------------		
		((ViewGroup) rootView).addView(custom_1);
		
		
		return rootView;
    }
		
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Rel_Fragment");
	        
		    //-------------
	        /*
	        Intent intent = new Intent(getActivity(), MainActivity.class);		    
		    getActivity().startActivity(intent);
		    getActivity().finish();
		    */
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
				        	
				        	if (adapter.equals("feed")){
				        		uri = SqliteProvider.CONTENT_URI_NEWSFEED;
				        	} else {
				        		uri = SqliteProvider.CONTENT_URI_NEWSFEED_BY_HASHTAG;
				        	}
				            //Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
				            data_newsfeed = getActivity().getContentResolver().query(uri, null, null, null, null);
				            //------------
				            UserFunctions userFunc = new UserFunctions();
				            
				            json = userFunc.getPost_actions(feed_id);

				    		        	
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
	                    		    	//parseJsonFeed(data_newsfeed);
	                                	
	                    		    	try {
	                    		    			                    		    		
											parseJsonFeed(data_newsfeed, 
													      new JSONArray(json.getString("likes")),
													      new JSONArray(json.getString("comments")));
											
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
		//private void parseJsonFeed(Cursor newsfeed) {
		//private void parseJsonFeed(Cursor newsfeed, JSONArray likes) {
	    private void parseJsonFeed(Cursor newsfeed, JSONArray likes, JSONArray comments) {	
			
			Log.w(TAG, "parseJsonFeed: " + newsfeed.getCount());
			

			newsfeed.moveToPosition(position);
			
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

			// hashtag might be null sometimes
			String hashtag = newsfeed.isNull(KEY_HASHTAG) ? null : newsfeed
					.getString(KEY_HASHTAG);
			
			item.setHashtag_1(hashtag);				
			
			
			feedItems.add(item);
			
			//------------
			SupportRelAdapterItem supportreladapter_newsfeed_item = new SupportRelAdapterItem();
			supportreladapter_newsfeed_item.setType_action("newsfeed");
    		supportreladapterItems.add(supportreladapter_newsfeed_item);
			//------------			
	        try {
	        	
	        	//Your code goes here
	        	ContentValues[] valueList;
	        	valueList = new ContentValues[likes.length()];
	        	
	        	for (int i = 0; i < likes.length(); i++) {
					
	        		JSONObject likeObj = (JSONObject) likes.get(i);	        		
	        		
	        		LikeItem like_item = new LikeItem();
	        		ContentValues values = new ContentValues();
	        		
	        		like_item.setName(likeObj.getString("name"));
	        		values.put(DatabaseHelper.KEY_NAME,likeObj.getString("name"));
	        		
	        		like_item.setProfilePic(likeObj.getString("picurl"));
	        		values.put(DatabaseHelper.KEY_PICURL,likeObj.getString("picurl"));
	        		
	        		like_item.setTimeStamp(likeObj.getString("time_stamp"));
	        		
	        		values.put(DatabaseHelper.KEY_TIME_STAMP,likeObj.getString("time_stamp"));
	        		
	        		if (i == 0) {
	        			SupportRelAdapterItem supportreladapter_likes_item = new SupportRelAdapterItem();
	        			supportreladapter_likes_item.setType_action("likes");
	        			supportreladapterItems.add(supportreladapter_likes_item);
	        			
	        		}
	        		
	        		likeItems.add(like_item);
	        		valueList[i] = values;
	        	}
	        	
	        	getActivity().getContentResolver().bulkInsert(SqliteProvider.CONTENT_URI_LIKES_create, valueList);
    		        	
			    } catch (Exception e) {
			            e.printStackTrace();
			    }
	        
	        try {
	        	
	        	//Your code goes here	        	
	        	for (int i = 0; i < comments.length(); i++) {
					
	        		JSONObject commentObj = (JSONObject) comments.get(i);	        		
	        		
	        		CommentItem comment_item = new CommentItem();
	        		
	        		comment_item.setName(commentObj.getString("name"));        		
	        		comment_item.setProfilePic(commentObj.getString("picurl"));
	        		comment_item.setTimeStamp(commentObj.getString("time_stamp"));
	        		//comment_item.setStatus(commentObj.getString("status"));
	        		comment_item.setComment(commentObj.getString("comment"));
	        		
	        		commentItems.add(comment_item);
	        		
	        		//------------------
	        		if (i == 0) {
	        			SupportRelAdapterItem supportreladapter_hc_item = new SupportRelAdapterItem();
	        			supportreladapter_hc_item.setType_action("header_comments");
	        			supportreladapterItems.add(supportreladapter_hc_item);
	        			
	        		}
	        		
	        		SupportRelAdapterItem supportreladapter_comments_item = new SupportRelAdapterItem();
	        		
	        		supportreladapter_comments_item.setType_action("comments");
	        		supportreladapterItems.add(supportreladapter_comments_item);
	        		

	        		//------------------
	        		
	        	}
    		        	
			    } catch (Exception e) {
			            e.printStackTrace();
			    }
	        	
			// notify data changes to list adapater
			listView.setAdapter(listAdapter);	
			
			listAdapter.notifyDataSetChanged();

			
		}



	    
}
