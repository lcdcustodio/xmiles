package com.xmiles.android;

import com.xmiles.android.fragment.EmConstrucao_Fragment;
import com.xmiles.android.fragment.Favorites_Fragment;
import com.xmiles.android.fragment.Profile_Fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		
		//*******
	    Fragment fgmt_inicio 	= new Profile_Fragment();
	    Fragment fgmt_favoritos = new Favorites_Fragment();
	    Fragment fgmt_ranking 	= new EmConstrucao_Fragment();
	    //*******
	    
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Enable Action Bar
	    actionBar.show();

	    Tab tab1 = actionBar
	          .newTab()
	          .setText("INÍCIO")
	          //.setIcon(R.drawable.android_logo)
	          .setTabListener(new MyTabsListener(fgmt_inicio));

	      actionBar.addTab(tab1);
	      actionBar.selectTab(tab1);
	    //*  	
	    Tab tab2 = actionBar
	          .newTab()
	          .setText("FAVORITOS")
	          //.setIcon(R.drawable.windows_logo)
	          .setTabListener(new MyTabsListener(fgmt_favoritos));
	      
	      actionBar.addTab(tab2);
	     //*/
	     /* 
	     Tab tab2 = actionBar
	    	   .newTab()
	    	   .setText("RECOMPENSAS")
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


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_frame, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
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

}
