package com.xmiles.android;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;



import com.xmiles.android.R;
import com.xmiles.android.adapter.ImageAdapter;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import android.view.View;



import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {

	GridView gridView;
	private static final String TAG = "FACEBOOK";
	static final String[] MOBILE_OS = new String[] { "Dashboard", "Reports",
	//static final String[] MOBILE_OS = new String[] { "Replay", "Reports",	
			"Live Fleet", "Alerts" };
	//-------------------------------------
	protected ListView vehicles;
	protected ListView alerts;
	//-------------------------------------
	//-------------------------------------	
    ProgressDialog progressdialog;
    //public Map<String, Integer> map_livefleet = new HashMap<String, Integer>();
    public Map<Integer, ArrayList<String>> map_livefleet = new HashMap<Integer, ArrayList<String>>();
	//-------------------------------------
    final int MARKER_UPDATE_INTERVAL = 20000; /* milliseconds */
    //----------------
    
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
	
		
		setContentView(R.layout.main);

		gridView = (GridView) findViewById(R.id.gridView1);

		gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
                        //------------------------------------------------		                
                        //------------------------------------------------		                
                        //progressdialog = ProgressDialog.show(Main.this, "",
                        //getString(R.string.please_wait), true, true);
                        //------------------------------------------------		                
                        //------------------------------------------------					

				switch(position){
				 case 0:{
					 
						Toast.makeText(
								getApplicationContext(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
					
					break;					 
				 }
				 case 1:{
					 
						Toast.makeText(
								getApplicationContext(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
					
					break;				
				 }				
				 case 2:{

						Toast.makeText(
								getApplicationContext(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();

					 
				 	break; 
				 }
				 case 3:{
					 
						Toast.makeText(
								getApplicationContext(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
						
					break;					 
				  }				 
				}				
			}
		});
		
	}
	
	   @Override
	    public void onDestroy() {

	        Log.d(TAG, "LiveFleet: onDestroy");
	        Toast.makeText(this, "Main onDestroy", Toast.LENGTH_SHORT).show();
	        super.onDestroy();
	    }
    
}
