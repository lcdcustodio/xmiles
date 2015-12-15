package com.xmiles.android.slidingmenu.adapter;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.xmiles.android.R;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.imageloader.ImageLoader;




import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlidingMenuLazyAdapter extends BaseAdapter {
    
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    private static final String TAG = "FACEBOOK";
    
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;	
	private Cursor users_info;
	//--------------------------
	//--------------------------
	private Context ctx;
	private Handler mHandler;
	//--------------------------
	//--------------------------
	protected static final Integer TYPE1 = 1;
	private static final Integer TYPE2   = 2;	
	private static final Integer TYPE3   = 3;
	//--------------------------
	

	public SlidingMenuLazyAdapter(Context context) {
    //public SlidingMenuLazyAdapter(Context context, Cursor data) {
    //public SlidingMenuLazyAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){	
        
        //users_info = data;
		this.ctx = context;
		
		mHandler = new Handler();
		//this.navDrawerItems = navDrawerItems;
       
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(ctx.getApplicationContext());
        
        Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;

        users_info = ctx.getContentResolver().query(uri, null, null, null, null);
        //data_userFavorites.moveToFirst();

    }

    public int getCount() {
        //return data.size();
    	return TYPE2;
    	//return navDrawerItems.size();
    }

    public Object getItem(int position) {
        //return position;
        //return navDrawerItems.get(position);
    	return null;
    }

    public long getItemId(int position) {
        return position;
    }
    
	@Override
	public int getItemViewType(int position) {
		//return TYPE1;
		//if (position < jsonArray.length() ){
		//*
		if  (position < TYPE1 ){	
			return TYPE1;
		} else {
			return TYPE2;
		}
		//*/
	}

    
    public View getView(int position, View convertView, ViewGroup parent) {
    	
		int viewType = this.getItemViewType(position);

	    switch(viewType){
	       case 1:
    	
		        View vi1=convertView;
		        //if(convertView==null)
		        //    vi1 = inflater.inflate(R.layout.sliding_menu_profile, null);
		        
				if (inflater == null){
					inflater = (LayoutInflater) ctx
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				}
				
				vi1 = inflater.inflate(R.layout.sliding_menu_profile, null);
		
		        TextView title = (TextView)vi1.findViewById(R.id.title); // title
		        TextView artist = (TextView)vi1.findViewById(R.id.artist); // artist name
		        

		        final ImageView profile_pic = (ImageView) vi1.findViewById(R.id.list_image);
		        
		        users_info.moveToLast();
		        
		        // Setting all values in listview
		        title.setText(users_info.getString(KEY_NAME));
		        artist.setText("Pontuação: 0 km");
		        
		        Log.v(TAG, "users_info.getString(KEY_PICURL): " + users_info.getString(KEY_PICURL));
		        
		        //imageLoader.DisplayImage(users_info.getString(KEY_PICURL), thumb_image);
		        mHandler.post(new Runnable() {	
		            @Override
		            public void run() {
		            	//imageLoader.DisplayImage(users_info.getString(KEY_PICURL), thumb_image);
			            ///*
				    	Drawable drawable;
						try {
							drawable = new LoadImageURL(users_info.getString(KEY_PICURL),ctx).execute().get();
							profile_pic.setImageDrawable(drawable);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	    			                    	
			    		//*/	                   
		            }
		        });
		
		        
		        return vi1;
		        
		        
	       case 2:
	       	
		        View vi2=convertView;

				if (inflater == null){
					inflater = (LayoutInflater) ctx
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				}
				
				vi2 = inflater.inflate(R.layout.sliding_menu_rewards_header, null);
		
		
		        
		        return vi2;
     
        
	       default:
	            break;
	        	   
	    }
		return null;
		
    }
}