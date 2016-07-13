package com.xmiles.android.listviewfeed;

import com.xmiles.android.listviewfeed.FeedImageView;
import com.xmiles.android.Likes;
import com.xmiles.android.R;
import com.xmiles.android.Relationship;
import com.xmiles.android.listviewfeed.AppController;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.Support;
//-----
//import com.xmiles.android.support.imageloader.*;
//-----
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class RelListAdapter extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	//----------
	private List<LikeItem> likeItems;
	private List<CommentItem> commentItems;
	private List<SupportRelAdapterItem> supportreladapterItems;
	//----------	
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	//private static ImageLoader imgLoader;
	
	//TAG
	private static final String TAG = "FACEBOOK";
	
	//--------------------------
	private static final Integer TYPE1   = 1;
	private static final Integer TYPE2   = 2;	
	private static final Integer TYPE3   = 3;
	private static final Integer TYPE4   = 4;
	//--------------------------

	//public RelListAdapter(Activity activity, List<FeedItem> feedItems) {
	//public RelListAdapter(Activity activity, List<FeedItem> feedItems, List<LikeItem> likeItems) {
	//public RelListAdapter(Activity activity, List<FeedItem> feedItems, List<SupportRelAdapterItem> supportreladapterItems) {	
	public RelListAdapter(Activity activity, List<FeedItem> feedItems, List<LikeItem> likeItems, List<CommentItem> commentItems, List<SupportRelAdapterItem> supportreladapterItems) {	
		this.activity = activity;
		this.feedItems = feedItems;
		//---------------
		this.likeItems = likeItems;		
		this.commentItems = commentItems;		
		this.supportreladapterItems = supportreladapterItems;
		//---------------		
		//imgLoader=new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {

		//return 3;
		//return 3 + commentItems.size();
		return supportreladapterItems.size();
	}

	@Override
	public Object getItem(int location) {
		//return feedItems.get(location);
		return null;
		
	}
	
	
	@Override
	public int getItemViewType(int position) {
		
		SupportRelAdapterItem supportreladapter_item = supportreladapterItems.get(position);
		
		//Log.e(TAG, "supportreladapter_item.getType_action(): " + supportreladapter_item.getType_action());
		
		if (supportreladapter_item.getType_action().equals("newsfeed")){
			return TYPE1;
		} else if (supportreladapter_item.getType_action().equals("likes")){
			return TYPE2;
		} else if (supportreladapter_item.getType_action().equals("header_comments")){
			return TYPE3;
		} else if (supportreladapter_item.getType_action().equals("comments")){
			return TYPE4;
		} else {		
		  return TYPE3;
		}
	
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
		//*
		int viewType = this.getItemViewType(position);
		
        View vi1=convertView;        
        View vi2=convertView;
        View vi3=convertView;
        View vi4=convertView;
        
        vi1 = inflater.inflate(R.layout.feed_item, null);        
        vi2 = inflater.inflate(R.layout.likes_item, null);
        vi3 = inflater.inflate(R.layout.comment_header, null);
        vi4 = inflater.inflate(R.layout.comment_item, null);
        
        Support support = new Support();
        //------------------------------------
		//vi1 gadgets
		TextView vi1_name = (TextView) vi1.findViewById(R.id.name);
		TextView vi1_timestamp = (TextView) vi1
				.findViewById(R.id.timestamp);
		TextView vi1_statusMsg = (TextView) vi1
				.findViewById(R.id.txtStatusMsg);
		//-------------------
		TextView vi1_rel_stats = (TextView) vi1
				.findViewById(R.id.rel_stats);		
		//-------------------
		TextView vi1_hashtag_1 = (TextView) vi1.findViewById(R.id.hashtag_1);
		TextView vi1_hashtag_2 = (TextView) vi1.findViewById(R.id.hashtag_2);
		//-------------------
		TextView vi1_url = (TextView) vi1.findViewById(R.id.txtUrl);
		NetworkImageView vi1_profilePic = (NetworkImageView) vi1
				.findViewById(R.id.profilePic);
		FeedImageView vi1_feedImageView = (FeedImageView) vi1
				.findViewById(R.id.feedImage1);

		//Buttons
		Button vi1_btn_like    = (Button) vi1.findViewById(R.id.Button_like);
		Button vi1_btn_comment = (Button) vi1.findViewById(R.id.Button_comment);
		
		vi1_btn_like.setVisibility(View.GONE);
		vi1_btn_comment.setVisibility(View.GONE);
		
		//View
		View vw01 = (View) vi1.findViewById(R.id.View01);
		vw01.setVisibility(View.GONE);

		//vi2 gadgets
		NetworkImageView vi2_profilePic_1 = (NetworkImageView) vi2.findViewById(R.id.profilePic_1);
		NetworkImageView vi2_profilePic_2 = (NetworkImageView) vi2.findViewById(R.id.profilePic_2);
		NetworkImageView vi2_profilePic_3 = (NetworkImageView) vi2.findViewById(R.id.profilePic_3);
		NetworkImageView vi2_profilePic_4 = (NetworkImageView) vi2.findViewById(R.id.profilePic_4);
		
		ImageView vi2_profilePic_5 = (ImageView) vi2.findViewById(R.id.profilePic_5);
		vi2_profilePic_5.setVisibility(View.GONE);
		
		LinearLayout vi2_frame_pictures   = (LinearLayout) vi2.findViewById(R.id.frame_pictures);

		//vi4 gadgets
		TextView vi4_name = (TextView) vi4.findViewById(R.id.name);
		TextView vi4_timestamp = (TextView) vi4.findViewById(R.id.timestamp);
		TextView vi4_statusMsg = (TextView) vi4.findViewById(R.id.txtStatusMsg);	
		
		NetworkImageView vi4_profilePic = (NetworkImageView) vi4.findViewById(R.id.profilePic);
		
		
		
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
        
        //------------------------------------
	    switch(viewType){
	       case 1:
	    	   	
				FeedItem feed_item = feedItems.get(position);
				
				vi1_name.setText(feed_item.getName());		 
			 	
				// like, comments stats
				vi1_rel_stats.setText(feed_item.getLike_stats() + " curtida(s) " +  feed_item.getComment_stats() + " comentário(s)");
				
				// Making comments unclickable
				vi1_rel_stats.setClickable(false);
							
				// Converting timestamp into x ago format
				CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
						Long.parseLong(support.getDateTime_long(feed_item.getTimeStamp())),
						System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
				vi1_timestamp.setText(timeAgo);
		
		
				// Chcek for empty status message
				if (!TextUtils.isEmpty(feed_item.getStatus())) {
					
					if (feed_item.getStatus().indexOf("<bold>") != -1){
						vi1_statusMsg.setText(Html.fromHtml(feed_item.getStatus().split("<bold>")[0] +
								                       "<b>" + feed_item.getStatus().split("<bold>")[1] + "</b>" +
								                       feed_item.getStatus().split("<bold>")[2]));
					} else {
						vi1_statusMsg.setText(feed_item.getStatus());
					}

					vi1_statusMsg.setVisibility(View.VISIBLE);
				} else {
					// status is empty, remove from view
					vi1_statusMsg.setVisibility(View.GONE);
				}
		
				// Checking for null feed url
				//if (feed_item.getUrl() != null) {
				if (feed_item.getUrl() != null && !feed_item.getUrl().equals("")) {	
					vi1_url.setText(Html.fromHtml("<a href=\"" + feed_item.getUrl() + "\">"
							+ feed_item.getUrl() + "</a> "));
		
					// Making url clickable
					vi1_url.setMovementMethod(LinkMovementMethod.getInstance());
					vi1_url.setVisibility(View.VISIBLE);
				} else {
					
					if (feed_item.getName().equals("xMiles")){
		
						vi1_url.setText(Html.fromHtml("<a href=\"" + "http://ec2-54-209-160-58.compute-1.amazonaws.com/pictures/xmiles_logo_rev05_transparente.png" + "\">"
								+ "www.xmiles.com.br" + "</a> "));
		
						// Making url clickable
						vi1_url.setMovementMethod(LinkMovementMethod.getInstance());
						vi1_url.setVisibility(View.VISIBLE);
						
					} else {
						// url is null, remove from the view
						vi1_url.setVisibility(View.GONE);
					}	
				}
		
				
				// Checking for null hashtag
				if (feed_item.getHashtag_1() != null && !feed_item.getHashtag_1().equals("")) {	
					
					//hashtag_1.setText(item.getHashtag_1());
					vi1_hashtag_1.setText(feed_item.getHashtag_1().split(",")[0]);
					vi1_hashtag_1.setVisibility(View.VISIBLE);
					
					if (feed_item.getHashtag_1().split(",").length > 1) {

						//Log.e(TAG,"item.getHashtag_1().split(,)[1]: " + feed_item.getHashtag_1().split(",")[1]);
						
						vi1_hashtag_2.setText(feed_item.getHashtag_1().split(",")[1]);
						vi1_hashtag_2.setVisibility(View.VISIBLE);
					} else {
						vi1_hashtag_2.setVisibility(View.GONE);
					}

					
				} else {
					
					// hashtag is null, remove from the view
					vi1_hashtag_1.setVisibility(View.GONE);
					vi1_hashtag_2.setVisibility(View.GONE);	
				}				
				
				// user profile pic
				vi1_profilePic.setImageUrl(feed_item.getProfilePic(), imageLoader);
		
				// Feed image
				if (feed_item.getImge() != null && !feed_item.getImge().equals("")) {	
					vi1_feedImageView.setImageUrl(feed_item.getImge(), imageLoader);
					vi1_feedImageView.setVisibility(View.VISIBLE);
					vi1_feedImageView
							.setResponseObserver(new FeedImageView.ResponseObserver() {
								@Override
								public void onError() {
								}
		
								@Override
								public void onSuccess() {
								}
							});
				} else {
					vi1_feedImageView.setVisibility(View.GONE);
				}

		        return vi1;
	

	       case 2:

	    	   vi2_frame_pictures.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
	    		    	//Your code goes here
	    		    	//------------			        	
					    Intent intent = new Intent(activity, Likes.class);
					    activity.startActivity(intent);

					}
	    	   });	

	    	   
	    	   
				for (int i = 0; i < likeItems.size(); i++) {
					
					LikeItem like_item = likeItems.get(i);
					//-------
					Log.e(TAG,"like_item.getName(): " + like_item.getName());
					Log.e(TAG,"like_item.getProfilePic(): " + like_item.getProfilePic());
					//-------					
				    switch(i){
				       case 0:
				    	    
				    	  vi2_profilePic_1.setImageUrl(like_item.getProfilePic(), imageLoader);
				    	  //String lala = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/v/t1.0-1/c170.50.621.621/s50x50/581112_142269442578251_461870301_n.jpg?oh=5714be4f31d258143d42628755f496b9&oe=57D72315&__gda__=1473270343_e4c5c633409e3f96ce5791349c6fa8f7";
				    	  //vi2_profilePic_1.setImageUrl(lala, imageLoader);
				    	  break;
				       case 1:

					      vi2_profilePic_2.setImageUrl(like_item.getProfilePic(), imageLoader);				    	   
					      break;
				       case 2:

					      vi2_profilePic_3.setImageUrl(like_item.getProfilePic(), imageLoader);
					      break;
				       case 3:

					      vi2_profilePic_4.setImageUrl(like_item.getProfilePic(), imageLoader);				    	   
					      break;
				       case 4:

					      vi2_profilePic_5.setVisibility(View.VISIBLE);
					      break;
					      
				       default:
				            break;
				    } 
				}
	
		        return vi2;
		        
	       case 3:
	    	   
		        return vi3;
		        
		        
	       case 4:
	    	    
	    	    int index;
	    	    index = 1;
	    	    
	    	    if (likeItems.size() > 0 && commentItems.size() > 1 ) {	    	    	
	    	    	index = 3; 
	    	    	
	    	    } else if (likeItems.size() > 0 && commentItems.size() > 0 ) {		    	  
		    	    index = 3;
		    	    
	    	    } else if (feedItems.size() > 0 && commentItems.size() > 1 ) {
	    	    	index = 2;
	    	    	
	    	    } else if (feedItems.size() > 0 && commentItems.size() > 0 ) {	    	    	
	    	    	index = 2;
	    	    	
	    	    }

    	    	CommentItem comment_item = commentItems.get(position - index);
	    	    
				vi4_name.setText(comment_item.getName());		 

				// Converting timestamp into x ago format
				CharSequence vi4_timeAgo = DateUtils.getRelativeTimeSpanString(
						Long.parseLong(support.getDateTime_long(comment_item.getTimeStamp())),
						System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
				vi4_timestamp.setText(vi4_timeAgo);
				
				//*
				// Chcek for empty status message
				if (!TextUtils.isEmpty(comment_item.getComment())) {
					
					vi4_statusMsg.setText(comment_item.getComment());
					vi4_statusMsg.setVisibility(View.VISIBLE);
				} else {
					// status is empty, remove from view
					vi4_statusMsg.setVisibility(View.GONE);
				}
				//*/
				// user profile pic
				vi4_profilePic.setImageUrl(comment_item.getProfilePic(), imageLoader);
	    	    
		        return vi4;	    	   
     
		        
	       default:
	            break;
	        	   
	    }     
        return null;
      
 
	}
	

}
