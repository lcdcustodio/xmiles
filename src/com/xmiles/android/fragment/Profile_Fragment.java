package com.xmiles.android.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.listviewfeed.HashtagListAdapter;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.imageloader.ProfileLazyAdapter;
import com.xmiles.android.support.imageloader.RankingLazyAdapter;
import com.xmiles.android.webservice.UserFunctions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Profile_Fragment extends Fragment {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;	
	private static final Integer KEY_U_ID   = 0;
	private static final String hashtag = "#histórico_pontuação";
	//---------------------
	protected static final Integer TYPE1 = 1;
	
	//---------------------
	ListView mListFavorites;
	Button Add_route;
	TextView Route;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	//ProgressDialog progressBar;
	Cursor data_History;
	
	//-----------------------
	ProfileLazyAdapter listAdapter;
	ListView listView;	
	private List<FeedItem> feedItems;
	
	//-----------------------	
	
	public Profile_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);

		View custom = inflater.inflate(R.layout.ranking_fgmt, null); 


		listView 		   = (ListView) custom.findViewById(R.id.list);
		//---------------
		//feedItems = new ArrayList<FeedItem>();
		//listAdapter = new ProfileLazyAdapter(getActivity(), feedItems);
		//listView.setAdapter(listAdapter);
		
		//---------------
        //progressBar = new ProgressDialog(getActivity());

        //progressBar.setCancelable(true);
		//progressBar.setMessage(getActivity().getString(R.string.please_wait));
		//progressBar.show();


		Ranking_Query rq = new Ranking_Query();
		

		((ViewGroup) rootView).addView(custom);
		
		return rootView;
    }
		
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        //Log.d(TAG, "onDestroy Profile_fgmt");
	    }
	 
	 public class Ranking_Query {
		 
		 public Ranking_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {


				            Uri uri = SqliteProvider.CONTENT_URI_HISTORY;
				            data_History = getActivity().getContentResolver().query(uri, null, null, null, null);
			            
	

				    		        	
					    } catch (Exception e) {
				            e.printStackTrace();
				    }
				}
		});

		thread.start();
		//-----------
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//-----------

		runThread();			 

	 }
	 
        private void runThread() {

            new Thread() {
                public void run() {

                        try {
                        	getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                	
                                	listAdapter=new ProfileLazyAdapter(getActivity(), data_History);        
                                	listView.setAdapter(listAdapter);


                                }
                            });
                            //Thread.sleep(400);
                            Thread.sleep(50);
                            //progressBar.dismiss();
                            
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
            }.start();
        }

 }
 


    
}
