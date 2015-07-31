package com.xmiles.android;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;





public class NewRoutes_Fragment extends FragmentActivity {

	private static final String TAG = "FACEBOOK";
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);

	      ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);

		  
	      //*******
	      Fragment fgmt_cities 	  = new Cities_Fragment();
	      //Fragment fgmt_cities 	  = new Profile_Fragment();
	      //Fragment fgmt_favoritos = new Routes_Fragment();
	      Fragment fgmt_busline = new Busline_Fragment();

	      //*******
	  	  
	      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	      //actionBar.setTitle(" Adicione Nova Rota");
	      //actionBar.setDisplayShowTitleEnabled(true);

	      // Enable Action Bar
	      actionBar.show();


	      Tab tab1 = actionBar
	          .newTab()
	          .setText("CIDADES")
	          //.setIcon(R.drawable.android_logo)
	          .setTabListener(new MyTabsListener(fgmt_cities));

	      actionBar.addTab(tab1);
	      actionBar.selectTab(tab1);

	      Tab tab2 = actionBar
	          .newTab()
	          .setText("LINHAS")
	          //.setIcon(R.drawable.windows_logo)
	          .setTabListener(new MyTabsListener(fgmt_busline));
	      
	      actionBar.addTab(tab2);
	      
	      /*
	      ImageButton imgbtnFbLogin = (ImageButton) findViewById(R.id.imgbtn_fblogin);
	      
	      
			imgbtnFbLogin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Log.d(TAG, "Busline -> Gmaps_Fragment");
					
                    Intent intent = new Intent(NewRoutes_Fragment.this, Gmaps_Fragment.class);
                    startActivity(intent);
                    finish();

					
				}
			});
			*/
	      
	}

	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      //getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	  }
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:

	            	finish();
	            	break;
	        }
	        return true;
	    }
	    protected class MyTabsListener implements ActionBar.TabListener{
		    private Fragment fragment;

		    public MyTabsListener(Fragment fragment2){
		        this.fragment = fragment2;
		    }
		    public void onTabSelected(Tab tab, FragmentTransaction ft){
		        //ft.add(R.id.frame_container, fragment);
		    	//ft.replace(R.id.frame_container, fragment);
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


}
