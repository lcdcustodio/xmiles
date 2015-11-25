package com.xmiles.android.fragment;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.scheduler.Getting_GpsBusData;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;

import com.xmiles.android.R;
import com.xmiles.android.Welcome;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.adapter.ImageAdapter;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.Score_Algorithm;
import com.xmiles.android.support.Support;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Profile_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

	TextView name;
	TextView city;
	TextView score;
	ImageView mUserPic;
	GridView gridView;
	Button   score_btn;
	AutoCompleteTextView buscode_search;
	//TAG
	private static final String TAG = "FACEBOOK";
	String json_name;
	String json_id;
	String json_city;	
	String picURL;
	//ProgressDialog progressBar;  
	private Handler mHandler;
	//---
	private static final Integer KEY_UF   = 3;
	private static final Integer KEY_CITY = 2;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;
	
	
	private static final Integer index_STIME     = 0;
	private static final Integer index_BUSCODE   = 1;
	private static final Integer index_LATITUDE  = 3;
	private static final Integer index_LONGITUDE = 4;
	private static final Integer index_SPEED 	 = 5;
	private static final Integer index_DIRECTION = 6;
	private static final Integer index_BUSLINE	 = 2;

	
	Cursor data_profile;

    // GPSTracker class
	GPSTracker gps;
    // Score Algorithm class
	Score_Algorithm sca;
	
	static final String[] MOBILE_OS = new String[] { "Mapa", "Histórico" };

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		mHandler = new Handler();
		
		getActivity().registerReceiver(ProfileFragmentReceiver, new IntentFilter("profilefragmentupdater"));
		
		View custom = inflater.inflate(R.layout.fgmt_background, container, false);
        View rootView = inflater.inflate(R.layout.profile_fgmt_custom, container, false);
        
        name = (TextView) rootView.findViewById(R.id.name);
        score = (TextView) rootView.findViewById(R.id.locat);
        city = (TextView) rootView.findViewById(R.id.info);
        mUserPic = (ImageView) rootView.findViewById(R.id.profile_pic);
        //-------
        View rootView1 = inflater.inflate(R.layout.profile_score_button, container, false);
        score_btn = (Button) rootView1.findViewById(R.id.button1);
		//-------
        View rootView1b = inflater.inflate(R.layout.profile_buscode_autocomplete_textview, container, false);
        buscode_search = (AutoCompleteTextView) rootView1b.findViewById(R.id.search);
        //-------
		View rootView2 = inflater.inflate(R.layout.profile_fgmt_gridview, container, false);
		gridView = (GridView) rootView2.findViewById(R.id.gridView1);
		gridView.setAdapter(new ImageAdapter(getActivity(), MOBILE_OS));
		//----------------------
		//----------------------
	    //HANDLE EVENT - TYPE BUSCODE
		buscode_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchContent = buscode_search.getText().toString();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {//&&
                        //Util.isValidString(searchContent)) {

                    //Check Service Location
            		gps = new GPSTracker(getActivity());
            		gps.getLocation(0);

                    if(!gps.canGetGPSLocation()){	
            			gps.showSettingsAlert();
            		} else {
            			sca = new Score_Algorithm(getActivity());
            			
            			//JSONObject json = sca.getBusPosition("C41383");
            			Support support = new Support();
            			String url = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/onibus/";
            			JSONObject json = sca.getBusPosition(url, searchContent);
            			try {

							String json_header = json.getString("COLUMNS");
	
							if (!json_header.equals("[\"MENSAGEM\"]")){
								
								Toast.makeText(getActivity(), searchContent + " encontrado no sistema!", Toast.LENGTH_SHORT).show();

								Log.i("FACEBOOK", "getBusPosition: " + json.getString("DATA"));
								
								String [] dataBusArray = json.getString("DATA").substring(2, json.getString("DATA").length()-2).split(",");
								
								/** Setting up values to insert into UserProfile table */
								/*
								ContentValues contentValues = new ContentValues();
								contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.fixDateTime(dataBusArray[index_STIME].replace("\"","")));
								contentValues.put(DatabaseHelper.KEY_BUSCODE, dataBusArray[index_BUSCODE].replace("\"",""));
								contentValues.put(DatabaseHelper.KEY_B_LATITUDE, dataBusArray[index_LATITUDE]);
								contentValues.put(DatabaseHelper.KEY_B_LONGITUDE, dataBusArray[index_LONGITUDE]);																
								contentValues.put(DatabaseHelper.KEY_SPEED, dataBusArray[index_SPEED]);
								contentValues.put(DatabaseHelper.KEY_DIRECTION, dataBusArray[index_DIRECTION]);
								contentValues.put(DatabaseHelper.KEY_BUSLINE, dataBusArray[index_BUSLINE].replace("\"",""));								

								getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_DATA_insert, contentValues);
								*/
								//----------------------
								//----------------------
								ContentValues cV = new ContentValues();
								cV.put(DatabaseHelper.KEY_BUSCODE, dataBusArray[index_BUSCODE].replace("\"",""));
								cV.put(DatabaseHelper.KEY_URL, url);
								
								getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_URL_insert, cV);
								//----------------------
								//----------------------

								
								//GPS BUS DATA TEST 
								Getting_GpsBusData gbd = new Getting_GpsBusData();
								gbd.setAlarm(getActivity());

							} else {
								
								String url_brt = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/brt/";
								JSONObject json_brt = sca.getBrtPosition(url_brt, searchContent.substring(1, searchContent.length()));
								Log.v("FACEBOOK", "getBrtPosition: " + json_brt.getString("COLUMNS"));
								
								if (json_header.equals("[\"MENSAGEM\"]")){
									
									Toast.makeText(getActivity(), searchContent + " não encontrado no sistema!", Toast.LENGTH_SHORT).show();
								}


							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}


                }
                return false;
            }

        });		
        
        
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
             
				switch(position){
				 case 0:{
					 
						Toast.makeText(
								getActivity(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
					
					break;					 
				 }
				 case 1:{
					 
						Toast.makeText(
								getActivity(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
					
					break;				
				 }				
				}				
			}
		});
		
        //---------------		
		((ViewGroup) custom).addView(rootView);
		
		((ViewGroup) custom).addView(rootView1b);

		((ViewGroup) custom).addView(rootView2);
	
		((ViewGroup) custom).addView(rootView1);
        //------        
		//--------------		
		UserProfile_Query upq = new UserProfile_Query();
		//--------------
		
		/** Creating a loader for populating city TextView from sqlite database */
		getLoaderManager().initLoader(0, null, this);

        return custom;
        
    }
	
	
	private final BroadcastReceiver ProfileFragmentReceiver = new BroadcastReceiver() {


		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			getLoaderManager().restartLoader(0, null, Profile_Fragment.this);
		}};

	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        Log.i(TAG, "onDestroy Profile_fgmt");
	        //-------------
	        getActivity().unregisterReceiver(ProfileFragmentReceiver);
	        //-------------
	    }

	 public class UserProfile_Query {
		 
		 public UserProfile_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {
				            
				            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;

				            data_profile = getActivity().getContentResolver().query(uri, null, null, null, null);
				            data_profile.moveToFirst();
				    		        	
					    } catch (Exception e) {
					            e.printStackTrace();
					    }
					}
			});

			thread.start();
			//-----------
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//-----------			
			runThread();			 

		 }
		 
	        private void runThread() {

	            new Thread() {
	                public void run() {

	                        try {
	                        	getActivity().runOnUiThread(new Runnable() {

	                                @Override
	                                public void run() {
	                                	
	            	                	name.setText(data_profile.getString(KEY_NAME));
	            	                	score.setText("Pontuação: 0 km");
	    			                    	
	    			        		        mHandler.post(new Runnable() {	
	    			                            @Override
	    			                            public void run() {
	    			                            	try {
	    			                    	
				    			                    	Context c = getActivity();
				    			                    	//Log.e(LOG,"picURL: " + data_profile.getString(KEY_PICURL));
				    			                    	Drawable drawable = new LoadImageURL(data_profile.getString(KEY_PICURL),c).execute().get();	    			                    	
				    			                    	mUserPic.setImageDrawable(drawable);		                   
	    		    			                    } catch (Exception e) {
	    		    			        				// TODO Auto-generated catch block
	    		    			        				e.printStackTrace();
	    		    			                    }
	    			                    	
	    			                            }
	    			        		        });
	                                }
	                            });
	                            Thread.sleep(100);
	                            //progressBar.dismiss();
	                            
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                }
	            }.start();
	        }

	 }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		//return null;
		Uri uri = SqliteProvider.CONTENT_URI_USER_PLACES;
		
		return new CursorLoader(getActivity(), uri, null, null, null, null);

		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Profile_Fgmnt onLoadFinished");
		if(data.moveToFirst()){
			city.setText(data.getString(KEY_CITY) + " - " + data.getString(KEY_UF));
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}



}

