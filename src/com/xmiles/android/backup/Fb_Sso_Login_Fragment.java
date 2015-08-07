package com.xmiles.android.backup;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;

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

//import android.app.Fragment;
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


import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.R.menu;
import com.xmiles.android.facebook_api_support.SessionEvents;
import com.xmiles.android.facebook_api_support.SessionEvents.AuthListener;
import com.xmiles.android.facebook_api_support.SessionEvents.LogoutListener;
import com.xmiles.android.facebook_api_support.SessionStore;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.fragment.EmConstrucao_Fragment;
import com.xmiles.android.fragment.Favorites_Fragment;
import com.xmiles.android.fragment.Profile_Fragment;
import com.xmiles.android.scheduler.FbPlaces_AlarmReceiver;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.webservice.UserFunctions;



//public class BtnFbLogin_Fragment extends FragmentActivity implements ActionBar.TabListener {
	public class Fb_Sso_Login_Fragment extends FragmentActivity {	

	// Your Facebook APP ID	
	private static final String APP_ID = "844332932270301";

	// Facebook Permissions	
	public String[] permissions = { "read_stream", "publish_actions", "user_photos", "user_location" };
	
    private static final String TAG = "FACEBOOK";
    private static final Integer KEY_ID = 0;

    // Instance of Facebook Class	
	private AsyncFacebookRunner mAsyncRunner;
	
	// FAcebook LoginButtons	
	ImageButton imgbtnFbLogin;

    //get Context        
    Context ctx;

    FbPlaces_AlarmReceiver FbPlaces_alarm;
    
    DatabaseHelper  mDatabaseHelper;
	
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //setContentView(R.layout.activity_main);
      setContentView(R.layout.activity_main_bckup_rev02);

  	
      // define service
      //SampleAlarmReceiver alarm = new SampleAlarmReceiver();
  	  //alarm = new FbPlaces_AlarmReceiver(ctx);
      FbPlaces_alarm = new FbPlaces_AlarmReceiver();
      

      Log.i(TAG,"OnCreated BtnFbLogin");
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
      		// TEMPORARY - resetUserFavorites  
      		mDatabaseHelper = new DatabaseHelper(getApplicationContext());
      		mDatabaseHelper.resetUserFavorites();     		
      		
      	
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
		  getMenuInflater().inflate(R.menu.main_frame, menu);

	      return true;
	  }
  
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        super.onOptionsItemSelected(item);
	 
	        switch(item.getItemId()){
	            
	        	case android.R.id.home:

	            	//Toast.makeText(getBaseContext(), "You selected UpButton", Toast.LENGTH_SHORT).show();

	            	break;
	        		
	        		
	        		
	        	case R.id.new_route:

	            	Toast.makeText(getBaseContext(), "You selected New Route", Toast.LENGTH_SHORT).show();
	                

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
	  	FbPlaces_alarm.cancelAlarm(this);
		  
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
							FbPlaces_alarm.setAlarm(Fb_Sso_Login_Fragment.this);
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
      
      //*******
      Fragment fgmt_inicio 	  = new Profile_Fragment();
      Fragment fgmt_favoritos = new Favorites_Fragment();
      Fragment fgmt_ranking = new EmConstrucao_Fragment();
      //*******
  	  
      ActionBar actionBar = getActionBar();
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      //actionBar.setDisplayShowTitleEnabled(true);

      // Enable Action Bar
      actionBar.show();


      Tab tab1 = actionBar
          .newTab()
          .setText("INÍCIO")
          //.setIcon(R.drawable.android_logo)
          .setTabListener(new MyTabsListener(fgmt_inicio));

      actionBar.addTab(tab1);
      actionBar.selectTab(tab1);

      Tab tab2 = actionBar
          .newTab()
          .setText("FAVORITOS")
          //.setIcon(R.drawable.windows_logo)
          .setTabListener(new MyTabsListener(fgmt_favoritos));
      
      actionBar.addTab(tab2);
      
      Tab tab3 = actionBar
              .newTab()
              .setText("RANKING")
              //.setIcon(R.drawable.windows_logo)
              .setTabListener(new MyTabsListener(fgmt_ranking));
          
          actionBar.addTab(tab3);
      
      

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