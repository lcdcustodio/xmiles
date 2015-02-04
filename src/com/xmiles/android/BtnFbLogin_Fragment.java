package com.xmiles.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.xmiles.android.facebook_api_support.SessionEvents;
import com.xmiles.android.facebook_api_support.SessionEvents.AuthListener;
import com.xmiles.android.facebook_api_support.SessionEvents.LogoutListener;
import com.xmiles.android.facebook_api_support.SessionStore;
import com.xmiles.android.facebook_api_support.Utility;

//public class MainActivity extends Activity {
public class BtnFbLogin_Fragment extends FragmentActivity {
//public class MainActivity extends android.support.v4.app.FragmentActivity {
//public class MainActivity extends android.support.v4.app.FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>  {	
	


	
	// Your Facebook APP ID	
	private static final String APP_ID = "844332932270301";

	// Facebook Permissions	
    public String[] permissions = { "read_stream", "offline_access", "publish_stream", "user_photos", "publish_checkins",
          "photo_upload", };	

    private static final String TAG = "FACEBOOK";

    // Instance of Facebook Class	
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";

	
	// FAcebook LoginButtons	
	ImageButton imgbtnFbLogin;

	
    // define service
    //SampleAlarmReceiver alarm = new SampleAlarmReceiver();
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      // start service
      //alarm.setAlarm(this);

      //mTitle = mDrawerTitle = getTitle();

      // enabling action bar app icon and behaving it as toggle button
      //getActionBar().setDisplayHomeAsUpEnabled(true);
      getActionBar().setTitle(null);
      //getActionBar().setHomeButtonEnabled(true);

      
      //-----------------------------
      //Facebook Authentication
      imgbtnFbLogin = (ImageButton) findViewById(R.id.imgbtn_fblogin);
      
      
	    // Create the Facebook Object using the app id.
	    Utility.mFacebook = new Facebook(APP_ID);
      // Instantiate the asynrunner object for asynchronous api calls.
      Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);        
      // restore session if one exists
      SessionStore.restore(Utility.mFacebook, this);
      SessionEvents.addAuthListener(new FbAPIsAuthListener());
      SessionEvents.addLogoutListener(new FbAPIsLogoutListener());
      //---------
      //--------- 
      if (Utility.mFacebook.isSessionValid()) {	
      	//----------
      	Log.i(TAG, "" + "FB Sessions " + Utility.mFacebook.isSessionValid());
			requestUserData();		
      	
		} else {
	        //----hide action bar---        
	        getActionBar().hide();
	        //------------------------------			
		}
      
		imgbtnFbLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Log.d("Image Button", "button Clicked");
				Log.d(TAG, "Facebook Button Clicked");
				loginToFacebook();
			}
		});
		
      //-----------------------------

      if (savedInstanceState == null) {
          // on first time display view for first nav item
          //displayView(0);
      }
  }
  
	  @Override
	  public void onDestroy() {
	
	  	
	  	//close Database
	  	//DatabaseHelper mDatabaseHelper = new DatabaseHelper(getApplicationContext());
	  	//mDatabaseHelper.closeDB();
	      // cancel service        
	  	//alarm.cancelAlarm(this);
	  	//stop ServiceLocation    	
	  	//stopService(new Intent(MainActivity.this, ServiceLocation.class));
	      Log.d(TAG, "onDestroy"); 
	      super.onDestroy();
	  }


  /**
   * Diplaying fragment view for selected nav drawer list item
   * */
  private void displayView(int position) {
      // update the main content by replacing fragments
      Fragment fragment = null;

      switch (position) {
      // Banner
      case 0:
      	//fragment = new HomeFragment();
          break;
      // Profile    
      case 1:
          //fragment = new FindPeopleFragment();
      	//fragment = new ProfileFragment();
      	//fragment = new ProfileFragment_rev02();
      	break;
      // Map Header
      case 2:        	
          break;
      // Map Bitmap
      case 3:
      	//fragment = new HomeFragment();            
          break;
      // Event    
      case 4:        	
          break;
      // Workshops    
      case 5:
      	//fragment = new WhatsHotFragment();
          break;
      // Palestras    
      case 6:
      	//fragment = new HomeFragment();
          break;
      // Expositores    
      case 7:
      	//fragment = new HomeFragment();
          break;
      // People    
      case 8:        	
          break;
      // Amigos    
      case 9:
      	//fragment = new FriendsFragment_old5();
      	//fragment = new FriendsFragment_old4();
      	//fragment = new FriendsFragment();
          //fragment = new PhotosFragment();
          break;
      // Palestrantes    
      case 10:
        //	fragment = new SpeakersFragment();
          break;
      // Participantes    
      case 11:
       // 	fragment = new HomeFragment();
          break;
          
      default:
          break;
      }
      
      if (fragment != null) {

          /** TESTE */
          
          //-------
          android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
          android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();        
          fragmentTransaction.replace(R.id.frame_container, fragment);
          fragmentTransaction.commit();
          
          /** FIM TESTE */

      } else {
          // error in creating fragment
          Log.e("MainActivity", "Error in creating fragment");
      }
  }


  /**
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */

    
  
  
  /**
	 * Function to login into facebook
	 * */
	public void loginToFacebook() {

		Log.d(TAG, "" + "FB Sessions " + Utility.mFacebook.isSessionValid());
			

		if (!Utility.mFacebook.isSessionValid()) {	

			Utility.mFacebook.authorize(this,
				permissions,
				Utility.mFacebook.FORCE_DIALOG_AUTH,
			    new DialogListener() {
				
							
						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event

							//----------------------
							SessionStore.save(Utility.mFacebook, getBaseContext());

							// start service
					        //alarm.setAlarm(MainActivity.this);
					        //ServiceLocation					        
					        //startService(new Intent(MainActivity.this, ServiceLocation.class));					        
					        
							//-------------------
							requestUserData();
							//-------------------
							
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

  public class FbAPIsAuthListener implements AuthListener {

      @Override
      public void onAuthSucceed() {
          requestUserData();
      }

      @Override
      public void onAuthFail(String error) {
          //mText.setText("Login Failed: " + error);
      }
  }

  /*
   * The Callback for notifying the application when log out starts and
   * finishes.
   */
  public class FbAPIsLogoutListener implements LogoutListener {
      @Override
      public void onLogoutBegin() {
          //mText.setText("Logging out...");
      }

      @Override
      public void onLogoutFinish() {
          //mText.setText("You have logged out! ");
          //mUserPic.setImageBitmap(null);
      }
  }
  public void requestUserData() {
		//----
  	Log.d(TAG, "requestUserData()");
	  //----
      //---------------
	  // Enable Action Bar
  	  getActionBar().show();

  }
}