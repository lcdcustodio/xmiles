package com.xmiles.android.listviewfeed;

import com.xmiles.android.listviewfeed.FeedImageView;
import com.xmiles.android.R;
import com.xmiles.android.Relationship;
import com.xmiles.android.listviewfeed.AppController;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.Support;

import java.util.List;

import android.app.Activity;
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

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		//-------------------
		TextView rel_stats = (TextView) convertView
				.findViewById(R.id.rel_stats);		

		rel_stats.setTag(position);
		
		rel_stats.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					 //View parentView = (View)v.getParent();
					 int position=(Integer)v.getTag();

					 Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
					 Cursor data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);
					 
					 //data_newsfeed.moveToPosition(data_newsfeed.getCount() - position - 1);
					 
					 data_newsfeed.moveToPosition(position);

				     //TextView child = (TextView)((ViewGroup) parentView).getChildAt(1);
				     //-------------------------------------------
				     //Log.d(TAG, "child " + child.getText().toString());
					 Log.d(TAG, "data_newsfeed.getString(KEY_STATUS) " + data_newsfeed.getString(KEY_STATUS));
				     //-------------------------------------------
				     Log.i(TAG, "Position " + position);
				     
				     Bundle args = new Bundle();
				    
				     args.putInt("position", position);
				    
				     args.putString("name", data_newsfeed.getString(KEY_NAME));
				     args.putString("image", data_newsfeed.getString(KEY_IMAGE));				    	 
				     args.putString("status", data_newsfeed.getString(KEY_STATUS));
				     args.putString("profilepic", data_newsfeed.getString(KEY_PICURL));
				     args.putString("url",data_newsfeed.getString(KEY_URL));
				     args.putString("like_stats",data_newsfeed.getString(KEY_LIKE_STATS)); 
				     args.putString("comment_stats",data_newsfeed.getString(KEY_COMMENT_STATS));
				     args.putString("time_stamp",data_newsfeed.getString(KEY_TIME_STAMP));
				     args.putString("custom_time_stamp",data_newsfeed.getString(KEY_CUSTOM_TIME_STAMP));
				    
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
		if (item.getUrl() != null) {
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
