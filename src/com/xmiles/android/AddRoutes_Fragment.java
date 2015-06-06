package com.xmiles.android;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.Toast;



public class AddRoutes_Fragment extends FragmentActivity {

	private static View view;
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */

	private static final String TAG = "FACEBOOK";
	private static GoogleMap mMap;
	private static Double latitude, longitude;

	public AddRoutes_Fragment(){}

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
                final Dialog dialog = new Dialog(AddRoutes_Fragment.this);
                dialog.setContentView(R.layout.addroutes_dialog);
                dialog.setTitle("  Linhas de Ônibus:");		                
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!
                //------------------------------------------------		                
                //------------------------------------------------		                
                //set up ListView		                
                //face2me_users = (ListView) dialog.findViewById(R.id.dialog_listview);		                
                //face2me_users.setAdapter(new f2m_users_ListAdapter(Profile.this));
                //-------------------	                
                dialog.show();

                
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

}