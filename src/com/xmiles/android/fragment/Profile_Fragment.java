package com.xmiles.android.fragment;



import com.xmiles.android.sqlite.contentprovider.SqliteProvider;

import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.adapter.ImageAdapter;
import com.xmiles.android.support.LoadImageURL;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Profile_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

	TextView name;
	TextView city;
	TextView score;
	ImageView mUserPic;
	GridView gridView;
	//TAG
	private static final String TAG = "FACEBOOK";
	String json_name;
	String json_id;
	String json_city;	
	String picURL;
	//ProgressDialog progressBar;  
	private Handler mHandler;
	//---
	private static final Integer KEY_UF   = 3;
	private static final Integer KEY_CITY = 2;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;
	Cursor data_profile;

	
	static final String[] MOBILE_OS = new String[] { "Gravar", "Rotas",	
				"Ranking", "Histórico" };
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		mHandler = new Handler();
		
		getActivity().registerReceiver(ProfileFragmentReceiver, new IntentFilter("profilefragmentupdater"));
		
		View custom = inflater.inflate(R.layout.fgmt_background, container, false);
        View rootView = inflater.inflate(R.layout.profile_fgmt_custom, container, false);
        
        name = (TextView) rootView.findViewById(R.id.name);
        score = (TextView) rootView.findViewById(R.id.locat);
        city = (TextView) rootView.findViewById(R.id.info);
        mUserPic = (ImageView) rootView.findViewById(R.id.profile_pic);

		//-------
		View rootView2 = inflater.inflate(R.layout.profile_fgmt_gridview, container, false);
		gridView = (GridView) rootView2.findViewById(R.id.gridView1);
		gridView.setAdapter(new ImageAdapter(getActivity(), MOBILE_OS));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
                        //------------------------------------------------		                
                        //------------------------------------------------		                
                        //progressdialog = ProgressDialog.show(Main.this, "",
                        //getString(R.string.please_wait), true, true);
                        //------------------------------------------------		                
                        //------------------------------------------------					

				switch(position){
				 case 0:{
					 
						Toast.makeText(
								getActivity(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
					
					break;					 
				 }
				 case 1:{
					 
						Toast.makeText(
								getActivity(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
					
					break;				
				 }				
				 case 2:{

						Toast.makeText(
								getActivity(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();

					 
				 	break; 
				 }
				 case 3:{
					 
						Toast.makeText(
								getActivity(),
								((TextView) v.findViewById(R.id.grid_item_label))
										.getText() + " - em construção!", Toast.LENGTH_SHORT).show();
						
					break;					 
				  }				 
				}				
			}
		});
		
        //---------------		
        ((ViewGroup) custom).addView(rootView);

		((ViewGroup) custom).addView(rootView2);
        //------        
        /*
        progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		*/
		//--------------		
		UserProfile_Query upq = new UserProfile_Query();
		//--------------
		
		/** Creating a loader for populating city TextView from sqlite database */
		getLoaderManager().initLoader(0, null, this);

        return custom;
        
    }
	
	
	private final BroadcastReceiver ProfileFragmentReceiver = new BroadcastReceiver() {


		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			getLoaderManager().restartLoader(0, null, Profile_Fragment.this);
		}};

	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        Log.i(TAG, "onDestroy Profile_fgmt");
	        //-------------
	        getActivity().unregisterReceiver(ProfileFragmentReceiver);
	        //-------------
	        /* 
	        FragmentManager fragMgr = getFragmentManager();
	        Fragment currentFragment = (Fragment) fragMgr.findFragmentById(0);

	        if (currentFragment != null){	        	
	        	FragmentTransaction fragTrans = fragMgr.beginTransaction();
	            fragTrans.remove(currentFragment);
	            fragTrans.commit();
	        }
	        */
	    }

	 public class UserProfile_Query {
		 
		 public UserProfile_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {

				            
				            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;

				            data_profile = getActivity().getContentResolver().query(uri, null, null, null, null);
				            data_profile.moveToFirst();

				    		        	
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
	                                	
	            	                	//city.setText(json_city);
	            	                	name.setText(data_profile.getString(KEY_NAME));
	            	                	score.setText("Pontuação: 0 km");
	    			                    	
	    			        		        mHandler.post(new Runnable() {	
	    			                            @Override
	    			                            public void run() {
	    			                            	try {
	    			                    	
				    			                    	Context c = getActivity();
				    			                    	//Log.e(LOG,"picURL: " + data_profile.getString(KEY_PICURL));
				    			                    	Drawable drawable = new LoadImageURL(data_profile.getString(KEY_PICURL),c).execute().get();	    			                    	
				    			                    	mUserPic.setImageDrawable(drawable);		                   
	    		    			                    } catch (Exception e) {
	    		    			        				// TODO Auto-generated catch block
	    		    			        				e.printStackTrace();
	    		    			                    }
	    			                    	
	    			                            }
	    			        		        });
	                                }
	                            });
	                            Thread.sleep(100);
	                            //progressBar.dismiss();
	                            
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                }
	            }.start();
	        }

	 }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		//return null;
		Uri uri = SqliteProvider.CONTENT_URI_USER_PLACES;
		
		return new CursorLoader(getActivity(), uri, null, null, null, null);

		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Profile_Fgmnt onLoadFinished");
		if(data.moveToFirst()){
			city.setText(data.getString(KEY_CITY) + " - " + data.getString(KEY_UF));
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}



}

