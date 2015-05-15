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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class Routes_Fragment extends Fragment {

	
	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_ID = 0;
	//---------------------
	ListView mListFavorites;
	SimpleCursorAdapter mAdapter;
	protected static JSONArray jsonArray;
	protected static JSONObject json;
	ProgressDialog progressBar;
	
	
	public Routes_Fragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView = inflater.inflate(R.layout.fgmt_background, container, false);
		View rootView2 = inflater.inflate(R.layout.favorites_header, container, false);
		
		View custom = inflater.inflate(R.layout.favorites_fragment, null); 
		View custom2 = inflater.inflate(R.layout.favorites_add_routes_button, container, false);
		
		mListFavorites = (ListView) custom.findViewById(R.id.list_favorites);
		
        progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		//--------------

		
		Favorites_Query fq = new Favorites_Query();
		
		((ViewGroup) rootView).addView(rootView2);
		((ViewGroup) rootView).addView(custom);
		((ViewGroup) rootView).addView(custom2);
        return rootView;
    }
	
	 @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	         
	        FragmentManager fragMgr = getFragmentManager();
	        Fragment currentFragment = (Fragment) fragMgr.findFragmentById(0);
	         

	        if (currentFragment != null){	        	
	        	FragmentTransaction fragTrans = fragMgr.beginTransaction();
	            fragTrans.remove(currentFragment);
	            fragTrans.commit();
	        }
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
				        		//Log.i(TAG,"testing SQLITE: " + data.getString(KEY_ID) + "," + data.getString(1));

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
				return jsonArray.length();	
				
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

	            JSONObject jsonObject = null;
	            try {
	                jsonObject = jsonArray.getJSONObject(position);
	            } catch (JSONException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }				
				
				 View hView = convertView;
				 
				
				if (mInflater == null){
					mInflater = (LayoutInflater) getActivity()
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				}
		        if (convertView == null){		        	
		        	
		        	hView = mInflater.inflate(R.layout.favorites_items, null);	                
	                ViewHolder holder = new ViewHolder();
	                holder.profile_pic = (ImageView) hView.findViewById(R.id.profile_pic);
	                holder.name = (TextView) hView.findViewById(R.id.name);
	                holder._de = (TextView) hView.findViewById(R.id._de);
	                holder.info = (TextView) hView.findViewById(R.id.info);
	                //------
	                hView.setTag(holder);
	            }		    
		        
		        final ViewHolder holder = (ViewHolder) hView.getTag();
		        /*
	            try {
	                holder.profile_pic.setImageBitmap(Utility.model.getImage(
	                      jsonObject.getString("id"), jsonObject.optJSONObject("picture").optJSONObject("data").getString("url")));
	                    
	            } catch (JSONException e) {
	                holder.name.setText("");
	            }
	            */
	            try {
	                holder.name.setText("Linha: " + jsonObject.getString("busline"));
	            } catch (JSONException e) {
	                holder.name.setText("");
	            }
	            
	            try {
	                holder._de.setText("De: " +jsonObject.getString("_from"));
	            } catch (JSONException e) {
	                holder._de.setText("");
	            }
	            
	            try {
	                holder.info.setText("Para: " +jsonObject.getString("_to"));
	            } catch (JSONException e) {
	                holder.info.setText("");
	            }

	            return hView;
			}
		}

	    class ViewHolder {
	        ImageView profile_pic;
	        TextView name;
	        TextView _de;
	        TextView info;

	    }

	 
}
