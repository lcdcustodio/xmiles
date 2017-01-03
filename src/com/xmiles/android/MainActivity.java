package com.xmiles.android;

import com.xmiles.android.fragment.Feed_Fragment;

import com.xmiles.android.fragment.Ranking_Fragment;

import com.xmiles.android.pushnotifications.ServerUtilities;
import com.xmiles.android.pushnotifications.WakeLocker;
import com.xmiles.android.slidingmenu.adapter.SlidingMenuLazyAdapter;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
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

public class MainActivity extends FragmentActivity {

	//-----------------------------
	SlidingMenuLazyAdapter adapter;
	ListView mDrawerList;
	DrawerLayout mDrawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	//-----------------------------
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
		setContentView(R.layout.activity_main);
		

		//Action TAB
		
		//*******
	    Fragment fgmt_newsfeed 	= new Feed_Fragment();
	    Fragment fgmt_ranking 	= new Ranking_Fragment();
	    //*******
	    
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
	    // set Color
	    //actionBar.setBackgroundDrawable(new ColorDrawable(R.color.facebook));
	    
	    // set Title
	    actionBar.setDisplayShowTitleEnabled(true);

	    /*
	     * Line below is commented due to crash on LG-P875h device
	    */	    
	    //actionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'> &nbsp xMiles</font></b>"));

	    
	    // Enable Action Bar
	    actionBar.show();

	    Tab tab2 = actionBar
	    	   .newTab()
	    	   .setText("FEED")
	    	   //.setIcon(R.drawable.nav_news_feed)	
	    	   .setTabListener(new MyTabsListener(fgmt_newsfeed));
	    	  
	      actionBar.addTab(tab2);
	      actionBar.selectTab(tab2);
	      
	    Tab tab3 = actionBar
	              .newTab()
	              .setText("RANKING")
	              //.setIcon(R.drawable.computer)
	              .setTabListener(new MyTabsListener(fgmt_ranking));
	          
	          actionBar.addTab(tab3);
	          
	          
	    //Sliding Menu
	  	actionBar.setDisplayHomeAsUpEnabled(true);
	  	actionBar.setHomeButtonEnabled(true);
	  	//actionBar.setTitle("");
	  	
	    
	          
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	    
	    runThread();
        //adapter=new SlidingMenuLazyAdapter(getApplicationContext());        
        //mDrawerList.setAdapter(adapter);
	    mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	    
	    
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				//R.drawable.ic_drawer_rev03, //nav menu toggle icon
				R.drawable.ic_menu, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name// nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				//getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};//*/
		mDrawerLayout.setDrawerListener(mDrawerToggle);



	}
	
	 private void runThread(){
	     runOnUiThread (new Thread(new Runnable() {
		 //new Thread() {
	         public void run() {

	        	 adapter=new SlidingMenuLazyAdapter(getApplicationContext());        
	             mDrawerList.setAdapter(adapter);
	             
	             try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	     }));
		 //}.start();
	     
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
 				Log.e(TAG, "Already registered with GCM");
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

	
	  protected class MyTabsListener implements ActionBar.TabListener{
		    private Fragment fragment;

		    public MyTabsListener(Fragment fragment2){
		        this.fragment = fragment2;
		    }
		    public void onTabSelected(Tab tab, FragmentTransaction ft){

		    	  android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		          android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();        
		          fragmentTransaction.replace(R.id.frame_container, fragment);
		          fragmentTransaction.commit();

		    }
		    public void onTabReselected(Tab tab, FragmentTransaction ft) {
		    }
		    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		        //ft.remove(fragment);
		    }
		}
	  
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// toggle nav drawer on selecting action bar app icon/title
			
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
			
			// Handle action bar actions click
			//*
			switch (item.getItemId()) {

			case R.id.buscode_search:
				
				
		        //Check Service Location
				gps = new GPSTracker(getApplicationContext());
				gps.getLocation(0);
				
				
	            Uri uri_1 = SqliteProvider.CONTENT_URI_USER_LOCATION;
	        	Cursor data_UserLocation = getApplicationContext().getContentResolver().query(uri_1, null, null, null, null);			        	


		        if(!gps.canGetGPSLocation() || !gps.canGetNW_Location()){	
					//gps.showSettingsAlert();
		        	Toast.makeText(getApplicationContext(), getString(R.string.location_service), Toast.LENGTH_SHORT).show();
				} else{
					
					if (data_UserLocation.getCount() == 0) {
					
		                Intent intent = new Intent(getApplicationContext(), Gmaps.class);                
		                startActivity(intent);
					} else {

			        	Toast.makeText(getApplicationContext(), getString(R.string.busmsg2), Toast.LENGTH_SHORT).show();

						
					}
				}
				
				return true;	
				
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		/* *
		 * Called when invalidateOptionsMenu() is triggered
		 */
		@Override
		public boolean onPrepareOptionsMenu(Menu menu) {
			// if nav drawer is opened, hide the action items
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			//menu.findItem(R.id.about_it).setVisible(!drawerOpen);
			return super.onPrepareOptionsMenu(menu);
		}

		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			// Sync the toggle state after onRestoreInstanceState has occurred.
			mDrawerToggle.syncState();
		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			// Pass any configuration change to the drawer toggls
			mDrawerToggle.onConfigurationChanged(newConfig);
		}

		/**
		 * Slide menu item click listener
		 * */
		private class SlideMenuClickListener implements
				ListView.OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// display view for selected nav drawer item
				//displayView(position);
				if (position > 1){
					Toast.makeText(getApplicationContext(), "Necessário atingir pontuação mínima da recompensa", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
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
			
			Log.i(TAG,"onDestroy MainActivity");
			
			if (mRegisterTask != null) {
				mRegisterTask.cancel(true);
			}
			try {
				unregisterReceiver(mHandleMessageReceiver);
				GCMRegistrar.onDestroy(this);
			} catch (Exception e) {
				Log.e("UnRegister Receiver Error", "> " + e.getMessage());
			}
			super.onDestroy();
		}
		
		//END TEST - PUSH NOTIFICATION By GCM (androidhive example)
		//-------------	

}
