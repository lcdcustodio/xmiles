package com.xmiles.android;



import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.xmiles.android.backup.FbPlaces_Download;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.facebook_places.Facebook_Places;
import com.xmiles.android.fragment.NoInternetConnection_Fragment;
import com.xmiles.android.fragment.Push_Fragment;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.ConnectionDetector;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.GetDistance;
import com.xmiles.android.support.GetGpsToken;
import com.xmiles.android.support.Score_Algorithm;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.UserFunctions;
import com.xmiles.android.scheduler.NewsFeed_Inbox_Upload;

import com.xmiles.android.scheduler.Getting_GpsBusData;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Gmaps extends FragmentActivity implements OnInfoWindowClickListener, OnMapReadyCallback{
//public class Gmaps extends FragmentActivity {


	private static final String TAG = "FACEBOOK";
	private static GoogleMap mMap;

	private static final Integer MAX_DIST 	  = 3; //3km
	private static final Integer MAX_TIME_OFFSET = 300; //300secs = 5min


	private static final Integer index_BUSCODE   = 1;
	private static final Integer index_LATITUDE  = 3;
	private static final Integer index_LONGITUDE = 4;



	AutoCompleteTextView buscode_search;

	Spinner dialog_cities;
	ProgressDialog progressBar;

	

	RelativeLayout rel_body;
	LinearLayout header;
	RelativeLayout footer;
	TextView tv_bug_msg;
	//----------

	private static final Integer KEY_ID = 0;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICTURE = 2;

	private static final Integer KEY_NEARBY = 1;
	private static final Integer KEY_NEARBY_PICURL = 6;


	private static final Integer KEY_BUSCODE  = 1;
	private static final Integer KEY_BUS_TYPE = 2;
	private static final Integer KEY_BUSLINE  = 2;
	private static final Integer KEY_BUSLINE_DESCRIPTION = 3;
	private static final Integer KEY_BUSLINE_COMPANY = 4;
	private static final Integer KEY_HASHTAG = 5;

	//protected static JSONArray jsonArray;
	protected static JSONObject json_buscode;

    // GPSTracker class
	GPSTracker gps;

    // Score Algorithm class
	Score_Algorithm sca;


	// Connection detector
	ConnectionDetector cd;

	
	public Gmaps(){}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmaps);
        

        ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    //MapFragment mapFragment = (MapFragment) getFragmentManager()
	    //        .findFragmentById(R.id.gmap_addroutes);
	    
		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.gmap_addroutes);
			
	   	//Progress Dialog
        progressBar = new ProgressDialog(Gmaps.this);
		progressBar.setCancelable(true);
		progressBar.setMessage(Gmaps.this.getString(R.string.please_wait));
		progressBar.show();
	
		fragment.getMapAsync(this);
		//mapFragment.getMapAsync(this);
	}
	
	@Override
	public void onMapReady(GoogleMap gmaps) {
	
		//mMap = fragment.getMap();
		mMap = gmaps;
    	mMap.setMyLocationEnabled(true);
    	
    	/*
	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            if (!isFinishing()) {
	            	
	            	progressBar.dismiss();
	            	
	            }
	        }
	    }, 5000);
		*/

    	mMap.setOnInfoWindowClickListener(this);
    	mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    	//-------------------------------
    	rel_body = (RelativeLayout) findViewById(R.id.rel01);
    	header   = (LinearLayout) findViewById(R.id.header);
    	footer = (RelativeLayout) findViewById(R.id.footer);
    	tv_bug_msg = (TextView)  findViewById(R.id.tv_bug_msg);
    	//-------------------------------
        //Check Service Location
		gps = new GPSTracker(getApplicationContext());
		gps.getLocation(0);
        
        //if(!gps.canGetGPSLocation()){
        if(!gps.canGetGPSLocation() || !gps.canGetNW_Location()){        	
			gps.showSettingsAlert();
		}
        //-------
        buscode_search = (AutoCompleteTextView) findViewById(R.id.search);
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

                	
    				cd = new ConnectionDetector(getApplicationContext());
    				
    				// Check if Internet present
    				if (!cd.isConnectingToInternet()) {
    		 
    			        //----do something---
    					Toast.makeText(getApplicationContext(), getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
    					finish();
    
    		        	
    		        } else {

                	
	                	//Progress Dialog
	                    progressBar = new ProgressDialog(Gmaps.this);
	            		progressBar.setCancelable(true);
	            		progressBar.setMessage(Gmaps.this.getString(R.string.please_wait));
	            		progressBar.show();
           		
            		
	                	//Hide keyboard
	                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
	                            Context.INPUT_METHOD_SERVICE);
	                    imm.hideSoftInputFromWindow(buscode_search.getWindowToken(), 0);
	
	                    //get buscode digits
	                    String buscode_digits = getBuscode_digits(searchContent);
	
	            		// get Buscode Details
	            		//runBuscodeDetails_thread(searchContent);
	
	                    //Check Service Location
	            		gps.getLocation(0);
	
	                    if(!gps.canGetGPSLocation()){
	            			gps.showSettingsAlert();
	            		} else {
	            			
	            			
	            			sca = new Score_Algorithm(getApplicationContext());
	
	            			String buscode_uppercase = searchContent.toUpperCase();
	
	            			JSONObject json = sca.getApiBusPosition(buscode_digits, buscode_uppercase);

	            			try {
	
	            				//-----------------------
	            				progressBar.dismiss();
	            				mMap.clear();
	            				//-----------------------
	
	            				/*
	            				 * If Login success = 1 then GET Blabla and later
	            				 */
	                            if(Integer.parseInt(json.getString("success")) == 1){
	                            	JSONArray jArray = new JSONArray(json.getString("api_buscode"));
	
	      					        LatLng loc = new LatLng(Double.parseDouble(jArray.getJSONObject(0).getString("latitude")),
				        						Double.parseDouble(jArray.getJSONObject(0).getString("longitude")));
	
							   		// Adding a marker
	      					        String bus_type = jArray.getJSONObject(0).getString("bus_type");
	
	      					        if (bus_type.equals("BUS")){
	      					        	bus_type = "ônibus";
	      					        }
	
									Marker marker = mMap.addMarker(new MarkerOptions().position(loc)
													.title(bus_type + " " + jArray.getJSONObject(0).getString("buscode") + " localizado")
													.snippet(getApplicationContext().getString(R.string.busmsg1))
													.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_gmaps_icon_blue)));
	
									mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 11.0f));
	
	
									marker.showInfoWindow();
	
									// Add a circle
									mMap.addCircle(new CircleOptions()
									     .center(loc)
									     .radius(3000)  //set radius in meters
									     .strokeWidth(0)
										 .fillColor(Color.argb(50, 0, 0, 0)));
	
									//----------------------
	      							//----------------------
	      							ContentValues cV = new ContentValues();
	      							cV.put(DatabaseHelper.KEY_BUSCODE, jArray.getJSONObject(0).getString("buscode"));
	      							//cV.put(DatabaseHelper.KEY_URL, url);
	
	      							cV.put(DatabaseHelper.KEY_BUS_TYPE, bus_type);
	      							//----------------------------
	      							cV.put(DatabaseHelper.KEY_FLAG, 0);
	      							//----------------------------
	      							//----------------------
	      					        Uri uri_0 = SqliteProvider.CONTENT_URI_USER_PROFILE;
	      					        Cursor data_profile = getApplicationContext().getContentResolver().query(uri_0, null, null, null, null);
	      					        data_profile.moveToFirst();      							
	      							//hashcode - android side      							
	      							GetGpsToken gt = new GetGpsToken();
	      							cV.put(DatabaseHelper.KEY_HASHCODE, gt.md5(data_profile.getString(KEY_ID) + System.currentTimeMillis()));
	      							//----------------------
	      							//----------------------
	      							getApplicationContext().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_URL_insert, cV);
	      							//----------------------
	
	      							rel_body.setBackgroundColor(getResources().getColor(R.color.feed_item_bg));
	      							
	      							final int sdk = android.os.Build.VERSION.SDK_INT;
	      							if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
	          							footer.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_botton));
	          							header.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_top));      							
	      							} else {
	          							footer.setBackground( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_botton));
	          							header.setBackground( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_top));      							
	      							}
	
	      						} else {
	
	      							rel_body.setBackgroundColor(getResources().getColor(R.color.red));
	      							
	      							tv_bug_msg.setText("Veículo " + searchContent + " não localizado!");
	      							
	      							final int sdk = android.os.Build.VERSION.SDK_INT;
	      							if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
	          							footer.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_botton_red));
	          							header.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_top_red));      							
	      							} else {
	          							footer.setBackground( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_botton_red));
	          							header.setBackground( getResources().getDrawable(R.drawable.bg_parent_rounded_corner_top_red));      							
	      							}
	      							
	      							//sca.GpsNotFound(url, searchContent);
	      							sca.GpsNotFound("xMiles_ApiBus", searchContent);
	      							//-----------------------
	
	
	      						}
	
	      					} catch (JSONException e) {
	      						// TODO Auto-generated catch block
	      						e.printStackTrace();
	      					}
	            		}
	                //---
    		        }
    				//---
                }
                return false;
            }

        });


	}
	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		
		cd = new ConnectionDetector(getApplicationContext());
		
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
 
	        //----do something---
			Toast.makeText(getApplicationContext(), getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

        	
        } else {


		
	        //-------------------
			Getting_GpsBusData gbd = new Getting_GpsBusData();
			gbd.setAlarm(getApplicationContext());
			//-------------------
	        Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
	        Cursor data_profile = getApplicationContext().getContentResolver().query(uri_1, null, null, null, null);
	        data_profile.moveToFirst();
	
	        Uri uri_2 = SqliteProvider.CONTENT_URI_BUS_GPS_URL;
			Cursor bus_gps_url = getApplicationContext().getContentResolver().query(uri_2, null, null, null, null);
			bus_gps_url.moveToLast();
	
	
	        Uri uri_3 = SqliteProvider.CONTENT_URI_USER_PLACES;
			Cursor fb_places = getApplicationContext().getContentResolver().query(uri_3, null, null, null, null);
			fb_places.moveToFirst();
			/*
	        Uri uri_4 = SqliteProvider.CONTENT_URI_BUSCODE;
			Cursor buscode_info = getApplicationContext().getContentResolver().query(uri_4, null, null, null, null);
			buscode_info.moveToFirst();
			*/
	
	        Support support = new Support();
	
			ContentValues contentValues = new ContentValues();
	
			contentValues.put(DatabaseHelper.KEY_ID, "-1");
			contentValues.put(DatabaseHelper.KEY_NAME, data_profile.getString(KEY_NAME));
			//---------------
			//*
			
			String status_buscode = "Conectado(a) ao " + bus_gps_url.getString(KEY_BUS_TYPE) + " <bold>" + bus_gps_url.getString(KEY_BUSCODE) + "<bold>";
			//String status_buscode = "Conectado ao ônibus <bold>" + bus_gps_url.getString(KEY_BUSCODE) + "<bold>";
			String status_nearby = "";
			String status_buscode_details = "";
			//----------------
	
			//----------------
			if (fb_places.getCount() > 0){
				//status_nearby = " próximo ao " + fb_places.getString(KEY_NEARBY);
				status_nearby = " próximo - " + fb_places.getString(KEY_NEARBY);
			}
			/*
			if (buscode_info.getCount() > 0){
				status_buscode = "Conectado(a) ao " + bus_gps_url.getString(KEY_BUS_TYPE) + " <bold>" + bus_gps_url.getString(KEY_BUSCODE);
				//status_buscode = "Conectado ao ônibus <bold>" + bus_gps_url.getString(KEY_BUSCODE);		
				status_buscode_details = " da linha " + buscode_info.getString(KEY_BUSLINE) + "<bold>";
			}
			*/
	
			String status = status_buscode + status_buscode_details + status_nearby;
	
			if (status_buscode_details.equals("")){
				contentValues.put(DatabaseHelper.KEY_HASHTAG, "#" + bus_gps_url.getString(KEY_BUSCODE).toUpperCase());
				//contentValues.put(DatabaseHelper.KEY_HASHTAG, "#" + bus_gps_url.getString(KEY_BUSCODE).toLowerCase());
			}/* else {
				contentValues.put(DatabaseHelper.KEY_HASHTAG, "#" + bus_gps_url.getString(KEY_BUSCODE).toUpperCase() +
				//contentValues.put(DatabaseHelper.KEY_HASHTAG, "#" + bus_gps_url.getString(KEY_BUSCODE).toLowerCase() +
						"," + buscode_info.getString(KEY_HASHTAG));
			}
			*/
			contentValues.put(DatabaseHelper.KEY_STATUS, status);
	
			contentValues.put(DatabaseHelper.KEY_PICURL, data_profile.getString(KEY_PICTURE));
			contentValues.put(DatabaseHelper.KEY_TIME_STAMP, support.getDateTime());
			//----------------------------
	
			String checkin = "http://maps.googleapis.com/maps/api/staticmap?zoom=16&size=560x240&markers=size:mid|color:red|"
			         + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude() + "&sensor=false";
			
			//*/
			contentValues.put(DatabaseHelper.KEY_IMAGE, checkin);
	
	
			//----------------------------
			//News Feed Upload
			//----------------------------
			//contentValues.put(DatabaseHelper.KEY_FLAG_ACTION, "ADD");
			//contentValues.put(DatabaseHelper.KEY_FEED_TYPE, "User gets into the bus");
			//contentValues.put(DatabaseHelper.KEY_LIKE_STATS, "0");
			//contentValues.put(DatabaseHelper.KEY_COMMENT_STATS, "0");
			//----------------------------
			// like, comments stats
			contentValues.put(DatabaseHelper.KEY_LIKE_STATS, "0");
			contentValues.put(DatabaseHelper.KEY_COMMENT_STATS, "0");
	
			//sender
			contentValues.put(DatabaseHelper.KEY_SENDER, data_profile.getString(KEY_ID));
	
			//you_like_this
			contentValues.put(DatabaseHelper.KEY_YOU_LIKE_THIS, "NO");
	
			//feed_type
			contentValues.put(DatabaseHelper.KEY_FEED_TYPE, "User gets into the bus");
	
			getApplicationContext().getContentResolver().insert(SqliteProvider.CONTENT_URI_NEWSFEED_insert, contentValues);
			getApplicationContext().getContentResolver().insert(SqliteProvider.CONTENT_URI_NEWSFEED_UPLOAD_insert, contentValues);
	
			//---------------------------------------
			//-----------------------------
	    	Intent intent=new Intent("feedfragmentupdater");
	    	getApplicationContext().sendBroadcast(intent);
			//-----------------------------
			//---------------------------------------
			NewsFeed_Inbox_Upload nfi = new NewsFeed_Inbox_Upload();
			nfi.setAlarm(getApplicationContext());
	    }	
		//---------------------------------------
		//---------------------------------------
		finish();
		//---------------------------------------
		//---------------------------------------
		
		
		
	}
	public void runBuscodeDetails_thread(final String buscode){

		 Thread thread_1 = new Thread(new Runnable(){
			    @Override
			    public void run() {
			        try {

				    	//Your code goes here
				    	//------------
	                	DatabaseHelper mDatabaseHelper;
	                	mDatabaseHelper = new DatabaseHelper(getApplicationContext());
	                	mDatabaseHelper.resetBuscode();


            			UserFunctions userFunc = new UserFunctions();
            			Log.i(TAG, "buscode.toUpperCase(): " + buscode.toUpperCase());
            			json_buscode = userFunc.getBuscode_details(buscode.toUpperCase());

				    } catch (Exception e) {
				            e.printStackTrace();
				    }
				}
		});

		 thread_1.start();

		try {
			thread_1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//----------
		 Thread thread_2 = new Thread(new Runnable(){
			    @Override
			    public void run() {
			        try {

				    	//Your code goes here
				    	//------------

						if(Integer.parseInt(json_buscode.getString("success")) == 1){

  							ContentValues cV = new ContentValues();

							JSONArray jsonArray = new JSONArray(json_buscode.getString("buscode"));
							JSONObject jsonObject = jsonArray.getJSONObject(0);

							cV.put(DatabaseHelper.KEY_BUSCODE, jsonObject.getString("buscode"));
							cV.put(DatabaseHelper.KEY_BUSLINE, jsonObject.getString("busline"));
  							cV.put(DatabaseHelper.KEY_BUSLINE_DESCRIPTION, jsonObject.getString("busline_description"));
  							cV.put(DatabaseHelper.KEY_BUSLINE_COMPANY, jsonObject.getString("company"));
  							//-------------
  							cV.put(DatabaseHelper.KEY_HASHTAG, jsonObject.getString("hashtag"));
  							//-------------
  							getApplicationContext().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUSCODE_insert, cV);

						 }



				    } catch (Exception e) {
				            e.printStackTrace();
				    }
				}
		});

		 thread_2.start();


	}

	public String getBuscode_digits(String buscode){

		String result = "";
		char ch;
		for (int i = 0; i < buscode.length(); i++) {
			ch = buscode.charAt(i);

			if (Character.isDigit(ch)){

				result = result + ch;
			}

		}

		return result;

	}



	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.gmaps_type, menu);

	      return true;
	  }


	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
	    @Override
	    //public void onMyLocationChange(Location location) {
	    public void onMyLocationChange(final Location location) {

	        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

	        // switch Off gmaps update
	        mMap.setOnMyLocationChangeListener(null);
	        //--------------------------
	        //--------------------------
	        if(mMap != null){

	        	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.0f));
	        	
	        	progressBar.dismiss();
	        	
        		// start FbPlaces                
                //int MIN_DISTANCE = 1000;
            	/*
                 
            	try {
					new Facebook_Places(getApplicationContext(),Utility.mFacebook.getAccessToken(),location.getLatitude(),location.getLongitude(),MIN_DISTANCE).execute().get();
					//new Facebook_Places(getApplicationContext(),Utility.mFacebook.getAccessToken(),lat_test,long_test,MIN_DISTANCE).execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	*/
                
			    Thread thread_fbplaces = new Thread(new Runnable(){
			        @Override
			        public void run() {
			        	
		        		// start FbPlaces                
		                int MIN_DISTANCE = 1000;


					    try {
					    	new Facebook_Places(getApplicationContext(),Utility.mFacebook.getAccessToken(),location.getLatitude(),location.getLongitude(),MIN_DISTANCE).execute().get();
						
					    } catch (InterruptedException e) {
					    	// TODO Auto-generated catch block
					    	e.printStackTrace();
					    } catch (ExecutionException e) {
					    	// TODO Auto-generated catch block
					    	e.printStackTrace();
					    }

			        }

			    });

			    thread_fbplaces.start();			           		    	                    
  


	        }
	    }

	};




	/**** The mapfragment's id must be removed from the FragmentManager
	 **** or else if the same it is passed on the next time then
	 **** app will crash ****/
	  @Override
	  public void onDestroy() {

		  Log.i(TAG, "onDestroy Gmaps");

		super.onDestroy();
	}

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {

	        	case android.R.id.home:
	            	finish();
	            	break;


	            case R.id.satelite:


	            	mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	            	//return true;
	            	break;

	            case R.id.rua:
	            	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

	            	break;

	            case R.id.terreno:
	            	mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

	            	break;

	            default:
	            	return super.onOptionsItemSelected(item);
	        }
	        return true;
	    }







}