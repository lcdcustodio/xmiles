package com.xmiles.android;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.xmiles.android.facebook_api_support.SessionEvents;
import com.xmiles.android.facebook_api_support.SessionStore;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.facebook_api_support.SessionEvents.AuthListener;
import com.xmiles.android.fragment.FbLogin_Fragment;
import com.xmiles.android.fragment.Splash_Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class Welcome extends FragmentActivity {

	// Your Facebook APP ID
	private static final String APP_ID = "844332932270301";
	// TAG
    private static final String TAG = "FACEBOOK";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_extra);

        //----hide action bar---
		//<!--Toolbar Hive example -->
        //getActionBar().hide();

  	    // Create the Facebook Object using the app id.
  	    Utility.mFacebook = new Facebook(APP_ID);

  	    // Instantiate the asynrunner object for asynchronous api calls.
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        // restore session if one exists
        SessionStore.restore(Utility.mFacebook, this);
        SessionEvents.addAuthListener(new FbAPIsAuthListener());


        if (Utility.mFacebook.isSessionValid()) {
          	//----------
          	Log.i(TAG, "" + "FB Sessions " + Utility.mFacebook.isSessionValid());

    		//requestUserData();
		    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
		    fragmentTransaction.replace(R.id.frame_container, new Splash_Fragment());
		    fragmentTransaction.commit();


    	} else {

    	    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    	    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
    	    fragmentTransaction.replace(R.id.frame_container, new FbLogin_Fragment());
    	    fragmentTransaction.commit();


    	}

	}
	/*
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
	*/
	  public class FbAPIsAuthListener implements AuthListener {

	      @Override
	      public void onAuthSucceed() {
	    	  //requestUserData();
			  android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
			  android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
			  fragmentTransaction.replace(R.id.frame_container, new Splash_Fragment());
			  fragmentTransaction.commit();

	      }

	      @Override
	      public void onAuthFail(String error) {
	          //mText.setText("Login Failed: " + error);
	      }
	  }

}
