package com.xmiles.android;


import com.xmiles.android.fragment.Likes_Fragment;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Likes extends FragmentActivity {
	
	private static final String TAG = "FACEBOOK";
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      //setContentView(R.layout.activity_main);
	      setContentView(R.layout.activity_extra);

	      ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);

		  //Bundle args = getIntent().getExtras();
		  
		  // set Fragmentclass Arguments
		  Fragment fgmt_likes = new Likes_Fragment();
		  //fgmt_likes.setArguments(args);

		  //*
  	      android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
  	      android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
  	      fragmentTransaction.replace(R.id.frame_container, fgmt_likes);
  	      fragmentTransaction.commit();

	      // Enable Action Bar
	      actionBar.show();

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
}
