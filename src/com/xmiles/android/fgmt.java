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


public class fgmt extends FragmentActivity {
	


  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      

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
	      super.onDestroy();
	  }


}