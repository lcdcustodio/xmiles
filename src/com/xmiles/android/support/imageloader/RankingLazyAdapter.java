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
    	return ranking_info.getCount();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.users_custom_listview, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        //ImageView reward_image_vi3 = (ImageView) vi.findViewById(R.id.list_image);
        
        ranking_info.moveToPosition(position);
        
        // Setting all values in listview
        title.setText(ranking_info.getString(KEY_NAME));
        artist.setText(ranking_info.getString(KEY_SCORE) + " pontos");     
        duration.setText(position + 1 + " ° lugar");
        imageLoader.DisplayImage(ranking_info.getString(KEY_PICURL), thumb_image);
        
        /*
		Drawable drawable_vi3 = null;
		try {
			drawable_vi3 = new LoadImageURL(ranking_info.getString(KEY_PICURL),ctx).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		reward_image_vi3.setImageDrawable(drawable_vi3);
		*/
        
        return vi;
    }
}