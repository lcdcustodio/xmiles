package com.xmiles.android.support;


import java.io.InputStream;
import java.net.URL;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public class LoadImageURL extends AsyncTask<Void,Void,Drawable> {

	String picurl;
	Context c;
	
	public LoadImageURL (String url, Context context){
		this.picurl = url;
		this.c = context;

	}
	
	
	  @Override
	  protected void onPostExecute(Drawable result) {
	   super.onPostExecute(result);
	   
	  }

	
	@Override
	protected void onPreExecute() {
	   super.onPreExecute();
	   
	}
	
	@Override
	protected Drawable doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
	      try
	      {
	          InputStream is = (InputStream) new URL(picurl).getContent();
	          Drawable d = Drawable.createFromStream(is, "src name");
	          return d;
	      }catch (Exception e) {
	          System.out.println("Exc="+e);
	          return null;
	      }		
		
	}
	

}
