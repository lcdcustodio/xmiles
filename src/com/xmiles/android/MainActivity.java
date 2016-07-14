package com.xmiles.android;

import com.xmiles.android.fragment.Feed_Fragment;

import com.xmiles.android.fragment.Ranking_Fragment;

import com.xmiles.android.slidingmenu.adapter.SlidingMenuLazyAdapter;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
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

import static com.xmiles.android.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.xmiles.android.CommonUtilities.EXTRA_MESSAGE;
import static com.xmiles.android.CommonUtilities.SENDER_ID;
import static com.xmiles.android.CommonUtilities.SERVER_URL;

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

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	//END TEST - PUSH NOTIFICATION By GCM (androidhive example)
	//-------------

	
	
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
	    actionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'> &nbsp xMiles</font></b>"));

	    
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
 			//alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
 			//		"Please set your Server URL and GCM Sender ID", false);
 			// stop executing code by return
 			 //return;
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
 				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
 			} else {
 				// Try to register again, but not in the UI thread.
 				// It's also necessary to cancel the thread onDestroy(),
 				// hence the use of AsyncTask instead of a raw thread.

 				//ServerUtilities.register(getApplicationContext(), "bill clinton", "bill@gmail.com", regId);
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						//ServerUtilities.register(context, name, email, regId);
						ServerUtilities.register(context, "bill clinton", "bill@gmail.com", regId);
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
			/*
			case R.id.about_it:
				
				Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
				
				return true;
				
			case R.id.how_works:
				
				//Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
				
				//Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
				Uri uri = Uri.parse("https://www.youtube.com/embed/OvgtMaMftZw");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				
				return true;

			case R.id.policies:
				
				Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
				
				return true;
			*/
			case R.id.buscode_search:
				
				Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
				
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
		  		Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
		  			
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
