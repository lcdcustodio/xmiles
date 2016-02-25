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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class RelListAdapter extends BaseAdapter {	
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
	//--------------------------
	private static final Integer TYPE1   = 1;
	private static final Integer TYPE2   = 2;	
	private static final Integer TYPE3   = 3;
	//--------------------------

	

	public RelListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
	}

	@Override
	public int getCount() {
		//return feedItems.size();
		//return feedItems.size() + 1;
		//return 2;
		return 10;
	}

	@Override
	public Object getItem(int location) {
		//return feedItems.get(location);
		return null;
		
	}
	
	
	@Override
	public int getItemViewType(int position) {

	    switch(position){
	       case 0:
	    	   return TYPE1; 
	       case 1:
	    	   return TYPE2;
	    	   
	       default:
	            break;
	        	   
	    }
		return TYPE3;
	}	

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.e(TAG, "position: " + position);
		
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//*
		int viewType = this.getItemViewType(position);
		
        View vi1=convertView;
        View vi2=convertView;
        
        vi1 = inflater.inflate(R.layout.feed_item, null);
        vi2 = inflater.inflate(R.layout.likes_header, null);
        //------------------------------------
		//*
		TextView name = (TextView) vi1.findViewById(R.id.name);
		TextView timestamp = (TextView) vi1
				.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) vi1
				.findViewById(R.id.txtStatusMsg);
		//-------------------
		TextView rel_stats = (TextView) vi1
				.findViewById(R.id.rel_stats);		
		//-------------------
		TextView url = (TextView) vi1.findViewById(R.id.txtUrl);
		NetworkImageView profilePic = (NetworkImageView) vi1
				.findViewById(R.id.profilePic);
		FeedImageView feedImageView = (FeedImageView) vi1
				.findViewById(R.id.feedImage1);

		//Buttons
		Button btn_like    = (Button) vi1.findViewById(R.id.Button_like);
		Button btn_comment = (Button) vi1.findViewById(R.id.Button_comment);
		
		btn_like.setVisibility(View.GONE);
		btn_comment.setVisibility(View.GONE);
		
		//View
		View vw01 = (View) vi1.findViewById(R.id.View01);
		vw01.setVisibility(View.GONE);


		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
        
        //------------------------------------
	    switch(viewType){
	       case 1:
	    	   	
	    	   	Log.i(TAG, "feedItems.get(position): " + position);
	    	   	Log.v(TAG, "feedItems.size(): " + feedItems.size());
				FeedItem item = feedItems.get(position);
				
				name.setText(item.getName());		 
			 	// like, comments stats
			 	rel_stats.setText(item.getLike_stats() + " curtida(s) " +  item.getComment_stats() + " comentário(s)");
				
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

		        return vi1;
		        
		        
	       case 2:
		        
		        return vi2;
		        
	       default:
	            break;
	        	   
	    }     
        //return null;
        return vi2;
        //*/
        /*
        
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
		*/
	}
	

}
