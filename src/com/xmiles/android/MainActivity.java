package com.xmiles.android;

import com.xmiles.android.fragment.Feed_Fragment;
import com.xmiles.android.fragment.NoInternetConnection_Fragment;

import com.xmiles.android.fragment.Ranking_Fragment;

import com.xmiles.android.pushnotifications.ServerUtilities;
import com.xmiles.android.pushnotifications.WakeLocker;


import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.ConnectionDetector;
import com.xmiles.android.support.GPSTracker;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

//-------------
//BEGIN TEST - PUSH NOTIFICATION By GCM (androidhive example)

import static com.xmiles.android.pushnotifications.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.xmiles.android.pushnotifications.CommonUtilities.EXTRA_MESSAGE;
import static com.xmiles.android.pushnotifications.CommonUtilities.SENDER_ID;
import static com.xmiles.android.pushnotifications.CommonUtilities.SERVER_URL;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.maps.GeoPoint;

public class MainActivity extends FragmentActivity {

	//-----------------------------
	//cities boundaries
	private LatLngBounds xmiles_bounds = new LatLngBounds(
			new LatLng(-23.079425, -43.741027), new LatLng(-22.7600, -43.1303));//,
			//new LatLng(-30, -51), new LatLng(-29, -50));
			//new LatLng(-23.079425, -43.741027 ), new LatLng(-22.7600, -43.1303));
	
	private LatLngBounds SaoPaulo = new LatLngBounds(
			new LatLng(-23.971097, -46.901589), new LatLng(-23.348461, -46.359139));

	private LatLngBounds PortoAlegre = new LatLngBounds(
			new LatLng(-30.261530, -51.306411), new LatLng(-29.960472, -51.009013));

	//-----------------------------	

	// Connection detector
	ConnectionDetector cd;


	
	private static final String TAG = "FACEBOOK";
	//-------------
	//BEGIN TEST - PUSH NOTIFICATION By GCM (androidhive example)

	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_U_ID = 0;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	//END TEST - PUSH NOTIFICATION By GCM (androidhive example)
	//-------------

    // GPSTracker class
	GPSTracker gps;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extra);
		//setContentView(R.layout.activity_main);
		

	    Fragment fgmt_newsfeed 	= new Feed_Fragment();
	    
	    //ActionBar actionBar = getActionBar();
	    

	    /*
	     * Line below is commented due to crash on LG-P875h device
	    */	    
	    //actionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'> &nbsp xMiles</font></b>"));


	    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
	    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
	    fragmentTransaction.replace(R.id.frame_container, fgmt_newsfeed);
	    fragmentTransaction.commit();
    
	    

	    //----
	    runThread();
	    //----	    


	}
	
	 //*
	 private void runThread(){
	     runOnUiThread (new Thread(new Runnable() {
		 //new Thread() {
	         public void run() {

	        	 //adapter=new SlidingMenuLazyAdapter(getApplicationContext());        
	             //mDrawerList.setAdapter(adapter);
	             
	             try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	     }));
		 //}.start();
	   //*/  
	     
	     
			//-------------
     	//BEGIN TEST - PUSH NOTIFICATION By GCM (androidhive example)
        
 		// Check if GCM configuration is set
 		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
 				|| SENDER_ID.length() == 0) {
 			// GCM sernder id / server url is missing
 			Toast.makeText(getApplicationContext(), "Please set your Server URL and GCM Sender ID", 
 					Toast.LENGTH_LONG).show();

 		}

 		// Make sure the device has the proper dependencies.
 		GCMRegistrar.checkDevice(getApplicationContext());
 		
 		// Make sure the manifest was properly set - comment out this line
 		// while developing the app, then uncomment it when it's ready.
 		GCMRegistrar.checkManifest(getApplicationContext());
 		
 		//lblMessage = (TextView) findViewById(R.id.lblMessage);
 		
 		registerReceiver(mHandleMessageReceiver, new IntentFilter(
 				DISPLAY_MESSAGE_ACTION));
 		
 		// Get GCM registration id
 		final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
 		
 		// Check if regid already presents
 		if (regId.equals("")) {
 			// Registration is not present, register now with GCM			
 			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
 		}  else {
 			// Device is already registered on GCM
 			if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
 				// Skips registration.				
 				//Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
 				//Log.e(TAG, "Already registered with GCM");
 			} else {
 				// Try to register again, but not in the UI thread.
 				// It's also necessary to cancel the thread onDestroy(),
 				// hence the use of AsyncTask instead of a raw thread.

				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						//ServerUtilities.register(context, name, email, regId);
						
				        Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;        
				        Cursor users_info = context.getContentResolver().query(uri_1, null, null, null, null);
				        users_info.moveToLast();
						
						ServerUtilities.register(context,  users_info.getString(KEY_NAME), users_info.getString(KEY_U_ID), regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
 			}
 			
 		}	
		
        //END TEST - PUSH NOTIFICATION By GCM (androidhive example)
		//-------------

	     
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main_frame, menu);
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	  
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// toggle nav drawer on selecting action bar app icon/title
			/*
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
			*/
			
			// Handle action bar actions click
			//*
			switch (item.getItemId()) {

			case R.id.buscode_search:
				
				//new Chaos_Monkey_Test(getApplicationContext()).execute();
				//Chaos_Monkey_SetAlarm cms = new Chaos_Monkey_SetAlarm();
				//cms.setAlarm(getApplicationContext());
				
				//*
				cd = new ConnectionDetector(getApplicationContext());
				
				// Check if Internet present
				if (!cd.isConnectingToInternet()) {
		 
			        //----hide action bar---
			        getActionBar().hide();
	
				    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
				    fragmentTransaction.replace(R.id.frame_container, new NoInternetConnection_Fragment());
				    fragmentTransaction.commit();

		        	
		        } else {
		        	
		        	go2Gmaps();
				
		        }
		        //*/

				return true;	
				
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		
		public void go2Gmaps(){
			
	        //Check Service Location
			gps = new GPSTracker(getApplicationContext());
			gps.getLocation(0);
			
			
            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_LOCATION;
        	Cursor data_UserLocation = getApplicationContext().getContentResolver().query(uri_1, null, null, null, null);			        	


	        if(!gps.canGetGPSLocation() || !gps.canGetNW_Location()){	
				//gps.showSettingsAlert();
	        	//Toast.makeText(getApplicationContext(), getString(R.string.location_service), Toast.LENGTH_SHORT).show();
	        	Toast.makeText(getApplicationContext(), getString(R.string.content_notification_01), Toast.LENGTH_SHORT).show();
			} else{
				gps.getLocation(2);
				
				GeoPoint curGeoPoint = new GeoPoint(
		                (int) (gps.getLatitude()  * 1E6),
		                (int) (gps.getLongitude() * 1E6));
				
                if (!xmiles_bounds.contains(new LatLng(curGeoPoint.getLatitudeE6() / 1E6,curGeoPoint.getLongitudeE6() / 1E6))){

                    Toast.makeText(getApplicationContext(), getString(R.string.city_out_of_scope), Toast.LENGTH_LONG).show();
                
                } else if (SaoPaulo.contains(new LatLng(curGeoPoint.getLatitudeE6() / 1E6,curGeoPoint.getLongitudeE6() / 1E6))){
                	
                    Toast.makeText(getApplicationContext(), getString(R.string.saopaulo_out_of_scope), Toast.LENGTH_LONG).show();
                    
                } else if (PortoAlegre.contains(new LatLng(curGeoPoint.getLatitudeE6() / 1E6,curGeoPoint.getLongitudeE6() / 1E6))){
                	
                    Toast.makeText(getApplicationContext(), getString(R.string.portoalegre_out_of_scope), Toast.LENGTH_LONG).show();
                    
                    
                } else if (xmiles_bounds.contains(new LatLng(curGeoPoint.getLatitudeE6() / 1E6,curGeoPoint.getLongitudeE6() / 1E6))){					
				
					if (data_UserLocation.getCount() == 0) {
					
		                Intent intent = new Intent(getApplicationContext(), Gmaps.class);                
		                startActivity(intent);
					} else {

			        	Toast.makeText(getApplicationContext(), getString(R.string.busmsg2), Toast.LENGTH_SHORT).show();

						
					}
                }

			}
			
		}
		
		/* *
		 * Called when invalidateOptionsMenu() is triggered
		 */
		@Override
		public boolean onPrepareOptionsMenu(Menu menu) {
			// if nav drawer is opened, hide the action items
			//boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			
			//menu.findItem(R.id.about_it).setVisible(!drawerOpen);
			return super.onPrepareOptionsMenu(menu);
		}

		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			// Sync the toggle state after onRestoreInstanceState has occurred.
			//mDrawerToggle.syncState();
		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			// Pass any configuration change to the drawer toggls
			//mDrawerToggle.onConfigurationChanged(newConfig);
		}

		/**
		 * Slide menu item click listener
		 * */
		/*
		private class SlideMenuClickListener implements
				ListView.OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// display view for selected nav drawer item
				//displayView(position);
				if (position > 1){
				

					cd = new ConnectionDetector(getApplicationContext());
					
					// Check if Internet present
					if (!cd.isConnectingToInternet()) {
			 
						//mDrawerLayout.closeDrawer(Gravity.LEFT);
						//----hide action bar---
				        getActionBar().hide();				        
		
					    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
					    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
					    fragmentTransaction.replace(R.id.frame_container, new NoInternetConnection_Fragment());
					    fragmentTransaction.commit();

			        	
			        } else {
			        	
					    new Handler().postDelayed(new Runnable() {

					        @Override
					        public void run() {
					            if (!isFinishing()) {
					            	
					            	mDrawerLayout.closeDrawer(Gravity.LEFT);
					            	
					            }
					        }
					    }, 1500);

				    
						Uri uri_2b = SqliteProvider.CONTENT_URI_RANKING;
						Cursor rank = getApplicationContext().getContentResolver().query(uri_2b, null, null, null, null);

						rank.moveToPosition(position - 2);
						int KEY_ID_PROFILE = 0;
						
						Intent intent = new Intent(getApplicationContext(),Hashtag.class);
	
						Bundle args = new Bundle();				    
						args.putString("hashtag", "#hist�rico_pontua��o");			    
						args.putString("user_id", rank.getString(KEY_ID_PROFILE));
						
						intent.putExtras(args);
				    
						startActivity(intent);

			        }				    
					
					
				}
			}
		}
		*/
		//-------------
		//BEGIN TEST - PUSH NOTIFICATION By GCM (androidhive example)
		  	
		  	
		/**
		* Receiving push messages
		* */
		
		private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		  	@Override
		  	public void onReceive(Context context, Intent intent) {
		  		String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
		  		// Waking up mobile if it is sleeping
		  		WakeLocker.acquire(getApplicationContext());
		  			
		  		// Showing received message
		  		//lblMessage.append(newMessage + "\n");			
		  		//Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
		  			
		  		// Releasing wake lock
		  		WakeLocker.release();
		  	}
		};
		
		@Override
		protected void onDestroy() {
			
			//Log.i(TAG,"onDestroy MainActivity");
			
			if (mRegisterTask != null) {
				mRegisterTask.cancel(true);
			}
			try {
				unregisterReceiver(mHandleMessageReceiver);
				GCMRegistrar.onDestroy(this);
			} catch (Exception e) {
				//Log.e("UnRegister Receiver Error", "> " + e.getMessage());
			}
			super.onDestroy();
		}
		
		//END TEST - PUSH NOTIFICATION By GCM (androidhive example)
		//-------------	

}
