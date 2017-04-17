package com.xmiles.android.support.imageloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.xmiles.android.R;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.Support;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileLazyAdapter extends BaseAdapter {
    
	private Context ctx;
    
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader; 
    private static ImageLoader imageLoader;
    
    private static final Integer KEY_ID = 0;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;
	private static final Integer KEY_SCORE = 3;
	
	private static final Integer KEY_RANK = 4;
	//-----
	private static final Integer TYPE1   = 1;
	private static final Integer TYPE2   = 2;
	private static final Integer TYPE3   = 3;
	private static final Integer TYPE4   = 4;
	private static final Integer TYPE5   = 5;
	//-----
	private static final Integer KEY_STATUS = 1;
	private static final Integer KEY_TIME_STAMP = 2;
	
	
	//TAG
	private static final String TAG = "FACEBOOK";

	private Cursor users_info;
	private Cursor history_info;
    
    public ProfileLazyAdapter(Context context, Cursor data) {
    	this.ctx = context;


        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(ctx.getApplicationContext());
        
                
        Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
        users_info = ctx.getContentResolver().query(uri_1, null, null, null, null);
        
        history_info = data;
        
    }

    public int getCount() {

    	//return 3;
    	int count;
    	if (history_info.getCount() == 0){
    		count = 3;
    	} else {
    		count = 4 + history_info.getCount();
    	}
    	return count;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public int getItemViewType(int position) {
 	
	    switch(position){
	       case 0:
	    	   return TYPE1; 
	       case 1:
	    	   return TYPE2;
	       case 2:
	    	   return TYPE3;
	       case 3:
	    	   return TYPE4;	    	   
	       default:
	            break;
	        	   
	    }
		return TYPE5;

    	
    	
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View vi	  		 	   = convertView;
        View vi_header		   = convertView;
        View vi_footer		   = convertView;
        
        View vi_header_profile = convertView;
    	View vi_profile 	   = convertView;
        
        int viewType = this.getItemViewType(position);

 
        vi 		  		  = inflater.inflate(R.layout.history_item, null);
        vi_header 		  = inflater.inflate(R.layout.ranking_header_item, null);
        
        vi_header_profile = inflater.inflate(R.layout.profile_header_item, null);        
        vi_profile 	   	  = inflater.inflate(R.layout.profile_item, null);
        vi_footer		  = inflater.inflate(R.layout.ranking_footer_item, null);
        
        TextView history_score = (TextView)vi.findViewById(R.id.score); // title
        TextView timestamp = (TextView)vi.findViewById(R.id.timestamp); // artist name
        

        TextView history_header = (TextView)vi_header.findViewById(R.id.rnk_header_item); // title
        //-----------
        
        //TextView profile_header = (TextView)vi_header_profile.findViewById(R.id.rnk_header_item); // title

        TextView  profile_name = (TextView)vi_profile.findViewById(R.id.title); // title
        TextView  score = (TextView)vi_profile.findViewById(R.id.artist); // artist name
        TextView  rnk = (TextView)vi_profile.findViewById(R.id.duration); // duration
        ImageView profile_pic=(ImageView)vi_profile.findViewById(R.id.list_image); // thumb image
        
        Support support = new Support();

        
		//*
	    switch(viewType){
	    
	       case 1:	    
	    	    //profile_header.setText("");

	    	    
	    	    return vi_header_profile;
	    	    
	       case 2:
	    	    users_info.moveToLast();
	    	    
	    	    profile_name.setText(users_info.getString(KEY_NAME));
	    	    
		        if (users_info.isNull(KEY_SCORE)) {
		        	score.setText(" ");
		        } else {
		        	score.setText(users_info.getString(KEY_SCORE) + " pontos");
		        }
	    	    
		        if (users_info.isNull(KEY_RANK)){
		        	rnk.setText("0 pontos");
		        } else {

		        	rnk.setText(users_info.getString(KEY_RANK) + "° lugar no ranking");
		        }
		        
		        imageLoader.DisplayImage(users_info.getString(KEY_PICURL), profile_pic);
		        
		        return vi_profile;
	       case 3:
		        return vi_footer;
		        
	       case 4:
	    	   
	    	    history_header.setText("Histórico");
	    	    
				return vi_header;

	       case 5:        


		        //----------
	    	    history_info.moveToPosition(position - 4);
	    	    //FeedItem item = feedItems.get(position - 4);
	    	   
	    	   
	    	    //history_score.setText(item.getStatus().split("<bold>")[1]);
	    	    history_score.setText(history_info.getString(KEY_STATUS).split("<bold>")[1]);
	    	    
	    		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
	    				Long.parseLong(support.getDateTime_long(history_info.getString(KEY_TIME_STAMP))),
	    				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

	    	    timestamp.setText(timeAgo);
	    	    
		        return vi;
	        
	       default:
	            break;
	        	   
	    }     
        return null;    
		
    }
}