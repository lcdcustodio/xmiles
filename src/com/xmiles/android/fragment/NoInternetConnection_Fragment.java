package com.xmiles.android.fragment;

import com.xmiles.android.R;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoInternetConnection_Fragment extends Fragment {

	//TAG
	//private static final String TAG = "FACEBOOK";

	public NoInternetConnection_Fragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.no_internet_connection, container,
				false);


		return rootView;

	}


}
