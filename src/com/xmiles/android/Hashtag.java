package com.xmiles.android;


import com.xmiles.android.fragment.Hashtag_Fragment;
import com.xmiles.android.fragment.Rel_Fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class Hashtag extends FragmentActivity {
	
	private static final String TAG = "FACEBOOK";
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);

	      setContentView(R.layout.activity_extra);

	      ActionBar actionBar = getActionBar();
		  //------------------
		  Bundle args = getIntent().getExtras();
		  
		  // set Fragmentclass Arguments
		  Fragment fgmt_hash = new Hashtag_Fragment();
		  fgmt_hash.setArguments(args);
		  //------------------
		  
	      actionBar.setDisplayHomeAsUpEnabled(true);
	      actionBar.setDisplayShowHomeEnabled(false);
	      actionBar.setDisplayShowTitleEnabled(true);
	      //actionBar.setTitle(Html.fromHtml("<i><b><font color='#ffffff'>    " +  args.getString("hashtag") + "</font></b></i>"));
	      actionBar.setTitle(Html.fromHtml("<b><font color='#ffffff'>      " +  args.getString("hashtag") + "</font></b>"));
	      actionBar.setDisplayUseLogoEnabled(false);
	      


		  //*
  	      android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
  	      android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
  	      fragmentTransaction.replace(R.id.frame_container, fgmt_hash);
  	      fragmentTransaction.commit();

	      // Enable Action Bar
	      actionBar.show();

	}
	
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      //getMenuInflater().inflate(R.menu.main, menu);
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
	  
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	            case android.R.id.home:

	            	finish();
	            	break;
	      }
	      return true;
	    }
}
