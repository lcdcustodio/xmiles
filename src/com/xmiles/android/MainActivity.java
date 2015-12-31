package com.xmiles.android;







import com.xmiles.android.fragment.EmConstrucao_Fragment;
import com.xmiles.android.fragment.Favorites_Fragment;
import com.xmiles.android.fragment.Profile_Fragment;
import com.xmiles.android.fragment.Ranking_Fragment;
import com.xmiles.android.slidingmenu.adapter.SlidingMenuLazyAdapter;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	//-----------------------------
	SlidingMenuLazyAdapter adapter;
	ListView mDrawerList;
	DrawerLayout mDrawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	//-----------------------------
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		//Action TAB
		
		//*******
	    Fragment fgmt_inicio 	= new Profile_Fragment();
	    //Fragment fgmt_favoritos = new Favorites_Fragment();
	    //Fragment fgmt_ranking 	= new EmConstrucao_Fragment();
	    Fragment fgmt_ranking 	= new Ranking_Fragment();
	    //*******
	    
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Enable Action Bar
	    actionBar.show();

	    Tab tab1 = actionBar
	          .newTab()
	          .setText("MAPA")
	          //.setIcon(R.drawable.android_logo)
	          .setTabListener(new MyTabsListener(fgmt_inicio));

	      actionBar.addTab(tab1);
	      actionBar.selectTab(tab1);
	      /*
	      Tab tab2 = actionBar
	    	   .newTab()
	    	   .setText("HISTÓRICO")
	    	   //.setIcon(R.drawable.windows_logo)
	    	   .setTabListener(new MyTabsListener(fgmt_ranking));
	    	  
	      actionBar.addTab(tab2);
		  */	
	      
	      
	    Tab tab3 = actionBar
	              .newTab()
	              .setText("RANKING")
	              //.setIcon(R.drawable.windows_logo)
	              .setTabListener(new MyTabsListener(fgmt_ranking));
	          
	          actionBar.addTab(tab3);
	          
	          
	    //Sliding Menu
	  	actionBar.setDisplayHomeAsUpEnabled(true);
	  	actionBar.setHomeButtonEnabled(true);
	  	actionBar.setTitle("");      
	          
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	    
	    runThread();
        //adapter=new SlidingMenuLazyAdapter(getApplicationContext());        
        //mDrawerList.setAdapter(adapter);
	    mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	    
	    
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_frame, menu);
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
			switch (item.getItemId()) {
			case R.id.about_it:
				
				Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
				
				return true;
				
			case R.id.how_works:
				
				Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
				
				return true;

			case R.id.policies:
				
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
			menu.findItem(R.id.about_it).setVisible(!drawerOpen);
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

}
