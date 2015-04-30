package com.xmiles.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


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
import com.xmiles.android.scheduler.FbPlaces_AlarmReceiver;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.webservice.UserFunctions;



//public class BtnFbLogin_Fragment extends FragmentActivity implements ActionBar.TabListener {
	public class BtnFbLogin_Fragment_bkp2 extends FragmentActivity {	

	// Your Facebook APP ID	
	private static final String APP_ID = "844332932270301";

	// Facebook Permissions	
    public String[] permissions = { "read_stream", "offline_access", "publish_stream", "user_photos", "publish_checkins",
          "photo_upload","user_location" };	

    private static final String TAG = "FACEBOOK";
    private static final Integer KEY_ID = 0;

    // Instance of Facebook Class	
	private AsyncFacebookRunner mAsyncRunner;
	
	// FAcebook LoginButtons	
	ImageButton imgbtnFbLogin;

    //get Context        
    Context ctx;

    FbPlaces_AlarmReceiver FbPlaces_alarm;
	
    ListView busLine;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      //setContentView(R.layout.activity_main_bkp);

      //get Context        
      //ctx = getApplicationContext();
  	
      // define service
      //SampleAlarmReceiver alarm = new SampleAlarmReceiver();
  	  //alarm = new FbPlaces_AlarmReceiver(ctx);
      FbPlaces_alarm = new FbPlaces_AlarmReceiver();
      
      // enabling action bar app icon and behaving it as toggle button
      //getActionBar().setDisplayHomeAsUpEnabled(true);
      //getActionBar().setTitle(null);
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

      		// start service
      		FbPlaces_alarm.setAlarm(this);
      	
      	    //ServiceLocation					        
            //startService(new Intent(BtnFbLogin_Fragment.this, ServiceLocation.class));					        

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
  }
  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	  }
  
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        super.onOptionsItemSelected(item);
	 
	        switch(item.getItemId()){
	            case R.id.rotas:
	                //Toast.makeText(getBaseContext(), "You selected Rotas", Toast.LENGTH_SHORT).show();
	                
	                //----------------------
            		//*
            		Thread thread = new Thread(new Runnable(){
            		    @Override
            		    public void run() {
            		        try {
            		//*/        

			                Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
		                	Cursor data = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
		                	
		                	if (data != null && data.getCount() > 0){
		                		data.moveToFirst();
		                		Log.i(TAG,"testing SQLITE: " + data.getString(KEY_ID) + "," + data.getString(1));

		                		//Your code goes here
                            	UserFunctions userFunc = new UserFunctions();
                		        JSONObject json = userFunc.favoritesRoutes(data.getString(KEY_ID));

		                	}
		            		        	
		    		        } catch (Exception e) {
		    		            e.printStackTrace();
		    		        }
		    		    }
		    		});
		
		    		thread.start();
                	
	                //----------------------


	            	break;
	 
	            case R.id.computer:
	                Toast.makeText(getBaseContext(), "You selected Computer", Toast.LENGTH_SHORT).show();
	                break;
	            }	            
	        return true;
	    }
	    
	  @Override
	  public void onDestroy() {
	
	  	
	  	//close Database
	  	DatabaseHelper mDatabaseHelper = new DatabaseHelper(getApplicationContext());
	  	mDatabaseHelper.closeDB();
	    // cancel service        
	  	//FbPlaces_alarm.cancelAlarm(this);
		  
	  	//stop ServiceLocation    	
	  	//stopService(new Intent(BtnFbLogin_Fragment.this, ServiceLocation.class));
	    
	  	Log.d(TAG, "onDestroy: BtnFbLogin_Fragment"); 
	    super.onDestroy();
	  }
  
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
							FbPlaces_alarm.setAlarm(BtnFbLogin_Fragment_bkp2.this);
					        //ServiceLocation					        
					        //startService(new Intent(BtnFbLogin_Fragment.this, ServiceLocation.class));					        
					        
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
      //---------------	  
  	  
  	  /**
  	   * Diplaying fragment
  	   * */
      
      
  	  //Fragment fragment = new Profile_Fragment();
  	  //Fragment fragment = new ActionTab_Fragment();

  	  android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
      android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();        
      //fragmentTransaction.replace(R.id.frame_container, fragment);
      
      //android.app.FragmentManager fragMgr = getFragmentManager();
      //Fragment currentFragment = (Fragment) fragMgr.findFragmentById(0);
      
      fragmentTransaction.commit();
      

  }
  

}