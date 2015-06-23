package com.xmiles.android;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
//import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xmiles.android.Busline_Fragment.BuslineListAdapter;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.webservice.UserFunctions;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Gmaps_Fragment extends FragmentActivity implements OnInfoWindowClickListener{
//public class Gmaps_Fragment extends FragmentActivity {

	private static View view;
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */

	private static final String TAG = "FACEBOOK";
	private static GoogleMap mMap;
	private static Double latitude, longitude;
	
	Spinner dialog_cities;
	ProgressDialog progressBar;
	TextView tv_busline;
	TextView tv_city;
	TextView tv_from;
	TextView tv_to;
	

	protected static JSONArray jsonArray;
	protected static JSONObject json;

	public Gmaps_Fragment(){}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmaps_fgmt);

        ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    

	    //---------------------
	    Bundle args = getIntent().getExtras();
	    String busline = args.getString("busline");
	    String city = args.getString("city");
	    //---------------------
        progressBar = new ProgressDialog(this);
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		//-------------------
	    
	    Bus_Stop_Query bsq = new Bus_Stop_Query(busline,city);
	    //----
	    TextView ct = (TextView) findViewById(R.id.city_header);
	    ct.setText(city);
	    tv_busline = (TextView) findViewById(R.id.busline);
	    tv_busline.setText(busline);
	    tv_city = (TextView) findViewById(R.id.city);
	    tv_city.setText(city);
	    tv_from = (TextView) findViewById(R.id._de);
	    tv_to = (TextView) findViewById(R.id.info);
	    //----
		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.gmap_addroutes);

    	mMap = fragment.getMap();       
    	mMap.setMyLocationEnabled(true);
    	
    	mMap.setOnInfoWindowClickListener(this);
	}
	//*
	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, marker.getSnippet(), Toast.LENGTH_LONG).show();
		marker.hideInfoWindow();
		marker.setIcon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		
		 
		if (tv_from.getText().toString().isEmpty()){
			tv_from.setText("De: " + marker.getSnippet());
		}else {
			tv_to.setText("Para: " + marker.getSnippet());
		}
	}
	//*/
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.modos, menu);
	      return true;
	  }

	
	public void renderMarkerOptions(JSONArray bus_stop){
		
		
		
		for (int position = 0; position < 10; position++) {
			
			JSONObject jsonObject = null;
			
			try {
				jsonObject = bus_stop.getJSONObject(position);
				//-----------
				latitude = Double.parseDouble(jsonObject.getString("latitude"));
				longitude = Double.parseDouble(jsonObject.getString("longitude"));
				//-----------
				// Adding a marker
				mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
						.title(jsonObject.getString("busline"))						
						.snippet(jsonObject.getString("bus_stop"))
						);
				//*
				mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					 
		            // Use default InfoWindow frame
		            @Override
		            public View getInfoWindow(Marker arg0) {
		                return null;
		            }
		 
		            // Defines the contents of the InfoWindow
		            @Override
		            public View getInfoContents(Marker arg0) {
		 
		                // Getting view from the layout file info_window_layout
		                View v = getLayoutInflater().inflate(R.layout.gmaps_infowindow_items, null);
		                
		                TextView bus_stop = (TextView) v.findViewById(R.id._de);		                
						bus_stop.setText(arg0.getSnippet());
						

		 
		 
		                // Returning the view containing InfoWindow contents
		                return v;
		 
		            }
		        });
				//*/
				if (position == 0) {

					//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
				    //        longitude), 12.0f));
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
				            longitude), 11.0f));


				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/**** The mapfragment's id must be removed from the FragmentManager
	 **** or else if the same it is passed on the next time then
	 **** app will crash ****/
	  @Override
	  public void onDestroy() {
	    //Log.d(TAG, "lala");

		super.onDestroy();
	}

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            
	        	case android.R.id.home:	            	
	            	finish();
	            	break;
	            	

	            case R.id.satelite:
	            	
	                //Toast.makeText(getBaseContext(), "You selected Sat�lite", Toast.LENGTH_SHORT).show();
	            	mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	            	//return true;
	            	break;	            	
	            
	            case R.id.rua:
	            	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	            	//Toast.makeText(getBaseContext(), "You selected Rua", Toast.LENGTH_SHORT).show();
	            	break;
	            	
	            case R.id.terreno:
	            	mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	            	
	            	break;
	
	            default:
	            	return super.onOptionsItemSelected(item);
	        }
	        return true;
	    }
	    
	    
	 public class Bus_Stop_Query{
		 
		 String bl;
		 String ct;
		 
		 public Bus_Stop_Query(String busline, String city){
			 this.bl = busline;
			 this.ct = city;
		 
		     Thread thread = new Thread(new Runnable(){
			    @Override
			    public void run() {
			        try {

			        	//Your code goes here
			        	jsonArray = null;
			        	
			        	UserFunctions userFunc = new UserFunctions();
			        	json = userFunc.bus_stop(bl, ct);

			        	jsonArray = new JSONArray(json.getString("busline"));
			        	
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
	                        	runOnUiThread(new Runnable() {

	                                @Override
	                                public void run() {
	                                	renderMarkerOptions(jsonArray);
	                                }
	                            });
	                            Thread.sleep(400);
	                            progressBar.dismiss();
	                            
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                }
	            }.start();
	        }       

		 
	 }




}