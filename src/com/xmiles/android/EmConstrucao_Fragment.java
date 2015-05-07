package com.xmiles.android;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class EmConstrucao_Fragment extends Fragment {

public EmConstrucao_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.em_construcao_fgmt, container, false);
         
        return rootView;
    }
}
