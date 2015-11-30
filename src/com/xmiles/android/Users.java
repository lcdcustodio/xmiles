package com.xmiles.android;

import com.xmiles.android.fragment.Favorites_Fragment;
import com.xmiles.android.fragment.FbLogin_Fragment;
import com.xmiles.android.fragment.Users_Fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Users extends FragmentActivity {
	
	private static final String TAG = "FACEBOOK";
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_main);

	      ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);
		  
		  
  	    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
  	    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
  	    fragmentTransaction.replace(R.id.frame_container, new Users_Fragment());
  	    fragmentTransaction.commit();
		  
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
