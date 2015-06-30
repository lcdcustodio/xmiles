package com.xmiles.android;


import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.scheduler.Favorites_AlarmReceiver;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;

import com.xmiles.android.R;
import com.xmiles.android.adapter.ImageAdapter;
import com.xmiles.android.facebook_api_support.BaseRequestListener;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.support.LoadImageURL;
import com.xmiles.android.support.Support;
import com.xmiles.android.webservice.UserFunctions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Profile_Fragment extends Fragment {

	TextView name;
	TextView city;
	TextView pontuacao;
	ImageView mUserPic;
	GridView gridView;
	private static final String LOG = "FACEBOOK";
	String json_name;
	String json_id;
	String json_city;
	String score;
	String picURL;
	ProgressDialog progressBar;  
	private Handler mHandler;
	
	static final String[] MOBILE_OS = new String[] { "Gravar", "Rotas",	
				"Ranking", "Histórico" };
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		mHandler = new Handler();
		
		View custom = inflater.inflate(R.layout.fgmt_background, container, false);
        View rootView = inflater.inflate(R.layout.profile_fgmt_custom, container, false);
        
        name = (TextView) rootView.findViewById(R.id.name);
        pontuacao = (TextView) rootView.findViewById(R.id.locat);
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

        
        progressBar = new ProgressDialog(getActivity());
		progressBar.setCancelable(true);
		progressBar.setMessage("Please, wait ...");
		progressBar.show();
		//--------------
        
        Bundle params = new Bundle();
        
        //params.putString("fields", "name, picture,city,gender,relationship_status");
        params.putString("fields", "name, picture,birthday,location,gender,relationship_status");
        //params.putString("fields", "name, picture.height(64),birthday,location,gender,relationship_status");
        Utility.mAsyncRunner.request("me", params, new URListener());
        //------
        
        return custom;
        
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

	    public class URListener extends BaseRequestListener {

	        @Override
	        public void onComplete(final String response, final Object state) {
	            JSONObject jsonObject;
	            progressBar.dismiss();
	            try {
	                jsonObject = new JSONObject(response);
                
	                Log.i(LOG, "response_profile: " + response);

	                picURL    = jsonObject.optJSONObject("picture").optJSONObject("data").getString("url");
	                json_name = jsonObject.getString("name");
	                json_id   = jsonObject.getString("id");
	                //-----------
	                score = "Pontuação: 0 km";
	                //-----------


                	//*
                	try {
                    	Uri uri = SqliteProvider.CONTENT_URI_USER_PLACES;
                    	Cursor data = getActivity().getContentResolver().query(uri, null, null, null, null); 
                    	
                    	if (data != null && data.getCount() > 0){
                    		data.moveToFirst();
                			json_city = data.getString(2);
                			
                		} else{
    		                try {
    		                	Log.i(LOG, "city: " + jsonObject.optJSONObject("location").getString("name"));		                	
    		                	//json_city = jsonObject.optJSONObject("location").getString("name");
    		                	json_city = jsonObject.optJSONObject("location").getString("name").split(",")[0];

    		                } catch (JSONException je) {
    		                    // TODO Auto-generated catch block
    		                	json_city = " ";
    		                    je.printStackTrace();
    		                }
                			
                		}
                	}catch ( Exception ex ) {
	                	json_city = " ";
	                    ex.printStackTrace();
	                    
                	}
                	//*/
                	
	                //----------------------------------------------
                	//Fb_Sso_Login(json_name, json_id, jsonObject.getString("gender"), picURL);
                	xMiles_WebService_Login(json_name, json_id, jsonObject.getString("gender"), picURL);
	                //----------------------------------------------
                	
                    Support support = new Support();
                    
        			/** Setting up values to insert into UserProfile table */
        			ContentValues contentValues = new ContentValues();
        			
        			contentValues.put(DatabaseHelper.KEY_ID,json_id);
        			contentValues.put(DatabaseHelper.KEY_NAME, json_name);        			
        			contentValues.put(DatabaseHelper.KEY_PICTURE, picURL);        			
        			contentValues.put(DatabaseHelper.KEY_CREATED_AT, support.getDateTime());
        			
        			getActivity().getContentResolver().insert(SqliteProvider.CONTENT_URI_USER_PROFILE, contentValues);
	                //----------------------------------------------
	                runThread();
	                //----------------------------------------------
	          		// TEMPORARY - start service
	                Favorites_AlarmReceiver FA = new Favorites_AlarmReceiver();
	          		FA.setAlarm(getActivity());

	                

	            } catch (JSONException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }

	        private void runThread() {

	            new Thread() {
	                public void run() {

	                        try {
	                        	getActivity().runOnUiThread(new Runnable() {

	                                @Override
	                                public void run() {
	            	                	city.setText(json_city);
	            	                	name.setText(json_name);
	            	                	pontuacao.setText(score);
	    			                    	
	    			        		        mHandler.post(new Runnable() {	
	    			                            @Override
	    			                            public void run() {
	    			                            	try {
	    			                    	
				    			                    	Context c = getActivity();
				    			                    	Log.e(LOG,"picURL: " + picURL);
				    			                    	Drawable drawable = new LoadImageURL(picURL,c).execute().get();	    			                    	
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
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }

	                }
	            }.start();
	        }
	    }
	    //public void Fb_Sso_Login(String name,String id, String gender, String picURL) {
	    public void xMiles_WebService_Login(String name,String id, String gender, String picURL) {
	        UserFunctions userFunction = new UserFunctions();
	        //---------------------------------------------        
	        //---------------------------------------------
	        JSONObject json = userFunction.loginUser(id);
	        // check for login response
	        try {
	            if (json.getString("success") != null) {
	                
	                String res = json.getString("success");
	                if(Integer.parseInt(res) != 1){
	                	userFunction.registerUser(name, id, gender, picURL);

	                }
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    	
	    }

}

