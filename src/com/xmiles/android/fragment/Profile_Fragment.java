package com.xmiles.android.fragment;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import com.xmiles.android.Users;
import com.xmiles.android.Welcome;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.adapter.ImageAdapter;
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
	private static final Integer MAX_POINTS = 2;
	
	private static final Integer KEY_BUSCODE 	 = 2;
	private static final Integer KEY_B_LATITUDE  = 4;
	private static final Integer KEY_B_LONGITUDE = 5;
	private static final Integer KEY_U_DIFF_TIME = 7;
	private static final Integer KEY_U_DIFF_DISTANCE = 6;
	private static final Integer KEY_U_LOCATION_PROVIDER = 4;
	
	private static final Integer index_BUSCODE   = 1;
	private static final Integer index_BUSLINE   = 2;
	private static GoogleMap mMap;
	// --- 
	TextView tv_busline;
	TextView tv_buscode;
	TextView tv_p_connected;
	TextView tv_info;
	RelativeLayout frame_bus;
	
	Cursor data_profile;

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
		} else{
			
			//renderMarker marker = new renderMarker();
			//gps.getLocation(2);
			
			//Log.w(TAG, "gps.getLatitude(): " + gps.getLatitude());			
			//Log.w(TAG, "gps.getLongitude(): " + gps.getLongitude());
		}
        
	    tv_busline = (TextView) ViewMap.findViewById(R.id.busline);	    
	    tv_buscode = (TextView) ViewMap.findViewById(R.id.buscode);	    
	    tv_p_connected = (TextView) ViewMap.findViewById(R.id.people_connected);
	    tv_info = (TextView) ViewMap.findViewById(R.id.info);	    
	    frame_bus = (RelativeLayout) ViewMap.findViewById(R.id.frame_bus);
	    
	    frame_bus.setVisibility(View.INVISIBLE);
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
            				frame_bus.setVisibility(View.VISIBLE);
            				//-----------------------

							String json_header = json.getString("COLUMNS");
	
							if (!json_header.equals("[\"MENSAGEM\"]")){
								
								Toast.makeText(getActivity(), searchContent + " encontrado no sistema!", Toast.LENGTH_SHORT).show();

								Log.i("FACEBOOK", "getBusPosition: " + json.getString("DATA"));
								
								String [] dataBusArray = json.getString("DATA").substring(2, json.getString("DATA").length()-2).split(",");
								
								/** Setting up values to insert into UserProfile table */		
								//----------------------
								//----------------------
								ContentValues cV = new ContentValues();
								cV.put(DatabaseHelper.KEY_BUSCODE, dataBusArray[index_BUSCODE].replace("\"",""));
								cV.put(DatabaseHelper.KEY_URL, url);
								
								getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_URL_insert, cV);
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
	            
		        //--------------------------
		        //----FAKE BUS POSITION-----
		        //--------------------------	        	        
				// lets place some 05 random markers
	            /*
				for (int i = 0; i < 5; i++) {
					// random latitude and logitude
					double[] randomLocation = createRandLocation(location.getLatitude(),
							location.getLongitude());
					
		            // Adding a marker
					Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(randomLocation[0], randomLocation[1]))
							.title("Pessoas no ônibus")						
							.snippet("Clique aqui e veja que está no ônibus")							
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_gmaps_icon_yellow)));
					
					marker.showInfoWindow();	

				 }	
				 */
	             /*
	            
                 Uri uri_2 = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
	        	 Cursor data_GpsBusData = getActivity().getContentResolver().query(uri_2, null, null, null, null);
	        	 //-------------			        	
	             Uri uri_3 = SqliteProvider.CONTENT_URI_USER_LOCATION;
	        	 Cursor data_UserLocation = getActivity().getContentResolver().query(uri_3, null, null, null, null);			        	

	        	 if (data_GpsBusData.getCount()>0 && data_UserLocation.getCount()>0){
	        		 
			         // switch Off gmaps update
			         mMap.setOnMyLocationChangeListener(null);
	        		 
	        		 data_GpsBusData.moveToLast();
	        		 data_UserLocation.moveToLast();
			         
	        		 // Adding a marker
					 Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(data_GpsBusData.getDouble(KEY_B_LATITUDE), 
								data_GpsBusData.getDouble(KEY_B_LONGITUDE)))
								.title("Distancia: " + data_UserLocation.getString(KEY_U_DIFF_DISTANCE))						
								.snippet("TimeOffset: " + data_UserLocation.getString(KEY_U_DIFF_TIME))							
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_gmaps_icon_blue)));
						
					 marker.showInfoWindow();

	        	 }

		         */	            
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

	 public class renderMarker{
		 
		 
		 public renderMarker(){
		 
		     Thread thread = new Thread(new Runnable(){
			    @Override
			    public void run() {
			        try {

			        	//Your code goes here
			        	/*
			        	jsonArray = null;
			        	
			        	UserFunctions userFunc = new UserFunctions();
			        	json = userFunc.bus_stop(bl, ct);

			        	jsonArray = new JSONArray(json.getString("busline"));
			        	*/
			        	//Log.i(TAG,"testing 1: " + jsonArray.get(1));

			        
			    		        	
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
			//----------
			runThread();			 

		 }
		 
	        private void runThread() {

	            new Thread() {
	                public void run() {

	                        try {
	                        	getActivity().runOnUiThread(new Runnable() {

	                                @Override
	                                public void run() {
	                                	//renderMarkerOptions(jsonArray);
	                                	//renderFbPlaces();
	                        			//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),
	                        			//		gps.getLongitude()), 11.0f));
	                        			//Log.w(TAG, "gps.getLatitude(): " + gps.getLatitude());			
	                        			//Log.w(TAG, "gps.getLongitude(): " + gps.getLongitude());


	                                }
	                            });
	                            Thread.sleep(400);
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
		
		//Uri uri = SqliteProvider.CONTENT_URI_USER_PLACES;		
		//return new CursorLoader(getActivity(), uri, null, null, null, null);

		Uri uri = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
		return new CursorLoader(getActivity(), uri, null, null, null, null);
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Profile_Fgmnt onLoadFinished");
		if(data.moveToFirst()){
			//city.setText(data.getString(KEY_CITY) + " - " + data.getString(KEY_UF));
		}
		
        Uri uri_2 = SqliteProvider.CONTENT_URI_BUS_GPS_DATA;
   	    Cursor data_GpsBusData = getActivity().getContentResolver().query(uri_2, null, null, null, null);
   	    //-------------			        	
        Uri uri_3 = SqliteProvider.CONTENT_URI_USER_LOCATION;
   	    Cursor data_UserLocation = getActivity().getContentResolver().query(uri_3, null, null, null, null);			        	

	   	if (data_GpsBusData.getCount()>0 && data_UserLocation.getCount()>0){
	   		 
		   		 
	   		data_GpsBusData.moveToLast();
	   		data_UserLocation.moveToLast();
		    
	   		mMap.clear();
	   		// Adding a marker	   		
			Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(data_GpsBusData.getDouble(KEY_B_LATITUDE), 
							data_GpsBusData.getDouble(KEY_B_LONGITUDE)))
							.title(data_GpsBusData.getString(KEY_BUSCODE))						
							.snippet("Clique e veja quem está no ônibus")							
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_gmaps_icon_blue)));
					
			marker.showInfoWindow();
			
			Log.w(TAG, "data_GpsBusData.getInt(KEY_ID): " + data_GpsBusData.getInt(KEY_ID));
			
			if (data_GpsBusData.getInt(KEY_ID) <= MAX_POINTS ) {
			
				//tv_busline
				tv_buscode.setText("Distancia (km): " + data_UserLocation.getString(KEY_U_DIFF_DISTANCE));
				//if (Double.parseDouble(data_UserLocation.getString(KEY_U_DIFF_DISTANCE))<5){
					tv_busline.setText("#hop: " + data_GpsBusData.getString(KEY_ID)); 
				//} else {
					//tv_busline.setText("");
				//}
				tv_p_connected.setText("TimeOffset (sec): " + data_UserLocation.getString(KEY_U_DIFF_TIME));
				tv_info.setText("Location Source: " + data_UserLocation.getString(KEY_U_LOCATION_PROVIDER));		    
			    //tv_info.setText(getActivity().getString(R.string.sample_string));
			} else {
				frame_bus.setVisibility(View.INVISIBLE);
				/*
				tv_busline.setText("Você está desconectado do ônibus");
				tv_buscode.setText("");
				tv_p_connected.setText("");
				tv_info.setText("");
				*/
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
        //Intent intent = new Intent(getActivity(), NewRoutes.class);
		

        Intent intent = new Intent(getActivity(), Users.class);
        startActivity(intent);
	}

	/*
	 * creating random postion around a location for testing purpose only
	 */
	private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10) };
	}


}

