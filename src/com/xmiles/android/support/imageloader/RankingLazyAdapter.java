package com.xmiles.android.support.imageloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.xmiles.android.R;
import com.xmiles.android.support.LoadImageURL;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RankingLazyAdapter extends BaseAdapter {
    
	private Context ctx;
    private ArrayList<HashMap<String, String>> data;
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    private static final Integer KEY_ID = 0;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;
	private static final Integer KEY_SCORE = 3;
	//-----
	private static final Integer TYPE1   = 1;
	private static final Integer TYPE2   = 2;	
	//-----
	
	private Cursor ranking_info;
    
    public RankingLazyAdapter(Context context, Cursor data) {
    	ctx = context;

        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(ctx.getApplicationContext());
        
        ranking_info = data; 	 
    }

    public int getCount() {
        //return data.size();
    	//return 5;
    	//return ranking_info.getCount();
    	return 1 + ranking_info.getCount();
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

        //if(convertView==null)

        vi 		  = inflater.inflate(R.layout.ranking_item, null);
        vi_header = inflater.inflate(R.layout.ranking_header_item, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

		//rnk_header_item
        TextView rnk_header = (TextView)vi_header.findViewById(R.id.rnk_header_item); // title
        
		//*
	    switch(viewType){
	       case 1:
		
			//vw01.setVisibility(View.GONE);
	    	if (ranking_info.getCount() < 6){
	    		rnk_header.setText("TOP 5");
	    	} else if (ranking_info.getCount() < 11){
	    		rnk_header.setText("TOP 10");
	    	}
	    	   
			return vi_header;

	       case 2:        
	        //ranking_info.moveToPosition(position);
	        ranking_info.moveToPosition(position - 1 );
	        
	        // Setting all values in listview
	        title.setText(ranking_info.getString(KEY_NAME));
	        artist.setText(ranking_info.getString(KEY_SCORE) + " pontos");     
	        //duration.setText(position + 1 + " ° lugar");
	        duration.setText(position + " ° lugar");
	        imageLoader.DisplayImage(ranking_info.getString(KEY_PICURL), thumb_image);
	        

	        
	        return vi;
	        
	       default:
	            break;
	        	   
	    }     
       return null;    
		
    }
}