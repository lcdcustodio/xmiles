package com.xmiles.android.backup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.xmiles.android.NewRoutes;
import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.imageloader.LazyAdapter;
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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Like_Fragment_backup extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;	
	//---------------------
	protected static final Integer TYPE1 = 1;
	
	//---------------------
	ListView mListFavorites;
	Button Add_route;
	TextView Route;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	Cursor data_userFavorites;
	
	//-----------------------
	LazyAdapter adapter;
	ListView list;
	
	//-----------------------	
	private Handler mHandler;
	
	public Like_Fragment_backup(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		mHandler = new Handler();
		
		getActivity().registerReceiver(UsersFragmentReceiver, new IntentFilter("fragmentupdater"));
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);

		View rootView2 = inflater.inflate(R.layout.user_header, container, false);
		
		View custom = inflater.inflate(R.layout.favorites_fragment, null); 
		
		//mListFavorites = (ListView) custom.findViewById(R.id.list_favorites);
		list 		   = (ListView) custom.findViewById(R.id.list_favorites);
		//Route          = (TextView) rootView2.findViewById(R.id.rotas);
		//---------------		
		//mListFavorites.addFooterView(rootView1);
		//---------------		

		//---------------
		
        progressBar = new ProgressDialog(getActivity());
		//*
        progressBar.setCancelable(true);
		progressBar.setMessage(getActivity().getString(R.string.please_wait));
		progressBar.show();
		//*/
		//--------------

		
		Users_Query uq = new Users_Query();
		
		((ViewGroup) rootView).addView(rootView2);
		((ViewGroup) rootView).addView(custom);
		
		/** Creating a loader for populating listview from sqlite database */
		//getLoaderManager().initLoader(0, null, this);
		
        // Create an empty adapter we will use to display the loaded data.
		//FavoritesListAdapter mAdapter = new FavoritesListAdapter(getActivity());
        //setListAdapter(mAdapter);
		
		return rootView;
    }
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		Uri uri = SqliteProvider.CONTENT_URI_USER_FAVORITES;
		
		return new CursorLoader(getActivity(), uri, null, null, null, null);
		//return data_userFavorites; 
		//return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub
		//mListFavorites.setAdapter(new FavoritesListAdapter(getActivity(), data));

		Log.d(TAG, "Favotires_Fgmnt onLoadFinished");

		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private final BroadcastReceiver UsersFragmentReceiver = new BroadcastReceiver() {


		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			getLoaderManager().restartLoader(0, null, Like_Fragment_backup.this);
		}};

	
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Like_fgmt");
	        //-------------
	        getActivity().unregisterReceiver(UsersFragmentReceiver);
	        //-------------
	    }
	 
	 public class Users_Query {
		 
		 public Users_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {


				            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;

				            data_userFavorites = getActivity().getContentResolver().query(uri, null, null, null, null);
				            data_userFavorites.moveToFirst();

				    		        	
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
			//progressBar.dismiss();
			runThread();			 

		 }
		 
	        private void runThread() {

	            new Thread() {
	                public void run() {

	                        try {
	                        	getActivity().runOnUiThread(new Runnable() {

	                                @Override
	                                public void run() {
	                                	
	                                    adapter=new LazyAdapter(getActivity(), data_userFavorites);        
	                                    list.setAdapter(adapter);

	                                }
	                            });
	                            //Thread.sleep(400);
	                            Thread.sleep(50);
	                            progressBar.dismiss();
	                            
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                }
	            }.start();
	        }

	 }
	 


	    
}
