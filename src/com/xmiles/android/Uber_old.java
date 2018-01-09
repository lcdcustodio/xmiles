package com.xmiles.android;


import com.xmiles.android.fragment.Ranking_Fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Uber_old extends FragmentActivity {
	
	private static final String TAG = "FACEBOOK";
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);

	      //setContentView(R.layout.activity_extra);
	      setContentView(R.layout.webview_temp);

	      ActionBar actionBar = getActionBar();
		  actionBar.setDisplayHomeAsUpEnabled(true);
		  
		  actionBar.setTitle("#ubernoxmiles");


		  WebView webView = (WebView) findViewById(R.id.web);

		  //webView.setWebViewClient(new WebViewClient());
		  //---TEMP-----
		  //*
		  webView.setWebViewClient(new WebViewClient() {
			    public boolean shouldOverrideUrlLoading(WebView view, String url){
			        // do your handling codes here, which url is the requested url
			        // probably you need to open that url rather than redirect:
			        view.loadUrl(url);
			        
			        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
			        
			        return false; // then it is not handled by default action
			   }
			});
			//*/
		  //---TEMP-----		  
		  webView.getSettings().setJavaScriptEnabled(true);
		  webView.loadUrl("https://login.uber.com/oauth/v2/authorize?client_id=qimwuhpaW1P1i-XuskE7Z0z7_5iM4Eb1&response_type=code");

		  		  
		  // set Fragmentclass Arguments
		  /*
		  Fragment fgmt_rnk = new Ranking_Fragment();


		  
  	      android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
  	      android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
  	      fragmentTransaction.replace(R.id.frame_container, fgmt_rnk);
  	      fragmentTransaction.commit();
  	      */

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
