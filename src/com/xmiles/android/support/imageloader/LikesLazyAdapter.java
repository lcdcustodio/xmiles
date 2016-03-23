package com.xmiles.android.support.imageloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.xmiles.android.R;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.Support;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LikesLazyAdapter extends BaseAdapter {
    
	private Context ctx;
    private ArrayList<HashMap<String, String>> data;
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    
	private static final Integer KEY_NAME = 2;
	private static final Integer KEY_PICURL = 3;
	private static final Integer KEY_TIME_STAMP = 4;
	//-----
	private static final Integer TYPE1   = 1;
	private static final Integer TYPE2   = 2;	
	//-----
	
	private Cursor likes_info;
    
    public LikesLazyAdapter(Context context, Cursor data) {
    	ctx = context;

        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(ctx.getApplicationContext());
        
        likes_info = data; 	 
    }

    public int getCount() {
        //return data.size();
    	//return 5;
    	//return likes_info.getCount();
    	return 1 + likes_info.getCount();
    	//return 1;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public int getItemViewType(int position) {

		if (position == 0) {
			return TYPE1;
		} else {
			return TYPE2;
		}
    	
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View vi		  = convertView;
        View vi_header= convertView;
        
        int viewType = this.getItemViewType(position);


        vi 		  = inflater.inflate(R.layout.likes_fgmt_item, null);
        vi_header = inflater.inflate(R.layout.likes_fgmt_header_item, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title

        TextView time_stamp = (TextView)vi.findViewById(R.id.artist); 
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

		//rnk_header_item
        TextView rnk_header = (TextView)vi_header.findViewById(R.id.rnk_header_item); // title
        
		//*
	    switch(viewType){
	       case 1:
		
	    	rnk_header.setText("Curtidas");
	    	   
			return vi_header;

	       case 2:        
	        //likes_info.moveToPosition(position);
	        likes_info.moveToPosition(position - 1 );
	        
	        // Setting all values in listview
	        title.setText(likes_info.getString(KEY_NAME));
     
	        
	        imageLoader.DisplayImage(likes_info.getString(KEY_PICURL), thumb_image);
	        
			Support support = new Support();
			
			// Converting timestamp into x ago format
			CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
					Long.parseLong(support.getDateTime_long(likes_info.getString(KEY_TIME_STAMP))),
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
			//-----------------------
			//Log.i("FACEBOOK","likes_info.getString(KEY_TIME_STAMP):" + likes_info.getString(KEY_TIME_STAMP));
			time_stamp.setText(timeAgo);
			//time_stamp.setText("lugar");

	        
	        return vi;
	        
	       default:
	            break;
	        	   
	    }     
       return null;    
		
    }
}