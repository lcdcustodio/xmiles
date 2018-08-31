package com.xmiles.android.support.imageloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.xmiles.android.R;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.LoadImageURL;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
	
	//TAG
	private static final String TAG = "FACEBOOK";

	private Cursor users_info;
	private Cursor ranking_info;
    
    public RankingLazyAdapter(Context context, Cursor data) {
    	ctx = context;

        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(ctx.getApplicationContext());
        
        ranking_info = data;
                
        //Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
        //users_info = ctx.getContentResolver().query(uri_1, null, null, null, null);
        
    }

    public int getCount() {
    	return 1 + ranking_info.getCount();
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
        
    	View vi	  		 	   = convertView;
        View vi_header		   = convertView;
        View vi_footer		   = convertView;
        
        //View vi_header_profile = convertView;
    	//View vi_profile 	   = convertView;
        
        int viewType = this.getItemViewType(position);

        //if(convertView==null)

        vi 		  		  = inflater.inflate(R.layout.ranking_item, null);
        vi_header 		  = inflater.inflate(R.layout.ranking_header_item, null);
        //vi_header_profile = inflater.inflate(R.layout.ranking_header_item, null);
        //vi_profile 	   	  = inflater.inflate(R.layout.ranking_item, null);
        vi_footer		  = inflater.inflate(R.layout.ranking_footer_item, null);
        
        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        TextView rnk_header = (TextView)vi_header.findViewById(R.id.rnk_header_item); // title
        //-----------
        
	    switch(viewType){
	    

	       case 1:

		    	if (ranking_info.getCount() < 6){
		    		rnk_header.setText("Ranking - Top 5");
		    	} else if (ranking_info.getCount() < 11){
		    		rnk_header.setText("Ranking - Top 10");
		    	} else if (ranking_info.getCount() < 21){
		    		rnk_header.setText("Ranking - Top 20");		    		
		    	} else if (ranking_info.getCount() < 51){
		    		rnk_header.setText("Ranking - Top 50");
		    	}
		    	   
				return vi_header;

	       case 2:        

		        ranking_info.moveToPosition(position - 1 );
		        //ranking_info.moveToPosition(position - 4 );
		        
		        // Setting all values in listview
		        title.setText(ranking_info.getString(KEY_NAME));
		        artist.setText(ranking_info.getString(KEY_SCORE) + " pontos");     
		        duration.setText(position + " ° lugar");

		        imageLoader.DisplayImage(ranking_info.getString(KEY_PICURL), thumb_image);
		        //----------
		        //Log.e(TAG, "ranking_info.getString(KEY_NAME): " + ranking_info.getString(KEY_NAME));
		        //Log.w(TAG, "ranking_info.getString(KEY_PICURL): " + ranking_info.getString(KEY_PICURL));
		        //----------
		        return vi;
	        
	       default:
	            break;
	        	   
	    }     
        return null;    
		
    }
}