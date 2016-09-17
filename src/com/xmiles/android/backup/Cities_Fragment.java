package com.xmiles.android.backup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.UserFunctions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Cities_Fragment extends Fragment implements OnItemClickListener {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_ID = 0;
	//---------------------
	//---------------------
	ListView mListCities;
	TextView header;
	SimpleCursorAdapter mAdapter;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	int KEY_TEMP = 0;
	
	public Cities_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		View rootView2 = inflater.inflate(R.layout.newroutes_header, container, false);
		
		View custom = inflater.inflate(R.layout.cities_fragment, null); 
		
		mListCities = (ListView) custom.findViewById(R.id.list_cities);
		
		header = (TextView) rootView2.findViewById(R.id.rotas);
		header.setText("Escolha a cidade:");
		//-------TEMP-----------
	    /*
		String[] cities = new String[] {
	            "Rio de Janeiro",
	            "Porto Alegre"
	        };
	    */    
	    String[] cities = new String[] {
	            "Rio de Janeiro - RJ",
	            "Porto Alegre - RS"
	        };
	    	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,cities);
	    mListCities.setAdapter(adapter);	    
	    mListCities.setItemChecked(KEY_TEMP, true);
	    //-------------
	    insert_DB(cities[KEY_TEMP]);
	    //-------------
	    mListCities.setOnItemClickListener((OnItemClickListener) this);
	    
		//----------------------
		/*
        progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		*/
		//--------------

		
		//Favorites_Query fq = new Favorites_Query();
		
		((ViewGroup) rootView).addView(rootView2);
		((ViewGroup) rootView).addView(custom);

		return rootView;
    }
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//Log.d(TAG, (String)parent.getItemAtPosition(position));
		//Toast.makeText(getActivity(), (String)parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
		insert_DB((String)parent.getItemAtPosition(position));
	}
	
	public void insert_DB(String city_uf){
		
        //----------------------------------------------
    	
        Support support = new Support();
        
		/** Setting up values to insert into UserProfile table */
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(DatabaseHelper.KEY_CITY_BUSLINE,city_uf);        			
		contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());
		
		//getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_CITY_BUSLINE, contentValues);
		getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_CITY_BUSLINE_create, contentValues);
        //----------------------------------------------

	}
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Cities_fgmt");
	        
	    }

	    
}
