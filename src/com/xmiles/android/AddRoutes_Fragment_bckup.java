package com.xmiles.android;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class AddRoutes_Fragment_bckup extends Fragment {

	private static View view;
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */

	private static final String TAG = "FACEBOOK";
	private static GoogleMap mMap;
	private static Double latitude, longitude;
	
	public AddRoutes_Fragment_bckup(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    if (container == null) {
	        return null;
	    }
	    //---------------------
	    ActionBar actionBar = getActivity().getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    //---------------------
	    View rootView = inflater.inflate(R.layout.addroutes_fgmt, container, false);
	    	    
	    // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
	            latitude = 26.78;
	            longitude = 72.56;

	            setUpMapIfNeeded(); // For setting up the MapFragment


	    return rootView;
	}

	/***** Sets up the map if it is possible to do so *****/
	public void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.
	    	
			FragmentManager fm = getFragmentManager();
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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    if (mMap != null)
	        setUpMap();

	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.
			FragmentManager fm = getFragmentManager();
			SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.gmap_addroutes);	    	
	    	mMap = fragment.getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null)
	            setUpMap();
	    }
	}

	/**** The mapfragment's id must be removed from the FragmentManager
	 **** or else if the same it is passed on the next time then 
	 **** app will crash ****/
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    
	    //Log.d(TAG, "lala");
	    
	    Fragment fragment = (getFragmentManager().findFragmentById(R.id.gmap_addroutes));
	    //Fragment fragment = (getFragmentManager().findFragmentById(R.id.frame_container));
	    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    ft.remove(fragment);
	    //ft.detach(fragment);
	    ft.commit();
	    

	}
	
}