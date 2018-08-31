package com.xmiles.android.fragment;

import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.toolbox.JsonObjectRequest;
import com.xmiles.android.R;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.webservice.UserFunctions;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Uber_Fragment extends Fragment {

	//TAG
	//private static final String TAG = "FACEBOOK";
	private static String URL_1 = "https://login.uber.com/oauth/v2/authorize?client_id=qimwuhpaW1P1i-XuskE7Z0z7_5iM4Eb1&response_type=code";
	//private static String URL_2 = "https://auth.uber.com";
	private static String URL_2 = "https://sso.cisco.com";
	private WebView webView;
	private ProgressBar pb;
	private static final Integer KEY_USER_ID 	= 0;
	
	public Uber_Fragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.webview, container,
				false);

		webView = (WebView) rootView.findViewById(R.id.web);
		pb      = (ProgressBar) rootView.findViewById(R.id.pb);
		
		
		webView.setWebViewClient(new WebViewClient() {

			    @Override 
			    public boolean shouldOverrideUrlLoading(WebView view, String url){
			        // do your handling codes here, which url is the requested url
			        // probably you need to open that url rather than redirect:
			        view.loadUrl(url);
			        
			        //Toast.makeText(getActivity().getApplicationContext(), url, Toast.LENGTH_LONG).show();
			        
			        if (url.split("/autho")[0].equals(URL_2)){
			        	
			        	String uber_code = url.split("code=")[1];
				    
			        	Uplad_Uber_Info uber = new Uplad_Uber_Info(uber_code);	
					    
					    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
					    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
					    
					    //fgmnt temp 
					    fragmentTransaction.replace(R.id.frame_container, new NoInternetConnection_Fragment());
					    fragmentTransaction.commit();
					    //fgmnt temp			        
					    
					    
			        } 
			        
			        return false; // then it is not handled by default action
			   }

			     
			    @Override 
			    public void onPageFinished(WebView view, String url) {
			    	//super.onPageFinished(webView, url);

			    	pb.setVisibility(View.INVISIBLE);
			    	view.findViewById(R.id.web).setVisibility(View.VISIBLE);
			    } 
			 
			   
			    
			    
		});
		
		// Enable Javascript
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		webView.loadUrl(URL_1);


		return rootView;

	}
	
	
	
	public class Uplad_Uber_Info{		
	
		 public Uplad_Uber_Info(final String uber_code){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {
				        	
							Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;
							Cursor data_profile = getActivity().getApplicationContext().getContentResolver().query(uri_1, null, null, null, null);
							data_profile.moveToFirst();

						    UserFunctions userFunction = new UserFunctions();					    
						    
						    JSONObject json = userFunction.uberToken(data_profile.getString(KEY_USER_ID), uber_code);
						    
						    if (json.getString("success") != null) {

				                String res = json.getString("success");
				                if(Integer.parseInt(res) == 1){
				                	
				                	//update uber table at Sqlite
				                	String lala = "lala";

				                	
				                }
						    }

				        	
				        } catch (JSONException e) {
				            e.printStackTrace();
				        }
				}
			 });

			 thread.start();			 
		 }		 
		
	}


}
