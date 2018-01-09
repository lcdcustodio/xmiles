package com.xmiles.android.fragment;

import com.xmiles.android.R;


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
	private static String URL_2 = "https://auth.uber.com";
	private WebView webView;
	private ProgressBar pb;
	
	public Uber_Fragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View rootView = inflater.inflate(R.layout.webview_temp, container,
		View rootView = inflater.inflate(R.layout.webview, container,
				false);

		webView = (WebView) rootView.findViewById(R.id.web);
		pb      = (ProgressBar) rootView.findViewById(R.id.pb);
		
		
		webView.setWebViewClient(new WebViewClient() {
			    //*
			    @Override 
			    public boolean shouldOverrideUrlLoading(WebView view, String url){
			        // do your handling codes here, which url is the requested url
			        // probably you need to open that url rather than redirect:
			        view.loadUrl(url);
			        
			        Toast.makeText(getActivity().getApplicationContext(), url, Toast.LENGTH_LONG).show();
			        
			        //if (!url.split("/login")[0].equals(URL_2)){
			        if (!url.split("/login")[0].equals(URL_2)){	
					    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
					    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
					    fragmentTransaction.replace(R.id.frame_container, new NoInternetConnection_Fragment());
					    fragmentTransaction.commit();
			        
			        } else if (url.split("/login")[0].equals(URL_2)){
			        	
		          
			      	
			        }
			        	
			        
			        return false; // then it is not handled by default action
			   }
				//*/

			     
			    @Override 
			    public void onPageFinished(WebView view, String url) {
			    	//super.onPageFinished(webView, url);
			    	//view.findViewById(R.id.pb).setVisibility(View.GONE);
			    	//findViewById(R.id.pb).setVisibility(View.GONE);
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


}
