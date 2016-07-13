package com.xmiles.android.fragment;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.backup.Scanning;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.facebook_api_support.SessionEvents;
import com.xmiles.android.facebook_api_support.SessionStore;
import com.xmiles.android.facebook_api_support.SessionEvents.AuthListener;
import com.xmiles.android.scheduler.Getting_UserLocation;

import android.app.ProgressDialog;
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
	public String[] permissions = { "read_stream", "publish_actions", "user_photos", "user_location", "user_friends", "user_birthday" };
	//TAG
	private static final String TAG = "FACEBOOK";
	
	ProgressDialog progressBar;
	//WebView webview;
	Button Fb_login;
	TextView txtHelp;
	
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


		    	Fblogin();
		    }
		});
		

		txtHelp = (TextView) rootView.findViewById(R.id.txtHelp);
		
		txtHelp.setText(Html.fromHtml("<a href=\"" + "https://www.youtube.com/embed/OvgtMaMftZw" + "\">"
				+ "Veja como funciona" + "</a> "));

		// Making url clickable
		txtHelp.setMovementMethod(LinkMovementMethod.getInstance());

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
		
		// start Scanning service
		//Scanning sc = new Scanning();			
		//sc.setAlarm(getActivity());

		// start Getting_Location service
		//Getting_UserLocation gl = new Getting_UserLocation();
		//gl.setAlarm(getActivity());
		
		
		//requestUserData();		
	    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
	    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();        
	    fragmentTransaction.replace(R.id.frame_container, new Splash_Fragment());
	    fragmentTransaction.commit();

		
		
	}


}
