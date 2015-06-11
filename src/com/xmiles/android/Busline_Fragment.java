package com.xmiles.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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


public class Busline_Fragment extends Fragment implements OnItemClickListener {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_ID = 0;
	//---------------------
	//---------------------
	ListView mListBusline;
	SimpleCursorAdapter mAdapter;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	
	public Busline_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		View rootView2 = inflater.inflate(R.layout.newroutes_header, container, false);
		
		View custom = inflater.inflate(R.layout.busline_fragment, null); 
		
		mListBusline = (ListView) custom.findViewById(R.id.list_busline);
		//mListBusline.-
		//-------TEMP-----------
		//mListBusline.setAdapter(new BuslineListAdapter(getActivity()));
	    
	    mListBusline.setOnItemClickListener((OnItemClickListener) this);
	    
		//----------------------
		//*
        progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		//*/
		//--------------

		
		Busline_Query fq = new Busline_Query();
		
		((ViewGroup) rootView).addView(rootView2);
		((ViewGroup) rootView).addView(custom);

		return rootView;
    }
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//Log.d(TAG, (String)parent.getItemAtPosition(position));
		Toast.makeText(getActivity(), (String)parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
		
	}
	
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        
	        Log.d(TAG, "onDestroy Routes_fgmt");
	        
	    }
	 
	 public class Busline_Query {
		 
		 public Busline_Query(){
			 
			 Thread thread = new Thread(new Runnable(){
				    @Override
				    public void run() {
				        try {

				            Uri uri = SqliteProvider.CONTENT_URI_CITY_BUSLINE;
				        	Cursor data = getActivity().getContentResolver().query(uri, null, null, null, null);
				        	
				        	if (data != null && data.getCount() > 0){
				        		data.moveToFirst();
				        		//Log.i(TAG,"testing SQLITE: " + data.getString(KEY_ID) + "," + data.getString(1));

				        		//Your code goes here
				        		UserFunctions userFunc = new UserFunctions();
				        		json = userFunc.busline(data.getString(KEY_ID));
	
				        		jsonArray = new JSONArray(json.getString("city"));
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
			//----------
			runThread();			 

		 }
		 
	        private void runThread() {

	            new Thread() {
	                public void run() {

	                        try {
	                        	getActivity().runOnUiThread(new Runnable() {

	                                @Override
	                                public void run() {
	            						mListBusline.setAdapter(new BuslineListAdapter(getActivity()));
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
	 
		public class BuslineListAdapter extends BaseAdapter {
		    private LayoutInflater mInflater;		    

		    public BuslineListAdapter(FragmentActivity fragmentActivity) {
		    	//friends_info = data; 	    		    	
		    }

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				//return jsonArray.length() + 1;
				
				return jsonArray.length();
				
			}
			
			@Override
			public int getItemViewType(int position) {
				return 0;
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

				
			    	   Type1Holder holder1; 

			           View v = convertView;
			           
						if (mInflater == null){
							mInflater = (LayoutInflater) getActivity()
				                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						}
			           
			            if (v == null) { 
			        	   v = mInflater.inflate(R.layout.busline_items, null);
			        	   holder1 = new Type1Holder (); 
			               
			        	   holder1.profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
			               holder1.name = (TextView) v.findViewById(R.id.name);
			               holder1.city = (TextView) v.findViewById(R.id.city);

			               v.setTag(holder1); 
			        	   
			           } else {
			              holder1 = (Type1Holder)v.getTag(); 
			           }
			       
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
						            
			            
			            } catch (JSONException e1) {
			                // TODO Auto-generated catch block
			                e1.printStackTrace();	                
			            }

			            return v;	
				

			}
		}

	    
	    class Type1Holder {
	        ImageView profile_pic;
	        TextView name;
	        TextView city;
	        

	    }




	    
}
