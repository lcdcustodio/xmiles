package com.xmiles.android.slidingmenu.adapter;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.xmiles.android.R;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.imageloader.ImageLoader;


import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SlidingMenuLazyAdapter extends BaseAdapter {
    
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    private static final String TAG = "FACEBOOK";
    
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;
	//--------
	private static final Integer KEY_REWARD = 0;
	private static final Integer KEY_REWARD_TYPE = 1;
	private static final Integer KEY_SCORE = 3;
	private static final Integer KEY_RANK = 4;	
	private static final Integer KEY_QUANTITY = 4;
	
    private static final Integer KEY_ID = 0;
	//private static final Integer KEY_NAME = 1;
	//private static final Integer KEY_PICURL = 2;
	//private static final Integer KEY_SCORE = 3;
	
	
	private Cursor users_info;
	//private Cursor rewards_info;
	private Cursor ranking;
	//--------------------------
	//--------------------------
	private Context ctx;
	//private Handler mHandler;
	//--------------------------
	//--------------------------
	private static final Integer TYPE1   = 1;
	private static final Integer TYPE2   = 2;	
	private static final Integer TYPE3   = 3;
	//--------------------------
	

	public SlidingMenuLazyAdapter(Context context) {
        

		this.ctx = context;
		
		//mHandler = new Handler();
		
		imageLoader=new ImageLoader(ctx.getApplicationContext());
       
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
        //Uri uri_2 = SqliteProvider.CONTENT_URI_REWARDS;
        Uri uri_2b = SqliteProvider.CONTENT_URI_RANKING;
        
        users_info = ctx.getContentResolver().query(uri_1, null, null, null, null);        
        //rewards_info = ctx.getContentResolver().query(uri_2, null, null, null, null);
        ranking = ctx.getContentResolver().query(uri_2b, null, null, null, null);
        
        Log.w(TAG, "ranking.getCount(): " + ranking.getCount());

    }

    public int getCount() {
    	//return TYPE3;    	
    	//return 2 + rewards_info.getCount();
    	return 2 + ranking.getCount(); 
    	

    }

    public Object getItem(int position) {
    	return null;
    }

    public long getItemId(int position) {
        return position;
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

    
    public View getView(int position, View convertView, ViewGroup parent) {
    	
		int viewType = this.getItemViewType(position);
		
        View vi1=convertView;
        View vi2=convertView;
        
        View vi3=convertView;
        
        //Log.e(TAG, "position: " + position);
        
        //if(convertView==null)
        	
        	vi1 = inflater.inflate(R.layout.sliding_menu_profile, null);
        	vi2 = inflater.inflate(R.layout.sliding_menu_rewards_header, null);

        	vi3 = inflater.inflate(R.layout.sliding_menu_rewards, null);

	    	TextView title = (TextView)vi1.findViewById(R.id.title); // title
		    TextView artist = (TextView)vi1.findViewById(R.id.artist); // artist name		        
		    TextView rank = (TextView)vi1.findViewById(R.id.rank); // rank
		    ImageView profile_pic = (ImageView) vi1.findViewById(R.id.list_image);

	        TextView reward_vi3 = (TextView)vi3.findViewById(R.id.reward);
	        TextView type_vi3   = (TextView)vi3.findViewById(R.id.type);
	        ImageView reward_image_vi3 = (ImageView) vi3.findViewById(R.id.reward_image);
	        TextView score_vi3	= (TextView)vi3.findViewById(R.id.reward_score);
	        //TextView total_vi3	= (TextView)vi3.findViewById(R.id.total);

	    switch(viewType){
	       case 1:

		        users_info.moveToLast();
		        
		        // Setting all values in listview
		        title.setText(users_info.getString(KEY_NAME));
		        
		        if (users_info.isNull(KEY_SCORE)) {
		        	artist.setText("0 pontos");
		        } else {
		        	artist.setText(users_info.getString(KEY_SCORE) + " pontos");
		        }
		        
		        if (users_info.isNull(KEY_RANK)){
		        	rank.setText("");
		        } else {
		        	rank.setText(users_info.getString(KEY_RANK) + "° no ranking");	
		        }
		        
		        
				Drawable drawable = null;
				try {
					drawable = new LoadImageURL(users_info.getString(KEY_PICURL),ctx).execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				profile_pic.setImageDrawable(drawable);
				
		        return vi1;
		        
		        
	       case 2:
		        
		        return vi2;

	       case 3:
		        
	    	    ranking.moveToPosition(position - 2);
		        
	    	    int pos = position - 1;
	    	    
		        // Setting all values in listview
		        reward_vi3.setText(ranking.getString(KEY_NAME));
		        type_vi3.setText(pos + " ° lugar");
		        
		        
		        
		        score_vi3.setText(ranking.getString(KEY_SCORE) + " pontos");
		        //total_vi3.setText(rewards_info.getString(KEY_QUANTITY));


				Drawable drawable_vi3 = null;
				try {
					drawable_vi3 = new LoadImageURL(ranking.getString(KEY_PICURL),ctx).execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				reward_image_vi3.setImageDrawable(drawable_vi3);
		        
		        return vi3;		        

        
	       default:
	            break;
	        	   
	    }
		return null;
		
    }
    

}