package com.xmiles.android;


import com.xmiles.android.fragment.Push_Fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Push extends FragmentActivity {
	
	private static final String TAG = "FACEBOOK";
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);

	      setContentView(R.layout.activity_extra);
	      
 	   	  //Progress Dialog
	      /*
  		  final ProgressDialog pb_push;
  		  pb_push = new ProgressDialog(this);
  		  pb_push.setCancelable(true);
  		  pb_push.setMessage(this.getString(R.string.please_wait));
  		  pb_push.show();		        		
  		
  	      new Handler().postDelayed(new Runnable() {

  	          @Override
  	          public void run() {
  	              if (!isFinishing()) {
  	            	
  	            	  pb_push.dismiss();
  	            	
  	              }
  	          }

				  private boolean isFinishing() {
					  // TODO Auto-generated method stub
					  return false;
				  }
  	      }, 3000);
  	      */	

	      ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);

		  Bundle args = getIntent().getExtras();
		  
		  // set Fragmentclass Arguments
		  Fragment fgmt_push = new Push_Fragment();
		  fgmt_push.setArguments(args);

		  //*
  	      android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
  	      android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
  	      fragmentTransaction.replace(R.id.frame_container, fgmt_push);
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
