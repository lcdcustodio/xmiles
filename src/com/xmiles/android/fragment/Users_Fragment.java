package com.xmiles.android.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.xmiles.android.NewRoutes;
import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.support.LoadImageURL;
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


public class Users_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_PICURL = 2;	
	//---------------------
	protected static final Integer TYPE1 = 1;
	private static final Integer TYPE2 = 2;	
	//---------------------
	ListView mListFavorites;
	Button Add_route;
	TextView Route;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	Cursor data_userFavorites;
	
	private Handler mHandler;
	
	public Users_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		mHandler = new Handler();
		
		getActivity().registerReceiver(UsersFragmentReceiver, new IntentFilter("fragmentupdater"));
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);

		//View rootView2 = inflater.inflate(R.layout.favorites_header, container, false);
		
		View custom = inflater.inflate(R.layout.favorites_fragment, null); 
		
		mListFavorites = (ListView) custom.findViewById(R.id.list_favorites);		
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
		
		//((ViewGroup) rootView).addView(rootView2);
		((ViewGroup) rootView).addView(custom);
		//((ViewGroup) rootView).addView(rootView1);
		
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
        if (data == null || data.getCount() == 0){
        	Route.setText("Não há rotas cadastradas");		            	
    	} else {
    		Route.setText("ROTAS");
    	}		
		mListFavorites.setAdapter(new FavoritesListAdapter(getActivity(), data));

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
			getLoaderManager().restartLoader(0, null, Users_Fragment.this);
		}};

	
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Favorites_fgmt");
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

				        	/*
				            Uri uri = SqliteProvider.CONTENT_URI_USER_FAVORITES;
				        	//Cursor data = getActivity().getContentResolver().query(uri, null, null, null, null);
				            data_userFavorites = getActivity().getContentResolver().query(uri, null, null, null, null);

				            if (data_userFavorites == null || data_userFavorites.getCount() == 0){

				            	Route.setText("Não há rotas cadastradas");		            	
				        	} else {
				        		Route.setText("ROTAS");
				        	}
				        	*/
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
	                                	
	            						//mListFavorites.setAdapter(new FavoritesListAdapter(getActivity()));
	                                	mListFavorites.setAdapter(new FavoritesListAdapter(getActivity(),data_userFavorites));
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
	 
		public class FavoritesListAdapter extends BaseAdapter {
		    private LayoutInflater mInflater;
		    Cursor users_info;

		    //public FavoritesListAdapter(FragmentActivity fragmentActivity) {
		    public FavoritesListAdapter(FragmentActivity fragmentActivity, Cursor data) {	
		    	users_info = data; 	    		    	
		    }

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				//return jsonArray.length() + 1;
				//return data_userFavorites.getCount() + 1;
				//-------------------
				//return data_userFavorites.getCount();
				//return favorites_info.getCount();
				return 5;
				//-------------------

				
			}
			
			@Override
			public int getItemViewType(int position) {
				return TYPE1;
				//if (position < jsonArray.length() ){
				/*
				if  (position < data_userFavorites.getCount() ){	
					return TYPE1;
				} else {
					return TYPE2;
				}
				*/
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup arg2) {				

				
				int viewType = this.getItemViewType(position);

				
			    switch(viewType){
			       case 1:
			           
			    	   final Type1Holder holder1; 

			           View v = convertView;
			           
						if (mInflater == null){
							mInflater = (LayoutInflater) getActivity()
				                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						}
			           
			           //if (v == null) { 
			        	   v = mInflater.inflate(R.layout.users_items, null);
			        	   holder1 = new Type1Holder (); 
			               
			        	   holder1.profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
			               holder1.name = (TextView) v.findViewById(R.id.name);
			               holder1.score = (TextView) v.findViewById(R.id.score);
			               v.setTag(holder1); 

			               //users_info.moveToPosition(position);
			               users_info.moveToLast();
				           holder1.name.setText( users_info.getString(KEY_NAME));					 
				           holder1.score.setText("Pontuação: 0 km");
				           //*
	        		        mHandler.post(new Runnable() {	
	                            @Override
	                            public void run() {
	                            	try {
	                    	
	                            		
    			                    	Context c = getActivity();
    			                    	Drawable drawable = new LoadImageURL(users_info.getString(KEY_PICURL),c).execute().get();	    			                    	
    			                    	holder1.profile_pic.setImageDrawable(drawable);		                   
    			                    } catch (Exception e) {
    			        				// TODO Auto-generated catch block
    			        				e.printStackTrace();
    			                    }
	                    	
	                            }
	        		        });
	        		        //*/

			            return v;	
			    	   
			       default:
			            break;
			        	   
			    }
				return null;
				

			}
		}

	    
	    class Type1Holder {
	        ImageView profile_pic;
	        TextView name;
	        TextView score;

	    }

	    
}
