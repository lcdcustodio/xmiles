package com.xmiles.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.webservice.UserFunctions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;

import android.app.ProgressDialog;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Favorites_Fragment_BckUp extends Fragment {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_ID = 0;
	//---------------------
	protected static final Integer TYPE1 = 1;
	private static final Integer TYPE2 = 2;	
	//---------------------
	ListView mListFavorites;
	SimpleCursorAdapter mAdapter;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	
	
	public Favorites_Fragment_BckUp(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		View rootView2 = inflater.inflate(R.layout.favorites_header, container, false);
		
		View custom = inflater.inflate(R.layout.favorites_fragment, null); 
		//View custom2 = inflater.inflate(R.layout.favorites_add_routes_button, container, false);		
		//View custom3 = inflater.inflate(R.layout.favorites_header, container, false);
		
		mListFavorites = (ListView) custom.findViewById(R.id.list_favorites);
		
        progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		//--------------

		
		Favorites_Query fq = new Favorites_Query();
		
		((ViewGroup) rootView).addView(rootView2);
		((ViewGroup) rootView).addView(custom);
		//((ViewGroup) rootView).addView(custom3);
		//((ViewGroup) rootView).addView(custom2);
        return rootView;
    }
	
	
	
	
	
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Routes_fgmt");
	        /* 
	        FragmentManager fragMgr = getFragmentManager();
	        //Fragment currentFragment = (Fragment) fragMgr.findFragmentById(0);
	        Fragment currentFragment = (Fragment) fragMgr.findFragmentById(R.id.frame_container);
	        //Fragment currentFragment = (Fragment) fragMgr.findFragmentByTag("Routes_Fragment");
	        Log.d(TAG, "onDestroy Routes_fgmt: " + currentFragment); 

	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		    ft.remove(currentFragment);
		    ft.commit();
		    //*
	        if (currentFragment != null){	        	
	        	FragmentTransaction fragTrans = fragMgr.beginTransaction();
	            fragTrans.remove(currentFragment);
	            //fragTrans.remove("Routes_Fragment");
	            fragTrans.commit();
	        }
	        */
	        
	    }
	 
	 public class Favorites_Query {
		 
		 public Favorites_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {

				            Uri uri = SqliteProvider.CONTENT_URI_USER_PROFILE;
				        	Cursor data = getActivity().getContentResolver().query(uri, null, null, null, null);
				        	
				        	if (data != null && data.getCount() > 0){
				        		data.moveToFirst();

				        		//Your code goes here
				        		UserFunctions userFunc = new UserFunctions();
				        		json = userFunc.favoritesRoutes(data.getString(KEY_ID));
	
				        		jsonArray = new JSONArray(json.getString("user"));
				        		//Log.i(TAG,"testing 1: " + jsonArray.get(1));

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
	            						mListFavorites.setAdapter(new FavoritesListAdapter(getActivity()));
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

		    public FavoritesListAdapter(FragmentActivity fragmentActivity) {
		    	//friends_info = data; 	    		    	
		    }

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return jsonArray.length() + 1;	
				//return jsonArray.length();
				
			}
			
			@Override
			public int getItemViewType(int position) {
				
				if (position < jsonArray.length() ){
					return TYPE1;
				} else {
					return TYPE2;
				}
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
			        	   
			           //} else {
			           //   holder1 = (Type1Holder)v.getTag(); 
			          // }
			       
					    JSONObject jsonObject = null;
					    
			            try {
			                jsonObject = jsonArray.getJSONObject(position);
			            
					 try {
				                holder1.name.setText(jsonObject.getString("busline"));
				             } catch (JSONException e) {
				                holder1.name.setText("");
				             }
					 try {
			                holder1.city.setText(jsonObject.getString("city"));
			             } catch (JSONException e) {
			                holder1.city.setText("");
			             }				            
				         try {
				                holder1._de.setText("De: " +jsonObject.getString("_from"));
				             } catch (JSONException e) {
				                holder1._de.setText("");
				             }
				            
				         try {
				                holder1.info.setText("Para: " +jsonObject.getString("_to"));
				             } catch (JSONException e) {
				                holder1.info.setText("");
				             }
			            
			            
			            } catch (JSONException e1) {
			                // TODO Auto-generated catch block
			                e1.printStackTrace();	                
			            }

			            return v;	



			       case 2:
			    	   
			    	   Type2Holder holder2; 

			           View v2 = convertView;
			           
						//if (mInflater == null){
						//mInflater = (LayoutInflater) getActivity()
				        //            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						//}			           
 
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
