package com.xmiles.android;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xmiles.android.webservice.UserFunctions;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Gmaps_Fragment extends FragmentActivity {

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
	ListView dialog_busline;

	protected static JSONArray jsonArray;
	protected static JSONObject json;

	public Gmaps_Fragment(){}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addroutes_fgmt);

        ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
	    latitude = 26.78;
	    longitude = 72.56;

	    setUpMapIfNeeded(); // For setting up the MapFragment
	    //---------------------
	    Button find_line = (Button) findViewById(R.id.gmap_button);
	    
        find_line.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "Botao Find_Line pressionado", Toast.LENGTH_SHORT).show();
                
				//set up dialog
                final Dialog dialog = new Dialog(Gmaps_Fragment.this);
                dialog.setContentView(R.layout.addroutes_dialog);
                dialog.setTitle("  Linhas de Ônibus:");		                
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!
                //------------------------------------------------		                
                //------------------------------------------------		                
                //set up ListView		                
                //face2me_users = (ListView) dialog.findViewById(R.id.dialog_listview);		                
                //face2me_users.setAdapter(new f2m_users_ListAdapter(Profile.this));
                dialog_busline = (ListView) dialog.findViewById(R.id.dialog_listview);
                //-------------------	                
                //set up Spinner
                //dialog_cities = (Spinner) dialog.findViewById(R.id.cities_spinner);
                //--------------
    	        progressBar = new ProgressDialog(getApplicationContext());
    			progressBar.setCancelable(true);
    			progressBar.setMessage("Please, wait ...");
    			//progressBar.show();
    			//--------------                
                Busline_Query bq = new Busline_Query();
                //-------------------
                dialog.show();
                //progressBar.show();
                
            }
        });	    
	    
	            

	}
	
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	  }

	/***** Sets up the map if it is possible to do so *****/
	public void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.

			FragmentManager fm = getSupportFragmentManager();
			SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.gmap_addroutes);

	    	mMap = fragment.getMap();

	        // Check if we were successful in obtaining the map.
	        if (mMap != null)
	            setUpMap();
	    }
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the
	 * camera.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void setUpMap() {
	    // For showing a move to my loction button
	    mMap.setMyLocationEnabled(true);
	    // For dropping a marker at a point on the Map
	    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
	    // For zooming automatically to the Dropped PIN Location
	    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
	            longitude), 12.0f));
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

	            	/*
	            	 * Trecho comentado abaixo é para o botão de confirmar adição de rota
	            	 */
	            	//Intent upIntent = new Intent(this, BtnFbLogin_Fragment.class);
	            	//NavUtils.navigateUpTo(this, upIntent);
	            	finish();
	            	break;
	        }
	        return true;
	    }

	    /*
	    @Override
	    public void onBackPressed() {
	        if(getFragmentManager().getBackStackEntryCount() == 0) {
	            super.onBackPressed();
	        }
	        else {
	            getFragmentManager().popBackStack();
	        }
	    }
	    */
	    public class Busline_Query {
	    	
	    	public Busline_Query() {
				 Thread thread = new Thread(new Runnable(){
					    @Override
					    public void run() {
					        try {


					        	//Your code goes here
					        	UserFunctions userFunc = new UserFunctions();
					        	json = userFunc.busline("Rio de Janeiro");
		
					        	jsonArray = new JSONArray(json.getString("city"));
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
				//-----------
				//progressBar.dismiss();
				runThread();			 

			 }
			 
		        private void runThread() {

		            new Thread() {
		                public void run() {

		                        try {
		                        	runOnUiThread(new Runnable() {

		                                @Override
		                                public void run() {
		            						dialog_busline.setAdapter(new BuslineListAdapter(this));
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
	    
	    
		public class BuslineListAdapter extends BaseAdapter {
		    private LayoutInflater mInflater;		    

		    public BuslineListAdapter(Runnable runnable) {
		    	//friends_info = data; 	    		    	
		    }

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				//return jsonArray.length() + 1;	
				return jsonArray.length();
				
			}
			
			@Override
			public int getItemViewType(int position) {
				/*
				if (position < jsonArray.length() ){
					return TYPE1;
				} else {
					return TYPE2;
				}
				*/
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup arg2) {				

			    
			           
			    	   Type1Holder holder1; 
			           View v = convertView;
			           
					if (mInflater == null){
						mInflater = (LayoutInflater) getApplication()
				               .getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
					}
			           
			           if (v == null) { 
			        	   v = mInflater.inflate(R.layout.favorites_items, null);
			        	   holder1 = new Type1Holder (); 
			               
			        	   holder1.profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
			               holder1.name = (TextView) v.findViewById(R.id.name);

			               v.setTag(holder1); 
			        	   
			           } else {
			              holder1 = (Type1Holder)v.getTag(); 
			           }
			       
					    JSONObject jsonObject = null;
					    
			            try {
			                jsonObject = jsonArray.getJSONObject(position);
			            
					 try {
				                holder1.name.setText(jsonObject.getString("busline"));
				             } catch (JSONException e) {
				                holder1.name.setText("");
				             }
				            
			            
			            
			            } catch (JSONException e1) {
			                // TODO Auto-generated catch block
			                e1.printStackTrace();	                
			            }


			       return v;
			}
		}

	    
	    class Type1Holder {
	        ImageView profile_pic;
	        TextView name;


	    }
}