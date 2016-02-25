package com.xmiles.android;


import com.xmiles.android.fragment.Rel_Fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Relationship extends FragmentActivity {
	
	private static final String TAG = "FACEBOOK";
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);

	      ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);
		  
	      //*******
	      //Fragment fgmt_users 	  = new Like_Fragment();
	      //Fragment fgmt_userhistory 	= new EmConstrucao_Fragment();
	      //*******
		  
		  Bundle args = getIntent().getExtras();
		  
		  // set Fragmentclass Arguments
		  Fragment fgmt_rel = new Rel_Fragment();
		  fgmt_rel.setArguments(args);

		  //*
  	      android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
  	      android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
  	      //fragmentTransaction.replace(R.id.frame_container, new Rel_Fragment());
  	      fragmentTransaction.replace(R.id.frame_container, fgmt_rel);
  	      fragmentTransaction.commit();
  	      //*/
	      //*******
	  	  /*
	      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	      */
	      //actionBar.setTitle(" Adicione Nova Rota");
	      //actionBar.setDisplayShowTitleEnabled(true);

	      // Enable Action Bar
	      actionBar.show();
	      /*

	      Tab tab1 = actionBar
	          .newTab()
	          .setText("CURTIDAS")
	          //.setIcon(R.drawable.android_logo)
	          .setTabListener(new MyTabsListener(fgmt_users));

	      actionBar.addTab(tab1);
	      actionBar.selectTab(tab1);

	      Tab tab2 = actionBar
	          .newTab()
	          .setText("COMENTÁRIOS")
	          //.setIcon(R.drawable.windows_logo)
	          .setTabListener(new MyTabsListener(fgmt_userhistory));
	      
	      actionBar.addTab(tab2);
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
	  	/*
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
		*/
}
