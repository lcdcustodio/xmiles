package com.xmiles.android;


import org.json.JSONException;
import org.json.JSONObject;

import com.xmiles.android.R;
import com.xmiles.android.adapter.ImageAdapter;
import com.xmiles.android.facebook_api_support.BaseRequestListener;
import com.xmiles.android.facebook_api_support.Utility;
import com.xmiles.android.support.LoadImageURL;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
	TextView email;
	TextView locat;
	ImageView mUserPic;
	GridView gridView;
	private static final String LOG = "FACEBOOK";
	String json_name;
	String json_email;
	String json_locat;
	String picURL;
	ProgressDialog progressBar;  
	private Handler mHandler;
	
	static final String[] MOBILE_OS = new String[] { "Gravar", "Rotas",	
				"Ranking", "Histórico" };
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		mHandler = new Handler();
		
		View custom = inflater.inflate(R.layout.profile_fgmt_background, container, false);
        View rootView = inflater.inflate(R.layout.profile_fgmt_custom, container, false);
        
        name = (TextView) rootView.findViewById(R.id.name);
        locat = (TextView) rootView.findViewById(R.id.locat);
        email = (TextView) rootView.findViewById(R.id.info);
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
        params.putString("fields", "name, picture,email,gender,relationship_status");
        Utility.mAsyncRunner.request("me", params, new URListener());
        //------
        
        return custom;
        
    }
	  /*
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	  }
  
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        super.onOptionsItemSelected(item);
	 
	        switch(item.getItemId()){
	            case R.id.phone:
	                Toast.makeText(getActivity(), "You selected Phone", Toast.LENGTH_SHORT).show();
	                break;
	 
	            case R.id.computer:
	                Toast.makeText(getActivity(), "You selected Computer", Toast.LENGTH_SHORT).show();
	                break;
	 
	            case R.id.gamepad:
	                Toast.makeText(getActivity(), "You selected Gamepad", Toast.LENGTH_SHORT).show();
	                break;
	 
	            }
	        return true;
	    }
	    */
	
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

	                picURL = jsonObject.optJSONObject("picture").optJSONObject("data").getString("url");
	                json_name = jsonObject.getString("name");
                
	                /*
	                try {
	                	json_locat = jsonObject.getString("relationship_status");
	                } catch (JSONException e) {
	                    // TODO Auto-generated catch block
	                	json_locat = "N/A";
	                    e.printStackTrace();
	                }
	                */
	                json_locat = "Pontuação: 0 km";
	                
	                try {
	                	Log.i(LOG, "email: " + jsonObject.getString("email"));
	                	json_email = jsonObject.getString("email");
	                } catch (JSONException e) {
	                    // TODO Auto-generated catch block
	                	json_email = "N/A";
	                    e.printStackTrace();
	                }
	                //----------------------------------------------
	                runThread();
	                //----------------------------------------------
	                

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
	            	                	email.setText(json_email);
	            	                	name.setText(json_name);
	            	                	locat.setText(json_locat);
	    			                    	
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
	
}

