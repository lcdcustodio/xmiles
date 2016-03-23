package com.xmiles.android.listviewfeed;

import com.xmiles.android.listviewfeed.FeedImageView;
import com.xmiles.android.R;
import com.xmiles.android.Relationship;
import com.xmiles.android.listviewfeed.AppController;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.Support;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class FeedListAdapter extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	//TAG
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


	public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		//TextView name = (TextView) convertView.findViewById(R.id.name);
		final TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
		//-------------------
		TextView rel_stats = (TextView) convertView.findViewById(R.id.rel_stats);		

		rel_stats.setTag(position);
		
		rel_stats.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					 int pos_rel_stats=(Integer)v.getTag();

					 Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
					 Cursor data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);
					
					 
					 data_newsfeed.moveToPosition(pos_rel_stats);


				     //-------------------------------------------				     
				     Bundle args = new Bundle();
				    
				     args.putInt("position", pos_rel_stats);
				    
				     args.putString("feed_id", data_newsfeed.getString(KEY_ID));
				     /*
				     args.putString("name", data_newsfeed.getString(KEY_NAME));
				     args.putString("image", data_newsfeed.getString(KEY_IMAGE));				    	 
				     args.putString("status", data_newsfeed.getString(KEY_STATUS));
				     args.putString("profilepic", data_newsfeed.getString(KEY_PICURL));
				     args.putString("url",data_newsfeed.getString(KEY_URL));
				     args.putString("like_stats",data_newsfeed.getString(KEY_LIKE_STATS)); 
				     args.putString("comment_stats",data_newsfeed.getString(KEY_COMMENT_STATS));
				     args.putString("time_stamp",data_newsfeed.getString(KEY_TIME_STAMP));
				     args.putString("custom_time_stamp",data_newsfeed.getString(KEY_CUSTOM_TIME_STAMP));
				     */
				     Intent intent = new Intent(activity, Relationship.class);				    	 
				     intent.putExtras(args);
				    
				     activity.startActivity(intent);
					
				}


			});			

		//-------------------
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.profilePic);
		FeedImageView feedImageView = (FeedImageView) convertView
				.findViewById(R.id.feedImage1);
		//-------------------
		//------TEST---------
		//-------------------		
		Button like = (Button) convertView.findViewById(R.id.Button_like);
		//like.setEnabled(false);

		/*
		if (position > 2){
			like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_light_grey_l, 0, 0, 0);
		} else {
			like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_active_new, 0, 0, 0);
		}
		*/
		//-------------------
		//------TEST---------
		//-------------------	
		like.setTag(position);		
		like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pos_btn_like = (Integer)v.getTag();
				//Log.e(TAG,"pos_btn_like: " + pos_btn_like);
				
				Uri uri_1 = SqliteProvider.CONTENT_URI_NEWSFEED;
				Cursor data_newsfeed = activity.getContentResolver().query(uri_1, null, null, null, null);				 
				data_newsfeed.moveToPosition(pos_btn_like);
				//------------------------------------------------------------
				Uri uri_2 = SqliteProvider.CONTENT_URI_NEWSFEED_update;
				
				ContentValues cv = new ContentValues();
				
				int like_stats = Integer.parseInt(data_newsfeed.getString(KEY_LIKE_STATS));
				
				cv.put(DatabaseHelper.KEY_LIKE_STATS, String.valueOf(like_stats + 1));


				activity.getContentResolver().update(uri_2, 
						cv, 
						DatabaseHelper.KEY_ID + " = " + data_newsfeed.getString(KEY_ID), null);
				//------------------------------------------------------------
				//------------------------------------------------------------
				Button btn_like = (Button) v.findViewById(R.id.Button_like);				
				btn_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_active_new, 0, 0, 0);

				//---------------
				feedItems.clear();
				//---------------------------
				//**************************
				//---------------------------
				Cursor newsfeed = activity.getContentResolver().query(uri_1, null, null, null, null);
				
				for (int i = 0; i < newsfeed.getCount(); i++) {

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
				
				//---------------------------
				notifyDataSetChanged();
				//---------------
				
			}
		});
		//-------------------
		Button comment = (Button) convertView.findViewById(R.id.Button_comment);		

		comment.setTag(position);		
		comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pos_btn_comment = (Integer)v.getTag();
				Log.e(TAG,"pos_btn_comment: " + pos_btn_comment);

				Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
				Cursor data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);
				 
				data_newsfeed.moveToPosition(pos_btn_comment);
				
			    Bundle args = new Bundle();				    
			    args.putInt("position", pos_btn_comment);
			    
			    args.putString("feed_id", data_newsfeed.getString(KEY_ID));
			    
			    Intent intent = new Intent(activity, Relationship.class);				    	 
			    intent.putExtras(args);
			    
			    activity.startActivity(intent);
			    
			}
		});
		
		
		//-------------------
		FeedItem item = feedItems.get(position);

		name.setText(item.getName());
		
		// Chcek for empty like, comments stats
	     int like_stats = Integer.parseInt(item.getLike_stats());
	     int comm_stats = Integer.parseInt(item.getComment_stats());
	     
	     if (like_stats + comm_stats == 0){
			 // rel_stats is empty, remove from view
	    	 rel_stats.setVisibility(View.GONE);
	    	 
	     } else {
	    	 rel_stats.setVisibility(View.VISIBLE); 
	 		// like, comments stats
	 		rel_stats.setText(item.getLike_stats() + " curtida(s) " +  item.getComment_stats() + " comentário(s)");
	    	 
	     }
		
		Support support = new Support();
		
		// Converting timestamp into x ago format
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Long.parseLong(support.getDateTime_long(item.getTimeStamp())),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);


		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.getStatus())) {
			
			if (item.getStatus().indexOf("<bold>") != -1){
				statusMsg.setText(Html.fromHtml(item.getStatus().split("<bold>")[0] +
						                       "<b>" + item.getStatus().split("<bold>")[1] + "</b>" +
						                       item.getStatus().split("<bold>")[2]));
			} else {
				statusMsg.setText(item.getStatus());
			}
			
			//statusMsg.setText(item.getStatus());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}

		// Checking for null feed url
		//if (item.getUrl() != null) {
		if (item.getUrl() != null && !item.getUrl().equals("")) {	
			url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
					+ item.getUrl() + "</a> "));

			// Making url clickable
			url.setMovementMethod(LinkMovementMethod.getInstance());
			url.setVisibility(View.VISIBLE);
		} else {
			
			
			
			if (item.getName().equals("xMiles")){

				url.setText(Html.fromHtml("<a href=\"" + "http://ec2-54-209-160-58.compute-1.amazonaws.com/pictures/xmiles_logo_rev05_transparente.png" + "\">"
						+ "www.xmiles.com.br" + "</a> "));

				// Making url clickable
				url.setMovementMethod(LinkMovementMethod.getInstance());
				url.setVisibility(View.VISIBLE);
				
			} else {
				// url is null, remove from the view
				url.setVisibility(View.GONE);
			}	
		}

		// user profile pic
		profilePic.setImageUrl(item.getProfilePic(), imageLoader);

		// Feed image
		//if (item.getImge() != null) {
		if (item.getImge() != null && !item.getImge().equals("")) {	
			feedImageView.setImageUrl(item.getImge(), imageLoader);
			feedImageView.setVisibility(View.VISIBLE);
			feedImageView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {
						}

						@Override
						public void onSuccess() {
						}
					});
		} else {
			feedImageView.setVisibility(View.GONE);
		}

		return convertView;
	}


}
