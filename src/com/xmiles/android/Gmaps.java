package com.xmiles.android;


import java.math.BigInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.GPSTracker;
import com.xmiles.android.support.GetDistance;
import com.xmiles.android.support.Score_Algorithm;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.UserFunctions;
import com.xmiles.android.scheduler.FbPlaces_Download;
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
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



//public class Gmaps extends FragmentActivity implements OnInfoWindowClickListener{
public class Gmaps extends FragmentActivity {


	private static final String TAG = "FACEBOOK";
	private static GoogleMap mMap;
	
	private static final Integer MAX_DIST 	  = 3; //3km
	private static final Integer MAX_TIME_OFFSET = 300; //300secs = 5min

		
	private static final Integer index_BUSCODE   = 1;	
	private static final Integer index_LATITUDE  = 3;
	private static final Integer index_LONGITUDE = 4;
	
	FbPlaces_Download FbPlaces;
	
	AutoCompleteTextView buscode_search;
	
	Spinner dialog_cities;
	ProgressDialog progressBar;
	TextView tv_busline;	
	TextView tv_from;
	TextView tv_to;

	Button connect;
	//----------
	String buscode;
	//----------
	
	private static final Integer KEY_ID = 0;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICTURE = 2;
	
	private static final Integer KEY_NEARBY = 1;
	private static final Integer KEY_NEARBY_PICURL = 6;
	
	
	private static final Integer KEY_BUSCODE = 1;
	private static final Integer KEY_BUSLINE = 2;
	private static final Integer KEY_BUSLINE_DESCRIPTION = 3;
	private static final Integer KEY_BUSLINE_COMPANY = 4;

	//protected static JSONArray jsonArray;
	protected static JSONObject json_buscode;
	
    // GPSTracker class
	GPSTracker gps;

    // Score Algorithm class
	Score_Algorithm sca;


	public Gmaps(){}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmaps_fgmt);

        ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	 
	    tv_busline = (TextView) findViewById(R.id.busline);
	    
	    tv_from = (TextView) findViewById(R.id._de);
	    tv_to = (TextView) findViewById(R.id.info);	    
	    //----
		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.gmap_addroutes);

    	mMap = fragment.getMap();       
    	mMap.setMyLocationEnabled(true);    	
    	//mMap.setOnInfoWindowClickListener(this);
    	mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    	//-------------------------------
		sca = new Score_Algorithm(getApplicationContext());
	    
    	//-------------------------------
        //Check Service Location
		gps = new GPSTracker(getApplicationContext());
		gps.getLocation(0);

        if(!gps.canGetGPSLocation()){	
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
                	
                	//Progress Dialog
                    progressBar = new ProgressDialog(Gmaps.this);
            		progressBar.setCancelable(true);
            		progressBar.setMessage(Gmaps.this.getString(R.string.please_wait));
            		progressBar.show();
            		
            		//Clean TextView MSG
            		cleanUp_Textview();
            		            		
                	//Hide keyboard                	
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(buscode_search.getWindowToken(), 0);
                	
            		// start FbPlaces service
            		runFbPlaces_thread();

            		// get Buscode Details
            		runBuscodeDetails_thread(searchContent);
                    
                    //Check Service Location            		
            		gps.getLocation(0);

                    if(!gps.canGetGPSLocation()){	
            			gps.showSettingsAlert();
            		} else {
            			sca = new Score_Algorithm(getApplicationContext());
            			
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

      							getApplicationContext().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUS_GPS_URL_insert, cV);
      							//----------------------


      							
      							GetDistance distance = new GetDistance();
      							
      					        gps.getLocation(2);
      					        
      							GeoPoint curGeoPoint = new GeoPoint(
      					                (int) (gps.getLatitude()  * 1E6),
      					                (int) (gps.getLongitude() * 1E6));

      						    float Lat    = (float) (curGeoPoint.getLatitudeE6() / 1E6);
      						    float Long   = (float) (curGeoPoint.getLongitudeE6() / 1E6);

      							
      							Double get_distance =  distance.calculo(Double.parseDouble(dataBusArray[index_LATITUDE]), 
    												 				Lat, 
    												 				Double.parseDouble(dataBusArray[index_LONGITUDE]), 
    												 				Long);

      							//if (get_distance < MAX_DIST) {
      							if (get_distance < 10000000) {
          							
      								tv_busline.setText("Conecte-se e acumule pontos");
      								connect.setVisibility(View.VISIBLE);
      							} else {
      								
      								tv_busline.setText("Não é possível conectar ao ônibus");
      								connect.setVisibility(View.INVISIBLE);
      							}
      							
      							
      						    tv_from.setText("Fonte do GPS do ônbius:");
      						    tv_to.setText("Prefeitura do Rio de Janeiro");	    

      							
      							//----------------------
      					   		mMap.clear();
      					   		
      					        LatLng loc = new LatLng(Double.parseDouble(dataBusArray[index_LATITUDE]), 
      					        						Double.parseDouble(dataBusArray[index_LONGITUDE]));

      					   		// Adding a marker	   		
      							Marker marker = mMap.addMarker(new MarkerOptions().position(loc)
      											.title("Ônibus " + dataBusArray[index_BUSCODE].replace("\"","") + " localizado")						
      											//.snippet(getApplicationContext().getString(R.string.busmsg1))							
      											.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_gmaps_icon_blue)));
      							
      							mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 11.0f));
      							
      							
      							marker.showInfoWindow();


      						} else {
      							
      							//String url_brt = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/brt/";
      							//JSONObject json_brt = sca.getBrtPosition(url_brt, searchContent.substring(1, searchContent.length()));
      							//Log.v("FACEBOOK", "getBrtPosition: " + json_brt.getString("COLUMNS"));
      							
      							if (json_header.equals("[\"MENSAGEM\"]")){
      								
      								connect.setVisibility(View.INVISIBLE);
      								
      								tv_busline.setText("Ônibus não encontrado no sistema");
          						    tv_from.setText("Fonte do GPS do ônbius:");
          						    tv_to.setText("Prefeitura do Rio de Janeiro");	    
      			
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
	    //----
      	connect = (Button) findViewById(R.id.button_save_route);
      	connect.setVisibility(View.INVISIBLE);
      	connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
	            //-------------------
				//Getting_GpsBusData gbd = new Getting_GpsBusData();
				//gbd.setAlarm(getApplicationContext());
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

	            Uri uri_4 = SqliteProvider.CONTENT_URI_BUSCODE;
				Cursor buscode_info = getApplicationContext().getContentResolver().query(uri_4, null, null, null, null);
				buscode_info.moveToFirst();

				
	            Support support = new Support();
				
				ContentValues contentValues = new ContentValues();
				
				contentValues.put(DatabaseHelper.KEY_ID, "1");
				contentValues.put(DatabaseHelper.KEY_NAME, data_profile.getString(KEY_NAME));
				//---------------				
				//*
				//String status_buscode = "Conectado ao ônibus " + bus_gps_url.getString(KEY_BUSCODE);
				String status_buscode = "Conectado ao ônibus <bold>" + bus_gps_url.getString(KEY_BUSCODE) + "<bold>";
				String status_nearby = "";
				String status_buscode_details = "";
				//----------------

				//----------------				
				if (fb_places.getCount() > 0){
					status_nearby = " próximo ao " + fb_places.getString(KEY_NEARBY);
				}
				if (buscode_info.getCount() > 0){
					status_buscode = "Conectado ao ônibus <bold>" + bus_gps_url.getString(KEY_BUSCODE);
					status_buscode_details = " da linha " + buscode_info.getString(KEY_BUSLINE) + "<bold>";
				}				

				String status = status_buscode + status_buscode_details + status_nearby;
				//String lala = status.split(",")[0];
				
				contentValues.put(DatabaseHelper.KEY_STATUS, status);
				
				contentValues.put(DatabaseHelper.KEY_PICURL, data_profile.getString(KEY_PICTURE));
				contentValues.put(DatabaseHelper.KEY_TIME_STAMP, support.getDateTime());
				//----------------------------
				//News Feed Upload
				//----------------------------				
				//contentValues.put(DatabaseHelper.KEY_FLAG_ACTION, "ADD");
				//contentValues.put(DatabaseHelper.KEY_FEED_TYPE, "User gets into the bus");
				//contentValues.put(DatabaseHelper.KEY_LIKE_STATS, "0");
				//contentValues.put(DatabaseHelper.KEY_COMMENT_STATS, "0");
				//----------------------------
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
				//---------------------------------------
				//---------------------------------------		    	
				finish();
				//---------------------------------------
				//---------------------------------------

			}	
		});


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
            			json_buscode = userFunc.getBuscode(buscode.toUpperCase());
				    	
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
  							
  							getApplicationContext().getContentResolver().insert(SqliteProvider.CONTENT_URI_BUSCODE_insert, cV);

						 }
										        	
			        	
				    	
				    } catch (Exception e) {
				            e.printStackTrace();
				    }
				}
		});

		 thread_2.start();

		
	}
	
	public void runFbPlaces_thread(){
		
		 Thread thread = new Thread(new Runnable(){
			    @Override
			    public void run() {
			        try {

				    	//Your code goes here
				    	//------------
	                	DatabaseHelper mDatabaseHelper;
	                	mDatabaseHelper = new DatabaseHelper(getApplicationContext());
	                	mDatabaseHelper.resetUserPlaces();
			        	
				    	FbPlaces = new FbPlaces_Download();
				    	FbPlaces.setAlarm(getApplicationContext());
			    		
				    	//Thread.sleep(200);
				    	
				    } catch (Exception e) {
				            e.printStackTrace();
				    }
				}
		});

		thread.start();
		
	}
	public void cleanUp_Textview(){
		
		tv_busline.setText("");				
		tv_from.setText("");
		tv_to.setText("");
		
	}
	
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.gmaps_type, menu);
	      /*
	      getMenuInflater().inflate(R.menu.options_menu, menu);	
	      
	      // Associate searchable configuration with the SearchView
	      SearchManager searchManager =
	           (SearchManager) getSystemService(getApplicationContext().SEARCH_SERVICE);
	      SearchView searchView =
	            (SearchView) menu.findItem(R.id.search).getActionView();
	      searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));
		
	      // Do not iconify the widget;expand it by default
	      searchView.setIconifiedByDefault(true);	  
	      */
	      return true;
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

	        	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.0f));
	            
	                   
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