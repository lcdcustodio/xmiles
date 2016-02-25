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

public class RelListAdapter_backup_rev02 extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	//------------------
	private int pos;
	private Cursor data_newsfeed;
	//------------------	
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
	//---------------------	
	private static final Integer TYPE1   = 1;
	private static final Integer TYPE2   = 2;	
	private static final Integer TYPE3   = 3;


	public RelListAdapter_backup_rev02(Activity activity, int pos) {
	//public RelListAdapter(Activity activity, List<FeedItem> feedItems, int pos) {
	//public RelListAdapter(Activity activity, List<FeedItem> feedItems) {	
		this.activity = activity;
		this.feedItems = feedItems;
		this.pos = pos;
		
        Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
        data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);

        data_newsfeed.moveToPosition(pos);
        
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		imageLoader = AppController.getInstance().getImageLoader();

		
	}

	@Override
	public int getCount() {
		//return feedItems.size();
		//return feedItems.size() + 1;
		return 2;
		//return 0;
		//return 5;
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
	public int getItemViewType(int position) {
		/*
	    switch(position){
	       case 1:
	    	   return TYPE1; 
	       case 2:
	    	   return TYPE2;
	    	   
	       default:
	            break;
	        	   
	    }
		return TYPE3;
		*/
		//*
	    switch(position){
	       case 0:
	    	   return TYPE1; 
	       case 1:
	    	   return TYPE2;
	    	   
	       default:
	            break;
	        	   
	    }
		return TYPE3;
		//*/
		
		
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int viewType = this.getItemViewType(position);
		
        View vi1=convertView;
        View vi2=convertView;
        
		
		if (convertView == null)
		
			vi1 = inflater.inflate(R.layout.feed_item, null);
			vi2 = inflater.inflate(R.layout.likes_header, null);
			
			//---------------------
			//*
			TextView name = (TextView) vi1.findViewById(R.id.name);
			//-------------------
			TextView rel_stats = (TextView) vi1
					.findViewById(R.id.rel_stats);		
			//-------------------



	    switch(viewType){
	       case 1:
	    	   
			   name.setText(data_newsfeed.getString(KEY_NAME));				
			   // like, comments stats
			   rel_stats.setText(data_newsfeed.getString(KEY_LIKE_STATS) + " curtida(s) " +  data_newsfeed.getString(KEY_COMMENT_STATS) + " comentário(s)");

			   return vi1;


	       case 2:
	    	   
	    	   return vi2;
				
		   default:
		        break;
	    	   
	    }

		return null;
	}

}
