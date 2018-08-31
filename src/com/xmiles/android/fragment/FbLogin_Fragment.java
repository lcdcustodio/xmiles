package com.xmiles.android.fragment;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;


import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.facebook_api_support.SessionEvents;
import com.xmiles.android.facebook_api_support.SessionStore;
import com.xmiles.android.facebook_api_support.SessionEvents.AuthListener;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.ConnectionDetector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FbLogin_Fragment extends Fragment {

	// Facebook Permissions	
	//public String[] permissions = { "read_stream", "publish_actions", "user_photos", "user_location" };
	//public String[] permissions = { "read_stream", "publish_actions", "user_photos", "user_location", "user_friends" };
	//public String[] permissions = { "read_stream", "publish_actions", "user_photos", "user_location", "user_friends", "user_birthday" };
	//public String[] permissions = { "publish_actions", "user_photos", "user_location", "user_friends", "user_birthday" };
	public String[] permissions = { "user_photos", "user_location", "user_friends", "user_birthday" };
	//TAG
	private static final String TAG = "FACEBOOK";
	
	ProgressDialog progressBar;
	//WebView webview;
	Button Fb_login;
	Button Youtube;
	TextView termofuse;
	
	// Connection detector
	ConnectionDetector cd;

	
	public FbLogin_Fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fblogin_fragment, container,		
				false);
		
		Fb_login = (Button) rootView.findViewById(R.id.fblogin);
		
		Fb_login.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {

		    	
		    	cd = new ConnectionDetector(getActivity().getApplicationContext());
		    	
				// Check if Internet present
				if (!cd.isConnectingToInternet()) {
		 		       	
				    
				    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
				    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
				    fragmentTransaction.replace(R.id.frame_container, new NoInternetConnection_Fragment());
				    fragmentTransaction.commit();

		        	
		        } else {
		    	
		        	Fblogin();
		        }
		    }
		});
		
		
		Youtube = (Button) rootView.findViewById(R.id.helpbutton);
		
		Youtube.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	cd = new ConnectionDetector(getActivity().getApplicationContext());
		    	
				// Check if Internet present
				if (!cd.isConnectingToInternet()) {
		 		       	
				    
				    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
				    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
				    fragmentTransaction.replace(R.id.frame_container, new NoInternetConnection_Fragment());
				    fragmentTransaction.commit();

		        	
		        } else {
		        	
		        	 
		        	Uri uri = Uri.parse("https://www.youtube.com/embed/GGi3iWkjJos"); 
		        	//Uri uri = Uri.parse("http://www.xmiles.com.br/youtube");
		        	
		        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		        	startActivity(intent);
		    	
		        }				
		    }
		});
		
		//*****************************************
		// Aguardando Vídeo da Solda Fria Produções
		//Youtube.setEnabled(false);
		// Aguardando Vídeo da Solda Fria Produções
		//*****************************************		

		termofuse = (TextView) rootView.findViewById(R.id.termofuse_2);
		
		termofuse.setText(Html.fromHtml("<a href=\"" + "http://www.xmiles.com.br/termo-de-uso" + "\">"
				+ "Termo de Uso" + "</a> "));

		// Making url clickable
		termofuse.setMovementMethod(LinkMovementMethod.getInstance());

		//--------
		//((ViewGroup) rootView).addView(custom);
		//--------		
		return rootView;
	}
	
	@SuppressWarnings("deprecation")
	public void Fblogin(){
		if (!Utility.mFacebook.isSessionValid()) {	

			Utility.mFacebook.authorize(getActivity(),
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
							SessionStore.save(Utility.mFacebook, getActivity().getBaseContext());					        
							//-------------------		
							Splash();
							//-------------------		
							
						}

						@Override
						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							
						}


					});
		}
	}
	
	public void Splash(){
		

		 //--------
		 DatabaseHelper mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());		 
		 mDatabaseHelper.resetUserProfile();
		 //--------		
		
	    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
	    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();        
	    fragmentTransaction.replace(R.id.frame_container, new Splash_Fragment());
	    fragmentTransaction.commit();

		
		
	}


}
