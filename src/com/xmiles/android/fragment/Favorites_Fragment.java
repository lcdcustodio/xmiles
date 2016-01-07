package com.xmiles.android.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.xmiles.android.NewRoutes;
import com.xmiles.android.R;
import com.xmiles.android.R.id;
import com.xmiles.android.R.layout;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
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
import android.net.Uri;
import android.os.Bundle;
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


public class Favorites_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_ID = 0;
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_BUSLINE = 2;
	private static final Integer KEY_CITY = 3;
	private static final Integer KEY_UF = 4;
	private static final Integer KEY_FROM = 5;
	private static final Integer KEY_TO = 6;
	//---------------------
	public static final String KEY_FAVORITE_ID = "favorite_id";
	public static final String KEY_BUSLINE_ = "busline";
	//---------------------
	protected static final Integer TYPE1 = 1;
	private static final Integer TYPE2 = 2;	
	//---------------------
	private static final String projection[] = new String[]{DatabaseHelper.KEY_FAVORITE_ID,
			DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME,
			DatabaseHelper.KEY_BUSLINE, DatabaseHelper.KEY_CITY,
			DatabaseHelper.KEY_UF, DatabaseHelper.KEY_FROM,
			DatabaseHelper.KEY_TO, DatabaseHelper.KEY_FROM_BUS_STOP_ID,
			DatabaseHelper.KEY_TO_BUS_STOP_ID};
	
	
	//--------------------
	ListView mListFavorites;
	Button Add_route;
	TextView Route;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	Cursor data_userFavorites;
	
	public Favorites_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		getActivity().registerReceiver(FavoritesFragmentReceiver, new IntentFilter("fragmentupdater"));
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		View rootView1 = inflater.inflate(R.layout.favorites_add_routes_button, container, false);
		//View rootView1 = inflater.inflate(R.layout.favorites_add_routes_button, null);
		View rootView2 = inflater.inflate(R.layout.favorites_header, container, false);
		
		View custom = inflater.inflate(R.layout.favorites_fragment, null); 
		
		mListFavorites = (ListView) custom.findViewById(R.id.list_favorites);		
		Add_route	   = (Button) rootView1.findViewById(R.id.button1);
		Route          = (TextView) rootView2.findViewById(R.id.rotas);
		//---------------		
		//mListFavorites.addFooterView(rootView1);
		//---------------		
		Add_route.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v2) {
            	//-------------
            	//Toast.makeText(getActivity(), "Botao Nova Rota pressionado", Toast.LENGTH_SHORT).show();
            	//------------
                Intent intent = new Intent(getActivity(), NewRoutes.class);
                startActivity(intent);
            }
        });

		//---------------
        progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		//--------------

		
		Favorites_Query fq = new Favorites_Query();
		
		((ViewGroup) rootView).addView(rootView1);
		((ViewGroup) rootView).addView(rootView2);
		((ViewGroup) rootView).addView(custom);
		//((ViewGroup) rootView).addView(rootView1);
		
		/** Creating a loader for populating listview from sqlite database */
		getLoaderManager().initLoader(0, null, this);
		
        // Create an empty adapter we will use to display the loaded data.
		//FavoritesListAdapter mAdapter = new FavoritesListAdapter(getActivity());
        //setListAdapter(mAdapter);
		
		return rootView;
    }
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		Uri uri = SqliteProvider.CONTENT_URI_USER_FAVORITES;
		
		//return new CursorLoader(getActivity(), uri, null, null, null, null);
		//String sortOrder = KEY_FAVORITE_ID + " DESC";
		String sortOrder = KEY_BUSLINE_ + " ASC";
		//DatabaseHelper.KEY_FAVORITE_ID
		//return new CursorLoader(getActivity(), uri, null, null, null, sortOrder);
		

		//CursorLoader loader = new CursorLoader(getActivity(), uri, projection, null, null, DatabaseHelper.KEY_BUSLINE + " ASC");
		CursorLoader loader = new CursorLoader(getActivity(), uri, projection, null, null, null);
		loader.setSortOrder(DatabaseHelper.KEY_BUSLINE + " DESC");
		//return new CursorLoader(getActivity(), uri, projection, null, null, DatabaseHelper.KEY_FAVORITE_ID + " DESC");
		//return new CursorLoader(getActivity(), uri, projection, null, null, DatabaseHelper.KEY_BUSLINE + " ASC");
		return loader;
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
	
	private final BroadcastReceiver FavoritesFragmentReceiver = new BroadcastReceiver() {


		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			getLoaderManager().restartLoader(0, null, Favorites_Fragment.this);
		}};

	
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Favorites_fgmt");
	        //-------------
	        getActivity().unregisterReceiver(FavoritesFragmentReceiver);
	        //-------------
	    }
	 
	 public class Favorites_Query {
		 
		 public Favorites_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {

				            //Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
				            Uri uri = SqliteProvider.CONTENT_URI_USER_FAVORITES;
				        	//Cursor data = getActivity().getContentResolver().query(uri, null, null, null, null);
				            data_userFavorites = getActivity().getContentResolver().query(uri, null, null, null, null);
				            //data_userFavorites = getActivity().getContentResolver().query(uri, projection, null, null, DatabaseHelper.KEY_FAVORITE_ID + " DESC");
				            //data_userFavorites = getActivity().getContentResolver().query(uri, projection, null, null, DatabaseHelper.KEY_FAVORITE_ID + " DESC");

				            if (data_userFavorites == null || data_userFavorites.getCount() == 0){

				            	Route.setText("Não há rotas cadastradas");		            	
				        	} else {
				        		Route.setText("ROTAS");
				        	}

				    		        	
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
	                            Thread.sleep(400);
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
		    Cursor favorites_info;

		    //public FavoritesListAdapter(FragmentActivity fragmentActivity) {
		    public FavoritesListAdapter(FragmentActivity fragmentActivity, Cursor data) {	
		    	favorites_info = data; 	    		    	
		    }

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				//return jsonArray.length() + 1;
				//return data_userFavorites.getCount() + 1;
				//-------------------
				//return data_userFavorites.getCount();
				return favorites_info.getCount();
				//-------------------
				//return jsonArray.length();
				
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
			           
			    	   Type1Holder holder1; 

			           View v = convertView;
			           
						if (mInflater == null){
							mInflater = (LayoutInflater) getActivity()
				                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						}
			           
			           //if (v == null) { 
			        	   v = mInflater.inflate(R.layout.favorites_items, null);
			        	   holder1 = new Type1Holder (); 
			               
			        	   holder1.profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
			               holder1.name = (TextView) v.findViewById(R.id.name);
			               holder1.city = (TextView) v.findViewById(R.id.city);
			               holder1._de = (TextView) v.findViewById(R.id._de);
			               holder1.info = (TextView) v.findViewById(R.id.info);
			               v.setTag(holder1); 
			        	   
			               if (favorites_info.getCount() == 0) {
			               //if (data_userFavorites.getCount() == 0) {  	   
			               //if (data_userFavorites.getCount() > 0) {
			            	   
			            	   holder1.name.setText( "Não há rotas cadastradas");
			            	   //holder1.profile_pic.setVisibility(View.GONE);
			               } else {
			       
				               //data_userFavorites.moveToPosition(position);
				               favorites_info.moveToPosition(position);
				               /*
				               holder1.name.setText( data_userFavorites.getString(KEY_BUSLINE));					 
				               holder1.city.setText(data_userFavorites.getString(KEY_CITY) + " - " 
				            		   + data_userFavorites.getString(KEY_UF));
				               holder1._de.setText("De: " + data_userFavorites.getString(KEY_FROM));
					           holder1.info.setText("Para: " + data_userFavorites.getString(KEY_TO));
					           */
				               holder1.name.setText( favorites_info.getString(KEY_BUSLINE));					 
				               holder1.city.setText(favorites_info.getString(KEY_CITY) + " - " 
				            		   + favorites_info.getString(KEY_UF));
				               holder1._de.setText("De: " + favorites_info.getString(KEY_FROM));
					           holder1.info.setText("Para: " + favorites_info.getString(KEY_TO));
			            
			               }

			            return v;	


			       /*     
			       case 2:
			    	   
			    	   Type2Holder holder2; 

			           View v2 = convertView;
			           
						if (mInflater == null){
						  mInflater = (LayoutInflater) getActivity()
				                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						}			           
 
			        	v2 = mInflater.inflate(R.layout.favorites_add_routes_button, null);			        	   
			        	holder2 = new Type2Holder (); 			               
			        	holder2.add_routes = (Button) v2.findViewById(R.id.button1);
		        	
			            ((Button)v2.findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {

			                @Override
			                public void onClick(View v2) {
			                	//-------------
			                	//Toast.makeText(getActivity(), "Botao Nova Rota pressionado", Toast.LENGTH_SHORT).show();
			                	//------------
			                	
	                            //Intent intent = new Intent(getActivity(), Gmaps_Fragment.class);
	                            Intent intent = new Intent(getActivity(), NewRoutes_Fragment.class);
	                            startActivity(intent);
	                            
			      	    	  	
			                	
			                }
			            });
			            
			            v2.setTag(holder2); 
			        	   
			    	   
			           return v2;
			       */
			    	   
			       default:
			            break;
			        	   
			    }
				return null;
				

			}
		}

	    
	    class Type1Holder {
	        ImageView profile_pic;
	        TextView name;
	        TextView city;
	        TextView _de;
	        TextView info;

	    }

	    class Type2Holder {
	        Button add_routes;

	    }


	    
}
