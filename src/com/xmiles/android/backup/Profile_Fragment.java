package com.xmiles.android.backup;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xmiles.android.scheduler.Getting_GpsBusData;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;

import com.xmiles.android.NewRoutes;
import com.xmiles.android.R;
import com.xmiles.android.Relationship;
import com.xmiles.android.Welcome;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.Score_Algorithm;
import com.xmiles.android.webservice.UserFunctions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.InputType;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//public class Profile_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {
public class Profile_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnInfoWindowClickListener  {
	
	ProgressDialog progressBar;
		
	AutoCompleteTextView buscode_search;
	//TAG
	private static final String TAG = "FACEBOOK";
	String json_name;
	String json_id;
	String json_city;	
	String picURL;
	//---
	private static final Integer KEY_ID = 0;
	private static final Integer KEY_UF   = 3;
	private static final Integer KEY_CITY = 2;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;
	private static final Integer KEY_FLAG = 3;
	
	//private static final Integer MAX_POINTS = 4;
	private static final Integer MAX_POINTS = 1;
	private static final Integer MAX_DIST 	  = 3; //3km
	private static final Integer MAX_TIME_OFFSET = 300; //300secs = 5min
	
	private static final Integer KEY_BUSCODE 	 = 2;
	private static final Integer KEY_B_LATITUDE  = 4;
	private static final Integer KEY_B_LONGITUDE = 5;
	private static final Integer KEY_U_DIFF_TIME = 7;
	private static final Integer KEY_U_DIFF_DISTANCE = 6;
	private static final Integer KEY_U_LOCATION_PROVIDER = 4;
	
	private static final Integer index_BUSCODE   = 1;
	private static final Integer index_BUSLINE   = 2;
	
	private static final Integer index_LATITUDE  = 3;
	private static final Integer index_LONGITUDE = 4;
	
	private static GoogleMap mMap;
	// --- 
	TextView tv_busline;
	TextView tv_flag;
	TextView tv_buscode;
	TextView tv_p_connected;
	TextView tv_info;
	RelativeLayout frame_bus;


    // GPSTracker class
	GPSTracker gps;
    // Score Algorithm class
	Score_Algorithm sca;
	

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		getActivity().registerReceiver(ProfileFragmentReceiver, new IntentFilter("profilefragmentupdater"));
		
		View ViewRoot = inflater.inflate(R.layout.fgmt_background, container, false);
		View ViewMap = inflater.inflate(R.layout.profile_gmaps_fgmt, container, false);
		
		FragmentManager fm = getChildFragmentManager();
		SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.gmap_addroutes);

    	mMap = fragment.getMap();       
    	mMap.setMyLocationEnabled(true);
    	mMap.setOnInfoWindowClickListener(this);
    	
    	mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    	
        //Check Service Location
		gps = new GPSTracker(getActivity());
		gps.getLocation(0);

        if(!gps.canGetGPSLocation()){	
			gps.showSettingsAlert();
		} 
        
	    tv_busline = (TextView) ViewMap.findViewById(R.id.busline);
	    tv_flag = (TextView) ViewMap.findViewById(R.id.flag);
	    tv_buscode = (TextView) ViewMap.findViewById(R.id.buscode);	    
	    tv_p_connected = (TextView) ViewMap.findViewById(R.id.people_connected);
	    tv_info = (TextView) ViewMap.findViewById(R.id.info);	    
	    frame_bus = (RelativeLayout) ViewMap.findViewById(R.id.frame_bus);

	    frame_bus.setVisibility(View.INVISIBLE);
        //-------	    
	    Log.w(TAG, "frame_bus.getVisibility(): " + frame_bus.getVisibility());
        //-------		
        buscode_search = (AutoCompleteTextView) ViewMap.findViewById(R.id.search);
        //-------
        
		//----------------------
		//----------------------
	    //HANDLE EVENT - TYPE BUSCODE
		buscode_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchContent = buscode_search.getText().toString();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	
                	//Progress Dialog
                    progressBar = new ProgressDialog(getActivity());
            		progressBar.setCancelable(true);
            		progressBar.setMessage(getActivity().getString(R.string.please_wait));
            		progressBar.show();

                	//Hide keyboard                	
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(buscode_search.getWindowToken(), 0);
                	
                    //Check Service Location            		
            		gps.getLocation(0);

                    if(!gps.canGetGPSLocation()){	
            			gps.showSettingsAlert();
            		} else {
            			sca = new Score_Algorithm(getActivity());
            			
            			//"C41383"            			
            			String url = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/onibus/";
            			JSONObject json = sca.getBusPosition(url, searchContent);
            			try {
            				//-----------------------
            				progressBar.dismiss();  
            				mMap.clear();
            				//-----------------------

							String json_header = json.getString("COLUMNS");
	
							if (!json_header.equals("[\"MENSAGEM\"]")){
								
								frame_bus.setVisibility(View.VISIBLE);
						        //-------	    
							    Log.w(TAG, "frame_bus.getVisibility(): " + frame_bus.getVisibility());
						        //-------		


								Log.i("FACEBOOK", "getBusPosition: " + json.getString("DATA"));
								
								String [] dataBusArray = json.getString("DATA").substring(2, json.getString("DATA").length()-2).split(",");
								
								/** Setting up values to insert into UserProfile table */		
								//----------------------
								//----------------------
								ContentValues cV = new ContentValues();
								cV.put(DatabaseHelper.KEY_BUSCODE, dataBusArray[index_BUSCODE].replace("\"",""));
								cV.put(DatabaseHelper.KEY_URL, url);
								//----------------------------
								cV.put(DatabaseHelper.KEY_FLAG, 0);
								//----------------------------								
								getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_URL_insert, cV);
								//----------------------
								//Toast.makeText(getActivity(), "Ônibus " + dataBusArray[index_BUSCODE].replace("\"","") + " encontrado", Toast.LENGTH_LONG).show();
								tv_busline.setText("Ônibus " + dataBusArray[index_BUSCODE].replace("\"","") + " encontrado");
								tv_flag.setText("");
								tv_buscode.setText("");
								tv_info.setText("");
								tv_p_connected.setText("");
								//----------------------
						   		mMap.clear();
						   		
						        LatLng loc = new LatLng(Double.parseDouble(dataBusArray[index_LATITUDE]), 
						        						Double.parseDouble(dataBusArray[index_LONGITUDE]));

						   		// Adding a marker	   		
								Marker marker = mMap.addMarker(new MarkerOptions().position(loc)
												.title(dataBusArray[index_BUSCODE].replace("\"",""))						
												.snippet(getActivity().getString(R.string.busmsg1))							
												.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_gmaps_icon_yellow)));
								
								mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 11.0f));
								
								mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
									 
						            // Use default InfoWindow frame
						            @Override
						            public View getInfoWindow(Marker arg0) {

						                // Getting view from the layout file info_window_layout
						                View v = getActivity().getLayoutInflater().inflate(R.layout.profile_infowindow_bus_found, null);
						                
						                TextView bus_stop = (TextView) v.findViewById(R.id.text_1);		                
										bus_stop.setText(arg0.getTitle());						               
						 
						                // Returning the view containing InfoWindow contents
						                return v;
						            }
						 
						            // Defines the contents of the InfoWindow
						            @Override
						            public View getInfoContents(Marker arg0) {
						 

						                return null;
						 
						            }
						        });
										
								marker.showInfoWindow();


							} else {
								
								//String url_brt = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/brt/";
								//JSONObject json_brt = sca.getBrtPosition(url_brt, searchContent.substring(1, searchContent.length()));
								//Log.v("FACEBOOK", "getBrtPosition: " + json_brt.getString("COLUMNS"));
								
								if (json_header.equals("[\"MENSAGEM\"]")){
									
									Toast.makeText(getActivity(), searchContent + " não encontrado no sistema!", Toast.LENGTH_LONG).show();
									frame_bus.setVisibility(View.INVISIBLE);
									//-----------------------
									sca.GpsNotFound(url, searchContent);
									//-----------------------
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
		
        //---------------		
		((ViewGroup) ViewRoot).addView(ViewMap);
        //------        
		
		/** Creating a loader for populating city TextView from sqlite database */
		getLoaderManager().initLoader(0, null, this);

        return ViewRoot;
        
    }
	
	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
	    @Override
	    public void onMyLocationChange(Location location) {
	    	
	        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

	        // switch Off gmaps update
	        mMap.setOnMyLocationChangeListener(null);
	        //--------------------------
	        //--------------------------
	        if(mMap != null){

	        	//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.0f));
	            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 11.0f));
	                   
	        }
	    }

	};

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


	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub

		Uri uri = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
		return new CursorLoader(getActivity(), uri, null, null, null, null);
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Profile_Fgmnt onLoadFinished");
		
        Uri uri_2 = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
   	    Cursor data_GpsBusData = getActivity().getContentResolver().query(uri_2, null, null, null, null);
   	    //-------------			        	
        Uri uri_3 = SqliteProvider.CONTENT_URI_USER_LOCATION;
   	    Cursor data_UserLocation = getActivity().getContentResolver().query(uri_3, null, null, null, null);
   	    //-------------
		Uri uri_4 = SqliteProvider.CONTENT_URI_BUS_GPS_URL;
		Cursor bus_gps_url = getActivity().getContentResolver().query(uri_4, null, null, null, null);
		bus_gps_url.moveToLast();


	   	if (data_GpsBusData.getCount()>0 && data_UserLocation.getCount()>0){
	   		 
		   		 
	   		data_GpsBusData.moveToLast();
	   		data_UserLocation.moveToLast();
		    
	   		mMap.clear();
	   		// Adding a marker	   		
			Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(data_GpsBusData.getDouble(KEY_B_LATITUDE), 
							data_GpsBusData.getDouble(KEY_B_LONGITUDE)))
							.title(data_GpsBusData.getString(KEY_BUSCODE))						
							.snippet(getActivity().getString(R.string.busmsg2))							
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_gmaps_icon_blue)));
			
			mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
				 
	            @Override
	            public View getInfoWindow(Marker arg0) {
	                // Getting view from the layout file info_window_layout
	                View v = getActivity().getLayoutInflater().inflate(R.layout.profile_infowindow_score, null);
	               
	                // Returning the view containing InfoWindow contents
	                return v;
	            }
	 
	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker arg0) {

	                return null;
	 
	            }
	        });
			
			marker.showInfoWindow();
			
			if (frame_bus.getVisibility() != 0 ){
				
				frame_bus.setVisibility(View.VISIBLE);
				buscode_search.setEnabled(false);
				//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data_GpsBusData.getDouble(KEY_B_LATITUDE), 
				//		data_GpsBusData.getDouble(KEY_B_LONGITUDE)), 11.0f));
			}
			
			//if (data_UserLocation.getDouble(KEY_U_DIFF_DISTANCE) <= MAX_DIST &&
			//		 data_UserLocation.getDouble(KEY_U_DIFF_TIME) <= MAX_TIME_OFFSET) {
			if (bus_gps_url.getInt(KEY_FLAG) <= MAX_POINTS) {	
			//if (data_GpsBusData.getInt(KEY_ID) <= MAX_POINTS ) {
			
				//tv_busline
				tv_buscode.setText("Distancia (km): " + data_UserLocation.getString(KEY_U_DIFF_DISTANCE));
				tv_busline.setText("#hop: " + data_GpsBusData.getString(KEY_ID));
				tv_flag.setText("#hop(D>3km TO>5min): " + bus_gps_url.getInt(KEY_FLAG));
				tv_p_connected.setText("TimeOffset (sec): " + data_UserLocation.getString(KEY_U_DIFF_TIME));
				tv_info.setText("Location Source: " + data_UserLocation.getString(KEY_U_LOCATION_PROVIDER));		    

			} else {

                try {
                	getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
            				frame_bus.setVisibility(View.INVISIBLE);
            				buscode_search.setEnabled(true);
            				//buscode_search.setVisibility(View.VISIBLE);    
            				mMap.clear();
                        }
                    });
                    Thread.sleep(100);
                 
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
				

			}
	
	   	 }
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub		
		
		
		if (arg0.getSnippet().equals(getActivity().getString(R.string.busmsg1))){

			
			//*
			buscode_search.setEnabled(false);
			
			
			Getting_GpsBusData gbd = new Getting_GpsBusData();
			gbd.setAlarm(getActivity());
			//*/
	        //Intent intent = new Intent(getActivity(), Users.class);
	        //startActivity(intent);
			

		} else if(arg0.getSnippet().equals(getActivity().getString(R.string.busmsg2))) {
			
	        Intent intent = new Intent(getActivity(), Relationship.class);
	        startActivity(intent);
	        //--------------------
	        Toast.makeText(getActivity(), "Em construção", Toast.LENGTH_LONG).show();
	        //--------------------	        
			
		}

	}



}

