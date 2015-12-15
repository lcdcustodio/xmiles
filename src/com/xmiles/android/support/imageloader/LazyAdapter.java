package com.xmiles.android.support.imageloader;

import java.util.ArrayList;
import java.util.HashMap;

import com.xmiles.android.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;	
	private Cursor users_info;
    
    public LazyAdapter(Activity a, Cursor data) {
        activity = a;
        //data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        
        users_info = data; 	 
    }

    public int getCount() {
        //return data.size();
    	return 5;
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
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        //HashMap<String, String> song = new HashMap<String, String>();
        //song = data.get(position);
        
        users_info.moveToLast();
        
        // Setting all values in listview
        title.setText(users_info.getString(KEY_NAME));
        artist.setText("Pontuação: 0 km");
        duration.setText("Curtir");
        imageLoader.DisplayImage(users_info.getString(KEY_PICURL), thumb_image);
        return vi;
    }
}